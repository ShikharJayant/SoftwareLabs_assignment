package com.example.softwarelabs_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.*;

import java.util.Arrays;

public class LoginFragment1 extends Fragment {

    public LoginFragment1() {super(R.layout.login_first);}

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        callbackManager = CallbackManager.Factory.create();

        // GOOGLE INIT
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // UI
        EditText etEmail = view.findViewById(R.id.et_email);
        EditText etPassword = view.findViewById(R.id.et_password);

        Button btnLogin = view.findViewById(R.id.btn_login);
        TextView txtForgot = view.findViewById(R.id.txt_forgotpass);
        TextView txtCreateAcc = view.findViewById(R.id.txt_signup);

        AppCompatImageButton btnFacebook = view.findViewById(R.id.btn_Facebook);
        AppCompatImageButton btnGoogle = view.findViewById(R.id.btn_Google);
        AppCompatImageButton btnApple = view.findViewById(R.id.btn_Apple);

        TextView txtLogout = view.findViewById(R.id.txtLogout);
        if (mAuth.getCurrentUser() != null) txtLogout.setVisibility(View.VISIBLE);
        else   txtLogout.setVisibility(View.GONE);

        txtForgot.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new LoginFragment2());
        });

        txtCreateAcc.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new SignupFragment1());
        });

        btnApple.setOnClickListener(v -> {
            ((SignupActivity) requireActivity())
                    .loadFragment(new HomeScreen1());
            Toast.makeText(requireContext()
                    ,"Welcome back",
                    Toast.LENGTH_SHORT).show();
        });

       //Logout
        txtLogout.setOnClickListener(v -> {

            mAuth.signOut();    //Firebase

            googleSignInClient.signOut();       //Google

            LoginManager.getInstance().logOut();    //Fb

            Toast.makeText(requireContext(),
                    "Logged out",
                    Toast.LENGTH_SHORT).show();

            // Refresh same screen
            ((SignupActivity) requireActivity()).loadFragment(new LoginFragment1());

        });

        // EMAIL LOGIN
        btnLogin.setOnClickListener(v -> {

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty()) {
                etEmail.setError("Enter email");
                return;
            }

            if (password.isEmpty()) {
                etPassword.setError("Enter password");
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {

                            Toast.makeText(requireContext(),
                                    "Login Successful",
                                    Toast.LENGTH_SHORT).show();

                            ((SignupActivity) requireActivity())
                                    .loadFragment(new HomeScreen1());

                        } else {
                            Toast.makeText(requireContext(),
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // FACEBOOK LOGIN
        btnFacebook.setOnClickListener(v -> {
            LoginManager.getInstance().logIn(
                    LoginFragment1.this,
                    Arrays.asList("email", "public_profile")
            );
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult result) {
                        handleFacebookAccessToken(result.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(requireContext(),
                                "Facebook Login cancelled",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(requireContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

        // GOOGLE LOGIN
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });



    }

    // FACEBOOK → FIREBASE
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {
                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        if (isNewUser) {
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new SignupFragment1());
                            Toast.makeText(requireContext(),
                                    "Complete the signup.",
                                    Toast.LENGTH_LONG).show();
                        }
                        else{
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new HomeScreen1());
                            Toast.makeText(requireContext(),
                                    "Facebook Login Successful",
                                    Toast.LENGTH_LONG).show();
                        }


                    }
                    else {

                        Exception e = task.getException();

                        if (e instanceof FirebaseAuthUserCollisionException) {

                            Toast.makeText(requireContext(),
                                    "Account already exists login via other methods.",
                                    Toast.LENGTH_LONG).show();

//                            linkFacebookAccount(token);

                        }
                        else {
                            Toast.makeText(requireContext(),
                                    e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void linkFacebookAccount(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());


        mAuth.getCurrentUser()
                .linkWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(requireContext(),
                                "Accounts linked successfully",
                                Toast.LENGTH_SHORT).show();

                        ((SignupActivity) requireActivity())
                                .loadFragment(new HomeScreen1());

                    } else {
                        Toast.makeText(requireContext(),
                                "Linking failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    // GOOGLE → FIREBASE
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {

                    if (task.isSuccessful()) {

                        boolean isNewUser = task.getResult()
                                .getAdditionalUserInfo()
                                .isNewUser();

                        if (isNewUser) {
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new SignupFragment1());
                            Toast.makeText(requireContext(),
                                    "New user complete signup process first",
                                    Toast.LENGTH_LONG).show();
                        }
                        else {
                            ((SignupActivity) requireActivity())
                                    .loadFragment(new HomeScreen1());
                            Toast.makeText(requireContext(),
                                    "Welcome back",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(requireContext(),
                                "Google Login Failed",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

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
                Toast.makeText(requireContext(),
                        "Google Sign-In Failed",
                        Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}