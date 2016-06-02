package com.wuwind.corelibrary.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/11/6.
 */
public class TextEmptyButton extends Button {

    private TextView[] ets;
    private Context context;
    private Toast toast;

    public TextEmptyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setEditText(TextView... ets) {
        this.ets = ets;
    }

    public void addEditText(EditText et) {
        if (ets == null)
            ets = new EditText[]{et};
        else {
            EditText[] ets2 = new EditText[ets.length + 1];
            System.arraycopy(ets, 0, ets2, 0, ets.length);
            ets2[ets.length] = et;
            ets = ets2;
        }

    }

    private boolean checkEts() {
        if (ets != null)
            for (int i = 0; i < ets.length; i++) {
            	TextView et = ets[i];
                if (TextUtils.isEmpty(et.getText().toString().trim())) {
                    if (null == toast)
                        toast = Toast.makeText(context, et.getHint(), Toast.LENGTH_SHORT);
                    toast.setText(et.getHint());
                    toast.show();
                    return false;
                }
            }
        return true;
    }

    @Override
    public boolean performClick() {
        if (!checkEts())
            return true;
        return super.performClick();
    }
}
