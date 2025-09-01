import java.sql.*;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db", "root", "vinith7904169086"
            );
        } catch (Exception e) {
            e.printStackTrace();  // VERY IMPORTANT: shows error in terminal
            return null;
        }
    }
}

