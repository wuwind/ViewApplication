package com.wuwind.corelibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;
    private static TextView textView;
    private static int mBackground;

    private ToastUtil() {
        throw new AssertionError();
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * custom toast
     * @param context
     * @param msg   显示内容
     * @param duration 显示时长
     */
    public static void show(Context context, CharSequence msg, int duration) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            if (mBackground == 0) {
                toastView.setBackgroundColor(0xbb000000);
            } else {
                toastView.setBackgroundResource(mBackground);
            }
            toastView.removeAllViews();
            textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(0xffffffff);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            toastView.addView(textView, 0);
        } else {
            if (textView == null) {
                LinearLayout toastView = (LinearLayout) toast.getView();
                toastView.removeAllViews();
                LayoutParams p = new LayoutParams(
                        DisplayUtil.dip2px(context, 200), DisplayUtil.dip2px(
                        context, 100));
                textView = new TextView(context);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(0xffffffff);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                toastView.addView(textView, 0, p);
            } else {
                LinearLayout toastView = (LinearLayout) toast.getView();
                textView = (TextView) toastView.getChildAt(0);
            }
        }
        textView.setText(msg);
        toast.show();
    }


    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    /**
     * see {@link #show(Context, CharSequence, int)}
     */
    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }

    /**
     * 设置背景  默认半透明黑色长方形
     * @param background
     */
    public static void setmBackground(int background) {
        mBackground = background;
    }
}
