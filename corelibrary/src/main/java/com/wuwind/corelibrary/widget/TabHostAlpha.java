package com.wuwind.corelibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wuwind.corelibrary.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wuhf on 2016/4/23.
 * Description ：单选tab栏
 */
public class TabHostAlpha extends FrameLayout {

    private List<View> tabViews = new ArrayList<>();
    private OnTabChangeListener listener;
    private int selectedIndex; //当前选中index
    private LinearLayout backLinear, frontLinear;

    public TabHostAlpha(Context context) {
        this(context,null);
    }

    public TabHostAlpha(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backLinear = new LinearLayout(getContext());
        frontLinear = new LinearLayout(getContext());
        addView(backLinear);
        addView(frontLinear);
    }

    public interface OnTabChangeListener {
        void onChange(int position);
    }

    public void addTab(int[] imgs, String[] titles) {
        for (int i = 0; i < imgs.length; i++) {
            backLinear.addView(getTabView(imgs[i], titles[i]));
            View view = getTabView(imgs[i], titles[i]);
            tabViews.add(view);
            view.setTag(i);
            frontLinear.addView(view);
        }
        refreshSelected();
    }

    private View getTabView(int img, String text) {
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.item_tab, null);
        TabViewHolder tabViewHolder = new TabViewHolder(view);
        tabViewHolder.iconIv.setImageResource(img);
        tabViewHolder.tabText.setText(text);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        view.setLayoutParams(lp);
        view.setOnClickListener(clickListener);
        return view;
    }

    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onSelect((int) v.getTag());
        }
    };

    static class TabViewHolder {
        ImageView iconIv;
        TextView tabText;
        TabViewHolder(View view) {
            iconIv = (ImageView) view.findViewById(R.id.iconIv);
            tabText = (TextView) view.findViewById(R.id.tabText);
        }
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

        float appearAlpha = Math.abs(selectedIndex - position - percent);
        float disAlpha = 1f - appearAlpha;
        tabViews.get(selectedIndex).setAlpha(disAlpha);
        if ((position + percent) > selectedIndex) {
            //左滑
            tabViews.get(selectedIndex + 1).setAlpha(appearAlpha);
            tabViews.get(selectedIndex + 1).setSelected(true);
        } else if((position + percent) < selectedIndex) {
            //右滑
            tabViews.get(selectedIndex - 1).setAlpha(appearAlpha);
            tabViews.get(selectedIndex - 1).setSelected(true);
        }

    }
}





