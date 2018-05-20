package com.sky.customviewstudy.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sky.customviewstudy.R;

/**
 * Created by 91299 on 2018/4/25   0025.
 */

public class ViewMatrix extends View {

    private int width;
    private int height;
    private Bitmap bitmap;
    private Camera camera;

    public ViewMatrix(Context context) {
        this(context,null);
    }

    public ViewMatrix(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewMatrix(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init(){
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ceshi);
        camera = new Camera();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,0,0,null);
        Matrix matrix = new Matrix();

        camera.rotateX(15);
        camera.rotateY(15);
        canvas.translate(0,50);
        camera.getMatrix(matrix);

        matrix.postScale(0.5f,0.5f);
        matrix.preTranslate(-width/2,-height/2);
        matrix.postTranslate(width/2,height/2);
        canvas.drawBitmap(bitmap,matrix,null);
    }
}
