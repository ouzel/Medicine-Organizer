package com.example.myapplication.med;

public class MedModel {
    private int id;
    private String med;
    private String date;
    private int amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String task) {
        this.med = task;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
