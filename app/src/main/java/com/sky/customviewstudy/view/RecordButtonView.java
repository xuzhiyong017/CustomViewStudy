package com.sky.customviewstudy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 91299 on 2018/4/12   0012.
 */

public class RecordButtonView extends View {

    public RecordButtonView(Context context) {
        this(context,null);
    }

    public RecordButtonView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordButtonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //控件宽高
    private int mViewHeight;
    private int mViewWidth;

    //动画进度值0~1
    private float mAnimatorValue;
    //绘图画笔
    private Paint mPaint;
    //属性动画
    private ValueAnimator mStartAnimator;

    private void init(Context context){
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //初始化动画
        mStartAnimator = ValueAnimator.ofFloat(0,1f).setDuration(500L);
        //设置动画往返执行
        mStartAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //设置动画无限循环
        mStartAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mStartAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue =(float) animation.getAnimatedValue();
                //获取动画的值然后刷新view
                invalidate();
            }
        });

        //监听View动画执行，动态设置View的硬件加速模式
        mStartAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                setLayerType(LAYER_TYPE_HARDWARE,null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setLayerType(LAYER_TYPE_NONE,null);
            }
        });
        mStartAnimator.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动画布到控件中间
        canvas.translate(mViewWidth/2,mViewHeight/2);

        //这里没有对圆环大小做控制，只是为了测试效果，如果需要可以作为view属性
        //这是是个外圆半径200，内圆半径在150-190直接变化

        //动态计算内圆半径
        float offset = 150 + 40 * mAnimatorValue;

        //先画外圆
        mPaint.setColor(Color.RED);
        canvas.drawCircle(0,0,200,mPaint);

        //设置Xfermode为DST_OUT模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        //画内圆
        canvas.drawCircle(0,0,offset,mPaint);
        //重置Xfermode
        mPaint.setXfermode(null);

    }

}
