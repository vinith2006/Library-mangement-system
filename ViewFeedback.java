import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ViewFeedback extends JFrame {

    public ViewFeedback() {
        setTitle("Admin - View Feedback");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Background image with overlay
        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("background.jpg").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 120)); // dark overlay
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout(20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel heading = new JLabel("📬 User Feedback");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
        heading.setForeground(Color.WHITE);
        heading.setHorizontalAlignment(JLabel.CENTER);
        panel.add(heading, BorderLayout.NORTH);

        JTextArea feedbackArea = new JTextArea();
        feedbackArea.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        feedbackArea.setForeground(Color.BLACK);
        feedbackArea.setBackground(Color.WHITE);
        feedbackArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("🔙 Back");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backBtn.setBackground(new Color(0, 123, 255));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.addActionListener(e -> {
            dispose();
            new AdminDashboard();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);
        bottomPanel.add(backBtn);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        loadFeedback(feedbackArea);
        setContentPane(panel);
        setVisible(true);
    }

    private void loadFeedback(JTextArea feedbackArea) {
        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM feedback");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append("🧑 User: ").append(rs.getString("username")).append("\n");
                sb.append("💬 Feedback: ").append(rs.getString("message")).append("\n\n");
            }

            if (sb.length() == 0) {
                sb.append("No feedback found.");
            }

            feedbackArea.setText(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            feedbackArea.setText("⚠️ Error loading feedback.");
        }
    }
}
