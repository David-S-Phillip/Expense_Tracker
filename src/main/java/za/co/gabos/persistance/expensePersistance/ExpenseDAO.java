package za.co.gabos.persistance.expensePersistance;

import za.co.gabos.expense.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseDAO {
    Expense create(Expense expense);
    Optional<Expense> findById(int id);
    List<Expense> findByUserId(int userId);
    List<Expense> findByUserIdAndCategoryId(int userId, int categoryId);
    boolean deleteById(int id);
}