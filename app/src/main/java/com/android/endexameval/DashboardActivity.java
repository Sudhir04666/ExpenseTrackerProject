package com.android.endexameval;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {

    private TextView totalExpenseTextView;
    private RecyclerView categoryRecyclerView;
    private FloatingActionButton addExpenseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Initialize views
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        addExpenseButton = findViewById(R.id.addExpenseButton);

        // Setup RecyclerView with a layout manager
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Example data for categories (you'll eventually load this dynamically)
        setupCategoryList();

        // Add expense button click listener
        addExpenseButton.setOnClickListener(v -> {
            // Navigate to AddExpenseActivity
            startActivity(new Intent(DashboardActivity.this, AddExpenseActivity.class));
        });
    }

    private void setupCategoryList() {
        // TODO: Replace this with actual data loading logic
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Food", 50.00));
        categoryList.add(new Category("Transport", 30.00));
        categoryList.add(new Category("Accommodation", 120.00));

        // Set up adapter with the list
        CategoryAdapter adapter = new CategoryAdapter(categoryList);
        categoryRecyclerView.setAdapter(adapter);

        // Calculate total expenses
        updateTotalExpense(categoryList);
    }

    private void updateTotalExpense(List<Category> categories) {
        double total = 0.0;
        for (Category category : categories) {
            total += category.getTotal();
        }
        totalExpenseTextView.setText("Total Expenses: $" + String.format("%.2f", total));
    }
}

