package com.dream.app.fragment;

import android.support.v4.util.SparseArrayCompat;

import com.socks.library.KLog;

/**
 * ============================================================
 * 描 述 : Fragment工厂
 * 作 者 : 鸿浩
 * 时 间 : 2015/12/15.
 * ============================================================
 */
public class FragmentFactory {
    private static SparseArrayCompat<BaseFragment> compat = new SparseArrayCompat<>();

    public static BaseFragment getFragment(int position) {
        // 去缓存中取
        BaseFragment fragment = compat.get(position);
        if (fragment != null) {
            KLog.d("使用" + position + "的缓存");
            // 缓存中有
            return fragment;
        }
        switch (position) {
            case 0:// 首页
                fragment = new HomeFragment();
                break;
            case 1:// 一起买
                fragment = new TogetherFragment();
                break;
            case 2:// 九点抢
                fragment = new GrabFragment();
                break;
            case 3:// 我的
                fragment = new SelfFragment();
                break;
        }
        // 缓存起来
        KLog.d("为" + position + "缓存");
        compat.put(position, fragment);
        return fragment;
    }
}
