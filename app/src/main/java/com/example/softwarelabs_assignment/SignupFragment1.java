package com.example.softwarelabs_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.*;

import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;


public class SignupFragment1 extends Fragment {

    public SignupFragment1() {
        super(R.layout.signup_faairst);
    }

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;

    private static final int RC_SIGN_IN = 100;

    private TextView txtLogout;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        // 🔥 LOGOUT TEXT
        txtLogout = view.findViewById(R.id.txtLogout);

        // GOOGLE INIT
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // UI
        EditText etName = view.findViewById(R.id.et_name);
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPhone = view.findViewById(R.id.et_phonenumber);
        EditText etPassword = view.findViewById(R.id.et_password);
        EditText etCPassword = view.findViewById(R.id.et_cpassword);

        MaterialButton btnContinue = view.findViewById(R.id.bt_createA);
        TextView txtLogin = view.findViewById(R.id.txt_login);

        ImageButton btnGoogle = view.findViewById(R.id.btn_Google);
        ImageButton btnFacebook = view.findViewById(R.id.btn_Facebook);
        ImageButton btnApple = view.findViewById(R.id.btn_Apple);


        // 🔥 CHECK USER LOGIN STATE
        updateLogoutVisibility();

        // 🔥 AUTO-FILL IF USER LOGGED IN
        autoFillUser(etName, etEmail);

        // 🔥 LOGOUT CLICK
        txtLogout.setOnClickListener(v -> {

            // Firebase logout
            mAuth.signOut();

            // Google logout
            googleSignInClient.signOut();

            // Facebook logout
            LoginManager.getInstance().logOut();

            Toast.makeText(getContext(),
                    "Logged out successfully",
                    Toast.LENGTH_SHORT).show();

            // Hide logout button
            txtLogout.setVisibility(View.GONE);

            // Clear fields
            etName.setText("");
            etEmail.setText("");
        });

        btnApple.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new HomeScreen1());
            Toast.makeText(requireContext()
                    ,"Welcome",
                    Toast.LENGTH_SHORT).show();
        });



        // GOOGLE CLICK
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // FACEBOOK CLICK
        btnFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logIn(
                    SignupFragment1.this,
                    Arrays.asList("email", "public_profile")
            );
        });

        // FACEBOOK CALLBACK
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        handleFacebookAccessToken(result.getAccessToken(), etName, etEmail);
                    }

                    @Override
                    public void onCancel() {}

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(getContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        // CONTINUE BUTTON
        btnContinue.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etCPassword.getText().toString().trim();

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

            if (password.length() < 7) {
                Toast.makeText(getContext(),
                        "Password must be at least 7 characters",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            SignupActivity activity = (SignupActivity) requireActivity();
            SignupData data = activity.signupData;

            data.fullName = name;
            data.email = email;
            data.phone = phone;
            data.password = password;

            activity.loadFragment(new SignupFragment2());
        });

        txtLogin.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment1());
        });
    }

    // 🔥 SHOW / HIDE LOGOUT
    private void updateLogoutVisibility() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            txtLogout.setVisibility(View.VISIBLE);
        } else {
            txtLogout.setVisibility(View.GONE);
        }
    }

    // 🔥 AUTO-FILL
    private void autoFillUser(EditText etName, EditText etEmail) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null)
                etName.setText(user.getDisplayName());

            if (user.getEmail() != null)
                etEmail.setText(user.getEmail());
        }
    }

    // GOOGLE RESULT
    @Override
    @SuppressWarnings("deprecation")
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account =
                        task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                Toast.makeText(getContext(),
                        "Google Sign-In Failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // Google authentication
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        updateLogoutVisibility();

                        if (isNewUser) {
                            autoFillUser(
                                    getView().findViewById(R.id.et_name),
                                    getView().findViewById(R.id.et_email)
                            );
                            Toast.makeText(getContext(),
                                    "Complete the signup process",
                                    Toast.LENGTH_SHORT).show();

                        }
                        else {
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new HomeScreen1());
                            Toast.makeText(getContext(),
                                    "Welcome back",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // FACEBOOK AUTH
    private void handleFacebookAccessToken(AccessToken token,
                                           EditText etName,
                                           EditText etEmail) {

        AuthCredential credential =
                FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        updateLogoutVisibility();

                        if (isNewUser) {
                            autoFillUser(etName, etEmail);
                            Toast.makeText(getContext(),
                                    "Complete the signup process",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(),
                                    "Welcome back",
                                    Toast.LENGTH_SHORT).show();
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new HomeScreen1())
                            ;
                        }
                    }
                    else{
                        Exception e = task.getException();

                        if(e instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getContext(),
                                    "User already logged in. Try other method.",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(),
                                    e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}