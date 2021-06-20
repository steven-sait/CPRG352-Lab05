package models;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username = null;
    private String password = null;
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    String getUsername() {
        return this.username;
    }
}
