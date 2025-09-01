import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AddBook extends JFrame {
    public AddBook() {
        setTitle("➕ Add New Book");
        setSize(400, 400);
        setLayout(null);
        getContentPane().setBackground(new Color(255, 255, 240));

        JLabel titleLbl = new JLabel("Title:");
        titleLbl.setBounds(50, 50, 100, 25);
        add(titleLbl);

        JTextField titleField = new JTextField();
        titleField.setBounds(150, 50, 180, 25);
        add(titleField);

        JLabel authorLbl = new JLabel("Author:");
        authorLbl.setBounds(50, 90, 100, 25);
        add(authorLbl);

        JTextField authorField = new JTextField();
        authorField.setBounds(150, 90, 180, 25);
        add(authorField);

        JLabel yearLbl = new JLabel("Year:");
        yearLbl.setBounds(50, 130, 100, 25);
        add(yearLbl);

        JTextField yearField = new JTextField();
        yearField.setBounds(150, 130, 180, 25);
        add(yearField);

        JLabel imageLbl = new JLabel("Image Name:");
        imageLbl.setBounds(50, 170, 100, 25);
        add(imageLbl);

        JTextField imageField = new JTextField("example.jpg");
        imageField.setBounds(150, 170, 180, 25);
        add(imageField);

        JButton addBtn = new JButton("Add Book");
        addBtn.setBounds(130, 230, 120, 30);
        add(addBtn);

        addBtn.addActionListener(e -> {
            String title = titleField.getText();
            String author = authorField.getText();
            String yearText = yearField.getText();
            String image = imageField.getText();

            try {
                int year = Integer.parseInt(yearText);
                Connection con = DBConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO books (title, author, year, available, image) VALUES (?, ?, ?, TRUE, ?)");
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setInt(3, year);
                stmt.setString(4, image);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "✅ Book added successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Failed to add book.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❗ Year must be a number.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}

