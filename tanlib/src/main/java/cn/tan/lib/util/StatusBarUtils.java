package cn.tan.lib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by tanranran on 16/2/18-16:47.
 */
public final class StatusBarUtils {

    static final String TAG = "StatusBarUtils";
    boolean lightStatusBar;
    boolean transparentStatusBar;//沉浸式状态栏
    boolean transparentNavigationBar;//沉浸式导航栏
    Activity activity;

    private StatusBarUtils(Activity activity, boolean lightStatusBar, boolean transparentStatusBar,boolean transparentNavigationBar) {
        this.lightStatusBar = lightStatusBar;
        this.transparentStatusBar = transparentStatusBar;
        this.transparentNavigationBar=transparentNavigationBar;
        this.activity = activity;
    }

    public static boolean isKitkat() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    public static boolean isMoreLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Builder from(Activity activity) {
        return new StatusBarUtils.Builder().setActivity(activity);
    }

    /**
     * Default status dp = 24 or 25
     * mhdpi = dp * 1
     * hdpi = dp * 1.5
     * xhdpi = dp * 2
     * xxhdpi = dp * 3
     * eg : 1920x1080, xxhdpi, => status/all = 25/640(dp) = 75/1080(px)
     *
     * don't forget toolbar's dp = 48
     *
     * @return px
     */
    public static int getStatusBarHeight(Context context) {
        return getInternalDimensionSize(context.getApplicationContext().getResources(), "status_bar_height");
    }
    public static int getNavigationBarHeight(Context context) {
        Resources res = context.getApplicationContext().getResources();
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (!ViewConfiguration.get(context).hasPermanentMenuKey()) {
                String key;
                if ((res.getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
                    key = "navigation_bar_height";
                } else {
                    key = "navigation_bar_height_landscape";
                }
                return getInternalDimensionSize(res, key);
            }
        }
        return result;
    }
    public static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
    /**
     * 调用私有API处理颜色
     */
    public void processPrivateAPI() {
        processFlyme(lightStatusBar);
        processMIUI(lightStatusBar);
    }

    public void process() {
        //处理4.4沉浸
        if (isKitkat()) {
            processKitkat();
        }
        //6.0处理沉浸与颜色，5.0只可以处理沉浸(不建议用白色背景)
        if (isMoreLollipop()) {
            processLollipopAbove();
        }
        //处理导航栏沉浸式
        processTranslucentNavigation();
        //调用私有API处理颜色
        processPrivateAPI();
    }

    /**
     * 处理4.4沉浸
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) void processKitkat() {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (transparentStatusBar) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上
     * Tested on: MIUIV7 5.0 Redmi-Note3
     */
    void processMIUI(boolean lightStatusBar) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), lightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {

        }
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private void processFlyme(boolean isLightStatusBar) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        try {
            Class<?> instance = Class.forName("android.view.WindowManager$LayoutParams");
            int value = instance.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(lp);
            Field field = instance.getDeclaredField("meizuFlags");
            field.setAccessible(true);
            int origin = field.getInt(lp);
            if (isLightStatusBar) {
                field.set(lp, origin | value);
            } else {
                field.set(lp, (~value) & origin);
            }
        } catch (Exception ignored) {
            //
        }
    }

    /**
     * 处理Lollipop以上
     * Lollipop可以设置为沉浸，不能设置字体颜色
     * M(API23)可以设定
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP) void processLollipopAbove() {
        Window window = activity.getWindow();
        int flag = window.getDecorView().getSystemUiVisibility();
        if (lightStatusBar) {//改变字体颜色
            flag |= (WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        if (transparentStatusBar) {//沉浸式
            flag |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        }
        window.getDecorView().setSystemUiVisibility(flag);
        window.setStatusBarColor(Color.TRANSPARENT);
    }
    public void processTranslucentNavigation(){
        if(transparentNavigationBar){
            if(isMoreLollipop()) {
                activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);
            }else{
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }
    final public static class Builder {
        private Activity activity;
        private boolean lightStatusBar = false;
        private boolean transparentStatusBar = false;
        private boolean transparentNavigationBar=false;
        Builder setActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setLightStatusBar(boolean lightStatusBar) {
            this.lightStatusBar = lightStatusBar;
            return this;
        }

        public Builder setTransparentStatusBar(boolean transparentStatusbar) {
            this.transparentStatusBar = transparentStatusbar;
            return this;
        }
        public Builder setTransparentNavigationBar(boolean transparentNavigationBar) {
            this.transparentNavigationBar = transparentNavigationBar;
            return this;
        }

        public void process() {
            new StatusBarUtils(activity, lightStatusBar, transparentStatusBar,transparentNavigationBar).process();
        }
    }
}
