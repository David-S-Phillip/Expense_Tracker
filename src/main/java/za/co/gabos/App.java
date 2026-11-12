package za.co.gabos;

import za.co.gabos.persistance.Database;
import za.co.gabos.persistance.UserDAO;
import za.co.gabos.persistance.UserDAODb;
import za.co.gabos.user.User;

import java.util.Optional;

public class App {

    public static void main(String[] args) {
        Database db = new Database("jdbc:sqlite:expense_tracker.db");
        db.initializeDatabase();

        UserDAO userDao = new UserDAODb(db);
        User created = userDao.createUser(new User("david@example.com", "secret123"));
        System.out.println("Saved ID: " + created.getId());

        Optional<User> found = userDao.findByEmail("david@example.com");
        System.out.println("Found email: " + found.get().getEmail());
    }
}
