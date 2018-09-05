package com.sky.customviewstudy.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent2;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v4.view.NestedScrollingParent;
import android.widget.Scroller;

/**
 * @author: xuzhiyong
 * @date: 2018/9/1 11:21
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class NestedScrollView extends FrameLayout implements NestedScrollingParent2 {

    private NestedScrollingParentHelper mScrollingParentHelper;
    private Scroller mScroller;


    private int initTop = 200;
    private int curTop = 0;

    public NestedScrollView(Context context) {
        this(context, null);
    }

    public NestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mScrollingParentHelper =  new NestedScrollingParentHelper(this);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            Log.d("NestedScrollView","-----getCurrX------       "+mScroller.getCurrX()+"-----getCurrY------      "+mScroller.getCurrY());
            offsetTopAndBottom(mScroller.getCurrY() - curTop);
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initTop = top;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        return !target.canScrollVertically(-1);
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        mScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
    }

    @Override
    public int getNestedScrollAxes() {
        return mScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mScrollingParentHelper.onStopNestedScroll(target);
        Log.d("NestedScrollView", "onStopNestedScroll:" + (getTop() - initTop));
        Log.d("NestedScrollView", "onStopNestedScroll:initTop=" + initTop);

        curTop = getTop();
        int dy = curTop - initTop;
        if(dy < 300 && dy > 0){
            mScroller.abortAnimation();
            mScroller.startScroll(0,curTop,0,-dy);
            invalidate();
        }else if(dy > 300){
            mScroller.abortAnimation();
            mScroller.startScroll(0,curTop,0,getHeight());
            invalidate();
        }

    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @Nullable int[] consumed, int type) {
        Log.d("NestedScrollView", "dy:" + dy);
        if(dy < 0 && !target.canScrollVertically(-1)){
            offsetTopAndBottom(-dy);
            consumed[1] = dy;
        }else{
            consumed[1] = 0;
        }
//        if (dy > 0) {
//            offsetTopAndBottom(dy);
//            consumed[1] += dy;
//        } else {
//            offsetTopAndBottom(dy);
//            consumed[1] += dy;//父亲消耗
//        }

    }
}
