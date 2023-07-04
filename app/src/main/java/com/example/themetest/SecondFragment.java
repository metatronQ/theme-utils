package com.example.themetest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.themetest.databinding.FragmentSecondBinding;
import com.example.themeutil.ThemeManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment implements ThemeManager.OnThemeChangedListener {

    public SecondFragment() {
        // Required empty public constructor
    }

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    private FragmentSecondBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeManager.getInstance().bind(this, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        binding.secondBlueBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveTheme);
        });
        binding.secondRedBt.setOnClickListener(v -> {
            ThemeManager.getInstance().setCurrentThemeRes(com.example.themeutil.R.style.BaseLiveThemeDark);
        });
        return binding.getRoot();
    }

    @Override
    public void onThemeChanged() {
        binding.invalidateAll();
    }
}