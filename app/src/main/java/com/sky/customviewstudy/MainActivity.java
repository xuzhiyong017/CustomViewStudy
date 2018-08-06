package com.sky.customviewstudy;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sky.customviewstudy.activity.ColorActivity;
import com.sky.customviewstudy.activity.ColorFilterActivity;
import com.sky.customviewstudy.activity.MatrixActivity;
import com.sky.customviewstudy.activity.MosaicActivity;
import com.sky.customviewstudy.activity.PaintActivity;
import com.sky.customviewstudy.activity.PathActivity;
import com.sky.customviewstudy.activity.PileAvertViewActivity;
import com.sky.customviewstudy.activity.ReversalSeekBarActivity;
import com.sky.customviewstudy.activity.RxJavaActivity;
import com.sky.customviewstudy.activity.SquareActivity;
import com.sky.customviewstudy.service.VideoWallPagerService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mosaic(View v) {
        startActivity(new Intent(this, MosaicActivity.class));

    }

    public void filter(View v) {
        startActivity(new Intent(this, ColorFilterActivity.class));

    }

    public void paint(View v) {
        startActivity(new Intent(this, PaintActivity.class));

    }

    public void colorSelect(View v) {
        startActivity(new Intent(this, ColorActivity.class));

    }

    public void matrixRotate(View v) {
        startActivity(new Intent(this, MatrixActivity.class));

    }

    public void caremaWallPager(View v){
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, VideoWallPagerService.class));
        startActivity(intent);
    }

    public void reversalSeekBar(View v){
        startActivity(new Intent(this, ReversalSeekBarActivity.class));
    }

    public void rxjavaJump(View v){
        startActivity(new Intent(this, RxJavaActivity.class));
    }

    public void pileAvert(View v){
        startActivity(new Intent(this, PileAvertViewActivity.class));
    }
}
