package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.MosaicView;
import com.sky.customviewstudy.view.SimpleMosaicView;

public class MosaicActivity extends AppCompatActivity {

    private MosaicView mosaicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mosaic);
        mosaicView = (MosaicView) findViewById(R.id.mosaic_view);
    }

    public void undo(View view){
        mosaicView.undo();
    }

    public void redo(View view){
        mosaicView.redo();
    }

    public void mosaic(View view){
        mosaicView.setMode(MosaicView.Mode.DRAW);
    }

    public void eraser(View view){
        mosaicView.setMode(MosaicView.Mode.ERASER);
    }
    public void save(View view){
        mosaicView.saveBitmap();
    }
}
