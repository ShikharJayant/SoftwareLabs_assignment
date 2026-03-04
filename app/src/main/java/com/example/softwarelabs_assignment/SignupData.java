package com.example.softwarelabs_assignment;

import android.net.Uri;

import java.util.ArrayList;

public class SignupData {
    public String fullName;
    public String email;
    public String phone;
    public String password;

    public String businessName;
    public String informalName;
    public String address;
    public String city;
    public String state;
    public String zipCode;

    public ArrayList<String> workingDays = new ArrayList<>();
    public ArrayList<String> workingHours = new ArrayList<>();

    public String proofUri;

    public SignupData(){
    }
}
