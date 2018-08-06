package com.sky.customviewstudy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sky.customviewstudy.R;

/**
 * Created by xuzhiyong on 2018/7/3.
 */
public class ReversalSeekBar extends View {
    private static final String TAG = "ReversalSeekBar";
    private int mWidth;
    private int mHeight;

    private int mSeekBarLeft;
    private int mSeekBarRight;

    private int mBgTop;
    private int mBgBottom;
    private int mRoundSize;

    //View的末尾位置
    private int mViewEnd;

    private Paint mNormalPaint;
    private Paint mPointerPaint;
    private Paint mProgressPaint;
    private Paint mLinePaint;


    private float mPointerLeft;
    private float mPointerRight;
    private float mPointerTop;
    private float mPointerBottom;

    //是否处于点击状态
    private boolean mIsOnDrag;
    private float mCurrentLeftOffset = 0;

    private float mLastX;

    private Drawable mPointerDrawable;
    private int mHalfDrawableWidth;

    private float mCurrentProgress;

    public ReversalSeekBar(Context context) {
        super(context);
        init(null);
    }

    public ReversalSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ReversalSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    /**
     * 1~0
     */
    public void setProgress(final float progress) {
        if (progress < 0 || progress > 1.0)
            throw new IllegalArgumentException("progress must between 0 and 1");
        mCurrentProgress = 1- progress;
        onSizeChanged(mWidth,mHeight,mWidth,mHeight);
        invalidate();
    }

    public float getProgress() {
        return mCurrentProgress;
    }

    private void init(AttributeSet attrs) {
        int progressColor = Color.parseColor("#FF4081");
        int backgroundColor = Color.parseColor("#BBBBBB");
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ReversalSeekBar);
            mPointerDrawable = a.getDrawable(R.styleable.ReversalSeekBar_rs_pointerBackground);
            mHalfDrawableWidth = mPointerDrawable.getIntrinsicWidth() / 2;
            progressColor = a.getColor(R.styleable.ReversalSeekBar_rs_progressColor,
                    Color.parseColor("#FF4081"));
            backgroundColor = a.getColor(R.styleable.ReversalSeekBar_rs_backgroundColor,
                    Color.parseColor("#BBBBBB"));
            mCurrentProgress = a.getFloat(R.styleable.ReversalSeekBar_rs_progress, 0f);
            a.recycle();
        }
        mNormalPaint = new Paint();
        mNormalPaint.setColor(backgroundColor);

        mPointerPaint = new Paint();
        mPointerPaint.setColor(Color.RED);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);

        mLinePaint = new Paint();
        mLinePaint.setColor(progressColor);
        mLinePaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mSeekBarLeft = mHalfDrawableWidth;
        mSeekBarRight = mWidth - mHalfDrawableWidth;

        mBgTop = mHeight/2 - 8;
        mBgBottom = mHeight/2 + 8;
        mRoundSize = (mBgBottom-mBgTop) / 2;

        mViewEnd = mWidth;

        float dis = (mSeekBarRight - mSeekBarLeft + mHalfDrawableWidth) * mCurrentProgress;
        mPointerLeft = mViewEnd - dis - mHalfDrawableWidth;
        mLastX = mPointerLeft;
        calculatePointerRect();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw  bg
        RectF rectF = new RectF();
        rectF.left = mSeekBarLeft;
        rectF.right = mSeekBarRight;
        rectF.top = mBgTop;
        rectF.bottom = mBgBottom;
        canvas.drawRoundRect(rectF, mRoundSize, mRoundSize, mNormalPaint);

        //draw progress
        if (mPointerRight < (mWidth/2)) {
            RectF pRecf = new RectF();
            pRecf.left = mPointerRight - mHalfDrawableWidth;
            pRecf.top = mBgTop;
            pRecf.right = mWidth/2;
            pRecf.bottom = mBgBottom;
            canvas.drawRoundRect(pRecf,
                    mRoundSize, mRoundSize, mProgressPaint);
        }else{
            RectF pRecf = new RectF();
            pRecf.left =mWidth/2 ;
            pRecf.top = mBgTop;
            pRecf.right = mPointerRight - mHalfDrawableWidth;
            pRecf.bottom = mBgBottom;
            canvas.drawRoundRect(pRecf,
                    mRoundSize, mRoundSize, mProgressPaint);
        }

        canvas.drawLine(mWidth/2,mBgTop -5,mWidth/2,mBgBottom+5,mLinePaint);

        //draw pointer
        Rect rect = new Rect();
        rect.left = (int) mPointerLeft;
        rect.top = (int) mPointerTop;
        rect.right = (int) mPointerRight;
        rect.bottom = (int) mPointerBottom;

        mPointerDrawable.setBounds(rect);
        mPointerDrawable.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) return false;

        boolean isHandle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isHandle = handleDownEvent(event);
                break;
            case MotionEvent.ACTION_MOVE:
                isHandle = handleMoveEvent(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isHandle = handleUpEvent(event);
                break;

        }
        return isHandle;
    }

    private boolean handleUpEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mIsOnDrag) {
            mIsOnDrag = false;
            if (mListener != null) {
                mListener.onSeekUp();
            }
            return true;
        }
        return false;
    }

    private boolean handleMoveEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (mIsOnDrag) {
            mCurrentLeftOffset = x - mLastX;
            //计算出标尺的Rect
            calculatePointerRect();
            if (mPointerRight - mHalfDrawableWidth <= mSeekBarLeft) {
                mPointerLeft = 0;
                mPointerRight = mPointerLeft + mPointerDrawable.getIntrinsicWidth();
            }
            if (mPointerLeft + mHalfDrawableWidth >= mSeekBarRight) {
                mPointerRight = mWidth;
                mPointerLeft = mWidth - mPointerDrawable.getIntrinsicWidth();
            }
            invalidate();
            callbackProgress();
            mLastX = x;
            return true;
        }
        return false;
    }

    private void callbackProgress() {
        if (mPointerLeft == 0) {
            callbackProgressInternal(0);
        } else if (mPointerRight == mWidth) {
            callbackProgressInternal(1);

        } else {
            float pointerMiddle = mPointerLeft + mHalfDrawableWidth;
            if (pointerMiddle == mViewEnd) {
                callbackProgressInternal(1);
            } else {
                float percent = Math.abs(pointerMiddle) / mViewEnd * 1.0f;
                callbackProgressInternal(percent);
            }
        }
    }

    private void callbackProgressInternal(float progress) {
        mCurrentProgress = progress;
        if (mListener != null) {
            mListener.onSeekProgress(progress);
        }
    }


    private boolean handleDownEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x >= mPointerLeft - 100 && x <= mPointerRight + 100) {
            if (mListener != null)
                mListener.onSeekDown();
            mIsOnDrag = true;
            mLastX = x;
            return true;
        }
        return false;
    }

    private void calculatePointerRect() {
        //draw pointer
        float pointerLeft = getPointerLeft(mCurrentLeftOffset);
        float pointerRight = pointerLeft + mPointerDrawable.getIntrinsicWidth();
        mPointerLeft = pointerLeft;
        mPointerRight = pointerRight;
        mPointerTop = 0;
        mPointerBottom = mHeight;
    }

    /**
     * 进行复位
     */
    public void resetSeekBar() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mPointerLeft, mViewEnd - mPointerDrawable.getIntrinsicWidth());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(200);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPointerLeft = (float) animation.getAnimatedValue();
                mPointerRight = mPointerLeft + mPointerDrawable.getIntrinsicWidth();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private float getPointerLeft(float offset) {
        return mPointerLeft + offset;
    }

    private OnSeekProgressListener mListener;

    public void setOnSeekProgressListener(OnSeekProgressListener listener) {
        mListener = listener;
    }

    public interface OnSeekProgressListener {
        void onSeekDown();

        void onSeekUp();

        void onSeekProgress(float progress);
    }
}
