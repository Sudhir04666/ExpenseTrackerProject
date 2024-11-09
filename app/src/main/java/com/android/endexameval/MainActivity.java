package com.android.endexameval;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView expenseRecyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList;
    private DatabaseReference databaseReference;
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expenseRecyclerView = findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        filterSpinner = findViewById(R.id.filterSpinner);

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList, this);
        expenseRecyclerView.setAdapter(expenseAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("expenses");

        loadExpenses();

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = filterSpinner.getSelectedItem().toString();
                filterExpenses(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadExpenses();
            }
        });
    }

    private void loadExpenses() {
        databaseReference.orderByChild("date").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                expenseList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Expense expense = dataSnapshot.getValue(Expense.class);
                    expenseList.add(expense);
                }
                expenseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
            }
        });
    }

    private void filterExpenses(String category) {
        if (category.equals("All")) {
            loadExpenses();
        } else {
            List<Expense> filteredList = new ArrayList<>();
            for (Expense expense : expenseList) {
                if (expense.getCategory().equals(category)) {
                    filteredList.add(expense);
                }
            }
            expenseAdapter = new ExpenseAdapter(filteredList, this);
            expenseRecyclerView.setAdapter(expenseAdapter);
        }
    }
}
