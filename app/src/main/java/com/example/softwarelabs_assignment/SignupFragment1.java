package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class SignupFragment1 extends Fragment {

    public SignupFragment1() {
        super(R.layout.signup_faairst);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find Views
        EditText etName = view.findViewById(R.id.et_name);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPhone = view.findViewById(R.id.et_phonenumber);
        EditText etPassword = view.findViewById(R.id.et_password);
        EditText etCPassword = view.findViewById(R.id.et_cpassword);

        MaterialButton btnContinue = view.findViewById(R.id.bt_createA);
        TextView txtLogin = view.findViewById(R.id.txt_login);

        // Continue Button
        btnContinue.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etCPassword.getText().toString().trim();

            //  Validation
            if (TextUtils.isEmpty(name) ||
                    TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(phone) ||
                    TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(confirmPassword)) {

                Toast.makeText(getContext(),
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(),
                        "Passwords do not match",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getContext(),
                        "Password must be at least 6 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Save Data to Activity
            SignupActivity activity = (SignupActivity) requireActivity();
            SignupData data = activity.signupData;

            data.fullName = name;
            data.email = email;
            data.phone = phone;
            data.password = password;

            // Move to Fragment 2
            activity.loadFragment(new SignupFragment2());
        });

        // Login Text Click
        txtLogin.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment1());
        });
    }
}