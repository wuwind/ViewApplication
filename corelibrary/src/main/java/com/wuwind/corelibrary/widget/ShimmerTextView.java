package com.wuwind.corelibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Wuhf on 2016/6/2.
 * Description ：
 */
public class ShimmerTextView extends TextView {

    private int mViewWidth, mViewHeight;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;
    private boolean mAnimating = true;
    private int mTranslate = 0;
    public static final int left = 0, center_horizontal = 1, right = 2, top = 3, center_vertical = 4, bottom = 5;
    private int mode, shimmerColor = 0xff00ffff, speed = 50;
    private float mScale = 0.1f;

    public ShimmerTextView(Context context) {
        super(context);
    }

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mode < right) {

            if (mViewWidth == 0) {
                mViewWidth = getMeasuredWidth();
                if (mViewWidth > 0) {
                    mPaint = getPaint();
                    mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                            new int[]{getCurrentTextColor(), shimmerColor, getCurrentTextColor()},
                            new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                    mPaint.setShader(mLinearGradient);
                    mGradientMatrix = new Matrix();
                }
            }
        } else {

            if (mViewHeight == 0) {
                mViewHeight = getMeasuredHeight();
                if (mViewHeight > 0) {
                    mPaint = getPaint();

                    mLinearGradient = new LinearGradient(0, 0, 0, mViewHeight, new int[]{getCurrentTextColor(), shimmerColor, getCurrentTextColor()},
                            new float[]{0, 0.5f, 1}, Shader.TileMode.CLAMP);
                    mPaint.setShader(mLinearGradient);
                    mGradientMatrix = new Matrix();
                }
            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            switch (mode) {
                case left:
                    left();
                    break;
                case center_horizontal:
                    centerHorizontal();
                    break;
                case right:
                    right();
                    break;
                case top:
                    top();
                    break;
                case center_vertical:
                    centerVertical();
                    break;
                case bottom:
                    bottom();
                    break;
            }
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(speed);
        }
    }

    private void left() {
        mTranslate += mViewWidth / 10;
        if (mTranslate > mViewWidth) {
            mTranslate = -mViewWidth;
        }
        mGradientMatrix.setTranslate(mTranslate, 0);
    }

    private void right() {
        if (mTranslate <= -mViewWidth) {
            mTranslate = mViewWidth * 2;
        }
        mTranslate -= mViewWidth / 10;
        mGradientMatrix.setTranslate(mTranslate, 0);
    }

    private void top() {
        mTranslate += mViewHeight / 10;
        if (mTranslate >= mViewHeight * 2) {
            mTranslate = -mViewHeight;
        }
        mGradientMatrix.setTranslate(0, mTranslate);
    }

    private void bottom() {
        if (mTranslate <= -mViewHeight) {
            mTranslate = mViewHeight * 2;
        }
        mTranslate -= mViewHeight / 10;

        mGradientMatrix.setTranslate(0, mTranslate);
    }

    private void centerHorizontal() {
        mScale += 0.1f;
        if (mScale > 2f) {
            mScale = 0.01f;
        }
        mGradientMatrix.setScale(mScale, mScale, mViewWidth / 2, 0);
    }

    private void centerVertical() {
        mScale += 0.1f;
        if (mScale > 2f) {
            mScale = 0.1f;
        }
        mGradientMatrix.setScale(mScale, mScale, 0, mViewHeight / 2);
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setShimmerColor(int shimmerColor) {
        this.shimmerColor = shimmerColor;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDisplay(int mode, int shimmerColor, int speed) {
        this.mode = mode;
        this.speed = speed; this.shimmerColor = shimmerColor;
    }

}
