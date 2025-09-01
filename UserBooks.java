import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class UserBooks extends JFrame {
    public UserBooks(User user) {
        setTitle("📚 My Rented Books");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ✅ Background panel with dark overlay
        JPanel backgroundPanel = new JPanel() {
            Image bg = new ImageIcon("background.jpg").getImage();

            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 160));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new BorderLayout(20, 20));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // ✅ Title label
        JLabel header = new JLabel("📚 Books You've Rented", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.setForeground(Color.WHITE);
        backgroundPanel.add(header, BorderLayout.NORTH);

        // ✅ Text area for book list
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 18));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(0, 0, 0, 120));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        backgroundPanel.add(scrollPane, BorderLayout.CENTER);

        // ✅ Back button
        JButton backBtn = new JButton("🔙 Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backBtn.setBackground(new Color(0, 123, 255));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(120, 40));
        backBtn.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(backBtn);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ✅ Load rented books
        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT b.id, b.title, b.author, b.year FROM books b " +
                           "JOIN rents r ON b.id = r.book_id " +
                           "WHERE r.user_id = ? AND r.return_date IS NULL";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, user.getId());
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-5s %-30s %-25s %-6s\n", "ID", "Title", "Author", "Year"));
            sb.append("---------------------------------------------------------------\n");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                sb.append(String.format("%-5d %-30s %-25s %-6d\n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")));
            }

            if (hasData) {
                textArea.setText(sb.toString());
            } else {
                textArea.setText("📭 You haven't rented any books yet.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            textArea.setText("❌ Error loading your rented books.");
        }

        setContentPane(backgroundPanel);
        setVisible(true);
    }
}
