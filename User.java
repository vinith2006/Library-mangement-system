public class User {
    private int id;
    private String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    // ✅ Getter for id
    public int getId() {
        return id;
    }

    // ✅ Getter for username
    public String getUsername() {
        return username;
    }
}

