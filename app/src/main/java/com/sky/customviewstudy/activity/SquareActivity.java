package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.CropImage;

public class SquareActivity extends AppCompatActivity {


    private CropImage cropImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);
        cropImage = findViewById(R.id.crop_image);
    }

    public void startAnimator(View v){
        cropImage.startAnimator();
    }
}
