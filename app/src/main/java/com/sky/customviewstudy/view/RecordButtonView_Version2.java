package com.sky.customviewstudy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 91299 on 2018/4/12   0012.
 */

public class RecordButtonView_Version2 extends View {

    private float lastX;
    private float lastY;
    private float downX;
    private float downY;

    public RecordButtonView_Version2(Context context) {
        this(context,null);
    }

    public RecordButtonView_Version2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordButtonView_Version2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

    Path mOutSidePath;
    Path mInnerSidePath;
    int radius;

    private void init(Context context){
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mOutSidePath = new Path();
        mInnerSidePath = new Path();

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
//                setLayerType(LAYER_TYPE_HARDWARE,null); setLayerType(LAYER_TYPE_HARDWARE,null); //这里也可以设置LAYER_TYPE_SOFTWARE，可以达到同样的效果
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                setLayerType(LAYER_TYPE_NONE,null);
            }
        });
        mStartAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measure(widthMeasureSpec),measure(heightMeasureSpec));
    }

    private int measure(int measureSpec){
        int result = 0;
        //分别获取测量模式 和 测量大小
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //如果是精确度模式，呢就按xml中定义的来
        if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        //如果是最大值模式，就按我们定义的来
        else if(specMode == MeasureSpec.AT_MOST){
            result = 200;
        }

        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;

        if(mViewHeight > mViewWidth){
            radius = (mViewWidth - 20 )/ 2;
        }else{
            radius =( mViewHeight - 20)  /2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动画布到控件中间
        canvas.translate(mViewWidth/2,mViewHeight/2);

        //这里没有对圆环大小做控制，只是为了测试效果，如果需要可以作为view属性
        //这是是个外圆半径200，内圆半径在150-190直接变化

        //动态计算内圆半径
        float offset = radius - 50 + 40 * mAnimatorValue;

        //先画外圆
        mPaint.setColor(Color.RED);

        mOutSidePath.reset();
        mInnerSidePath.reset();

        mOutSidePath.addCircle(0,0,radius, Path.Direction.CCW);
        mInnerSidePath.addCircle(0,0,offset, Path.Direction.CCW);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            mOutSidePath.op(mInnerSidePath, Path.Op.DIFFERENCE);
        }else {
            canvas.clipPath(mInnerSidePath, Region.Op.DIFFERENCE);
        }
        canvas.drawPath(mOutSidePath,mPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                lastX =  event.getRawX();
                lastY =  event.getRawY();
                downX = lastX;
                downY = lastY;
                break;

            case MotionEvent.ACTION_MOVE:

//                  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
                float distanceX = lastX - event.getRawX();
                float distanceY = lastY - event.getRawY();

                float nextY = (getY() -getTop()) - distanceY;
                float nextX = (getX() - getLeft()) - distanceX;


                // 属性动画移动
                ObjectAnimator y = ObjectAnimator.ofFloat(this, "translationY", (getY() - getTop()), nextY);
                ObjectAnimator x = ObjectAnimator.ofFloat(this, "translationX", (getX() - getLeft()), nextX);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(x, y);
                animatorSet.setDuration(0);
                animatorSet.start();

                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:

                lastX = event.getRawX();
                lastY = event.getRawY();

                int nextYy = 0;
                int nextXx = 0;

                // 属性动画移动
                ObjectAnimator yy = ObjectAnimator.ofFloat(this, "translationY",getTranslationY(), nextYy);
                ObjectAnimator xx = ObjectAnimator.ofFloat(this, "translationX", getTranslationX(), nextXx);

                AnimatorSet animatorSetxy = new AnimatorSet();
                animatorSetxy.playTogether(xx, yy);
                animatorSetxy.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {


                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animatorSetxy.setDuration(200);
                animatorSetxy.start();

                break;

        }

        return true;
    }
}
