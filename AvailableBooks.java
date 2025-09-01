import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AvailableBooks extends JFrame {
    public AvailableBooks() {
        setTitle("📚 Available Books");
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel booksPanel = new JPanel(new GridLayout(0, 3, 25, 25)); // 3 per row
        booksPanel.setBackground(Color.WHITE);
        booksPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            Connection con = DBConnection.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM books WHERE available = TRUE AND image IS NOT NULL");

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                String imageFile = rs.getString("image");

                JPanel bookPanel = new JPanel(new BorderLayout());
                bookPanel.setPreferredSize(new Dimension(300, 320));
                bookPanel.setBackground(Color.WHITE);
                bookPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

                // Image block
                try {
                    ImageIcon icon = new ImageIcon("images/" + imageFile);
                    Image scaledImage = icon.getImage().getScaledInstance(160, 200, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                    imageLabel.setHorizontalAlignment(JLabel.CENTER);
                    bookPanel.add(imageLabel, BorderLayout.NORTH);
                } catch (Exception e) {
                    JLabel placeholder = new JLabel("Image not found", JLabel.CENTER);
                    placeholder.setPreferredSize(new Dimension(160, 200));
                    placeholder.setForeground(Color.RED);
                    bookPanel.add(placeholder, BorderLayout.NORTH);
                }

                // Book Info
                JLabel info = new JLabel("<html><div style='text-align: center;'>"
                        + "<b>ID:</b> " + id + "<br/>"
                        + "<b>" + title + "</b><br/>"
                        + "By " + author + "<br/>"
                        + "(" + year + ")</div></html>");
                info.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                info.setHorizontalAlignment(JLabel.CENTER);
                bookPanel.add(info, BorderLayout.CENTER);

                booksPanel.add(bookPanel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("⚠️ Error loading available books.");
            errorLabel.setFont(new Font("Arial", Font.BOLD, 18));
            booksPanel.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ⬅ Back Button
        JButton backBtn = new JButton("⬅ Back");
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(0, 122, 255));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(100, 40));
        backBtn.addActionListener(e -> dispose());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(backBtn);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
