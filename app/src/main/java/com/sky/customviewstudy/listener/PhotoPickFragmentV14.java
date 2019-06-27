package com.sky.customviewstudy.listener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.design.widget.CustomAppBarBehavior;
import android.support.v7.widget.LinearLayoutManager;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 15:10
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PhotoPickFragmentV14 extends AnimatorListenerAdapter {

    final /* synthetic */ CameraActivity a;

    public PhotoPickFragmentV14(CameraActivity photoPickFragmentV3) {
        this.a = photoPickFragmentV3;
    }


    @Override
    public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        int c = ((LinearLayoutManager) this.a.checkedPhotosRV.getLayoutManager()).getChildCount();
        this.a.checkedPhotosRV.scrollToPosition(c);
    }
}
