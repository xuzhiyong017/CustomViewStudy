package com.sky.customviewstudy.listener;

import android.animation.ValueAnimator;
import android.support.design.widget.CustomAppBarBehavior;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 15:19
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PhotoPickFragmentV322 implements ValueAnimator.AnimatorUpdateListener{

    float a = 0.0f;
    final /* synthetic */ CustomAppBarBehavior b;
    final /* synthetic */ CameraActivity c;


    public PhotoPickFragmentV322(CameraActivity photoPickFragmentV3, CustomAppBarBehavior customAppBarBehavior) {
        this.c = photoPickFragmentV3;
        this.b = customAppBarBehavior;
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float floatValue = ((Float) animation.getAnimatedValue()).floatValue();
        this.b.onNestedScroll(this.c.mMainContent, this.c.mAppbarLayout, this.c.mRecyclerView, 0, 0, 0, (int) (this.a - floatValue), 0);
        this.a = floatValue;


    }
}
