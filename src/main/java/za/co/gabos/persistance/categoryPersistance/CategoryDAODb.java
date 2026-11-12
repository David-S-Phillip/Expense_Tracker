package za.co.gabos.persistance.categoryPersistance;

import za.co.gabos.category.Category;
import za.co.gabos.persistance.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAODb implements CategoryDAO {
    private final Database db;

    public CategoryDAODb(Database db) {
        this.db = db;
    }

    @Override
    public Category create(Category category) {
        String sql = "INSERT INTO categories(name) VALUES(?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, category.getName());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    category.setId(keys.getInt(1));
                }
            }
            return category;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create category: " + category.getName(), e);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name FROM categories ORDER BY id ASC";
        List<Category> categories = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
            return categories;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve categories", e);
        }
    }

    @Override
    public Optional<Category> findById(int id) {
        String sql = "SELECT id, name FROM categories WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Category(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find category with id: " + id, e);
        }
    }
}

