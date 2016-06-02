package com.wuwind.corelibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuhf on 2016/4/23.
 * Description ：单选tab栏
 */
public class TabHost extends LinearLayout {

    private List<View> tabViews = new ArrayList<>();
    private OnTabChangeListener listener;
    private int selectedIndex; //当前选中index

    public TabHost(Context context) {
        super(context);
    }

    public TabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface OnTabChangeListener {
        void onChange(int position);
    }

    public void addTab(View tabView) {
        tabViews.add(tabView);
        refresh();
    }

    private void refresh() {
        int index = tabViews.size() - 1;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        View view = tabViews.get(index);
        view.setTag(index);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelect((int) v.getTag());
            }
        });
        view.setSelected(index == 0);
        addView(view, index, lp);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void addOnTabChangeListener(final OnTabChangeListener listener) {
        this.listener = listener;
    }

    public void setSelected(int selectedIndex) {
        onSelect(selectedIndex);
    }

    private void onSelect(int index) {
        if (selectedIndex == index)
            return;
        if (index >= tabViews.size())
            throw new IllegalArgumentException("index is out tabviews");
        if (null != listener)
            listener.onChange(index);
        selectedIndex = index;
        refreshSelected();
    }


    public void refreshSelected() {
        for (int i = 0; i < tabViews.size(); i++) {
            if (i == selectedIndex) {
                tabViews.get(i).setSelected(true);
                continue;
            }
            tabViews.get(i).setSelected(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onAlphaChange(int position, float percent) {
        if (percent <= 0) {
            refreshSelected();
            return;
        }
        float appearAlpha = Math.abs(selectedIndex - position - percent);
        float disAlpha = 1f - appearAlpha;

        if (disAlpha > 0.5) {
            tabViews.get(selectedIndex).setAlpha(disAlpha);
            if ((position + percent) > selectedIndex) {
                //左滑
                tabViews.get(selectedIndex + 1).setAlpha(disAlpha);
            } else {
                //右滑
                tabViews.get(selectedIndex - 1).setAlpha(disAlpha);
            }
        } else {
            tabViews.get(selectedIndex).setSelected(false);
            tabViews.get(selectedIndex).setAlpha(appearAlpha);
            if ((position + percent) > selectedIndex) {
                //左滑
                tabViews.get(selectedIndex + 1).setAlpha(appearAlpha);
                tabViews.get(selectedIndex + 1).setSelected(true);
            } else {
                //右滑
                tabViews.get(selectedIndex - 1).setAlpha(appearAlpha);
                tabViews.get(selectedIndex - 1).setSelected(true);
            }
        }

    }
}





