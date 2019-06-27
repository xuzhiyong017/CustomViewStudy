package com.sky.customviewstudy.listener;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/24 14:22
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class DragBarTouchListener implements View.OnTouchListener {

    /* renamed from: a */
    float a;
    /* renamed from: b */
    float b;
    /* renamed from: c */
    float c;
    /* renamed from: d */
    final /* synthetic */ CameraActivity activity;

    public DragBarTouchListener(CameraActivity photoPickFragmentV3) {
        this.activity = photoPickFragmentV3;
    }

    @Override
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.b = motionEvent.getRawY();
            this.a = motionEvent.getRawX();
            this.c = (float) this.activity.mAppbarLayout.getHeight();
        } else if (motionEvent.getAction() == 2) {
            this.activity.b(1.0f - Math.max(0.0f, Math.min((((motionEvent.getRawY() - this.b) + this.c) - activity.minHeight) / (activity.maxHeight - activity.minHeight), 1.0f)));
        } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
            float a = (float) getPix(activity, 200.0f);
            if ((Math.abs((float) this.activity.mAppbarLayout.getHeight()) - this.c) >a) {
                this.activity.k();
            } else {
                this.activity.l();
            }
        }

        return true;
    }

    public static int getPix(Context paramContext, float paramFloat)
    {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

}
