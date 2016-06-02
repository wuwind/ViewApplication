package com.wuwind.corelibrary.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.wuwind.corelibrary.R;

public class ToastUtil {

    private static Toast toast;
    private static TextView textView;

    private ToastUtil() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence msg, int duration) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastView = (LinearLayout) toast.getView();
            toastView.setBackgroundResource(R.drawable.shape_toast_bg);
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

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
