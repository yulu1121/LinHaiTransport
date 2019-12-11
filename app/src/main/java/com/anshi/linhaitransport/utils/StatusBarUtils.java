package com.anshi.linhaitransport.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *
 * Created by yulu on 2017/11/14.
 */

public class StatusBarUtils {
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(dialog.getContext(),colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变魅族的状态栏字体为黑色，要求FlyMe4以上
     */
    private static void processFlyMe(boolean isLightStatusBar, Activity activity) {
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
            ignored.printStackTrace();
        }
    }

    /**
     * 改变小米的状态栏字体颜色为黑色, 要求MIUI6以上  lightStatusBar为真时表示黑色字体
     */
    private static void processMIUI(boolean lightStatusBar, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags",int.class,int.class);
            extraFlagField.invoke(activity.getWindow(), lightStatusBar? darkModeFlag : 0, darkModeFlag);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    /**
     * 判断手机是否是小米
     * @return
     */
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    /**
     * 判断手机是否是魅族
     * @return
     */
    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * 设置状态栏文字色值为深色调
     * @param useDart 是否使用深色调
     * @param activity
     */
    public static void setStatusTextColor(boolean useDart, Activity activity) {
        if (isFlyme()) {
            processFlyMe(useDart, activity);
        } else if (isMIUI()) {
            processMIUI(useDart, activity);
        } else {
            if (useDart) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            activity.getWindow().getDecorView().findViewById(android.R.id.content).setPadding(0, 0, 0,0);
        }
    }

    public static int getStatusBarHeight(Context context) {

        Resources resources = context.getResources();

        int resourceId = resources.getIdentifier("status_bar_height","dimen","android");

        return resources.getDimensionPixelSize(resourceId);

    }

    public static void setLightNavigationBar (Activity activity, boolean light) {
    int vis = activity.getWindow().getDecorView().getSystemUiVisibility();
    if (light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vis |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;  // 黑色
        }
    } else {
     //白色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vis &= ~ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
    }
    activity.getWindow().getDecorView().setSystemUiVisibility(vis);
    }

    /**
     * 判断是否显示了导航栏
     * (说明这里的context 一定要是activity的context 否则类型转换失败)
     *
     * @param context
     * @return
     */
    public static boolean isShowNavBar(Context context) {
        if (null == context) {
            return false;
        }
        /**
         * 获取应用区域高度
         */
        Rect outRect1 = new Rect();
        try {
            ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        int activityHeight = outRect1.height();
        /**
         * 获取状态栏高度
         */
       // int statuBarHeight = getStatusBarHeight(context);
        /**
         * 屏幕物理高度 减去 状态栏高度
         */
        int remainHeight = getRealHeight(context);
        /**
         * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
         */
        return activityHeight != remainHeight;

    }

    private static int getRealHeight(Context context) {
        WindowManager wm = (WindowManager)
                    context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (wm != null) {
                wm.getDefaultDisplay().getRealSize(point);
            }
        } else {
            if (wm != null) {
                wm.getDefaultDisplay().getSize(point);
            }
        }
        return point.y;
    }

}
