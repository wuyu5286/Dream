package com.dream.app.fragment;

import android.view.View;

import com.dream.app.R;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/25.
 * ============================================================
 */
public class SelfFragment extends BaseFragment {

    public  static SelfFragment newInstance() {
        return new SelfFragment();
    }

    @Override
    protected int layoutResID() {
        return R.layout.fragment_self;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }
}
