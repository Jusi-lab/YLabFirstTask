package model;

import java.time.LocalDate;

// Модель транзакции

public class Transaction {
    private String id;
    private String userId;
    private double amount;
    private String category;
    private String type; // "income" или "expense"
    private LocalDate date;
    private String description;

    public Transaction(String id, String userId, double amount, String category, String type, LocalDate date, String description) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.type = type;
        this.date = date;
        this.description = description;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return String.format(
                "Transaction{id=%s, amount=%.2f, category='%s', type='%s', date=%s, description='%s'}",
                id, amount, category, type, date, description
        );
    }
}