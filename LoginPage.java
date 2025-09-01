import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPage() {
        setTitle("Login Page");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Background image panel
        JPanel panel = new JPanel() {
            Image bg = new ImageIcon("background.jpg").getImage();
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
                // Optional: dark overlay for contrast
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // ✅ Title
        JLabel titleLabel = new JLabel("Library Login");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        // ✅ Username
        gbc.gridy++;
        panel.add(createLabel("Username:"), gbc);
        gbc.gridy++;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(usernameField, gbc);

        // ✅ Password
        gbc.gridy++;
        panel.add(createLabel("Password:"), gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(passwordField, gbc);

        // ✅ Buttons
        JButton loginBtn = createButton("Login");
        JButton registerBtn = createButton("Register");
        JButton adminBtn = createButton("Admin Login");

        gbc.gridy++;
        panel.add(loginBtn, gbc);
        gbc.gridy++;
        panel.add(registerBtn, gbc);
        gbc.gridy++;
        panel.add(adminBtn, gbc);

        loginBtn.addActionListener(e -> loginUser());
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterPage();
        });
        adminBtn.addActionListener(e -> {
            dispose();
            new AdminLogin();
        });

        setContentPane(panel);
        setVisible(true);
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

    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                LibraryApp.loggedInUser = new User(userId, username);
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new LibraryGUI(LibraryApp.loggedInUser);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error occurred.");
        }
    }
}
