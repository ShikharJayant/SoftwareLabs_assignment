package com.example.softwarelabs_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment2 extends Fragment {
    public  LoginFragment2(){
        super(R.layout.login_forgotpass);
    }


    EditText etEmail;
    MaterialButton btnSendCode;
    TextView txtLogin;
    FirebaseAuth auth;



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEmail = view.findViewById(R.id.et_phoneno);
        btnSendCode = view.findViewById(R.id.btn_sendcode);
        txtLogin = view.findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        // Send reset email
        btnSendCode.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSendCode.setEnabled(false);

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        btnSendCode.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(),
                                    "Reset link sent to your email (check spam)",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),
                                    "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });


        txtLogin.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment1());
        });

        // Navigate back to Login Fragment
//        txtLogin.setOnClickListener(v -> {
//            requireActivity().getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, new LoginFragment1())
//                    .commit();
//        });
//
//








    }

}
