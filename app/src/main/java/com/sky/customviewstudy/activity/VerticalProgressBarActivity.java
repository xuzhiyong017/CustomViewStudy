package com.sky.customviewstudy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.scheduler.CountScheduler;
import com.sky.customviewstudy.view.TextClockView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: xuzhiyong
 * @date: 2019/6/27 14:31
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class VerticalProgressBarActivity extends Activity {

    TextClockView mTextClockView;
    private CountScheduler countScheduler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_progress_activity);
        mTextClockView = findViewById(R.id.text_clock);

         countScheduler = new CountScheduler(3000,10000);


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextClockView.doInvalidate();
                        countScheduler.add();
                    }
                });
            }
        },0,1000);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countScheduler.cancel();
    }
}
