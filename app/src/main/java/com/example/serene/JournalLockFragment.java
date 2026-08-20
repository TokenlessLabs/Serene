package com.example.serene;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class JournalLockFragment extends Fragment {
    EditText etPin;
    Button btnUnlock;
    String storedPin = null;
    public JournalLockFragment() {
        super(R.layout.fragment_journal_lock);
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etPin = view.findViewById(R.id.etPin);
        btnUnlock = view.findViewById(R.id.btnUnlock);
        btnUnlock.setEnabled(false);
        loadPinFromDB();
        btnUnlock.setOnClickListener(v -> {
            String enteredPin = etPin.getText().toString().trim();
            if (TextUtils.isEmpty(enteredPin) || enteredPin.length() != 4) {
                etPin.setError("Enter 4-digit PIN");
                return;
            }
            if (enteredPin.equals(storedPin)) {
                unlock();
            } else {
                etPin.setError("Incorrect PIN");
            }
        });
    }
    private void loadPinFromDB() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            etPin.setError("Please sign in again");
            return;
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid);
        ref.get().addOnSuccessListener(snapshot -> {
            if (!isAdded()) return;

            Boolean isLocked = snapshot.child("journalLock").getValue(Boolean.class);
            if (isLocked == null || !isLocked) {
                unlock();
                return;
            }

            storedPin = snapshot.child("journalPin").getValue(String.class);
            if (storedPin == null || storedPin.length() != 4) {
                etPin.setError("Journal PIN is unavailable");
                return;
            }
            btnUnlock.setEnabled(true);
        }).addOnFailureListener(error -> {
            if (!isAdded()) return;
            Toast.makeText(requireContext(),
                    "Unable to verify journal PIN. Try again later.",
                    Toast.LENGTH_SHORT).show();
        });
    }
    private void unlock() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof JournalFragment) {
            JournalFragment parent = (JournalFragment) parentFragment;
            parent.loadRoot(new JournalListFragment());
        }
    }
}
