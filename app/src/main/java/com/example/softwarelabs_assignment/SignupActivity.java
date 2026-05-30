package com.example.softwarelabs_assignment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SignupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        String screen = getIntent().getStringExtra("screen");

        if ("login".equals(screen)) {
            loadFragment(new LoginFragment1());
        }
        else {
            loadFragment(new SignupFragment1()); // default signup
        }
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    //Signup Data
    public SignupData signupData = new SignupData();

}
