package com.wuwind.corelibrary.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.wuwind.corelibrary.utils.LogUtil;

/**
 * 自定义可以滑动的RelativeLayout, 类似于IOS的滑动删除页面效果，当我们要使用
 * 此功能的时候，需要将该Activity的顶层布局设置为SildingFinishLayout，
 * 然后需要调用setTouchView()方法来设置需要滑动的View
 */
public class SildingLayoutVertical extends RelativeLayout implements OnTouchListener {
    /**
     * SildingFinishLayout布局的父布局
     */
    private ViewGroup mParentView;
    /**
     * 处理滑动逻辑的View
     */
    private View touchView;
    /**
     * 滑动的最小距离
     */
    private int mTouchSlop;
    /**
     * 按下点的X坐标
     */
    private int downX;
    /**
     * 按下点的Y坐标
     */
    private int downY;
    /**
     * 临时存储X坐标
     */
    private int tempX;
    /**
     * 临时存储Y坐标
     */
    private int tempY;
    /**
     * 滑动类
     */
    private Scroller mScroller;
    /**
     * SildingFinishLayout的宽度
     */
    private int viewWidth;
    /**
     * SildingFinishLayout的高度
     */
    private int viewHeight;
    /**
     * 记录是否正在滑动
     */
    private boolean isSilding;

    private OnSildingFinishListener onSildingFinishListener;
    private boolean isFinish;

    GestureDetector detector;

    public SildingLayoutVertical(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SildingLayoutVertical(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);

        detector = new GestureDetector(getContext(), simpleOnGestureListener);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            // 获取SildingFinishLayout所在布局的父布局
            mParentView = (ViewGroup) this.getParent();
            viewWidth = this.getWidth();
            viewHeight = this.getHeight();
        }
    }

    /**
     * 设置OnSildingFinishListener, 在onSildingFinish()方法中finish Activity
     *
     * @param onSildingFinishListener
     */
    public void setOnSildingFinishListener(OnSildingFinishListener onSildingFinishListener) {
        this.onSildingFinishListener = onSildingFinishListener;
    }

    /**
     * 设置Touch的View
     *
     * @param touchView
     */
    public void setTouchView(View touchView) {
        this.touchView = touchView;
        touchView.setOnTouchListener(this);
    }

    public View getTouchView() {
        return touchView;
    }

    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mParentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta + 1, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollTop() {
        int delta = (viewHeight - mParentView.getScrollY());
        if (delta < 0)
            delta = 0;
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(0, mParentView.getScrollY(), 0, delta - 1, Math.abs(delta));
        postInvalidate();
    }

    private void scrollBottom() {
        final int delta = (viewHeight + mParentView.getScrollY());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta + 1, Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mParentView.getScrollX();
        mScroller.startScroll(mParentView.getScrollX(), 0, -delta, 0, Math.abs(delta));
        postInvalidate();
    }

    private void scrollOriginV() {
        int delta = mParentView.getScrollY();
        mScroller.startScroll(0, mParentView.getScrollY(), 0, -delta, Math.abs(delta));
        postInvalidate();
    }

    /**
     * touch的View是否是AbsListView， 例如ListView, GridView等其子类
     *
     * @return
     */
    private boolean isTouchOnAbsListView() {
        return touchView instanceof AbsListView ? true : false;
    }

    /**
     * touch的view是否是ScrollView或者其子类
     *
     * @return
     */
    private boolean isTouchOnScrollView() {
        return touchView instanceof ScrollView ? true : false;
    }

    GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            downY = tempY = (int) e.getRawY();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtil.e(0, "onScroll");
            int moveY = (int) e2.getRawY();
            int deltaY = tempY - moveY;
            LogUtil.e(0, deltaY);
            tempY = moveY;
            if (Math.abs(deltaY) > mTouchSlop && Math.abs((int) e2.getRawX() - e1.getRawX()) < mTouchSlop) {
                isSilding = true;
            }

            if ((deltaY >= 0 || mParentView.getScrollY() > 0) && isSilding) {
                mParentView.scrollBy(0, deltaY);
                // 屏蔽在滑动过程中ListView ScrollView等自己的滑动事件
                if (isTouchOnScrollView() || isTouchOnAbsListView()) {
                    return true;
                }
            }
            if (e2.getAction() == MotionEvent.ACTION_UP) {
                if (mParentView.getScrollY() >= viewHeight / 3) {
                    LogUtil.e(0, "isFinish  --- ");
                    isFinish = true;
                    scrollTop();
                } else {
                    scrollOriginV();
                    isFinish = false;
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            LogUtil.e(0, "onFling");
            actionUp(velocityY < -10);
            return true;
        }
    };

    private void actionUp(boolean flag) {
        isSilding = false;
        LogUtil.e(0, mParentView.getScrollY());
        if (flag) {
            LogUtil.e(0, "isFinish  --- ");
            isFinish = true;
            scrollTop();
        } else {
            scrollOriginV();
            isFinish = false;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            actionUp(mParentView.getScrollY() >= viewHeight / 3);
        }

        return detector.onTouchEvent(event);

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = tempX = (int) event.getRawX();
//                downY = tempY = (int) event.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int moveX = (int) event.getRawX();
//                int moveY = (int) event.getRawY();
//                int deltaX = tempX - moveX;
//                int deltaY = tempY - moveY;
//                tempX = moveX;
//                tempY = moveY;
//                if (Math.abs(downY - moveY) > mTouchSlop && Math.abs((int) event.getRawX() - downX) < mTouchSlop) {
//                    isSilding = true;
//
//                    // 若touchView是AbsListView，
//                    // 则当手指滑动，取消item的点击事件，不然我们滑动也伴随着item点击事件的发生
//                    if (isTouchOnAbsListView()) {
//                        MotionEvent cancelEvent = MotionEvent.obtain(event);
//                        cancelEvent.setAction(MotionEvent.ACTION_CANCEL
//                                | (event.getActionIndex() << MotionEvent.ACTION_POINTER_INDEX_SHIFT));
//                        v.onTouchEvent(cancelEvent);
//                    }
//                }
//
//                if (downY - moveY >= 0 && isSilding) {
//                    mParentView.scrollBy(0, deltaY);
//
//                    // 屏蔽在滑动过程中ListView ScrollView等自己的滑动事件
//                    if (isTouchOnScrollView() || isTouchOnAbsListView()) {
//                        return true;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                isSilding = false;
//                LogUtil.e(0, mParentView.getScrollY());
//                if (mParentView.getScrollY() >= viewHeight / 3) {
//                    LogUtil.e(0, "isFinish  --- ");
//                    isFinish = true;
//                    scrollTop();
//                } else {
//                    scrollOriginV();
//                    isFinish = false;
//                }
//                break;
//        }
//
//        // 假如touch的view是AbsListView或者ScrollView 我们处理完上面自己的逻辑之后
//        // 再交给AbsListView, ScrollView自己处理其自己的逻辑
//        if (isTouchOnScrollView() || isTouchOnAbsListView()) {
//            return v.onTouchEvent(event);
//        }
//
//        // 其他的情况直接返回true
//        return true;
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
            if (mScroller.isFinished()) {
                LogUtil.e(0, "mScroller  isFinish  --- ");
                if (onSildingFinishListener != null && isFinish) {
                    onSildingFinishListener.onSildingFinish();
                }
            }
        }
    }


    public interface OnSildingFinishListener {
        public void onSildingFinish();
    }

}
