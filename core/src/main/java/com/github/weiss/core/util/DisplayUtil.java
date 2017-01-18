package com.github.weiss.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Window;

/**
 * 屏幕工具类
 * Created by Weiss on 2016/9/13.
 */

public class DisplayUtil {
    /**
     * 是否横屏
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 是否竖屏
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Get screen width, in pixels
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * Get screen height, in pixels
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * Get screen density, the logical density of the display
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * Get screen density dpi, the screen density expressed as dots-per-inch
     *
     * @param context
     * @return
     */
    public static int getScreenDensityDPI(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.densityDpi;
    }

    /**
     * Get titlebar height, this method cannot be used in onCreate(),onStart(),onResume(), unless it is called in the
     * post(Runnable).
     *
     * @param activity
     * @return
     */
    public static int getTitleBarHeight(Activity activity) {
        int statusBarHeight = getStatusBarHeight(activity);
        int contentViewTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;
        return titleBarHeight < 0 ? 0 : titleBarHeight;
    }

    /**
     * Get statusbar height, this method cannot be used in onCreate(),onStart(),onResume(), unless it is called in the
     * post(Runnable).
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * Get statusbar height
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight2(Activity activity) {
        int statusBarHeight = getStatusBarHeight(activity);
        if (0 == statusBarHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int id = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusBarHeight = activity.getResources().getDimensionPixelSize(id);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    /**
     * Convert dp to px by the density of phone
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return (int) (dipToPx(context, dp) + 0.5f);
    }

    /**
     * Convert dp to px
     *
     * @param context
     * @param dp
     * @return
     */
    private static float dipToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    /**
     * Convert px to dp by the density of phone
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return (int) (pxToDip(context, px) + 0.5f);
    }

    /**
     * Convert px to dp
     *
     * @param context
     * @param px
     * @return
     */
    private static float pxToDip(Context context, float px) {
        if (context == null) {
            return -1;
        }
        float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }

    /**
     * Convert px to sp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2sp(Context context, float px) {
        return (int) (pxToSp(context, px) + 0.5f);
    }

    /**
     * Convert px to sp
     *
     * @param context
     * @param px
     * @return
     */
    private static float pxToSp(Context context, float px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / fontScale;
    }

    /**
     * Convert sp to px
     *
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, float sp) {
        return (int) (spToPx(context, sp) + 0.5f);
    }

    /**
     * Convert sp to px
     *
     * @param context
     * @param sp
     * @return
     */
    private static float spToPx(Context context, float sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * fontScale;
    }

    /**
     * 将sp值转换为dp值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2dp(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)px2dip(context, (spValue * fontScale + 0.5f));
    }
}
