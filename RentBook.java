import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class RentBook extends JFrame {
    private JTextField bookIdField;

    public RentBook(User user) {
        setTitle("Rent a Book");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Background panel with overlay
        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("background.jpg").getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        JLabel titleLabel = new JLabel("📖 Rent a Book");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        panel.add(createLabel("Enter Book ID to Rent:"), gbc);

        gbc.gridy++;
        bookIdField = new JTextField(20);
        bookIdField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(bookIdField, gbc);

        gbc.gridy++;
        JButton rentBtn = createButton("Rent Book");
        panel.add(rentBtn, gbc);

        gbc.gridy++;
        JButton backBtn = createButton("⬅ Back");
        panel.add(backBtn, gbc);

        rentBtn.addActionListener(e -> rentBook(user));
        backBtn.addActionListener(e -> {
            dispose();
            new LibraryGUI(user);
        });

        setContentPane(panel);
        setVisible(true);
    }

    private void rentBook(User user) {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            Connection con = DBConnection.getConnection();

            PreparedStatement checkStmt = con.prepareStatement("SELECT available FROM books WHERE id = ?");
            checkStmt.setInt(1, bookId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("available")) {
                PreparedStatement rentStmt = con.prepareStatement("INSERT INTO rents (user_id, book_id) VALUES (?, ?)");
                rentStmt.setInt(1, user.getId());
                rentStmt.setInt(2, bookId);
                rentStmt.executeUpdate();

                PreparedStatement updateStmt = con.prepareStatement("UPDATE books SET available = 0 WHERE id = ?");
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book rented successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Book is not available.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input or DB error.");
            ex.printStackTrace();
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 22));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }
}
