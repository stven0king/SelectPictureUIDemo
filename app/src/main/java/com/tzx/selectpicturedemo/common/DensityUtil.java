package com.tzx.selectpicturedemo.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int gettDisplayWidth(Context ctx){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        return width;
    }

    public static int gettDisplayHight(Context ctx){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels; // 屏幕宽度（像素）
        return height;
    }

    public static void printDisplayMetrics(Context ctx){
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        float density = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi; // 屏幕密度DPI（120 / 160 / 240 / 320 / 480 ）
    }

    public static int getDeviceDensityDpi(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        return metric.densityDpi;
    }

    /**
     * 屏幕密度DPI（120 mdpi 160 hdpi 240 xhdpi 320 xxhdpi 480 xxxhdpi 640
     * @param ctx
     * @return
     */
    public static float getDeviceDensity(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        winManager.getDefaultDisplay().getMetrics(metric);
        return metric.density;
    }

    /**
     * 获取status bar 高度
     */
    public static int getStatusBarHeight(Context ctx) {
        int statusBarHeight = 0;
        try {
            /**
             * 通过反射机制获取StatusBar高度
             */
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int height = Integer.parseInt(field.get(object).toString());
            /**
             * 设置StatusBar高度
             */
            statusBarHeight = ctx.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusBarHeight;
    }

    public static float range(int percentage, float start, float end) {
        return (end - start) * (float)percentage / 100.0F + start;
    }

    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

    //通过此方法获取navigation bar的高度
    @TargetApi(14)
    public static  int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context)) {
                return getInternalDimensionSize(res, NAV_BAR_HEIGHT_RES_NAME);
            }
        }
        //Log.v("card", "NavigationBarHeight=" + result);
        return result;
    }

    @TargetApi(14)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
        if (resourceId != 0) {
            //Log.d("card", "hasNavBar: resourceId != 0");
            boolean hasNav = res.getBoolean(resourceId);
            // 查看是否有通过系统属性来控制navigation bar。
            if ("1".equals(getNavBarOverride())) {
                hasNav = false;
            } else if ("0".equals(getNavBarOverride())) {
                hasNav = true;
            }
            //Log.d("card", "hasNavBar: hasNav=" + hasNav);
            return hasNav;
        } else {
            //可通过此方法来查看设备是否存在物理按键(menu,back,home键)。
            //Log.d("card", "hasNavBar: resourceId == 0");
            if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)  {
                boolean hasMenuKey = ViewConfiguration.get(context)
                        .hasPermanentMenuKey();
                boolean hasBackKey = KeyCharacterMap
                        .deviceHasKey(KeyEvent.KEYCODE_BACK);
                //Log.d("card", "hasNavBar: hasMenuKey=" + hasMenuKey + " hasBackKey=" + hasBackKey);
                return !hasMenuKey && !hasBackKey;
            }
            return false;
        }
    }

    // 安卓系统允许修改系统的属性来控制navigation bar的显示和隐藏，此方法用来判断是否有修改过相关属性。
    // (修改系统文件，在build.prop最后加入qemu.hw.mainkeys=1即可隐藏navigation bar)
    // 相关属性模拟器中有使用。
    // 当返回值等于"1"表示隐藏navigation bar，等于"0"表示显示navigation bar。
    @TargetApi(19)
    public static String getNavBarOverride() {
        String isNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                isNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                isNavBarOverride = null;
            }
        }
        //Log.d("card", "getNavBarOverride: isNavBarOverride=" + isNavBarOverride);
        return isNavBarOverride;
    }

    //通过此方法获取资源对应的像素值
    public static  int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*
    * 判断是否是魅族系统
    * @return
    */
    public static boolean isFlyme() {

        try {
            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {

        }

        if (Build.DEVICE.equals("mx2")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

    // 获取魅族SmartBar高度
    public static int getSmartBarHeight(Context  zcontext) {
        final Context context = zcontext.getApplicationContext();
        final boolean isMeiZu = Build.MANUFACTURER.equals("Meizu");

        final boolean autoHideSmartBar = Settings.System.getInt(context.getContentResolver(),
                "mz_smartbar_auto_hide", 0) == 1;

        if (isMeiZu) {
            if (autoHideSmartBar) {
                return 0;
            } else {
                try {
                    Class c = Class.forName("com.android.internal.R$dimen");
                    Object obj = c.newInstance();
                    Field field = c.getField("mz_action_button_min_height");
                    int height = Integer.parseInt(field.get(obj).toString());
                    return context.getResources().getDimensionPixelSize(height);
                } catch (Throwable e) {
                    // 不自动隐藏smartbar同时又没有smartbar高度字段供访问，取系统navigationbar的高度
                    return getNavigationBarHeight(context);
                }
            }
        } else {
            return 0;
        }
    }
}