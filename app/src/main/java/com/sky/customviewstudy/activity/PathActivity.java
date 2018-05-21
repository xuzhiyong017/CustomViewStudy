package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.MosaicView;
import com.sky.customviewstudy.view.PathView;
import com.sky.customviewstudy.view.XfermodeView;

public class PathActivity extends AppCompatActivity {


    PathView mPathView;
    XfermodeView xfermodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);
        mPathView = findViewById(R.id.path_view);
        xfermodeView = findViewById(R.id.xfer_view);
    }

    private boolean isDraing = true;

    public void switchMode(View v){
        xfermodeView.mDraw = isDraing;
        mPathView.setMode(isDraing ? MosaicView.Mode.DRAW: MosaicView.Mode.ERASER);
        isDraing = !isDraing;
    }
}
