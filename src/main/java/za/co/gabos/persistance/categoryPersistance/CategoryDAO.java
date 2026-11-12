package za.co.gabos.persistance.categoryPersistance;
import za.co.gabos.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO {
    /**
     * Creates a category and persists it to the database.
     * @param category
     * @return Category, The reason we return a category is because the category will have been given a id value after being persisted to the database
     */
    Category create(Category category);

    /**
     * returns a list of all Categories in the category table
     * @return List containing Category instances
     */
    List<Category> findAll();

    /**
     * Looks up a single category py primary key id.
     * @param id
     * @return A category instance wrapped in a Optional tag
     */
    Optional<Category> findById(int id);
}
