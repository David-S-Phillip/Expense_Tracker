package za.co.gabos.expense;

import java.time.LocalDate;

public class Expense {
    private int id;
    private final int userId;
    private final int categoryId;
    private final String description;
    private final double amount;
    private final LocalDate date;

    public Expense(int userId, int categoryId, String description, double amount, LocalDate date) {
        this(0, userId, categoryId, description, amount, date);
    }

    public Expense(int id, int userId, int categoryId, String description, double amount, LocalDate date) {
        if (userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        if (categoryId <= 0) {
            throw new IllegalArgumentException("Category ID must be positive");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.description = description.trim();
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}