package com.example.serene;

import android.app.AlertDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JournalDetailFragment extends Fragment {
    TextView tvTitle, tvDate, tvContent, tvNoThemes;
    ImageView btnDelete;
    LinearLayout layoutThemes;
    TextView btnFavorite;
    boolean isFavorite = false;
    String journalId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_detail, container, false);
        tvTitle = view.findViewById(R.id.tvJournalTitle);
        tvDate = view.findViewById(R.id.tvJournalDate);
        tvContent = view.findViewById(R.id.tvJournalContent);
        layoutThemes = view.findViewById(R.id.layoutThemeChips);
        tvNoThemes = view.findViewById(R.id.tvNoThemes);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnFavorite = view.findViewById(R.id.btnFavorite);
        if (getArguments() != null) {
            journalId = getArguments().getString("journalId");
        }
        if (journalId == null || journalId.isEmpty()) {
            getParentFragmentManager().popBackStack();
            return view;
        }
        loadJournal();
        btnDelete.setOnClickListener(v -> new AlertDialog.Builder(requireContext())
                .setTitle("Delete Journal")
                .setMessage("Are you sure you want to delete this journal?")
                .setPositiveButton("Delete", (dialog, which) -> deleteJournal())
                .setNegativeButton("Cancel", null)
                .show());
        btnFavorite.setOnClickListener(v -> updateFavorite());
        return view;
    }

    private void deleteJournal() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        getJournalReference().removeValue()
                .addOnSuccessListener(unused -> {
                    if (getActivity() != null) {
                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                    }
                })
                .addOnFailureListener(error -> Toast.makeText(getContext(),
                        "Failed to delete journal", Toast.LENGTH_SHORT).show());
    }

    private void updateFavorite() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        boolean newFavoriteValue = !isFavorite;
        getJournalReference().child("isFavorite").setValue(newFavoriteValue)
                .addOnSuccessListener(unused -> {
                    isFavorite = newFavoriteValue;
                    updateFavoriteUI();
                })
                .addOnFailureListener(error -> Toast.makeText(getContext(),
                        "Failed to update favorite", Toast.LENGTH_SHORT).show());
    }

    private void updateFavoriteUI() {
        if (isFavorite) {
            btnFavorite.setText("Remove from favorites");
            btnFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.favorite_filled, 0, 0, 0
            );
        } else {
            btnFavorite.setText("Add to favorites");
            btnFavorite.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    R.drawable.favorite, 0, 0, 0
            );
        }
    }

    private void loadJournal() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        getJournalReference().get()
                .addOnSuccessListener(snapshot -> {
                    Journal journal = snapshot.getValue(Journal.class);
                    if (journal == null) return;
                    isFavorite = journal.isFavorite;
                    updateFavoriteUI();
                    tvTitle.setText(journal.title);
                    tvDate.setText(journal.date);
                    tvContent.setText(journal.content);
                    layoutThemes.removeAllViews();
                    if (journal.themes != null && !journal.themes.isEmpty()) {
                        tvNoThemes.setVisibility(View.GONE);
                        for (String theme : journal.themes) {
                            TextView chip = new TextView(getContext());
                            chip.setText(theme);
                            chip.setTextSize(11f);
                            chip.setPadding(24, 12, 24, 12);
                            chip.setBackgroundResource(R.drawable.chip_unselected);
                            chip.setBackgroundTintList(ColorStateList.valueOf(getThemeColor(theme)));
                            layoutThemes.addView(chip);
                        }
                    } else {
                        tvNoThemes.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(error -> {
                    if (!isAdded() || FirebaseAuth.getInstance().getCurrentUser() == null) return;
                    Toast.makeText(getContext(), "Failed to load journal", Toast.LENGTH_SHORT).show();
                });
    }

    private DatabaseReference getJournalReference() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return FirebaseDatabase.getInstance().getReference("users")
                .child(userId).child("journals").child(journalId);
    }

    private int getThemeColor(String theme) {
        switch (theme) {
            case "Stress": return Color.parseColor("#803040");
            case "Work": return Color.parseColor("#2A3A7A");
            case "Family": return Color.parseColor("#4A2A8A");
            case "Health": return Color.parseColor("#7A5020");
            case "Relationships": return Color.parseColor("#1A4A6A");
            case "Self": return Color.parseColor("#3A3A7A");
            default: return Color.parseColor("#5A5A9A");
        }
    }
}
