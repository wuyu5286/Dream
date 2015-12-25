package com.dream.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dream.app.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * ============================================================
 * 描 述 :
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/25.
 * ============================================================
 */
public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (Activity) context;
        } else {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutResID(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int layoutResID();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    protected abstract void initView(View view);

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract void initData();
}
