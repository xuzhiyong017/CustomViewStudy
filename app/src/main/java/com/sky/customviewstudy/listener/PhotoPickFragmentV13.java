package com.sky.customviewstudy.listener;

import android.animation.ValueAnimator;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 15:37
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PhotoPickFragmentV13 implements ValueAnimator.AnimatorUpdateListener{

    final /* synthetic */ CameraActivity a;

    public PhotoPickFragmentV13(CameraActivity cameraActivity){
        this.a = cameraActivity;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        this.a.b(1.0f - ((Float) animation.getAnimatedValue()).floatValue());


    }
}
