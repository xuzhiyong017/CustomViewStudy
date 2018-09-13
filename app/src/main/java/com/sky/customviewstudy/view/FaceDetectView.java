package com.sky.customviewstudy.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.svgparse.CityPath;
import com.sky.customviewstudy.svgparse.ParserCallBack;
import com.sky.customviewstudy.svgparse.SVGXmlParserUtils;
import com.sky.customviewstudy.svgparse.ViewAttr;

import java.util.List;

/**
 * @author: xuzhiyong
 * @date: 2018/9/5 15:03
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class FaceDetectView extends View implements ViewTreeObserver.OnGlobalLayoutListener,
        View.OnTouchListener,ScaleGestureDetector.OnScaleGestureListener,ParserCallBack {

    private static final String TAG = "FaceDetectView";
    private final Paint mPaint;
    /** 表示是否只有一次加载 */
    private boolean isOnce = false;
    /** 初始时的缩放值 */
    private float mInitScale;
    /** 双击时 的缩放值 */
    private float mClickScale;
    /** 最大的缩放值 */
    private float mMaxScale;
    /** 图片缩放矩阵 */
    private Matrix mMatrix;
    /** 图片缩放手势 */
    private ScaleGestureDetector mScaleGesture;

    // ----------------------------自由移动--------------------------------
    /** 可移动最短距离限制，大于这个值时就可移动 */
    private int mTouchSlop;
    /** 是否可以拖动 */
    private boolean isCanDrag;

    // ----------------------------双击放大--------------------------------
    private GestureDetector mGesture;
    // 是否自动缩放
    private boolean isAutoScale;

    private int viewWidth;
    private int viewHeight;
    private ViewAttr mViewAttr;
    private List<CityPath> list;
    private Path mPath;
    private float translateX;
    private float translateY;
    private float initTransX;
    private float initTransY;

    public FaceDetectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceDetectView(Context context) {
        this(context, null);
    }

    public FaceDetectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 必须设置才能触发
        this.setOnTouchListener(this);

        mMatrix = new Matrix();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //解析svg xml
        SVGXmlParserUtils.parserXml(getResources().openRawResource(R.raw.china), this);

        mScaleGesture = new ScaleGestureDetector(context, this);
        mGesture = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                if (list != null)
                    for (int i = 0; i < list.size(); i++) {
                        CityPath cityPath = list.get(i);
                        if (cityPath.isArea(x / getScale() - getTransX(), y / getScale()-getTransY())) {
                            mPath = cityPath.getmPath();
                            postInvalidate();
                            Toast.makeText(getContext(), cityPath.getTitle(), Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                return true;
            }
        });

        /**
         * 是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件，如viewpager
         * 就是用这个距离来判断用户是否翻页。
         */
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public void callback(List<CityPath> list, ViewAttr viewAttr) {
        this.list = list;
        mViewAttr = viewAttr;
        // 如果还没有加载图片
        initScale();
    }

    private  void initScale(){
        if (!isOnce && mViewAttr != null) {

            if(viewHeight > 0 && viewWidth >0 && mViewAttr.getWidth() >0 && mViewAttr.getHeight() > 0){
                isOnce = true;
                // 设定比例值
                float scale = 0.0f;

                // 如果图片的宽度>控件的宽度，缩小
                if (mViewAttr.getWidth() > viewWidth && mViewAttr.getHeight() < viewHeight) {
                    scale = viewWidth * 1.0f / mViewAttr.getWidth();
                }
                // 如果图片的高度>控件的高度，缩小
                if (mViewAttr.getHeight() > viewHeight && mViewAttr.getWidth() < viewWidth) {
                    scale = viewHeight * 1.0f / mViewAttr.getHeight();
                }
                // 如果图片的宽高度>控件的宽高度，缩小 或者 如果图片的宽高度<控件的宽高度，放大
                if ((mViewAttr.getWidth() > viewWidth && mViewAttr.getHeight() > viewHeight) || (mViewAttr.getWidth() < viewWidth && mViewAttr.getHeight() < viewHeight)) {
                    float f1 = viewWidth * 1.0f / mViewAttr.getWidth();
                    float f2 = viewHeight * 1.0f / mViewAttr.getHeight();
                    scale = Math.min(f1, f2);
                }

                // 初始化缩放值
                mInitScale = scale;
                mClickScale = mInitScale * 2;
                mMaxScale = mInitScale * 4;

                // 得到移动的距离
                int dx = viewWidth / 2 - mViewAttr.getWidth() / 2;
                int dy = viewHeight / 2 - mViewAttr.getHeight() / 2;

                initTransX = dx;
                initTransY = dy;
                translateX = dx;
                translateY = dy;
                // 平移
                mMatrix.postTranslate(dx, dy);

                // 在控件的中心缩放
                mMatrix.postScale(scale, scale, viewWidth / 2, viewHeight / 2);

                // 设置矩阵
                setImageMatrix(mMatrix);

                // 关于matrix，就是个3*3的矩阵
                /**
                 * xscale xskew xtrans yskew yscale ytrans 0 0 0
                 */
            }
            postInvalidate();
        }
    }


    private void setImageMatrix(Matrix matrix) {
        postInvalidate();
    }

    @Override
    public void onGlobalLayout() {
    }

    Matrix tmp = new Matrix();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (list == null) {
            return;
        }
        tmp.setScale(getScale(),getScale(),0,0);
        canvas.concat(tmp);
        canvas.translate(getTransX(),getTransY());
        canvas.drawColor(Color.YELLOW);
        for (int i = 0; i < list.size(); i++) {
            CityPath path = list.get(i);
            //绘制边的颜色
            mPaint.setStrokeWidth(2);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.GRAY);
            canvas.drawPath(path.getmPath(), mPaint);
        }
        if (mPath != null) {
            mPaint.setStrokeWidth(1);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.GREEN);
            canvas.drawPath(mPath, mPaint);
        }

    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        initScale();
    }

    /**
     * 注册全局事件
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 移除全局事件
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    /**
     * 获得缩放值
     *
     * @return
     */
    public float getScale() {
        /**
         * xscale xskew xtrans yskew yscale ytrans 0 0 0
         */
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    public float getTransX() {
        /**
         * xscale xskew xtrans yskew yscale ytrans 0 0 0
         */
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MTRANS_X];
    }


    public float getTransY() {
        /**
         * xscale xskew xtrans yskew yscale ytrans 0 0 0
         */
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MTRANS_Y];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {

        // 缩放因子，>0表示正在放大，<0表示正在缩小
        float intentScale = detector.getScaleFactor();
        float scale = getScale();

        // 进行缩放范围的控制
        // 判断，如果<最大缩放值，表示可以放大，如果》最小缩放，说明可以缩小
        if ((scale < mMaxScale && intentScale > 1.0f) || (scale > mInitScale && intentScale < 1.0f)) {

            // scale 变小时， intentScale变小
            if (scale * intentScale < mInitScale) {
                // intentScale * scale = mInitScale ;
                intentScale = mInitScale / scale;
            }

            // scale 变大时， intentScale变大
            if (scale * intentScale > mMaxScale) {
                // intentScale * scale = mMaxScale ;
                intentScale = mMaxScale / scale;
            }

//             以控件为中心缩放
             mMatrix.postScale(intentScale, intentScale, 0,
             0);

//             以手势为中心缩放
//            Log.d(TAG,"onScale="+intentScale);
//            mMatrix.postScale(intentScale, intentScale, detector.getFocusX(), detector.getFocusY());

            // 检测边界与中心点
            checkSideAndCenterWhenScale();

            setImageMatrix(mMatrix);
        }

        return true;
    }

    /**
     * 获得图片缩放后的矩阵
     *
     * @return
     */
    public RectF getMatrixRectF() {
        Matrix matrix = mMatrix;
        RectF rectF = new RectF();
        if (mViewAttr != null) {
            // 初始化矩阵
            rectF.set(0, 0, mViewAttr.getWidth(), mViewAttr.getHeight());
            // 移动s
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    private void checkSideAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0f;
        float deltaY = 0f;
        int width = getWidth();
        int height = getHeight();

        // 情况1， 如果图片的宽度大于控件的宽度
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;// 如果图片没有左边对齐，就往左边移动
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;// 如果图片没有右边对齐，就往右边移动
            }
        }
        // 情况2， 如果图片的宽度大于控件的宽度
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;//
            }
            if (rectF.bottom < height) {
                deltaY = height - rectF.bottom;// 往底部移动
            }
        }

        // 情况3,如图图片在控件内，则让其居中
        if (rectF.width() < width) {
            // deltaX = width/2-rectF.left - rectF.width()/2;
            // 或
            deltaX = width / 2f - rectF.right + rectF.width() / 2f;
        }

        if (rectF.height() < height) {
            deltaY = height / 2f - rectF.bottom + rectF.height() / 2f;
        }
        translateX += deltaX;
        translateY += deltaY;
        Log.d(TAG,"checkSideAndCenterWhenScale=dx*dy= "+deltaX+" * "+deltaY);
        mMatrix.postTranslate(deltaX, deltaY);
    }


    private float mScaleCenterX;
    private float mScaleCenterY;

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub
        mScaleCenterX = detector.getFocusX();
        mScaleCenterY = detector.getFocusY();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub

    }

    private float mLastX;
    private float mLastY;
    /** 上次手指的数量 */
    private int mLastPointerCount;

    /** 判断是否检测了x,y轴 */
    private boolean isCheckX;
    private boolean isCheckY;
    private long touchTime;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        // 把事件传递给双击手势
        if (mGesture.onTouchEvent(event)) {
            return true;
        }
        // 把事件传递给缩放手势
        mScaleGesture.onTouchEvent(event);

        float x = event.getX();
        float y = event.getY();

        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;

        // 说明手指改变
        if (mLastPointerCount != pointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;

        RectF rectF = getMatrixRectF();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchTime = System.currentTimeMillis();
                if (rectF.width() > getWidth()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (rectF.width() > getWidth()) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isMoveAction(dx, dy);
                }
                /**
                 * 如果能移动
                 */
                if (isCanDrag) {
                    //RectF rectF = getMatrixRectF();

                    isCheckX = isCheckY = true;

                    // 如果图片在控件内，不允许移动
                    if (rectF.width() < getWidth()) {
                        isCheckX = false;
                        dx = 0f;
                    }
                    if (rectF.height() < getHeight()) {
                        isCheckY = false;
                        dy = 0f;
                    }
                    translateX += dx;
                    translateY += dx;
                    mMatrix.postTranslate(dx, dy);

                    // 移动事检测边界
                    checkSideAndCenterWhenTransate();

                    setImageMatrix(mMatrix);
                }

                mLastX = x;
                mLastY = y;

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 清楚手指
                mLastPointerCount = 0;

                break;
        }

        return true;
    }

    private void checkSideAndCenterWhenTransate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0f;
        float deltaY = 0f;
        int width = getWidth();
        int height = getHeight();

        if (rectF.top > 0 && isCheckY) {
            deltaY = -rectF.top;// 往上边移动
        }
        if (rectF.bottom < height && isCheckY) {
            deltaY = height - rectF.bottom;// 往底部移动
        }

        if (rectF.left > 0 && isCheckX) {
            deltaX = -rectF.left;// 往左边移动
        }
        if (rectF.right < width && isCheckX) {
            deltaX = width - rectF.right;// 往右边移动
        }
        // 移动
        mMatrix.postTranslate(deltaX, deltaY);
        translateX += deltaX;
        translateY += deltaY;
    }

    private boolean isMoveAction(float dx, float dy) {
        // 求得两点的距离
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop && (System.currentTimeMillis() - touchTime) > 300;
    }

}
