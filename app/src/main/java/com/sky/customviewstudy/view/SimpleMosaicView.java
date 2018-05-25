package com.sky.customviewstudy.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 91299 on 2018/5/18   0018.
 */

public class SimpleMosaicView extends View {

    private static final String TAG = "MosaicView";
    private Paint mPaint;
    private Path path;
    private float downX;
    private float downY;
    private float tempX;
    private float tempY;
    private Mode mMode;
    private int mGridWidth;
    private Xfermode mSrcInXferMode;
    private Xfermode mSrcOverXfermode;
    private Xfermode mClearXfermode;

    Bitmap mLocalBitmap;
    Bitmap mCoverBitmap;

    List<Drawing> drawingList = new ArrayList<>();
    List<Drawing> deleteList = new ArrayList<>();


    public SimpleMosaicView(Context context) {
        this(context,null);
    }

    public SimpleMosaicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimpleMosaicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        path = new Path();
        mGridWidth = dp2px(15);
        mSrcInXferMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mSrcOverXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER);
        mClearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFilterBitmap(true);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(100);

        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mMode = Mode.DRAW;
    }

    public void setBitmap(Bitmap mSourceBitmap){
        mLocalBitmap = mSourceBitmap;
        mCoverBitmap = getGridMosaic();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);

        //实现马赛克功能
        canvas.drawBitmap(mLocalBitmap,0,0,null);
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        for(Drawing drawing :drawingList){
            drawing.draw(canvas);
        }

        mPaint.setXfermode(mSrcInXferMode);
        canvas.drawBitmap(mCoverBitmap,0,0,mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    public void setMode(Mode mode){
        mMode = mode;
    }

    public enum Mode{
        DRAW,ERASER;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                path = new Path();
                savePath(path);
                path.moveTo(downX,downY);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                path.quadTo(tempX,tempY,moveX,moveY);
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    private void savePath(Path path) {
        PathDrawing pathDrawing = new PathDrawing();
        pathDrawing.path = path;
        pathDrawing.paint = mPaint;
        pathDrawing.mode = mMode;

        drawingList.add(pathDrawing);
    }

    /**
     * 撤销功能
     */
    public void undo() {
        if(drawingList!=null&&drawingList.size()>=1){
            deleteList.add(drawingList.get(drawingList.size()-1));
            drawingList.remove(drawingList.size()-1);
            invalidate();
        }
    }


    /**
     * 反撤销功能
     */
    public void redo() {
        if(deleteList!=null&&!deleteList.isEmpty()){
            drawingList.add(deleteList.get(deleteList.size()-1));
            deleteList.remove(deleteList.size()-1);
            invalidate();
        }
    }

    public abstract class Drawing{
        public Paint paint;
        public Mode mode;
        abstract void draw(Canvas canvas);
    }

    public class PathDrawing extends Drawing{
        public Path path;

        @Override
        public void draw(Canvas canvas){
            if(mode == Mode.DRAW){
                paint.setXfermode(mSrcOverXfermode);
            }else{
                paint.setXfermode(mClearXfermode);
            }
            canvas.drawPath(path,paint);
        }
    }

    private Bitmap getGridMosaic() {
        if (mLocalBitmap.getWidth() <= 0 || mLocalBitmap.getHeight() <= 0) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(mLocalBitmap.getWidth(), mLocalBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        int horCount = (int) Math.ceil(mLocalBitmap.getWidth() / (float) mGridWidth);
        int verCount = (int) Math.ceil(mLocalBitmap.getHeight() / (float) mGridWidth);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        for (int horIndex = 0; horIndex < horCount; ++horIndex) {
            for (int verIndex = 0; verIndex < verCount; ++verIndex) {
                int l = mGridWidth * horIndex;
                int t = mGridWidth * verIndex;
                int r = l + mGridWidth;
                if (r > mLocalBitmap.getWidth()) {
                    r = mLocalBitmap.getWidth();
                }
                int b = t + mGridWidth;
                if (b > mLocalBitmap.getHeight()) {
                    b = mLocalBitmap.getHeight();
                }
                int color = mLocalBitmap.getPixel(l, t);
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
