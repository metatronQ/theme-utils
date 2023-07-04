package com.example.themetest;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.second_container, SecondFragment.newInstance())
                .commit();
    }
}