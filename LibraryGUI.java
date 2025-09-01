import java.awt.*;
import javax.swing.*;

public class LibraryGUI extends JFrame {

    public LibraryGUI(User user) {
        setTitle("Library Dashboard - Welcome " + user.getUsername());
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

        JLabel welcomeLabel = new JLabel("📚 Welcome, " + user.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(welcomeLabel, gbc);

        // ✅ Buttons
        JButton rentBtn = createButton("📖 Rent Book");
        JButton returnBtn = createButton("🔄 Return Book");
        JButton availableBtn = createButton("✅ Available Books");
        JButton myBooksBtn = createButton("📚 My Rented Books");
        JButton logoutBtn = createButton("🚪 Logout");

        gbc.gridy++;
        panel.add(rentBtn, gbc);
        gbc.gridy++;
        panel.add(returnBtn, gbc);
        gbc.gridy++;
        panel.add(availableBtn, gbc);
        gbc.gridy++;
        panel.add(myBooksBtn, gbc);
        gbc.gridy++;
        panel.add(logoutBtn, gbc);

        // ✅ Button actions
        rentBtn.addActionListener(e -> new RentBook(user));
        returnBtn.addActionListener(e -> new ReturnBook(user));
        availableBtn.addActionListener(e -> new AvailableBooks());
        myBooksBtn.addActionListener(e -> new UserBooks(user));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        setContentPane(panel);
        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(0, 123, 255));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(250, 40));
        return btn;
    }
}
