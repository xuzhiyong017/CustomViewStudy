package com.sky.customviewstudy.listener;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorHelper;
import android.support.design.widget.CustomAppBarBehavior;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.sky.customviewstudy.activity.CameraActivity;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 14:00
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class RecycleTouchListener implements View.OnTouchListener {

    /* renamed from: a */
    float currentRawY;
    /* renamed from: b */
    int offsetY;
    /* renamed from: c */
    VelocityTracker velocityTracker;
    /* renamed from: d */
    final /* synthetic */ CameraActivity activity;

    public RecycleTouchListener(CameraActivity activity) {
        this.activity = activity;
        this.currentRawY = -1.0f;
        this.offsetY = Integer.MIN_VALUE;
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && this.activity.isInitState() && this.activity.mSelectedAdapter.getItemCount() == 1 && this.activity.mAdapter.getSelectedList() == 1) {
            return false;
        }

        Log.d("Reycycle","OnTouch");

        CustomAppBarBehavior f = this.activity.f();
        int a = f.getTopAndBottomOffset();
        Rect rect = new Rect();
        if (this.offsetY == Integer.MIN_VALUE) {
            this.offsetY = f.getTopAndBottomOffset();
            CoordinatorHelper.setNestedScrollAccepted(activity.mRecyclerView);
            CoordinatorHelper.setNestedScrollAccepted(this.activity.mAppbarLayout);
        }
        if (f != null) {
            boolean canScroll = false;
            CustomAppBarBehavior customAppBarBehavior = f;
            this.activity.mAppbarLayout.getHitRect(rect);
            if (!rect.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY()) || this.activity.i || (this.activity.mSelectedAdapter.getItemCount() != 0)) {
                if (a != 0) {
                    canScroll = true;
                    customAppBarBehavior = f;
                } else {
                    canScroll = false;
                    customAppBarBehavior = f;
                }
            } else if (!f.canScroll) {
                canScroll = true;
                customAppBarBehavior = f;
            }
            customAppBarBehavior.setCanScroll(canScroll);
        }
        Object obj = (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) ? 1 : null;
        if (f.canScroll && obj != null) {
            float a2 = getYVelocity();
            if (a < 0 && a > (-this.activity.mAppbarLayout.getTotalScrollRange())) {
                int i = a - this.offsetY;
                Object obj2 = Math.abs(i) > getPix(this.activity, 40.0f) ? 1 : null;
                Object obj3 = Math.abs(a2) > 200.0f ? 1 : null;
                Object obj4 = i > 0 ? 1 : null;
                if ((obj2 == null || obj4 == null) && ((obj2 != null || obj4 == null || obj3 == null) && !(obj2 == null && obj3 == null && obj4 == null))) {
                    this.activity.v();
                } else {
                    this.activity.w();
                }
            }
            this.currentRawY = -1.0f;
            this.offsetY = Integer.MIN_VALUE;
        } else if (f != null && this.activity.mSelectedAdapter.getItemCount() > 0) {
            this.activity.initHeight();
            if (!this.activity.i || ((this.currentRawY != -1.0f && motionEvent.getRawY() > this.currentRawY) || this.activity.mRecyclerView.computeVerticalScrollOffset() == 0)) {
                boolean z2 = this.activity.i;
                if (!z2 || this.currentRawY != -1.0f) {
                    float a3 = (float)getPix(this.activity, 80.0f);
                    float rawY = z2 ? motionEvent.getRawY() - this.currentRawY : activity.maxHeight - motionEvent.getRawY();
                    float min = Math.min(1.0f, Math.max(0.0f, rawY / (activity.maxHeight - activity.minHeight)));
                    switch (motionEvent.getAction()) {
                        case 1:
                        case 3:
                            long j;
                            float a4 = getYVelocity();
                            int i2 = z2 ? 200000 : 300000;
                            Object obj5;
                            if (z2) {
                                obj5 = (a4 * rawY > 500.0f * a3 || a4 > 500.0f || rawY > a3) ? 1 : null;
                                j = a4 > 500.0f ? (long) (((float) i2) / a4) : (long) (400.0f * min);
                            } else {
                                obj5 = (a4 * rawY > -500.0f * a3 || a4 < -500.0f || rawY > a3) ? 1 : null;
                                j = a4 < -500.0f ? (long) (((float) i2) / (-a4)) : (long) (400.0f * (1.0f - min));
                            }
                            if (!z2 && obj5 != null) {
                                this.activity.b(min, j);
                            } else if (!z2 && rawY > 0.0f) {
                                this.activity.a(1.0f - min, 400);
                            } else if (z2 && obj5 != null) {
                                this.activity.a(min, j);
                            } else if (z2) {
                                this.activity.b(1.0f - min, 400);
                            }
                            if (z2) {
                                this.currentRawY = -1.0f;
                                break;
                            }
                            break;
                        case 2:
                            if (this.velocityTracker == null) {
                                this.velocityTracker = VelocityTracker.obtain();
                            }
                            this.velocityTracker.addMovement(motionEvent);
                            if (z2 || rawY <= 0.0f) {
                                if (z2) {
                                    this.activity.a(1.0f - min);
                                    break;
                                }
                            }
                            this.activity.a(min);
                            break;
                    }
                }
                this.currentRawY = motionEvent.getRawY();
            }
        }
        if (!(obj == null || this.velocityTracker == null)) {
            this.velocityTracker.recycle();
            this.velocityTracker = null;
        }
        return false;

    }

    private float getYVelocity() {
        if (this.velocityTracker == null) {
            return -1.0f;
        }
        this.velocityTracker.computeCurrentVelocity(1000, 2000.0f);
        return this.velocityTracker.getYVelocity();
    }

    public static int getPix(Context paramContext, float paramFloat) {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

}
