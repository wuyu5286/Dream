package com.dream.app.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * ============================================================
 * 描 述 : 控件下面弹出框
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/11.
 * ============================================================
 */
public class BelowView {
    private View convertView;
    private Context mContext;
    private PopupWindow mPopupWindow;
    private int animationStyle;

    public BelowView(Context context, View convertView) {
        this.mContext = context;
        this.convertView = convertView;
    }

    public BelowView(Context context, int resource) {
        this.mContext = context;
        this.convertView = View.inflate(context, resource, null);
    }

    public void showBelowView(View view, boolean CanceledOnTouchOutside, int xoff, int yoff) {
        mPopupWindow = new PopupWindow(convertView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        mPopupWindow.setOutsideTouchable(CanceledOnTouchOutside);
        if (animationStyle == 0) {
        } else {
            mPopupWindow.setAnimationStyle(animationStyle);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.showAsDropDown(view, xoff, yoff);
    }

    public void setAnimation(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public View getBelowView() {
        return convertView;
    }

    public void dismissBelowView() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }
}
