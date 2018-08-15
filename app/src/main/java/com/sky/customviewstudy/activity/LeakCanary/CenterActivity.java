package com.sky.customviewstudy.activity.LeakCanary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.utils.SingleInstance;

public class CenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        SingleInstance.getInstance(this).say();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
