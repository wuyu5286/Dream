package com.dream.app.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dream.app.R;
import com.dream.app.constant.ConstantValue;
import com.dream.app.fragment.FragmentFactory;
import com.dream.app.fragment.GrabFragment;
import com.dream.app.fragment.HomeFragment;
import com.dream.app.fragment.SelfFragment;
import com.dream.app.fragment.TogetherFragment;
import com.dream.app.utils.AnimatorUtils;
import com.dream.app.utils.ToastUtils;
import com.dream.app.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.main_open_menu)
    CircleImageView mOpenMenu;
    @Bind(R.id.main_tab)
    RadioGroup mTab;
    @Bind(R.id.main_menu)
    FrameLayout mMenu;
    @Bind(R.id.main_menu_item1)
    CircleImageView mMenuItem1;
    @Bind(R.id.main_menu_item4)
    CircleImageView mMenuItem4;
    @Bind(R.id.main_menu_item2)
    CircleImageView mMenuItem2;
    @Bind(R.id.main_menu_item3)
    CircleImageView mMenuItem3;
    @Bind(R.id.main_close_menu)
    CircleImageView mCloseMenu;
    private int index = 0;
    private HomeFragment mHomeFragment;
    private TogetherFragment mTogetherFragment;
    private GrabFragment mGrabFragment;
    private SelfFragment mSelfFragment;
    private FragmentManager mFragmentManager;
    private boolean isOpen = false;
    //点击退出时记录时间
    private long firstTime = 0;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mFragmentManager = getFragmentManager();
        mMenu.setOnClickListener(this);
        mOpenMenu.setOnClickListener(this);
        mCloseMenu.setOnClickListener(this);
        mMenuItem1.setOnClickListener(this);
        mMenuItem2.setOnClickListener(this);
        mMenuItem3.setOnClickListener(this);
        mMenuItem4.setOnClickListener(this);
        mTab.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        int position = intent.getIntExtra(ConstantValue.MAIN_INDEX, 0);
        onTabSelected(position);
    }

    private void onTabSelected(int position) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();//必须放在这里,每次调用都要实例化
        hideFragment(transaction);
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.setCustomAnimations(R.anim.fragment_slide_in_left,R.anim.fragment_slide_out_right);
//        transaction.setCustomAnimations(R.anim.fragment_slide_left_in, R.anim.fragment_slide_left_out, R.anim.fragment_slide_right_in, R.anim.fragment_slide_right_out);
        switch (position) {
            case 0:
                if (null == mHomeFragment) {
                    mHomeFragment = (HomeFragment) FragmentFactory.getFragment(0);
                    transaction.add(R.id.main_container, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }
                break;
            case 1:
                if (null == mTogetherFragment) {
                    mTogetherFragment = (TogetherFragment) FragmentFactory.getFragment(1);
                    transaction.add(R.id.main_container, mTogetherFragment);
                } else {
                    transaction.show(mTogetherFragment);
                }
                break;
            case 2:
                if (null == mGrabFragment) {
                    mGrabFragment = (GrabFragment) FragmentFactory.getFragment(2);
                    transaction.add(R.id.main_container, mGrabFragment);
                } else {
                    transaction.show(mGrabFragment);
                }
                break;
            case 3:
                if (null == mSelfFragment) {
                    mSelfFragment = (SelfFragment) FragmentFactory.getFragment(3);
                    transaction.add(R.id.main_container, mSelfFragment);
                } else {
                    transaction.show(mSelfFragment);
                }
                break;
        }
        index = position;
        transaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mHomeFragment != null) {
            transaction.hide(mHomeFragment);
        }
        if (mTogetherFragment != null) {
            transaction.hide(mTogetherFragment);
        }
        if (mGrabFragment != null) {
            transaction.hide(mGrabFragment);
        }
        if (mSelfFragment != null) {
            transaction.hide(mSelfFragment);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu:
                hideMenu();
                break;
            case R.id.main_open_menu:
                showMenu();
                break;
            case R.id.main_close_menu:
                hideMenu();
                break;
            case R.id.main_menu_item1:
                hideMenu();
                ToastUtils.show("1");
                break;
            case R.id.main_menu_item2:
                hideMenu();
                ToastUtils.show("2");
                break;
            case R.id.main_menu_item3:
                hideMenu();
                ToastUtils.show("3");
                break;
            case R.id.main_menu_item4:
                hideMenu();
                ToastUtils.show("4");
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.main_home:
                onTabSelected(0);
                break;
            case R.id.main_together:
                onTabSelected(1);
                break;
            case R.id.main_grab:
                onTabSelected(2);
                break;
            case R.id.main_self:
                onTabSelected(3);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ConstantValue.MAIN_INDEX, index);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        index = savedInstanceState.getInt(ConstantValue.MAIN_INDEX);
    }

    @Override
    public void onBackPressed() {
        if (!isOpen) {
            hideMenu();
            return;
        }
        long secondTime = System.currentTimeMillis();
        //如果两次按键的时间间隔大于1000毫秒,则不退出
        if (secondTime - firstTime > 1000) {
            ToastUtils.show("再按一次退出客户端");
            firstTime = secondTime;//更新firstTime
            return;
        }
        exitApp();
    }

    private void showMenu() {
        isOpen = false;
        mMenu.setVisibility(View.VISIBLE);
        List<Animator> animList = new ArrayList<>();
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(mCloseMenu, AnimatorUtils.rotation(0f, 225f));
        animList.add(anim);
        animList.add(showItemAnimator(mMenuItem1));
        animList.add(showItemAnimator(mMenuItem2));
        animList.add(showItemAnimator(mMenuItem3));
        animList.add(showItemAnimator(mMenuItem4));
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
//        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    private void hideMenu() {
        isOpen = true;
        List<Animator> animList = new ArrayList<>();
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(mCloseMenu, AnimatorUtils.rotation(225f, 0f));
        animList.add(anim);
        animList.add(hideItemAnimator(mMenuItem1));
        animList.add(hideItemAnimator(mMenuItem2));
        animList.add(hideItemAnimator(mMenuItem3));
        animList.add(hideItemAnimator(mMenuItem4));
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
//        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMenu.setVisibility(View.GONE);
            }
        });
        animSet.start();
    }

    private Animator showItemAnimator(View item) {
        float dx = mCloseMenu.getX() - item.getX();
        float dy = mCloseMenu.getY() - item.getY();
        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );
        return anim;
    }

    private Animator hideItemAnimator(final View item) {
        float dx = mCloseMenu.getX() - item.getX();
        float dy = mCloseMenu.getY() - item.getY();
        Animator anim = ObjectAnimator.ofPropertyValuesHolder(item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        return anim;
    }
}
