package com.sky.customviewstudy.activity;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.PaintView;

public class PaintActivity extends AppCompatActivity {

    PaintView mPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        mPaintView = findViewById(R.id.paint_view);
    }


    public void paint(View view){
        mPaintView.setMode(PaintView.Mode.DRAW);
    }

    public void eraser(View view){
        mPaintView.setMode(PaintView.Mode.ERASER);
    }

    public void undo(View view){
        mPaintView.undo();
    }

    public void redo(View view){
        mPaintView.redo();
    }
}
