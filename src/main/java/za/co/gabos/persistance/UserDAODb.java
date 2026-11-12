package za.co.gabos.persistance;

import za.co.gabos.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

/**
 * Implements UserDAO, this class contains the SQL Strings to create a user and to retrieve a users information
 */
public class UserDAODb implements UserDAO {

    private final Database db;

    public UserDAODb(Database db) {
        this.db = db;
    }

    /**
     * Persist a new user to the database
     * @param user represents a user with a email and password
     * @return This updates the user, after the method is called a user will gain a id attribute.
     */
    @Override
    public User createUser(User user) {
        String sql = """
                        INSERT INTO users(email, password) VALUES(?,?)
                """;

        try (Connection conn = this.db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
        return user;
    }

    /**
     * method finds a user if they exist in the database
     * @param email is a string representing a users email address
     * @return Optional containing the User if found, or empty if not
     */
    @Override
    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, email, password FROM users WHERE email = ?
                """;

        try (Connection conn = this.db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }

        return Optional.empty();
    }

}