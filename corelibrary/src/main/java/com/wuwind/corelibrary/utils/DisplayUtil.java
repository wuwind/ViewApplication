package com.wuwind.corelibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Wuhf on 2016/4/8.
 * Description ：
 */
public class DisplayUtil {

    /**
     * dip 转 px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dip
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取屏幕大小
     *
     * @param context
     * @return Point.x screenWidth Point.y screenHeight
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
     * 计算ListView的高度,并设置给listview
     *
     * @param lv
     */
    public static int measureLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) lv.getLayoutParams();
            params.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
            return  params.height;
        }
        return -1;
    }

    /**
     * 计算GridView的高度,并设置给GridView
     *
     * @param gv
     */
    @SuppressLint("NewApi")
    public static int measureGridViewHeight(GridView gv) {
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
                params.height = totalHeight + (gv.getVerticalSpacing() * (adapter.getCount() - 1));
            } else {
                params.height = totalHeight;
            }
            gv.setLayoutParams(params);
            return totalHeight;
        }
        return -1;
    }

    /**
     * 获取 状态栏高度
     * @param context
     * @return
     */
    @Deprecated
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取 状态栏高度
     * @param view 已显示的view
     * @return
     */
    public static int getStatusBarHeight(View view) {
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /**
     * 添加悬浮于所有界面之上的view
     * @param context
     * @param x
     * @param y
     * @param view
     */
    public static void addWindowView(Context context, int x, int y, View view) {
        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.x = x;
        params.y = y;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        windowManager.addView(view, params);
    }

}
