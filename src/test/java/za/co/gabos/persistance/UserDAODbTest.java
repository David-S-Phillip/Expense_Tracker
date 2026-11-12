package za.co.gabos.persistance;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import za.co.gabos.user.User;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the UserDAODb.java class
 * test whether the 2 methods createUser() and findByEmail work as expected
 * 2 positive tests, 2 negative tests and 1 edge case
 */
public class UserDAODbTest {

    private static final String TEST_DB_LOCATION = "target/test_expense_tracker.db";
    private Database db;
    private UserDAO DAO;
    private User user;

    @BeforeEach
    public void setUp(){
        db = new Database("jdbc:sqlite:" + TEST_DB_LOCATION);
        db.initializeDatabase();
        DAO = new UserDAODb(db);
    }


    @AfterEach
    public void tearDown(){
        File testDb = new File(TEST_DB_LOCATION);
        if (testDb.exists()){
            testDb.delete();
        }
    }

    /**
     * Tests what happens after a user is persisted to the database
     * a user should have a ID, Email and a Password
     */
    @DisplayName("Persist a user to the database")
    @Test
    public void addUserToDB(){
        User user = new User("testUser@Gmail.com","testPassword123");
        User persistedUser = DAO.createUser(user);

        assertEquals("testUser@Gmail.com", persistedUser.getEmail());
        assertEquals("testPassword123", persistedUser.getPassword());
        assertEquals(1,persistedUser.getId());
    }

    /**
     * Persist multiple user to a Database
     */
    @DisplayName("Persist multiple users with incrementing IDs")
    @Test
    public void addMultipleUsersToDB(){
        for (int i = 1; i <=5; i++){
            String email = "user" + i + "@gmail.com";
            String password = "password" + i;

            User created = DAO.createUser(new User(email, password));

            assertEquals(i, created.getId());
            assertEquals(email, created.getEmail());
        }
    }

    /**
     * Tests if we create a user with a id value, then persist to a empty database will that id number be rewritten
     */
    @DisplayName("Database overwrites pre-assigned user ID upon persistence")
    @Test
    public void createUserOverwritesPreAssignedId() {
        User userWithFakeId = new User(999, "customid@gmail.com", "password123");
        User persistedUser = DAO.createUser(userWithFakeId);

        assertNotEquals(999, persistedUser.getId(), "DAO should not keep the caller-supplied ID");
        assertEquals(1, persistedUser.getId(), "Database should assign the next auto-incremented ID");

        Optional<User> retrieved = DAO.findByEmail("customid@gmail.com");
        assertTrue(retrieved.isPresent());
        assertEquals(1, retrieved.get().getId());
    }





}
