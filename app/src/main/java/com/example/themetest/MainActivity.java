package com.example.themetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.themetest.databinding.ActivityMainBinding;
import com.example.themeutil.ThemeManager;

public class MainActivity extends AppCompatActivity implements ThemeManager.OnThemeChangedListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().bind(this, this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.testBlueBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveTheme);
        });
        binding.testRedBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveThemeDark);
        });
        binding.testFirstBt.setOnClickListener(v -> {
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onThemeChanged() {
        binding.invalidateAll();
    }
}