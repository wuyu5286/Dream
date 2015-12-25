package com.dream.app.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/25.
 * ============================================================
 */
public  class BaseActivity extends AppCompatActivity {
    protected Activity mActivity;
    /**
     * 管理运行的所有的Activity
     */
    public final static List<BaseActivity> activities = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        synchronized (activities) {
            activities.add(this);
        }
        mActivity = this;
    }



    @Override
    protected final void onPause() {
        super.onPause();
        mActivity = null;
    }


    @Override
    protected final void onResume() {
        super.onResume();
        mActivity = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (activities) {
            activities.remove(this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //关闭窗体动画显示
        overridePendingTransition(0, android.R.anim.fade_out);
    }

    public static void exitApp() {
        ListIterator<BaseActivity> iterator = activities.listIterator();
        while (iterator.hasNext()) {
            Activity next = iterator.next();
            next.finish();
        }
    }
}
