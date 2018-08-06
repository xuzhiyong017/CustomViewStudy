package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.ReversalSeekBar;

public class ReversalSeekBarActivity extends AppCompatActivity {

    TextView mNums;
    SeekBar mSetBar;
    private int process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rever_seek_bar);
        mNums = findViewById(R.id.num);
        mSetBar = findViewById(R.id.seek_bar_set);

        final ReversalSeekBar bar = findViewById(R.id.seek_bar);
        bar.setOnSeekProgressListener(new ReversalSeekBar.OnSeekProgressListener() {
            @Override
            public void onSeekDown() {

            }

            @Override
            public void onSeekUp() {

            }

            @Override
            public void onSeekProgress(float progress) {
                mNums.setText("progress :"+progress);
            }
        });


        mSetBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                process = progress;
                bar.setProgress(process / 100.f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}
