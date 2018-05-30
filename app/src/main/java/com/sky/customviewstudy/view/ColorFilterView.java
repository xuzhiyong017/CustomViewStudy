package com.sky.customviewstudy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.customviewstudy.R;

/**
 * Author: xuzhiyong
 * Time:2018/5/30 星期三
 * description:
 * projectName:CustomViewStudy
 */
public class ColorFilterView extends View {

    public ColorFilterView(Context context) {
        this(context,null);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Paint mPaint;
    private Bitmap bitmap;

    private void init() {

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girls);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect srcRecf = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        RectF rectF = new RectF(0,0,540,1920);
        mPaint.reset();

        canvas.drawBitmap(bitmap,srcRecf,rectF,mPaint);

        RectF rectF1 = new RectF(540,0,1080,1920);

        //透明度0.5
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                1,0,0,0,0,
//                0,1,0,0,0,
//                0,0,1,0,0,
//                0,0,0,0.5f,0,
//        });

        //0.213r,0.715g,0.072b
        //opengl 0.3r 0.59g 0.11b


        //黑白
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                0.213f,0.715f,0.072f,0,0,
//                0.213f,0.715f,0.072f,0,0,
//                0.213f,0.715f,0.072f,0,0,
//                0,0,0,1f,0,
//        });


        //反色，底片效果
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                -1,0,0,0,255,
//                0,-1,0,0,255,
//                0,0,-1,0,255,
//                0,0,0,1,0,
//        });

        //复古效果复古效果
//        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
//                1/2f,1/2f,1/2f,0,0,
//                1/3f,1/3f,1/3f,0,0,
//                1/4f,1/4f,1/4f,0,0,
//                0,0,0,1,0,
//        });

        //发射
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0,1,0,0,0,
                1,0,0,0,0,
                0,0,1,0,0,
                0,0,0,0.5f,0,
        });

        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap,srcRecf,rectF1,mPaint);
    }
}
