import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ViewLogins extends JFrame {
    public ViewLogins() {
        setTitle("View Login Details");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(50, 50, 50));

        JLabel title = new JLabel("👤 User Login Details", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        panel.add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setBackground(Color.DARK_GRAY);
        textArea.setForeground(Color.WHITE);

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

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("❌ Error loading login details.");
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton closeBtn = new JButton("⬅ Back");
        closeBtn.setFocusPainted(false);
        closeBtn.setBackground(new Color(0, 122, 255));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeBtn.addActionListener(e -> dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(50, 50, 50));
        btnPanel.add(closeBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }
}
