package za.co.gabos.persistance.expensePersistance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import za.co.gabos.expense.Expense;
import za.co.gabos.persistance.Database;
import za.co.gabos.persistance.userPersistance.UserDAO;
import za.co.gabos.persistance.userPersistance.UserDAODb;
import za.co.gabos.user.User;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseDAODbTest {

    @TempDir
    Path tempDir;

    private ExpenseDAO expenseDao;
    private User testUser;

    @BeforeEach
    public void setUp() {
        String dbUrl = "jdbc:sqlite:" + tempDir.resolve("test_expenses.db");
        Database db = new Database(dbUrl);
        db.initializeDatabase();

        UserDAO userDao = new UserDAODb(db);
        testUser = userDao.createUser(new User("David", "david@example.com"));

        expenseDao = new ExpenseDAODb(db);
    }

    // 1. POSITIVE: Create & retrieve by ID
    @DisplayName("Positive: Create expense persists row, assigns ID, and can be found by ID")
    @Test
    public void createAndFindExpenseById() {
        Expense created = expenseDao.create(
                new Expense(testUser.getId(), 1, "Weekly Groceries", 450.50, LocalDate.of(2026, 11, 13))
        );

        assertEquals(1, created.getId());

        Optional<Expense> found = expenseDao.findById(created.getId());
        assertTrue(found.isPresent());
        assertEquals("Weekly Groceries", found.get().getDescription());
        assertEquals(450.50, found.get().getAmount(), 0.001);
        assertEquals(LocalDate.of(2026, 11, 13), found.get().getDate());
    }

    // 2. POSITIVE: Filter by user and category + delete
    @DisplayName("Positive: Filter user expenses by category and delete an expense")
    @Test
    public void filterByCategoryAndDeleteExpense() {
        Expense groceries = expenseDao.create(
                new Expense(testUser.getId(), 1, "Bread & Milk", 65.00, LocalDate.of(2026, 11, 12))
        );
        expenseDao.create(
                new Expense(testUser.getId(), 2, "Taxi Fare", 40.00, LocalDate.of(2026, 11, 13))
        );

        List<Expense> foodOnly = expenseDao.findByUserIdAndCategoryId(testUser.getId(), 1);
        assertEquals(1, foodOnly.size());
        assertEquals("Bread & Milk", foodOnly.get(0).getDescription());

        assertTrue(expenseDao.deleteById(groceries.getId()));
        assertTrue(expenseDao.findById(groceries.getId()).isEmpty());
    }

    // 3. NEGATIVE: Foreign key violation on non-existent user_id
    @DisplayName("Negative: Creating expense with non-existent user ID throws RuntimeException")
    @Test
    public void createExpenseWithInvalidUserIdThrowsException() {
        Expense orphanExpense = new Expense(999, 1, "Ghost User Lunch", 120.00, LocalDate.now());

        assertThrows(RuntimeException.class, () -> expenseDao.create(orphanExpense));
    }

    // 4. NEGATIVE: Querying or deleting non-existent expense ID
    @DisplayName("Negative: Finding or deleting a missing expense ID returns empty/false")
    @Test
    public void findAndDeleteMissingExpenseId() {
        assertTrue(expenseDao.findById(999).isEmpty());
        assertFalse(expenseDao.deleteById(999));
    }

    // 5. EDGE CASE: Same-date expenses sort deterministically by newest ID first
    @DisplayName("Edge Case: Multiple expenses on the exact same date order by most recently inserted first")
    @Test
    public void sameDateExpensesReturnMostRecentFirst() {
        LocalDate sameDay = LocalDate.of(2026, 11, 13);
        expenseDao.create(new Expense(testUser.getId(), 1, "Morning Coffee", 35.00, sameDay));
        expenseDao.create(new Expense(testUser.getId(), 1, "Afternoon Lunch", 95.00, sameDay));

        List<Expense> results = expenseDao.findByUserId(testUser.getId());

        assertEquals(2, results.size());
        assertEquals("Afternoon Lunch", results.get(0).getDescription());
        assertEquals("Morning Coffee", results.get(1).getDescription());
    }
}