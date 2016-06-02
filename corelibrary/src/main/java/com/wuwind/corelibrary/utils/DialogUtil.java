package com.wuwind.corelibrary.utils;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;

import com.wuwind.corelibrary.R;


/**
 * APP对话框的样式统一控制
 */
public class DialogUtil {

    /**
     * 使用统一的风格创建对话框
     *
     * @param context
     * @param layoutResID 布局文件id
     * @return Dialog
     */
    public static Dialog createDialog(Context context, int layoutResID) {
        Dialog dialog = new MDialog(context, R.style.base_dialog);
        dialog.setContentView(layoutResID);
        return dialog;
    }

    public static Dialog createDialog(Context context, View view) {
        Dialog dialog = new MDialog(context, R.style.base_dialog);
        dialog.setContentView(view);
        return dialog;
    }

    public static Dialog createDialogAndShow(Context context, int layoutResID) {
        Dialog dialog = createDialog(context, layoutResID);
        dialog.show();
        return dialog;
    }

    public static void show(Dialog dialog) {
        if (dialog != null && dialog.isShowing())
            return;
        dialog.show();
    }

    public static void dissmiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void setShowLong(Dialog dialog, long showLong) {
        if (dialog != null && dialog instanceof MDialog) {
            ((DialogUtil.MDialog) (dialog)).setShowLong(showLong);
        }
    }

    public static void showAtBottom(Dialog dialog, boolean flag) {
        if (dialog != null && dialog instanceof MDialog) {
            ((DialogUtil.MDialog) (dialog)).showAtBottom();
        }
    }

    public static class MDialog extends Dialog implements DialogInterface.OnDismissListener {

        public long createTime;
        public long showTime;
        public long showLong;
        private OnDismissListener onDismissListener;

        public MDialog(Context context) {
            this(context, 0);
        }

        public MDialog(Context context, int themeResId) {
            super(context, themeResId);
            createTime = SystemClock.uptimeMillis();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
            super.setOnDismissListener(this);
        }

        public void setShowLong(long showLong) {
            this.showLong = showLong;
        }

        @Override
        public void setOnDismissListener(OnDismissListener listener) {
            onDismissListener = listener;
        }

        @Override
        public void show() {
            super.show();
            showTime = SystemClock.uptimeMillis();
        }

        @Override
        public void dismiss() {
            if (showLong > 0) {
                long timeNow = SystemClock.uptimeMillis();
                if (timeNow - showTime > showLong)
                    super.dismiss();
            } else
                super.dismiss();
        }

        public void showAtBottom() {
            show();
            int height = DisplayUtil.getDisplayMetrics(getContext()).y;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.x = 0;
            params.y = height - params.height;
            getWindow().setAttributes(params);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            if (onDismissListener != null)
                onDismissListener.onDismiss(dialog);
        }
    }

}
