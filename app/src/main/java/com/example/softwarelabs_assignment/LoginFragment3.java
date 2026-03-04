package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment3 extends Fragment {
    public  LoginFragment3(){
        super(R.layout.login_otp);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnContinue = view.findViewById(R.id.btn_submit);

        btnContinue.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment4());
        });



        TextView txtLogin = view.findViewById(R.id.txt_login);

        txtLogin.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment1());
        });



    }


}
