package com.wuwind.corelibrary.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Locale;

/**
 * Created by Wuhf on 2016/4/8.
 * Description ：
 */
public class DisplayUtil {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕大小
     *
     * @return
     */
    public static Point getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return new Point(screenWidth, screenHeight);
    }

    /**
     * 测量view的宽高
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 获得精确的宽度、高度
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.width,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.height,
                View.MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算ListView的高度
     *
     * @param lv
     */
    public static void measureLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 计算GridView的高度
     *
     * @param gv
     */
    @SuppressLint("NewApi")
    public static void measureGridViewHeight(GridView gv) {
        ListAdapter adapter = gv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) gv.getLayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                params.height = totalHeight
                        + (gv.getVerticalSpacing() * (adapter.getCount() - 1));
            } else {
                params.height = totalHeight;
            }
            gv.setLayoutParams(params);
        }
    }

    /**
     * 计算ListView的高度，父控件为LinearLayout
     *
     * @param lv
     */
    public static void measureLinearLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 计算ListView的高度，父控件为FrameLayout
     *
     * @param lv
     */
    public static void measureFrameLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lv
                    .getLayoutParams();
            params.height = totalHeight
                    + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 判断设备是否是模拟器
     *
     * @return
     */
    public static boolean isEmulator() {
        return "sdk".equals(Build.PRODUCT) || "google_sdk".equals(Build.PRODUCT) || "generic".equals(Build.BRAND.toLowerCase(Locale.getDefault()));
    }

    /**
     * 得到设备id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void copyToClipboard(Context context, String text) {
        // if()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }
}
