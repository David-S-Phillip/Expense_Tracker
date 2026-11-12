package za.co.gabos.user;

/**
 * This class represents a user stored in our database
 * each user requires a email address and a password
 * The user only gets a id value once persisted to the database
 */
public class User {
    private int id;
    private String email;
    private String password;

    public User(String email, String password){
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        if (password == null || password.isBlank()){
            throw new IllegalArgumentException("Password may not be null or empty");
        }
        this.email = email;
        this.password = password;
    }

    public User(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // for testing purposes
    @Override
    public String toString(){
        return this.id + " " + this.email + " " + this.password;
    }



}
