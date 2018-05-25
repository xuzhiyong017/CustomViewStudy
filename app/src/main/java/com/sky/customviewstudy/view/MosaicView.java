package com.sky.customviewstudy.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.sky.customviewstudy.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 91299 on 2018/5/18   0018.
 */

public class MosaicView extends View {

    private static final String TAG = "MosaicView";
    private Paint mPaint;
    private float downX;
    private float downY;
    private Path path;
    private float tempX;
    private float tempY;
    private Mode mMode;
    private int mGridWidth;
    private int mImageWidth;
    private int mImageHeight;
    private RectF mImageRect;

    public MosaicView(Context context) {
        this(context,null);
    }

    public MosaicView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MosaicView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    Bitmap mLocalBitmap;

    Bitmap mCoverBitmap;
    Bitmap mTouchBitmap;
    Canvas mTouchCanvas;

    List<Drawing> drawingList = new ArrayList<>();
    List<Drawing> deleteList = new ArrayList<>();

    Matrix matrix ;

    private void init(Context context) {


        setDrawingCacheEnabled(true);

        path = new Path();
        mGridWidth = dp2px(15);

        mLocalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ceshi);
        mCoverBitmap = getGridMosaic();

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFilterBitmap(true);
        mPaint.setPathEffect(new CornerPathEffect(10));
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(100);

        mTouchBitmap = Bitmap.createBitmap(mCoverBitmap.getWidth(),mCoverBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        mTouchCanvas = new Canvas(mTouchBitmap);
        mTouchCanvas.drawARGB(0,0,0,0);
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        mMode = Mode.DRAW;

        matrix = new Matrix();
        mImageRect = new RectF();

        setSrc(null);

    }

    // 设置 src
    public void setSrc(String file) {
        File file1 = new File(Environment.getExternalStorageDirectory(),"/pictures/20180512_112831726.jpg");
        BitmapFactory.Options bitmapSize = getBitmapSize(file1.getAbsolutePath());
        mImageWidth = bitmapSize.outWidth;
        mImageHeight = bitmapSize.outHeight;
        mLocalBitmap = BitmapFactory.decodeFile(file1.getAbsolutePath());
        requestLayout();
        postInvalidate();
    }

    public static BitmapFactory.Options getBitmapSize(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        return options;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        matrix.reset();
        matrix.postTranslate(getWidth()/2 - mLocalBitmap.getWidth()/2,getHeight()/2-mLocalBitmap.getHeight()/2);
        matrix.postScale(caulateScale(),caulateScale(),getWidth()/2,mLocalBitmap.getHeight()/2);

//        mImageWidth = mLocalBitmap.getWidth();
//        mImageHeight = mLocalBitmap.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mImageWidth <= 0 || mImageHeight <= 0) {
            return;
        }
        int contentWidth = right - left;
        int contentHeight = bottom - top;
        float widthRatio = (float) contentWidth / mImageWidth;
        float heightRatio = (float)contentHeight / mImageHeight;
        float ratio = widthRatio < heightRatio ? widthRatio : heightRatio;
        int realWidth = (int) (mImageWidth * ratio);
        int realHeight = (int) (mImageHeight * ratio);

        int imageLeft = (contentWidth - realWidth) / 2;
        int imageTop = (contentHeight - realHeight) / 2;
        int imageRight = imageLeft + realWidth;
        int imageBottom = imageTop + realHeight;
        mImageRect.set(imageLeft, imageTop, imageRight, imageBottom);
    }

    public float caulateScale(){
        float scale = 1.0f;

        float imageRadio = mLocalBitmap.getWidth() / (float)mLocalBitmap.getHeight();
        float viewRadio = getWidth() / (float)getHeight();

        if(imageRadio > viewRadio){
            scale = getWidth() /  (float)mLocalBitmap.getWidth();
        }else{
            scale = getHeight()  / (float)mLocalBitmap.getHeight();
        }
        return scale;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);


        //实现马赛克功能
        canvas.drawBitmap(mLocalBitmap,null,mImageRect,null);
        int sc = canvas.saveLayer(mImageRect, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        for(Drawing drawing :drawingList){
            drawing.draw(canvas);
        }

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mCoverBitmap,null,mImageRect,mPaint);
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
            reDraw();
        }
    }

    //撤销操作控制
    private void reDraw() {
//        mTouchBitmap = Bitmap.createBitmap(mCoverBitmap.getWidth(),mCoverBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        mTouchCanvas = new Canvas(mTouchBitmap);
//
//        mEraserBitmap = Bitmap.createBitmap(mCoverBitmap.getWidth(),mCoverBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        mEraseCanvas = new Canvas(mEraserBitmap);

//        mTouchCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        mEraseCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//
//        if(drawingList != null && drawingList.size() > 0){
//            for (Drawing drawing:drawingList){
//                if(drawing.mode == Mode.DRAW){
//                    drawing.draw(mTouchCanvas);
//                }else{
//                    drawing.draw(mEraseCanvas);
//                }
//            }
//        }
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
        public Mode mode;
        abstract void draw(Canvas canvas);
    }

    public class PathDrawing extends Drawing{
        public Path path;

        @Override
        public void draw(Canvas canvas){
            if(mode == Mode.DRAW){
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            }else{
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
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

    public void saveBitmap(){
        Bitmap bitmap = getDrawingCache();
        Bitmap bitmapNew = Bitmap.createBitmap(bitmap, ((int) mImageRect.left), ((int) mImageRect.top), ((int) mImageRect.width()), ((int) mImageRect.height()));
        Log.e(TAG, "保存图片");
        File f = new File("/sdcard/", "ceshi.png");
        if (f.exists()) {
            f.delete();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmapNew.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
