# theme-utils
livecycle+databinding可实现主题实时切换

## 步骤
1. 在Application中初始化初始参数：ThemeManager.getInstance().setup(this, style)。
2. 绑定需要监听主题切换的lifecycle，如：ThemeManager.getInstance().bind(ComponentActivity, OnThemeChangedListener)；当lifecycle进入ON_DESTROY阶段会自动解绑并移除监听。
3. 实现在databinding中使用的主题相关参数刷新方法，如：{@link #setThemeTextColor(TextView, int attrId)}；在布局中使用[app:]调用该方法，传递attr参数，可通过data标签来引入该参数（参考ThemeAttrs）。
4. 使用{@link #setCurrentThemeRes(int)}切换主题，主题会自动设置application及activity或fragment的主题，并通知所有OnThemeChangedListener监听。
5. 建议：OnThemeChangedListener回调中是你的自定义刷新方法，这里建议布局使用databinding，这样可以直接使用binding.invalidateAll()来自动刷新你在3中设置主题刷新的地方，而不需要重建activity或fragment。
