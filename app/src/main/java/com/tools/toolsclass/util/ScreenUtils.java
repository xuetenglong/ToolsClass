package com.tools.toolsclass.util;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.DisplayCutout;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.tools.toolsclass.R;
import com.tools.toolsclass.data.source.local.LocalDataSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


/**
 * ScreenUtils
 */
public class ScreenUtils {

    private static int mScreenHeight = 0;
    private static float density = Resources.getSystem().getDisplayMetrics().density;

    public static float getScreenDensity() {
        return density;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 虚拟像素
     * @return 像素
     */
    public static int dp2px(float dpValue) {
        // return (int) (0.5f + dpValue * density);
        return DensityUtil.dp2px(dpValue);
    }

    /**
     * dp转px  返回float类型
     *
     * @param context 上下文环境
     * @param dp      要转换的dp值
     * @return float类型
     */
    public static float dpToPx(@SuppressWarnings("unused") Context context, float dp) {
        /*
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
        */
        return dp * getScreenDensity();
    }

    /**
     * dp转px  返回int类型
     *
     * @param context 上下文环境
     * @param dp      要转换的dp值
     * @return int类型
     */
    public static int dpToPxInt(@SuppressWarnings("unused") Context context, float dp) {
        // return (int) (dpToPx(context, dp) + 0.5f);
        return dp2px(dp);
    }

    /**
     * sp转px
     *
     * @param context 上下文环境
     * @param spValue 要转换的spValue值
     * @return int类型
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float spToPx(float sp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 像素
     * @return 虚拟像素
     */
    public static float px2dp(int pxValue) {
        return (pxValue / density);
    }

    /**
     * px转dp
     *
     * @param context 上下文环境
     * @param px      要转换的px值
     * @return float类型
     */
    public static float pxToDp(Context context, float px) {
        if (context == null || px == 0) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    /**
     * px转dp
     *
     * @param context 上下文环境
     * @param px      要转换的px值
     * @return int类型
     */
    public static int pxToDpInt(Context context, float px) {
        return (int) (pxToDp(context, px) + 0.5f);
    }

    /**
     * 获取 status_bar_height 的高度
     *
     * @param mContext 上下文环境
     * @return int类型
     */
    public static int getStatusBarHeight(Context mContext) {
        int result = 0;
        if (mContext == null) {
            return result;
        }

        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取手机屏幕宽度
     *
     * @param context 上下文环境
     * @return int类型
     */
    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return 0;
        }
        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static Bitmap decodeBitmap(byte[] data, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
        int scaleFactor = calculateInSampleSize(bmOptions, width, height);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    || (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * 获取手机屏幕高度
     *
     * @param context 上下文环境
     * @return int类型
     */
    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        if (mScreenHeight == 0) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metric = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metric);
            mScreenHeight = metric.heightPixels;
        }
        return mScreenHeight;
    }

    /**
     * 当前是否是竖屏
     *
     * @param context context
     * @return boolean
     */
    public static final boolean isPortrait(Context context) {
        return context.getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 判断是否有NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            return context.getResources().getBoolean(id);
        }
        return false;
    }

    /**
     * 获取系统亮度
     * 取值在(0 -- 255)之间
     */
    public static int getSystemScreenBrightness(Context context) {
        int values = 0;
        try {
            values = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 设置当前屏幕量度0到255
     */
    public static void setActivityScreenBrightness(Activity activity, int paramInt) {
        if (activity == null) {
            return;
        }

        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = calculationScreenBrightnessValue(paramInt);
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 将0到255之间的值转化为0-1之前的浮点小数，代表量度
     *
     * @param paramInt 0-255
     * @return 最小为0：最低屏幕亮度，最大为1，最高屏幕亮度。
     */
    public static float calculationScreenBrightnessValue(int paramInt) {
        if (paramInt < 0) {
            paramInt = 0;
        }

        if (paramInt > 255) {
            paramInt = 255;
        }

        return paramInt / 255.0F;
    }

    public static void resetActivityScreenBrightness(Activity activity) {
        if (activity == null) {
            return;
        }

        Window localWindow = activity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        localLayoutParams.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        localWindow.setAttributes(localLayoutParams);
    }

    /**
     * 系统状态栏全透明。
     */
    public static void setStatusBarTransparent(@NonNull Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setStatusBarTransparentBlackText(@NonNull Activity activity) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.TRANSPARENT);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.color_99000000));
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void setStatusBarColor(@Nullable Activity activity, @ColorRes int color) {
        if (activity == null) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (color == android.R.color.transparent) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                return;
            }
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }


   /* public static int getSafeInsetTop(Context context) {
        int safeInsetTop = LocalDataSource.getSafeInsetTop();
        if (safeInsetTop == 0) {
            return getStatusBarHeight(context);
        }
        return safeInsetTop;
    }*/


    /**
     * 凹形屏/刘海屏安全高度 PX。
     */
    public static int getSafeInsetTop(Context context, @Nullable Window window) {
        int result = getStatusBarHeight(context);

        if (context == null) {
            return result;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (window != null) {
                View decorView = window.getDecorView();
                WindowInsets windowInsets = decorView.getRootWindowInsets();
                if (windowInsets != null) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    if (displayCutout != null) {
                        return displayCutout.getSafeInsetTop();
                    }
                }
            }
            return result;
        }

        // 华为/荣耀。
        if (hasNotchInScreen(context)) {
            // https://club.huawei.com/thread-15620158-1-1.html
            // if (Build.BRAND.equalsIgnoreCase("HONOR") || Build.BRAND.equalsIgnoreCase("HUAWEI")) {
            // }
            return result;
        }

        // 小米。
        try {
            @SuppressLint("PrivateApi") Class<?> clz = Class.forName("android.os.SystemProperties");
            Method mtd = clz.getMethod("getInt", String.class, int.class);
            int val = (int) mtd.invoke(null, "ro.miui.notch", 0);
            if (val == 1) {
                int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
                if (resourceId > 0) {
                    result = context.getResources().getDimensionPixelSize(resourceId);
                }
            }
            return result;
        } catch (Exception e) {
            Logger.e(e+"","");
        }

        // OPPO.
        if (context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism")) {
            return Constants.Screen.OPPO;
        }

        // VIVO。
        try {
            @SuppressLint("PrivateApi") Class<?> clz = Class.forName("android.util.FtFeature");
            Method method = clz.getMethod("isFeatureSupport", int.class);
            boolean val = (boolean) method.invoke(null, 0x00000020);
            if (val) {
                return ScreenUtils.dp2px(Constants.Screen.VIVO);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Logger.e(e+"","");
        }

        // 默认状态栏高度。
        return result;
    }

    /**
     * 判断是否刘海屏接口。
     */
    private static boolean hasNotchInScreen(Context context) {
        boolean ret = false;
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Logger.e("hasNotchInScreen ClassNotFoundException","");
        } catch (NoSuchMethodException e) {
            Logger.e("hasNotchInScreen NoSuchMethodException","");
        } catch (Exception e) {
            Logger.e("hasNotchInScreen Exception","");
        }
        return ret;
    }


}
