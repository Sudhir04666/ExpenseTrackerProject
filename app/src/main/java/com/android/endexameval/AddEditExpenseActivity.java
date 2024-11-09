package com.android.endexameval;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class AddEditExpenseActivity extends AppCompatActivity {

    private EditText expenseNameEditText, amountEditText;
    private Spinner categorySpinner;
    private TextView dateTextView;
    private Button saveButton;

    private DatabaseReference databaseReference;
    private String expenseId; // Will hold the expense ID if editing an expense

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_expense);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("expenses");

        // Initialize views
        expenseNameEditText = findViewById(R.id.expenseNameEditText);
        amountEditText = findViewById(R.id.amountEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        dateTextView = findViewById(R.id.dateTextView);
        saveButton = findViewById(R.id.saveButton);

        // Check if we are editing an existing expense
        expenseId = getIntent().getStringExtra("expenseId");
        if (expenseId != null) {
            loadExpenseData(expenseId); // Function to load existing expense data
            saveButton.setText("Update");
        }

        // Date Picker
        dateTextView.setOnClickListener(view -> showDatePicker());

        // Save or Update Button
        saveButton.setOnClickListener(v -> saveOrUpdateExpense());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // Format selected date as "YYYY-MM-DD"
                    String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    dateTextView.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveOrUpdateExpense() {
        String name = expenseNameEditText.getText().toString();
        String amountString = amountEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        String date = dateTextView.getText().toString();

        if (name.isEmpty() || amountString.isEmpty() || date.isEmpty()) {
            // Handle empty fields, show a message, etc.
            return;
        }

        double amount = Double.parseDouble(amountString);

        if (expenseId == null) {
            // Adding a new expense
            String id = databaseReference.push().getKey();
            Expense expense = new Expense(id, name, amount, category, date);
            databaseReference.child(id).setValue(expense);
        } else {
            // Updating an existing expense
            Expense expense = new Expense(expenseId, name, amount, category, date);
            databaseReference.child(expenseId).setValue(expense);
        }

        finish(); // Close activity and go back to previous screen
    }

    private void loadExpenseData(String expenseId) {
        // TODO: Load expense details from Firebase and set values to fields
    }
}

