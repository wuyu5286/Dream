package com.dream.app.utils;

import android.widget.Toast;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/9.
 * ============================================================
 */
public class ToastUtils {
    private static Toast toast;

    /**
     * 显示Toast
     */
    public static void show(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(UIUtils.getContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
