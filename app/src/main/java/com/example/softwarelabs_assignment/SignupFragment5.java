package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupFragment5 extends Fragment {

    private SignupData signupData;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    public SignupFragment5() {
        super(R.layout.signup_verification_2);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get signup data safely
        if (getActivity() instanceof SignupActivity) {
            signupData = ((SignupActivity) getActivity()).signupData;
        }

        if (signupData == null) {
            Toast.makeText(requireContext(),
                    "Signup data missing",
                    Toast.LENGTH_LONG).show();
            return;
        }

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Button btnContinue = view.findViewById(R.id.btn_Continue);
        Button btnBack = view.findViewById(R.id.btn_Back);

        TextView fileNameText = view.findViewById(R.id.tv_fileName);
        ImageView btnRemove = view.findViewById(R.id.btn_removeFile);

        // Show file if exists
        if (signupData.proofUri != null && !signupData.proofUri.isEmpty()) {
            fileNameText.setVisibility(View.VISIBLE);
            btnRemove.setVisibility(View.VISIBLE);
            fileNameText.setText(signupData.proofUri);
        } else {
            fileNameText.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        }

        // Remove file
        btnRemove.setOnClickListener(v -> {
            signupData.proofUri = null;
            fileNameText.setVisibility(View.GONE);
            btnRemove.setVisibility(View.GONE);
        });

        // Continue button
        btnContinue.setOnClickListener(v -> {

            if (signupData.proofUri == null || signupData.proofUri.isEmpty()) {
                Toast.makeText(requireContext(),
                        "Please attach proof first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            btnContinue.setEnabled(false);

            // Create user
//            auth.createUserWithEmailAndPassword(
//                    signupData.email,
//                    signupData.password
//            ).addOnCompleteListener(task -> {
//
//                if (task.isSuccessful()) {
//
//                    if (auth.getCurrentUser() == null) {
//                        btnContinue.setEnabled(true);
//                        Toast.makeText(requireContext(),
//                                "User creation failed",
//                                Toast.LENGTH_LONG).show();
//                        return;
//                    }
//
//                    String userId = auth.getCurrentUser().getUid();
//
//                    // Save to Realtime Database
//                    databaseReference.child(userId)
//                            .setValue(signupData)
//                            .addOnSuccessListener(unused -> {
//
//                                Toast.makeText(requireContext(),
//                                        "Signup Successful",
//                                        Toast.LENGTH_SHORT).show();
//
//                                ((SignupActivity) requireActivity())
//                                        .loadFragment(new SignupFragment6());
//
//                            })
//                            .addOnFailureListener(e -> {
//                                btnContinue.setEnabled(true);
//                                Toast.makeText(requireContext(),
//                                        "Database Error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
//                            });
//
//                } else {
//                    btnContinue.setEnabled(true);
//                    Toast.makeText(requireContext(),
//                            "Auth Failed: " +
//                                    (task.getException() != null ?
//                                            task.getException().getMessage() :
//                                            "Unknown error"),
//                            Toast.LENGTH_LONG).show();
//                }
//            });
//        });

            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {

                // ✅ Google/Facebook user already authenticated
                String userId = user.getUid();

                databaseReference.child(userId)
                        .setValue(signupData)
                        .addOnSuccessListener(unused -> {

                            Toast.makeText(requireContext(),
                                    "Signup Completed",
                                    Toast.LENGTH_SHORT).show();

                            ((SignupActivity) requireActivity())
                                    .loadFragment(new SignupFragment6());
                        })
                        .addOnFailureListener(e -> {
                            btnContinue.setEnabled(true);
                            Toast.makeText(requireContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        });

            } else {

                // ✅ Email signup flow
                auth.createUserWithEmailAndPassword(
                        signupData.email,
                        signupData.password
                ).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser newUser = auth.getCurrentUser();
                        if (newUser == null) return;

                        String userId = newUser.getUid();

                        databaseReference.child(userId)
                                .setValue(signupData)
                                .addOnSuccessListener(unused -> {

                                    Toast.makeText(requireContext(),
                                            "Signup Successful",
                                            Toast.LENGTH_SHORT).show();

                                    ((SignupActivity) requireActivity())
                                            .loadFragment(new SignupFragment6());
                                });

                    } else {
                        btnContinue.setEnabled(true);
                        Toast.makeText(requireContext(),
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}