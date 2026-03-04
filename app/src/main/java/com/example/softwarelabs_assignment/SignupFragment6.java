package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignupFragment6 extends Fragment {
    public SignupFragment6(){
        super(R.layout.signup_final);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnFinish = view.findViewById(R.id.bt_gotit);


        btnFinish.setOnClickListener(v -> {
            requireActivity().finishAffinity();
        });

//        btnContinue.setOnClickListener(v -> {
//            ((SignupActivity) requireActivity())
//                    .loadFragment(new SignupFragment4());
//        });
    }
}
