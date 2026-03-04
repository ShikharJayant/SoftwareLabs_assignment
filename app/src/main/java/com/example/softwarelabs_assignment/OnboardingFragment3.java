package com.example.softwarelabs_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OnboardingFragment3 extends Fragment {
    public OnboardingFragment3(){
        super(R.layout.activity_onboarding3);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnStart = view.findViewById(R.id.btnJoin);
//
//        btnStart.setOnClickListener(v -> {
//            Intent intent = new Intent(getActivity(), SignupActivity.class);
//            startActivity(intent);
//            requireActivity().finish();
//        });
//

        TextView txtLogin = view.findViewById(R.id.txt_Login);

//        txtLogin.setOnClickListener(v -> {
//            Intent intent = new Intent(requireActivity(), SignupActivity.class);
//            startActivity(intent);
//        });



        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SignupActivity.class);
            intent.putExtra("screen", "signup");
            startActivity(intent);
            requireActivity().finish();
        });

        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SignupActivity.class);
            intent.putExtra("screen", "login");
            startActivity(intent);
        });
    }
}

