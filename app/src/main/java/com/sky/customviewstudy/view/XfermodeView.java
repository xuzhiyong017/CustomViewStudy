package com.sky.customviewstudy.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sky.customviewstudy.R;

/**
 * Author: xuzhiyong
 * Time:2018/5/21 星期一
 * description:
 * projectName:CustomViewStudy
 */
public class XfermodeView extends View {
    private Bitmap mBgBitmap, mFgBitmap,mCoverBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private Path mPath;
    private int mGridWidth;
    public boolean mDraw = true;

    public XfermodeView(Context context) {
        super(context);
        init();
    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XfermodeView(Context context, AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();

        mGridWidth = dp2px(5);

//        mPaint.setAlpha(0);
        mPaint.setXfermode(
                new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        //让画笔和连接处更圆滑一些
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(100);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPath = new Path();
        mBgBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.beautygirl);
        mCoverBitmap = getGridMosaic();
        mFgBitmap =Bitmap.createBitmap(mBgBitmap.getWidth(),mBgBitmap.getHeight(), Bitmap.Config.ARGB_8888) ;
        mCanvas = new Canvas(mFgBitmap);
//        mCanvas.drawRect(0,0,mBgBitmap.getWidth(),mBgBitmap.getHeight(),mPaint);
//        mCanvas.drawBitmap(mCoverBitmap,0,0,mPaint);
//        mCanvas.drawColor(Color.GRAY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                break;
        }

        if(mDraw){
            mCanvas.save(Canvas.ALL_SAVE_FLAG);
            mCanvas.drawPath(mPath, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            mCanvas.drawBitmap(mCoverBitmap,0,0,mPaint);
            mPaint.setXfermode(null);
            mCanvas.restore();
        }else{
            mCanvas.save(Canvas.ALL_SAVE_FLAG);
            mPaint.setColor(Color.TRANSPARENT);
            mCanvas.drawPath(mPath, mPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            mCanvas.drawBitmap(mFgBitmap,0,0,mPaint);
            mPaint.setXfermode(null);
            mCanvas.restore();
        }


        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mBgBitmap, 0, 0, null);
        canvas.drawBitmap(mFgBitmap, 0, 0, null);
    }


    private Bitmap getGridMosaic() {
        if (mBgBitmap.getWidth() <= 0 || mBgBitmap.getHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(mBgBitmap.getWidth(), mBgBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int horCount = (int) Math.ceil(mBgBitmap.getWidth() / (float) mGridWidth);
        int verCount = (int) Math.ceil(mBgBitmap.getHeight() / (float) mGridWidth);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (int horIndex = 0; horIndex < horCount; ++horIndex) {
            for (int verIndex = 0; verIndex < verCount; ++verIndex) {
                int l = mGridWidth * horIndex;
                int t = mGridWidth * verIndex;
                int r = l + mGridWidth;
                if (r > mBgBitmap.getWidth()) {
                    r = mBgBitmap.getWidth();
                }
                int b = t + mGridWidth;
                if (b > mBgBitmap.getHeight()) {
                    b = mBgBitmap.getHeight();
                }
                int color = mBgBitmap.getPixel(l, t);
                Rect rect = new Rect(l, t, r, b);
                paint.setColor(color);
                canvas.drawRect(rect, paint);
            }
        }
        canvas.save();
        return bitmap;
    }


    private int dp2px(int dip) {
        Context context = this.getContext();
        Resources resources = context.getResources();
        int px = Math
                .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dip, resources.getDisplayMetrics()));
        return px;
    }

}
