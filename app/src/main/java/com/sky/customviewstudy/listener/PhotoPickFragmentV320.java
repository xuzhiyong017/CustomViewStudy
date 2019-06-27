package com.sky.customviewstudy.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.CustomAppBarBehavior;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 15:10
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PhotoPickFragmentV320 extends AnimatorListenerAdapter {

    final /* synthetic */ CustomAppBarBehavior a;
    final /* synthetic */ CameraActivity b;

    public PhotoPickFragmentV320(CameraActivity photoPickFragmentV3, CustomAppBarBehavior customAppBarBehavior) {
        this.b = photoPickFragmentV3;
        this.a = customAppBarBehavior;
    }


    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        if (this.a.getTopAndBottomOffset() != 0) {
            this.a.onNestedScroll(this.b.mMainContent, this.b.mAppbarLayout, this.b.mRecyclerView, 0, 0, 0, this.a.getTopAndBottomOffset() * 2, 0);
        }
//        PhotoPickFragmentV3.x();
        this.a.setCanScroll(false);


    }
}
