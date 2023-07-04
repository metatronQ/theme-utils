package com.example.themeutil;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.app.ComponentActivity;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.Map;

/**
 * @author chenfu
 * description：设置主题及添加主题切换监听的管理类
 *
 * <p>
 * Required: livecycle、databinding
 * Suggested: databinding refresh
 * </p>
 *
 * <p>
 * 主题切换布局刷新的几个步骤
 * 1. 在Application中初始化初始参数：ThemeManager.getInstance().setup(this, style)。
 * 2. 绑定需要监听主题切换的lifecycle，如：ThemeManager.getInstance().bind(ComponentActivity, OnThemeChangedListener)；
 *  当lifecycle进入ON_DESTROY阶段会自动解绑并移除监听。
 * 3. 实现在databinding中使用的主题相关参数刷新方法，如：{@link #setThemeTextColor(TextView, int attrId)}；
 *  在布局中使用[app:]调用该方法，传递attr参数，可通过data标签来引入该参数（参考ThemeAttrs）。
 * 4. 使用{@link #setCurrentThemeRes(int)}切换主题，主题会自动设置application及activity或fragment的主题，
 *  并通知所有OnThemeChangedListener监听。
 * 5. 建议：OnThemeChangedListener回调中是你的自定义刷新方法，这里建议布局使用databinding，
 *  这样可以直接使用binding.invalidateAll()来自动刷新你在3中设置主题刷新的地方，而不需要重建activity或fragment。
 * </p>
 */
public class ThemeManager {

    private ThemeManager() {
        // 防止外部实例化
    }

    public static ThemeManager getInstance() {
        return ThemeManagerSingleton.INSTANCE;
    }

    private static class ThemeManagerSingleton {
        private static final ThemeManager INSTANCE = new ThemeManager();
    }

    @StyleRes
    private int currentThemeRes;

    private static Resources.Theme themeHelper;

    private Application mApp;

    public void setup(Application application, @StyleRes int styleRes) {
        this.mApp = application;
        // 初始化主题
        currentThemeRes = styleRes;
        themeHelper = application.getResources().newTheme();
        themeHelper.applyStyle(currentThemeRes, true);
        mApp.setTheme(styleRes);
        handleActivityTheme();
    }

    public @StyleRes int getCurrentThemeRes() {
        return currentThemeRes;
    }

    public void setCurrentThemeRes(@StyleRes int theme) {
        currentThemeRes = theme;
        themeHelper.applyStyle(currentThemeRes, true);
        notifyThemeChanged();
    }

    private void handleActivityTheme() {
        mApp.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                activity.setTheme(getCurrentThemeRes());
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(
                    @NonNull Activity activity, @NonNull Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    public void bind(LifecycleOwner lifecycleOwner, OnThemeChangedListener listener) {
        lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                switch (event) {
                    case ON_CREATE:
                        addThemeChangedListener(source, listener);
                        break;
                    case ON_DESTROY:
                        removeThemeChangedListener(source);
                        source.getLifecycle().removeObserver(this);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private final Map<LifecycleOwner, OnThemeChangedListener> notifyListeners = new ArrayMap<>();

    public void addThemeChangedListener(LifecycleOwner owner, OnThemeChangedListener listener) {
        notifyListeners.put(owner, listener);
    }

    public void removeThemeChangedListener(LifecycleOwner owner) {
        notifyListeners.remove(owner);
    }

    private void notifyThemeChanged() {
        mApp.setTheme(getCurrentThemeRes());
        for (Map.Entry<LifecycleOwner, OnThemeChangedListener> entry : notifyListeners.entrySet()) {
            LifecycleOwner owner = entry.getKey();
            if (owner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                return;
            }
            if (owner instanceof ComponentActivity) {
                ((ComponentActivity) owner).setTheme(getCurrentThemeRes());
            }
            if (owner instanceof Fragment && ((Fragment) owner).getActivity() != null) {
                ((Fragment) owner).getActivity().setTheme(getCurrentThemeRes());
            }
            entry.getValue().onThemeChanged();
        }
    }

    /**
     * 添加主题变化的监听器
     */
    public interface OnThemeChangedListener {
        /**
         * 主题变化通知
         */
        void onThemeChanged();
    }

    // region 实现主题切换通知databinding的方法

    // 设置TextView主题文字颜色
    @BindingAdapter("themeTextColor")
    public static void setThemeTextColor(TextView textView, @AttrRes int attrId) {
        try {
            TypedValue typedValue = new TypedValue();
            themeHelper.resolveAttribute(attrId, typedValue, true);
            int textColor = typedValue.data;
            textView.setTextColor(textColor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // endregion
}
