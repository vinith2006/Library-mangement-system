import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // ✅ Background image panel with dark overlay
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("background.jpg").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 150)); // semi-transparent overlay
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // ✅ Title
        JLabel titleLabel = new JLabel("Welcome Admin");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        backgroundPanel.add(titleLabel, gbc);

        // ✅ Buttons
        JButton viewLoginsBtn = createButton("🧑‍💻 View Login Details");
        JButton viewRentedBooksBtn = createButton("📚 View Rented Books");
        JButton deleteBookBtn = createButton("❌ Delete Book by ID");
        JButton addBookBtn = createButton("➕ Add New Book");
        JButton logoutBtn = createButton("🚪 Logout");

        gbc.gridy++;
        backgroundPanel.add(viewLoginsBtn, gbc);
        gbc.gridy++;
        backgroundPanel.add(viewRentedBooksBtn, gbc);
        gbc.gridy++;
        backgroundPanel.add(deleteBookBtn, gbc);
        gbc.gridy++;
        backgroundPanel.add(addBookBtn, gbc);
        gbc.gridy++;
        backgroundPanel.add(logoutBtn, gbc);

        // ✅ Button Actions
        viewLoginsBtn.addActionListener(e -> showLoginDetails());
        viewRentedBooksBtn.addActionListener(e -> showRentedBooks());
        deleteBookBtn.addActionListener(e -> {
            String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to delete:");
            if (bookIdStr != null && !bookIdStr.isEmpty()) {
                try {
                    int bookId = Integer.parseInt(bookIdStr);
                    deleteBook(bookId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid ID format!");
                }
            }
        });
        addBookBtn.addActionListener(e -> openAddBookForm());
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 45));
        return btn;
    }

    private void showLoginDetails() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username FROM users");

            StringBuilder sb = new StringBuilder();
            sb.append("User ID\tUsername\n");
            sb.append("-------\t--------\n");

            while (rs.next()) {
                sb.append(rs.getInt("id")).append("\t").append(rs.getString("username")).append("\n");
            }

            textArea.setText(sb.toString());
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "User Logins", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading login details.");
        }
    }

    private void showRentedBooks() {
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT rents.user_id, users.username, books.id AS book_id, books.title " +
                "FROM rents " +
                "JOIN users ON rents.user_id = users.id " +
                "JOIN books ON rents.book_id = books.id " +
                "WHERE books.available = FALSE"
            );

            StringBuilder sb = new StringBuilder();
            sb.append("User ID\tUsername\tBook ID\tBook Title\n");
            sb.append("-------\t--------\t-------\t----------\n");

            while (rs.next()) {
                sb.append(rs.getInt("user_id")).append("\t")
                  .append(rs.getString("username")).append("\t")
                  .append(rs.getInt("book_id")).append("\t")
                  .append(rs.getString("title")).append("\n");
            }

            textArea.setText(sb.toString());
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Rented Books", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading rented books.");
        }
    }

    private void deleteBook(int bookId) {
        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement deleteRents = con.prepareStatement("DELETE FROM rents WHERE book_id = ?");
            deleteRents.setInt(1, bookId);
            deleteRents.executeUpdate();

            PreparedStatement deleteBook = con.prepareStatement("DELETE FROM books WHERE id = ?");
            deleteBook.setInt(1, bookId);
            int rows = deleteBook.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Book ID not found.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting book.");
        }
    }

    private void openAddBookForm() {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField imageField = new JTextField();
        JTextField yearField = new JTextField();
        JComboBox<String> availableBox = new JComboBox<>(new String[]{"Yes", "No"});

        JPanel form = new JPanel(new GridLayout(0, 1));
        form.add(new JLabel("Book Title:"));
        form.add(titleField);
        form.add(new JLabel("Author:"));
        form.add(authorField);
        form.add(new JLabel("Image File Name (e.g., book1.jpg):"));
        form.add(imageField);
        form.add(new JLabel("Year (e.g., 2022):"));
        form.add(yearField);
        form.add(new JLabel("Available:"));
        form.add(availableBox);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Connection con = DBConnection.getConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO books (title, author, image, year, available) VALUES (?, ?, ?, ?, ?)");
                pst.setString(1, titleField.getText());
                pst.setString(2, authorField.getText());
                pst.setString(3, imageField.getText());
                pst.setInt(4, Integer.parseInt(yearField.getText()));
                pst.setBoolean(5, availableBox.getSelectedItem().equals("Yes"));
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter a valid year.");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding book.");
            }
        }
    }
}
