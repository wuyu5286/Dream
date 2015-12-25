package com.dream.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * ============================================================
 * 描 述 : 点击事件不会传到下一个布局
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/24.
 * ============================================================
 */
public class MenuFrameLayout extends FrameLayout {
    public MenuFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MenuFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuFrameLayout(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
