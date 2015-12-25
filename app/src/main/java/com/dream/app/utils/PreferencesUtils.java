package com.dream.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dream.app.constant.PreferencesKey;


/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/4.
 * ============================================================
 */
public class PreferencesUtils {
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private static PreferencesUtils mUtils = null;

    private PreferencesUtils() {
    }

    public static PreferencesUtils instance() {
        if (mUtils == null) {
            synchronized (PreferencesUtils.class) {
                mUtils = new PreferencesUtils();
            }
        }
        return mUtils;
    }

    public void init(Context context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences("dream.ini", context.MODE_PRIVATE);
    }

    public void init(Context context, String fileName) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(fileName, context.MODE_PRIVATE);
    }

    public int getInt(PreferencesKey dimension) {
        int value = mSharedPreferences.getInt(dimension.name(), 0);
        return value;
    }

    public void putInt(PreferencesKey dimension, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(dimension.name(), value);
        editor.commit();
    }

    public String getString(PreferencesKey dimension) {
        String value = mSharedPreferences.getString(dimension.name(), "");
        return value;
    }

    public void putString(PreferencesKey dimension, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(dimension.name(), value);
        editor.commit();
    }

    public boolean getBoolean(PreferencesKey dimension) {
        boolean value = mSharedPreferences.getBoolean(dimension.name(), false);
        return value;
    }

    public void putBoolean(PreferencesKey dimension, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(dimension.name(), value);
        editor.commit();
    }

    /**
     * 移除一项
     */
    public boolean remove(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        return editor.commit();

    }

    /**
     * 清除文件内容
     */
    public boolean clear() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        return editor.commit();

    }

    /**
     * 某一项是否存在
     */
    public boolean contatins(String key) {
        return mSharedPreferences.contains(key);
    }


}
