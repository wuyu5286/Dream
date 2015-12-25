package com.dream.app.widget;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.dream.app.utils.UIUtils;


/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/23.
 * ============================================================
 */
public class WrapRecyclerViewUtils {

    public static View getView(@LayoutRes int layoutResID, boolean isVertical) {
        View view = View.inflate(UIUtils.getContext(), layoutResID, null);
        if (isVertical) {
            view.setLayoutParams(new WrapRecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)); // vertical
        } else {
            view.setLayoutParams(new WrapRecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)); // horizontal
        }
        return view;
    }
}
