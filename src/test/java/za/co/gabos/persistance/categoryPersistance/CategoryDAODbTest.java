package za.co.gabos.persistance.categoryPersistance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import za.co.gabos.category.Category;
import za.co.gabos.persistance.Database;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryDAODbTest {

    @TempDir
    Path tempDir;

    private CategoryDAO dao;

    @BeforeEach
    public void setUp() {
        String dbUrl = "jdbc:sqlite:" + tempDir.resolve("test_categories.db");
        Database db = new Database(dbUrl);
        db.initializeDatabase();
        dao = new CategoryDAODb(db);
    }

    @DisplayName("Database initialization seeds the 5 default categories")
    @Test
    public void findAllReturnsSeededDefaultCategories() {
        List<Category> categories = dao.findAll();

        assertEquals(5, categories.size());
        assertEquals("Food & Groceries", categories.get(0).getName());
        assertEquals("General", categories.get(4).getName());
    }

    @DisplayName("Create a new custom category and assign auto-incremented ID")
    @Test
    public void createCategoryPersistsAndAssignsId() {
        Category created = dao.create(new Category("Health & Fitness"));

        assertEquals(6, created.getId());
        assertEquals("Health & Fitness", created.getName());
        assertEquals(6, dao.findAll().size());
    }

    @DisplayName("Find existing category by ID returns populated Optional")
    @Test
    public void findByIdReturnsExistingCategory() {
        Optional<Category> found = dao.findById(2);

        assertTrue(found.isPresent());
        assertEquals(2, found.get().getId());
        assertEquals("Transport", found.get().getName());
    }

    @DisplayName("Find non-existent category ID returns empty Optional")
    @Test
    public void findByIdReturnsEmptyWhenMissing() {
        Optional<Category> found = dao.findById(999);

        assertTrue(found.isEmpty());
    }

    @DisplayName("Reject duplicate category name due to UNIQUE constraint")
    @Test
    public void createDuplicateCategoryThrowsException() {
        assertThrows(RuntimeException.class, () -> dao.create(new Category("Transport")));
    }

    @DisplayName("Database overwrites pre-assigned category ID upon persistence")
    @Test
    public void createCategoryOverwritesPreAssignedId() {
        Category categoryWithFakeId = new Category(888, "Subscriptions");
        Category persisted = dao.create(categoryWithFakeId);

        assertNotEquals(888, persisted.getId());
        assertEquals(6, persisted.getId());
    }
}