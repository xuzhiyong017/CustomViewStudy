package com.sky.customviewstudy.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sky.customviewstudy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: xuzhiyong
 * Time:2018/5/21 星期一
 * description:
 * projectName:CustomViewStudy
 */
public class PathView extends View {

    private static final String TAG = "MosaicView";
    private Paint mPaint;
    private float downX;
    private float downY;
    private Path path;
    private float tempX;
    private float tempY;
    private SimpleMosaicView.Mode mMode;
    private int mGridWidth;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    Bitmap mLocalBitmap;

    Bitmap mCoverBitmap;
    Bitmap mTouchBitmap;
    Bitmap mEraserBitmap;
    Canvas mTouchCanvas;
    Canvas mEraseCanvas;
    List<Drawing> drawingList = new ArrayList<>();
    List<Drawing> deleteList = new ArrayList<>();

    private void init(Context context) {

        path = new Path();
        mGridWidth = dp2px(5);

        mLocalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girls);
        mCoverBitmap = getGridMosaic();

        mPaint = new Paint();
        mPaint.setAlpha(0);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFilterBitmap(true);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(100);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mTouchBitmap = Bitmap.createBitmap(mCoverBitmap.getWidth(),mCoverBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mEraserBitmap = Bitmap.createBitmap(mCoverBitmap.getWidth(),mCoverBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mTouchCanvas = new Canvas(mTouchBitmap);
        mTouchCanvas.drawColor(Color.GRAY);
        mEraseCanvas = new Canvas(mEraserBitmap);
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mMode = SimpleMosaicView.Mode.DRAW;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        canvas.drawBitmap(mLocalBitmap,0,0,null);

        canvas.drawBitmap(mTouchBitmap,0,0,null);

    }

    public void setMode(SimpleMosaicView.Mode mode){
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
                path.reset();
                path.moveTo(downX,downY);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                path.quadTo(tempX,tempY,moveX,moveY);

//                if(mMode == MosaicView.Mode.DRAW){
//                    mPaint.setColor(Color.BLUE);
//                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//                }else if(mMode == MosaicView.Mode.ERASER){
//                    mPaint.setColor(Color.TRANSPARENT);
//                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//                }

                mTouchCanvas.drawPath(path,mPaint);

//                if(mMode == Mode.DRAW){
//                    mTouchCanvas.drawPath(path,mPaint);
//                }else if(mMode == Mode.ERASER){
//                    mEraseCanvas.drawPath(path,mPaint);
//                }
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                savePath();
                break;
        }
        return true;
    }

    private void savePath() {
        PathDrawing pathDrawing = new PathDrawing();
        pathDrawing.path = new Path(path);
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
            reDraw();
        }
    }

    //撤销操作控制
    private void reDraw() {

        mTouchCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mEraseCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        if(drawingList != null && drawingList.size() > 0){
            for (Drawing drawing:drawingList){
                if(drawing.mode == SimpleMosaicView.Mode.DRAW){
                    drawing.draw(mTouchCanvas);
                }else{
                    mPaint.setColor(Color.TRANSPARENT);
                    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    mTouchCanvas.drawBitmap(mCoverBitmap,0,0,mPaint);
                }
            }
        }
        invalidate();
    }

    /**
     * 反撤销功能
     */
    public void redo() {
        if(deleteList!=null&&!deleteList.isEmpty()){
            drawingList.add(deleteList.get(deleteList.size()-1));
            deleteList.remove(deleteList.size()-1);
            reDraw();
        }
    }

    public abstract class Drawing{
        public Paint paint;
        public SimpleMosaicView.Mode mode;
        abstract void draw(Canvas canvas);

        public abstract Path getPath();
    }

    public class PathDrawing extends Drawing {
        public Path path;

        @Override
        public void draw(Canvas canvas){
            canvas.drawPath(path,paint);
        }

        @Override
        public Path getPath() {
            return path;
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

    public static Bitmap getMosaicsBitmaps(Bitmap bmp, double precent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        int[] pixels = new int[bmpH * bmpW];
        bmp.getPixels(pixels, 0, bmpW, 0, 0, bmpW, bmpH);
        int raw = (int) (bmpW * precent);
        int unit;
        if (raw == 0) {
            unit = bmpW;
        } else {
            unit = bmpW / raw; //原来的unit*unit像素点合成一个，使用原左上角的值
        }
        if (unit >= bmpW || unit >= bmpH) {
            return getMosaicsBitmap(bmp, precent);
        }
        for (int i = 0; i < bmpH; ) {
            for (int j = 0; j < bmpW; ) {
                int leftTopPoint = i * bmpW + j;
                for (int k = 0; k < unit; k++) {
                    for (int m = 0; m < unit; m++) {
                        int point = (i + k) * bmpW + (j + m);
                        if (point < pixels.length) {
                            pixels[point] = pixels[leftTopPoint];
                        }
                    }
                }
                j += unit;
            }
            i += unit;
        }
        long end = System.currentTimeMillis();
        Log.v(TAG, "DrawTime:" + (end - start));
        return Bitmap.createBitmap(pixels, bmpW, bmpH, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getMosaicsBitmap(Bitmap bmp, double precent) {
        long start = System.currentTimeMillis();
        int bmpW = bmp.getWidth();
        int bmpH = bmp.getHeight();
        Bitmap resultBmp = Bitmap.createBitmap(bmpW, bmpH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        Paint paint = new Paint();
        double unit;
        if (precent == 0) {
            unit = bmpW;
        } else {
            unit = 1 / precent;
        }
        double resultBmpW = bmpW / unit;
        double resultBmpH = bmpH / unit;
        for (int i = 0; i < resultBmpH; i++) {
            for (int j = 0; j < resultBmpW; j++) {
                int pickPointX = (int) (unit * (j + 0.5));
                int pickPointY = (int) (unit * (i + 0.5));
                int color;
                if (pickPointX >= bmpW || pickPointY >= bmpH) {
                    color = bmp.getPixel(bmpW / 2, bmpH / 2);
                } else {
                    color = bmp.getPixel(pickPointX, pickPointY);
                }
                paint.setColor(color);
                canvas.drawRect((int) (unit * j), (int) (unit * i), (int) (unit * (j + 1)), (int) (unit * (i + 1)), paint);
            }
        }
        canvas.setBitmap(null);
        long end = System.currentTimeMillis();
        Log.v(TAG, "DrawTime:" + (end - start));
        return resultBmp;
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
