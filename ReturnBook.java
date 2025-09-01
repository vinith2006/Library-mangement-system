import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ReturnBook extends JFrame {
    private JTextField bookIdField;

    public ReturnBook(User user) {
        setTitle("Return a Book");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Background image panel
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

        JLabel titleLabel = new JLabel("🔄 Return a Book");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridy++;
        panel.add(createLabel("Enter Book ID to Return:"), gbc);

        gbc.gridy++;
        bookIdField = new JTextField(20);
        bookIdField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(bookIdField, gbc);

        gbc.gridy++;
        JButton returnBtn = createButton("Return Book");
        panel.add(returnBtn, gbc);

        gbc.gridy++;
        JButton backBtn = createButton("⬅ Back");
        panel.add(backBtn, gbc);

        returnBtn.addActionListener(e -> returnBook(user));
        backBtn.addActionListener(e -> {
            dispose();
            new LibraryGUI(user);
        });

        setContentPane(panel);
        setVisible(true);
    }

    private void returnBook(User user) {
        try {
            int bookId = Integer.parseInt(bookIdField.getText());
            Connection con = DBConnection.getConnection();

            PreparedStatement stmt = con.prepareStatement(
                    "SELECT id FROM rents WHERE user_id = ? AND book_id = ? AND return_date IS NULL");
            stmt.setInt(1, user.getId());
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int rentId = rs.getInt("id");

                PreparedStatement updateRent = con.prepareStatement(
                        "UPDATE rents SET return_date = NOW() WHERE id = ?");
                updateRent.setInt(1, rentId);
                updateRent.executeUpdate();

                PreparedStatement updateBook = con.prepareStatement(
                        "UPDATE books SET available = 1 WHERE id = ?");
                updateBook.setInt(1, bookId);
                updateBook.executeUpdate();

                JOptionPane.showMessageDialog(this, "Book returned successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "You haven't rented this book or already returned it.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: Invalid input or DB issue.");
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
