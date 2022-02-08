package com.example.sw0b_001.Test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.sw0b_001.R;
import com.example.sw0b_001.Security.DHKeyAgreement2;

public class TestSecurityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_security);

        try {
            DHKeyAgreement2.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}