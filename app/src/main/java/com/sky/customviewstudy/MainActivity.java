package com.sky.customviewstudy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sky.customviewstudy.activity.ColorActivity;
import com.sky.customviewstudy.activity.MatrixActivity;
import com.sky.customviewstudy.activity.MosaicActivity;
import com.sky.customviewstudy.activity.PathActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jump(View v){
        startActivity(new Intent(this, ColorActivity.class));
    }
}
