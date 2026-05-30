package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

public class SignupFragment4 extends Fragment {

    public SignupFragment4() {
        super(R.layout.signup_hours);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignupData signupData = ((SignupActivity) requireActivity()).signupData;

        // Day Buttons
        Button btnMon = view.findViewById(R.id.btnMon);
        Button btnTue = view.findViewById(R.id.btnTue);
        Button btnWed = view.findViewById(R.id.btnWed);
        Button btnThur = view.findViewById(R.id.btnThur);
        Button btnFri = view.findViewById(R.id.btnFri);
        Button btnSat = view.findViewById(R.id.btnSat);
        Button btnSun = view.findViewById(R.id.btnSun);

        setupDayToggle(btnMon, "Monday", signupData);
        setupDayToggle(btnTue, "Tuesday", signupData);
        setupDayToggle(btnWed, "Wednesday", signupData);
        setupDayToggle(btnThur, "Thursday", signupData);
        setupDayToggle(btnFri, "Friday", signupData);
        setupDayToggle(btnSat, "Saturday", signupData);
        setupDayToggle(btnSun, "Sunday", signupData);

        // Time Buttons
        Button t1 = view.findViewById(R.id.btn_time1);
        Button t2 = view.findViewById(R.id.btn_time2);
        Button t3 = view.findViewById(R.id.btn_time3);
        Button t4 = view.findViewById(R.id.btn_time4);
        Button t5 = view.findViewById(R.id.btn_time5);

        setupTimeToggle(t1, signupData);
        setupTimeToggle(t2, signupData);
        setupTimeToggle(t3, signupData);
        setupTimeToggle(t4, signupData);
        setupTimeToggle(t5, signupData);

        // Continue Button
        Button btnContinue = view.findViewById(R.id.btn_Continue);
        btnContinue.setOnClickListener(v -> {

            if (signupData.workingDays.isEmpty() ||
                    signupData.workingHours.isEmpty()) {

                // you can show Toast here if you want
                return;
            }


            ((SignupActivity) requireActivity())
                    .loadFragment(new SignupFragment5());
            // 🎉 Signup Complete
            // Next step: Save to Firebase / Database

        });

        // Back Button
        Button btnBack = view.findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );
    }




    private void setupDayToggle(Button button, String value, SignupData data) {
        button.setOnClickListener(v -> {
            if (data.workingDays.contains(value)) {
                data.workingDays.remove(value);
                button.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.grey)
                );
            } else {
                data.workingDays.add(value);
                button.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.btn_color1)
                );
            }
        });
    }




    private void setupTimeToggle(Button button, SignupData data) {
        button.setOnClickListener(v -> {
            String value = button.getText().toString();

            if (data.workingHours.contains(value)) {
                data.workingHours.remove(value);
                button.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.grey)
                );
            } else {
                data.workingHours.add(value);
                button.setBackgroundTintList(
                        ContextCompat.getColorStateList(requireContext(), R.color.btn_color2)
                );
            }
        });
    }
}