package com.example.softwarelabs_assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

public class SignupFragment3 extends Fragment {

    private Uri selectedUri;   // stores selected file

    public SignupFragment3() {
        super(R.layout.signup_verification_1);
    }

    // Modern File Picker (No deprecated onActivityResult)
    private final ActivityResultLauncher<Intent> filePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == getActivity().RESULT_OK &&
                                result.getData() != null) {

                            selectedUri = result.getData().getData();

                            Toast.makeText(getContext(),
                                    "Document Selected",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView ivCamera = view.findViewById(R.id.iv_camera);
        MaterialButton btnContinue = view.findViewById(R.id.btn_Continue);
        MaterialButton btnBack = view.findViewById(R.id.btn_Back);

        // 📂 Open File Picker
        ivCamera.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // allow any file (image/pdf)
            filePickerLauncher.launch(intent);
        });

        // Continue Button
        btnContinue.setOnClickListener(v -> {

            if (selectedUri == null) {
                Toast.makeText(getContext(),
                        "Please upload verification document",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Save URI to SignupData
            SignupActivity activity = (SignupActivity) requireActivity();
            SignupData data = activity.signupData;
            data.proofUri = selectedUri.toString();

            // Move to Fragment 4
            activity.loadFragment(new SignupFragment4());
        });

        // Back Button
        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );
    }
}