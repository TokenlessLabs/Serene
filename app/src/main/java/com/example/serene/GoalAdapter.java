package com.example.serene;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;
import java.util.List;

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {
    private final Context context;
    private List<Goal> goalList;
    private DatabaseReference goalsRef;
    public GoalAdapter(Context context, List<Goal> goalList, DatabaseReference goalsRef) {
        this.context = context;
        this.goalList = goalList;
        this.goalsRef = goalsRef;
    }
    public void updateList(List<Goal> newList) {
        this.goalList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_goal, parent, false);
        return new GoalViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull GoalViewHolder holder, int position) {
        Goal goal = goalList.get(position);
        holder.tvGoalTitle.setText(goal.getTitle());
        if (goal.getDate() != null && !goal.getDate().isEmpty()) {
            holder.tvGoalDate.setText("📅 " + goal.getDate());
            holder.tvGoalDate.setVisibility(View.VISIBLE);
        } else {
            holder.tvGoalDate.setVisibility(View.GONE);
        }
        if (goal.getTime() != null && !goal.getTime().isEmpty()) {
            holder.tvGoalTime.setText("⏰ " + goal.getTime());
            holder.tvGoalTime.setVisibility(View.VISIBLE);
        } else {
            holder.tvGoalTime.setVisibility(View.GONE);
        }
        applyPriority(holder.tvPriority, goal.getPriority());
        String status = goal.getStatus() == null ? "pending" : goal.getStatus();
        switch (status) {
            case "completed":
                holder.viewAccent.setBackgroundColor(Color.parseColor("#6058B0"));
                holder.tvGoalTitle.setPaintFlags(
                        holder.tvGoalTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                );
                holder.tvGoalTitle.setAlpha(0.5f);
                holder.tvOverdueBadge.setVisibility(View.GONE);
                break;
            case "overdue":
                holder.viewAccent.setBackgroundColor(Color.parseColor("#C05060"));
                holder.tvGoalTitle.setPaintFlags(
                        holder.tvGoalTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
                );
                holder.tvGoalTitle.setAlpha(1.0f);
                holder.tvOverdueBadge.setVisibility(View.VISIBLE);
                break;
            default:
                holder.viewAccent.setBackgroundColor(Color.parseColor("#8080C0"));
                holder.tvGoalTitle.setPaintFlags(
                        holder.tvGoalTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
                );
                holder.tvGoalTitle.setAlpha(1.0f);
                holder.tvOverdueBadge.setVisibility(View.GONE);
                break;
        }
        holder.checkGoal.setOnCheckedChangeListener(null);
        holder.checkGoal.setChecked("completed".equals(goal.getStatus()));
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                showEditDialog(goal, currentPosition);
            }
        });
        holder.checkGoal.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int pos = holder.getAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            Goal g = goalList.get(pos);
            String previousStatus = g.getStatus();
            String newStatus = isChecked ? "completed" : "pending";
            if (goalsRef != null) {
                goalsRef.child(g.getId())
                        .child("status")
                        .setValue(newStatus)
                        .addOnSuccessListener(unused -> {
                            g.setStatus(newStatus);
                            notifyItemChanged(pos);
                        })
                        .addOnFailureListener(error -> {
                            g.setStatus(previousStatus);
                            notifyItemChanged(pos);
                            Toast.makeText(context, "Failed to update goal", Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }

    private void applyPriority(TextView view, String priority) {
        if (priority == null) priority = "medium";
        switch (priority) {
            case "high":
                view.setText("HIGH");
                view.setBackgroundColor(Color.parseColor("#F8C8C8"));
                view.setTextColor(Color.parseColor("#803040"));
                break;
            case "low":
                view.setText("LOW");
                view.setBackgroundColor(Color.parseColor("#C8E8D0"));
                view.setTextColor(Color.parseColor("#2A5A34"));
                break;
            default:
                view.setText("MEDIUM");
                view.setBackgroundColor(Color.parseColor("#FFF1B8"));
                view.setTextColor(Color.parseColor("#7A5A00"));
                break;
        }
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return goalList.size();
    }
    static class GoalViewHolder extends RecyclerView.ViewHolder {
        View viewAccent;
        CheckBox checkGoal;
        TextView tvGoalTitle;
        TextView tvGoalDate;
        TextView tvGoalTime;
        TextView tvPriority;
        TextView tvOverdueBadge;
        GoalViewHolder(@NonNull View itemView) {
            super(itemView);
            viewAccent = itemView.findViewById(R.id.viewAccent);
            checkGoal = itemView.findViewById(R.id.checkGoal);
            tvGoalTitle = itemView.findViewById(R.id.tvGoalTitle);
            tvGoalDate = itemView.findViewById(R.id.tvGoalDate);
            tvGoalTime = itemView.findViewById(R.id.tvGoalTime);
            tvPriority = itemView.findViewById(R.id.tvPriority);
            tvOverdueBadge = itemView.findViewById(R.id.tvOverdueBadge);
        }
    }
    private void showEditDialog(Goal goal, int position) {
        View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_goal, null);
        EditText etGoal = dialogView.findViewById(R.id.etGoal);
        TextView tvDate = dialogView.findViewById(R.id.tvDate);
        TextView tvTime = dialogView.findViewById(R.id.tvTime);
        TextView low = dialogView.findViewById(R.id.priorityLow);
        TextView medium = dialogView.findViewById(R.id.priorityMedium);
        TextView high = dialogView.findViewById(R.id.priorityHigh);
        TextView dialogTitle = dialogView.findViewById(R.id.tvTitle);
        TextView btnDeleteGoal = dialogView.findViewById(R.id.btnDeleteGoal);
        dialogTitle.setText("Edit Goal");
        btnDeleteGoal.setVisibility(View.VISIBLE);
        etGoal.setText(goal.getTitle());
        final String[] selectedDate = {goal.getDate()};
        final String[] selectedTime = {goal.getTime()};
        final String[] selectedPriority = {goal.getPriority()};
        if (goal.getDate() != null && !goal.getDate().isEmpty()) {
            selectedDate[0] = goal.getDate();
            tvDate.setText("📅 " + selectedDate[0]);
        }
        if (goal.getTime() != null && !goal.getTime().isEmpty()) {
            selectedTime[0] = goal.getTime();
            tvTime.setText("⏰ " + selectedTime[0]);
        }
        updatePriorityUI(low, medium, high, selectedPriority[0]);
        low.setOnClickListener(v -> {
            selectedPriority[0] = "low";
            updatePriorityUI(low, medium, high, "low");
        });

        medium.setOnClickListener(v -> {
            selectedPriority[0] = "medium";
            updatePriorityUI(low, medium, high, "medium");
        });

        high.setOnClickListener(v -> {
            selectedPriority[0] = "high";
            updatePriorityUI(low, medium, high, "high");
        });
        tvDate.setOnClickListener(v -> {
            Calendar initialDate = Calendar.getInstance();
            if (selectedDate[0] != null && !selectedDate[0].isEmpty()) {
                try {
                    java.util.Date parsedDate = new java.text.SimpleDateFormat(
                            "dd/MM/yyyy", java.util.Locale.getDefault()).parse(selectedDate[0]);
                    if (parsedDate != null) initialDate.setTime(parsedDate);
                } catch (java.text.ParseException ignored) {
                }
            }
            android.app.DatePickerDialog picker = new android.app.DatePickerDialog(
                    context,
                    (view, year, month, day) -> {
                        selectedDate[0] = String.format(java.util.Locale.getDefault(),
                                "%02d/%02d/%04d", day, month + 1, year);
                        tvDate.setText("📅 " + selectedDate[0]);
                    },
                    initialDate.get(Calendar.YEAR),
                    initialDate.get(Calendar.MONTH),
                    initialDate.get(Calendar.DAY_OF_MONTH)
            );
            picker.show();
        });
        tvTime.setOnClickListener(v -> {
            android.app.TimePickerDialog picker = new android.app.TimePickerDialog(
                    context,
                    (view, hour, minute) -> {
                        selectedTime[0] = String.format("%02d:%02d", hour, minute);
                        tvTime.setText("⏰ " + selectedTime[0]);
                    },
                    12, 0, true
            );
            picker.show();
        });
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
        btnDeleteGoal.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Delete Goal")
                .setMessage("Are you sure you want to delete this goal?")
                .setPositiveButton("Delete", (confirmation, which) ->
                        goalsRef.child(goal.getId()).removeValue()
                                .addOnSuccessListener(unused -> dialog.dismiss())
                                .addOnFailureListener(error -> Toast.makeText(context,
                                        "Failed to delete goal", Toast.LENGTH_SHORT).show()))
                .setNegativeButton("Cancel", null)
                .show());
        dialogView.findViewById(R.id.btnSaveGoal).setOnClickListener(v -> {
            String title = etGoal.getText().toString().trim();
            if (title.isEmpty()) {
                etGoal.setError("Enter a goal");
                return;
            }
            String updatedStatus = getStatusForEditedGoal(
                    goal.getStatus(), selectedDate[0], selectedTime[0]);
            Goal updatedGoal = new Goal(goal.getId(), title, updatedStatus,
                    selectedPriority[0], selectedDate[0], selectedTime[0]);
            goalsRef.child(goal.getId()).setValue(updatedGoal)
                    .addOnSuccessListener(unused -> dialog.dismiss())
                    .addOnFailureListener(error ->
                            Toast.makeText(context, "Failed to update goal", Toast.LENGTH_SHORT).show());
        });
        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        dialog.show();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    private String getStatusForEditedGoal(String currentStatus, String date, String time) {
        if ("completed".equals(currentStatus)) return "completed";
        if (date == null || date.isEmpty()) return "pending";
        String dueTime = time == null || time.isEmpty() ? "23:59" : time;
        try {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                    "dd/MM/yyyy HH:mm", java.util.Locale.getDefault());
            format.setLenient(false);
            java.util.Date dueDate = format.parse(date + " " + dueTime);
            return dueDate != null && dueDate.before(new java.util.Date()) ? "overdue" : "pending";
        } catch (java.text.ParseException ignored) {
            return "pending";
        }
    }
    private void updatePriorityUI(TextView low, TextView medium, TextView high, String selected) {
        low.setBackgroundResource(R.drawable.chip_unselected);
        medium.setBackgroundResource(R.drawable.chip_unselected);
        high.setBackgroundResource(R.drawable.chip_unselected);
        switch (selected) {
            case "low":
                low.setBackgroundResource(R.drawable.chip_selected);
                break;
            case "medium":
                medium.setBackgroundResource(R.drawable.chip_selected);
                break;
            case "high":
                high.setBackgroundResource(R.drawable.chip_selected);
                break;
        }
    }
}
