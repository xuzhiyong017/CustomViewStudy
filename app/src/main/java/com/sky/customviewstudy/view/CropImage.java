package com.sky.customviewstudy.view;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 91299 on 2018/5/23   0023.
 */

public class CropImage extends View {

    private Paint mPaint;
    private float angle = 0;

    public CropImage(Context context) {
        this(context,null);
    }

    public CropImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CropImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private RectF mFirst;
    private RectF mSecond;

    private void init() {

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10);

        mFirst = new RectF(300f,400f,800,1100);
        mSecond = new RectF(300f,400f,800,1100);
    }




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        canvas.drawRect(mFirst,mPaint);
        canvas.save();
        canvas.scale(cacultSale(),cacultSale(),mFirst.centerX(),mFirst.centerY());
        canvas.rotate(angle*50,mFirst.centerX(),mFirst.centerY());
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(10);
        canvas.drawRect(mSecond,mPaint);
        canvas.restore();
    }


    public float cacultSale(){
        Log.d("CropImageView","angle="+angle +" cos="+Math.cos(angle));
        double scale = Math.cos(angle) + Math.sin(angle)*7/5;
        Log.d("CropImageView","scale="+scale);
        return (float) scale;
    }

    public void startAnimator(){
        Log.d("CropImageView","cos 30="+Math.cos(30));
        ValueAnimator animator = ValueAnimator.ofFloat(0,1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                angle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(10000);
        animator.start();
    }
}
