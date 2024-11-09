package com.android.endexameval;

public class Category {
    private String name;
    private double total;

    public Category(String name, double total) {
        this.name = name;
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }
}

