package com.wuwind.viewapplication.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wuwind.corelibrary.utils.LogUtil;

/**
 * Created by Wuhf on 2016/6/1.
 * Description ：
 */
public class ExpandView extends LinearLayout {

    private View title, children, rotationView;
    private boolean loadOnce;
    private int height;
    private static final int speed = 400;
    private boolean isShowing, isDismissing;

    private ValueAnimator dismissAnimator;
    private ValueAnimator showAnimator;


    public ExpandView(Context context) {
        this(context, null);
    }

    public ExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            loadOnce = true;
            init();
        }
    }

    private void init() {
        title = getChildAt(0);
        if (title == null)
            throw new IllegalArgumentException("expandview tilte is null");
        children = getChildAt(1);
        if (children == null)
            throw new IllegalArgumentException("expandview children is null");
        height = children.getMeasuredHeight();
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
                LogUtil.e(0, "onClick");
            }
        });
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        rotationView = viewGroup.getChildAt(1);
    }

    public void setVisiable() {
        if (!children.isShown()) {
            showAnimation();
        }
    }

    public void setInVisiable() {
        if (children.isShown()) {
            dismissnimation();
        }
    }

    public void toggle() {
        if (children.isShown())
            setInVisiable();
        else
            setVisiable();
    }

    private void showAnimation() {
        if (isShowing || isDismissing)
            return;

        if (showAnimator == null) {
            showAnimator = ValueAnimator.ofFloat(0, 1);
            showAnimator.setDuration(speed);
            showAnimator.setTarget(children);
            showAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    LogUtil.e(0, ((float) animation.getAnimatedValue()));
                    int heightTemp = (int) (height * percent);
                    LogUtil.e(0, heightTemp);
                    ViewGroup.LayoutParams layoutParams = children.getLayoutParams();
                    layoutParams.height = heightTemp;
                    children.setLayoutParams(layoutParams);
                    if (null != rotationView)
                        rotationView.setRotation(percent * 180);
                }
            });
            showAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    children.setVisibility(View.VISIBLE);
                    isShowing = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isShowing = false;
                }
            });
        }
        showAnimator.start();

    }

    private void dismissnimation() {
        if (isShowing || isDismissing)
            return;
        if (dismissAnimator == null) {
            dismissAnimator = ValueAnimator.ofFloat(1, 0);
            dismissAnimator.setDuration(speed);
            dismissAnimator.setTarget(children);
            dismissAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = (float) animation.getAnimatedValue();
                    LogUtil.e(0, ((float) animation.getAnimatedValue()));
                    int heightTemp = (int) (height * percent);
                    LogUtil.e(0, heightTemp);
                    ViewGroup.LayoutParams layoutParams = children.getLayoutParams();
                    layoutParams.height = heightTemp;
                    children.setLayoutParams(layoutParams);
                    if (null != rotationView)
                        rotationView.setRotation(percent * 180);
                }

            });
            dismissAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isDismissing = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    children.setVisibility(View.GONE);
                    isDismissing = false;
                }
            });
        }
        dismissAnimator.start();
    }

}
