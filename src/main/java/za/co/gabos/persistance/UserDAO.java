package za.co.gabos.persistance;
import za.co.gabos.user.User;

import java.util.Optional;
/**
 * This is a DAO interface for the User table
 * We need to be able to add a user to the table
 * Retreieve a user from the table
 * A user only recieves its ID once it's persisted to the database
 */
public interface UserDAO {
    /**
     * This method adds a user to the database
     * @param user is a instance of the User class
     * @return The created User with its generated database ID
     */
    User createUser(User user);

    /**
     * Retrieves a user by their email --> this method signature warns you that the user might not exist and we should check for .isEmpty() during javalin routing
     * @param email is a string representing a users email address
     * @return a User instance wrapped in a Optional
     */
    Optional<User> findByEmail(String email);
}
