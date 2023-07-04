package com.example.themetest;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.themetest.databinding.ActivityFirstBinding;
import com.example.themeutil.ThemeManager;

public class FirstActivity extends AppCompatActivity implements ThemeManager.OnThemeChangedListener {

    private ActivityFirstBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().bind(this, this);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_first);
        binding.firstBlueBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveTheme);
        });
        binding.firstRedBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveThemeDark);
        });
        binding.toSecondBt.setOnClickListener(v -> {
            startActivity(new Intent(this, SecondActivity.class));
        });
    }

    @Override
    public void onThemeChanged() {
        binding.invalidateAll();
    }
}