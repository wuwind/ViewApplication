package com.wuwind.corelibrary.widget;

/**
 * Created by Wuhf on 2016/5/18.
 * Description ：闪烁的文本
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class FlickerTextView extends TextView {
    boolean change = false;
    private Handler handler = null;

    private int mColor;

    public FlickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        startFlicker();
    }

    public FlickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        startFlicker();
    }

    public FlickerTextView(Context context) {
        super(context);
        startFlicker();
    }

    public void startFlicker(){
        mColor = getCurrentTextColor();
        handler = new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                if(change){
                    change = false;
                    setTextColor(Color.parseColor("#ccffffff")); //这个是透明，=看不到文字
                }else{
                    change = true;
                    setTextColor(mColor);
                }
            }
        };

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task,1,800);  //参数分别是delay（多长时间后执行），duration（执行间隔）
    }


}