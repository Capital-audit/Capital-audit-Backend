package com.example.capitalauditbackend.model;

public class PaymentData {
    private int ID;
    private int price;
    private String category;
    private boolean debit_credit;
    private boolean cleared;
    private String date;

    public PaymentData()
    {
    }

    // ID
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    // Price
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Debit/Credit
    public boolean isDebit_credit() {
        return debit_credit;
    }

    public void setDebit_credit(boolean debit_credit) {
        this.debit_credit = debit_credit;
    }

    // Cleared
    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    // Date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
