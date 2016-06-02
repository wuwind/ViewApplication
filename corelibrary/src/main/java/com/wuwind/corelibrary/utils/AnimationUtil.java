package com.wuwind.corelibrary.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工具类
 */
public class AnimationUtil {
    private static Animation topShowAnim;
    private static Animation topHideAnim;
    private static Animation bottomShowAnim;
    private static Animation bottomHideAnim;
    private static final int DEFAULT_DURATION = 200;

    static {
        bottomShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        bottomHideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f);
        bottomShowAnim.setDuration(DEFAULT_DURATION);
        bottomHideAnim.setDuration(DEFAULT_DURATION);
        topShowAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        topShowAnim.setDuration(DEFAULT_DURATION);
        topHideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        topHideAnim.setDuration(DEFAULT_DURATION);
    }

    /**
     * @param view
     * @param relativeRealPosition 相对自己真实的位置
     */
    public static void fromUpToDown(View view, boolean relativeRealPosition) {
        if (relativeRealPosition) {
            view.startAnimation(bottomHideAnim);
        } else {
            view.startAnimation(topShowAnim);
        }
    }

    /**
     * @param view
     * @param relativeRealPosition 相对自己真实的位置
     */
    public static void fromDownToUp(View view, boolean relativeRealPosition) {
        if (relativeRealPosition) {
            view.startAnimation(topHideAnim);
        } else {
            view.startAnimation(bottomShowAnim);
        }
    }

}
