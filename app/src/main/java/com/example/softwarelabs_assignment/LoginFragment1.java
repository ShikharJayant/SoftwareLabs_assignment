package com.example.softwarelabs_assignment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment1 extends Fragment {
    public  LoginFragment1(){
        super(R.layout.login_first);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnContinue = view.findViewById(R.id.btn_login);

        btnContinue.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment1());
        });

        TextView txtForgot = view.findViewById(R.id.txt_forgotpass);

        txtForgot.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment2());
        });


        TextView txtCreateAcc = view.findViewById(R.id.txt_signup);

        txtCreateAcc.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new SignupFragment1());
        });





    }





}
