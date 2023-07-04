package com.example.themetest;

import android.app.Application;
import com.example.themeutil.ThemeManager;

/**
 * @author chenfu
 * @date 2023/7/3
 * description：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ThemeManager.getInstance().setup(this, com.example.themeutil.R.style.BaseLiveTheme);
    }
}
