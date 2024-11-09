package com.android.endexameval;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList;
    private Context context;
    private DatabaseReference databaseReference;

    public ExpenseAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
        this.databaseReference = FirebaseDatabase.getInstance().getReference("expenses");
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseNameTextView.setText(expense.getName());
        holder.expenseAmountTextView.setText("Amount: $" + String.format("%.2f", expense.getAmount()));
        holder.expenseCategoryTextView.setText("Category: " + expense.getCategory());
        holder.expenseDateTextView.setText("Date: " + expense.getDate());

        // Edit button
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditExpenseActivity.class);
            intent.putExtra("expenseId", expense.getId());
            context.startActivity(intent);
        });

        // Delete button
        holder.deleteButton.setOnClickListener(v -> {
            databaseReference.child(expense.getId()).removeValue();
            expenseList.remove(position);
            notifyItemRemoved(position);
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseNameTextView, expenseAmountTextView, expenseCategoryTextView, expenseDateTextView;
        Button editButton, deleteButton;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            expenseNameTextView = itemView.findViewById(R.id.expenseNameTextView);
            expenseAmountTextView = itemView.findViewById(R.id.expenseAmountTextView);
            expenseCategoryTextView = itemView.findViewById(R.id.expenseCategoryTextView);
            expenseDateTextView = itemView.findViewById(R.id.expenseDateTextView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
