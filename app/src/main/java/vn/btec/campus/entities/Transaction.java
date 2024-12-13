package vn.btec.campus_expense_management.entities;

public class Transaction {
    private int id;
    private double amount;
    private String description;
    private String date;
    private int type;
    private int userId;
    private String category;

    public Transaction(int id, double amount, String description, String date, int type, int userId, String category) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.userId = userId;
        this.category = category;
    }

    // Getters and setters
    // ...existing code...
}
