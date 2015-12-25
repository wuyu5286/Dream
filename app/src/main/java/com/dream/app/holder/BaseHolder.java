package com.dream.app.holder;

import android.view.View;

import com.dream.app.utils.UIUtils;

import butterknife.ButterKnife;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/25.
 * ============================================================
 */
public abstract class BaseHolder<Data> {
    private View mView;
    private Data mData;

    public BaseHolder() {
        mView = View.inflate(UIUtils.getContext(), layoutResID(), null);
        ButterKnife.bind(this, mView);
    }

    public void setData(Data data) {
        this.mData = data;
        refreshView(mData);
    }

    public View getView() {
        return mView;
    }

    protected abstract int layoutResID();

    protected abstract void refreshView(Data data);
}
