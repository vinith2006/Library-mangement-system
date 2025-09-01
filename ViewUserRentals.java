import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ViewUserRentals extends JFrame {
    public ViewUserRentals() {
        setTitle("User Rentals");
        setSize(500, 400);
        setLayout(new BorderLayout());

        JTextArea area = new JTextArea();
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);

        try {
            Connection con = DBConnection.getConnection();
            String query = "SELECT u.username, b.title FROM rents r JOIN users u ON r.user_id = u.id JOIN books b ON r.book_id = b.id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append(rs.getString("username"))
                  .append(" has rented: ")
                  .append(rs.getString("title"))
                  .append("\n");
            }

            area.setText(sb.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        add(scroll, BorderLayout.CENTER);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
