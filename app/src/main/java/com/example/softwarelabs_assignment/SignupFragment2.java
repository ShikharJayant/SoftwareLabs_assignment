package com.example.softwarelabs_assignment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class SignupFragment2 extends Fragment {

    public SignupFragment2() {
        super(R.layout.signup_farminfo);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find Views
        EditText etBusinessName = view.findViewById(R.id.et_businessname);
        EditText etInformalName = view.findViewById(R.id.et_informalname);
        EditText etAddress = view.findViewById(R.id.et_address);
        EditText etCity = view.findViewById(R.id.et_city);
        EditText etZipCode = view.findViewById(R.id.et_zipcode);
        Spinner spinnerState = view.findViewById(R.id.spinner_state);

        MaterialButton btnContinue = view.findViewById(R.id.btn_Continue);
        MaterialButton btnBack = view.findViewById(R.id.btn_Back);

        //  Setup Spinner from arrays.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.india_states,
                android.R.layout.simple_spinner_dropdown_item
        );
        spinnerState.setAdapter(adapter);

        // Continue Button
        btnContinue.setOnClickListener(v -> {

            String businessName = etBusinessName.getText().toString().trim();
            String informalName = etInformalName.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String zipCode = etZipCode.getText().toString().trim();
            String state = spinnerState.getSelectedItem().toString();

            //  Validation
            if (TextUtils.isEmpty(businessName) ||
                    TextUtils.isEmpty(informalName) ||
                    TextUtils.isEmpty(address) ||
                    TextUtils.isEmpty(city) ||
                    TextUtils.isEmpty(zipCode)) {

                Toast.makeText(getContext(),
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (state.equals("Select State")) {
                Toast.makeText(getContext(),
                        "Please select a state",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //  Save Data to Activity
            SignupActivity activity = (SignupActivity) requireActivity();
            SignupData data = activity.signupData;

            data.businessName = businessName;
            data.informalName = informalName;
            data.address = address;
            data.city = city;
            data.state = state;
            data.zipCode = zipCode;

            // Move to Fragment 3
            activity.loadFragment(new SignupFragment3());
        });

        // Back Button
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}