package za.co.gabos.persistance.expensePersistance;

import za.co.gabos.expense.Expense;
import za.co.gabos.persistance.Database;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpenseDAODb implements ExpenseDAO {
    private final Database db;

    public ExpenseDAODb(Database db) {
        this.db = db;
    }

    @Override
    public Expense create(Expense expense) {
        String sql = "INSERT INTO expenses(user_id, category_id, description, amount, expense_date) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, expense.getUserId());
            stmt.setInt(2, expense.getCategoryId());
            stmt.setString(3, expense.getDescription());
            stmt.setDouble(4, expense.getAmount());
            stmt.setString(5, expense.getDate().toString());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    expense.setId(keys.getInt(1));
                }
            }
            return expense;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create expense: " + expense.getDescription(), e);
        }
    }

    @Override
    public Optional<Expense> findById(int id) {
        String sql = "SELECT id, user_id, category_id, description, amount, expense_date FROM expenses WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find expense with id: " + id, e);
        }
    }

    @Override
    public List<Expense> findByUserId(int userId) {
        String sql = "SELECT id, user_id, category_id, description, amount, expense_date " +
                "FROM expenses WHERE user_id = ? ORDER BY expense_date DESC, id DESC";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapRow(rs));
                }
            }
            return expenses;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve expenses for user id: " + userId, e);
        }
    }

    @Override
    public List<Expense> findByUserIdAndCategoryId(int userId, int categoryId) {
        String sql = "SELECT id, user_id, category_id, description, amount, expense_date " +
                "FROM expenses WHERE user_id = ? AND category_id = ? ORDER BY expense_date DESC, id DESC";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, categoryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    expenses.add(mapRow(rs));
                }
            }
            return expenses;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve expenses for user id: " + userId + " and category id: " + categoryId, e);
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM expenses WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete expense with id: " + id, e);
        }
    }

    private Expense mapRow(ResultSet rs) throws SQLException {
        return new Expense(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getInt("category_id"),
                rs.getString("description"),
                rs.getDouble("amount"),
                LocalDate.parse(rs.getString("expense_date"))
        );
    }
}