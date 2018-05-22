package com.sky.customviewstudy.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.adapter.ColorListAdapter;
import com.sky.customviewstudy.view.ScaleRecyclerView;

public class ColorActivity extends AppCompatActivity {

    public int[] mPaintColors = {Color.parseColor("#000000"),
            Color.parseColor("#ffffff"),
            Color.parseColor("#999999"),
            Color.parseColor("#703800"),
            Color.parseColor("#f3382c"),
            Color.parseColor("#ff852b"),
            Color.parseColor("#ffbf00"),
            Color.parseColor("#fff52f"),
            Color.parseColor("#95ed31"),
            Color.parseColor("#2cc542"),
            Color.parseColor("#3fddc1"),
            Color.parseColor("#00b4fe"),
            Color.parseColor("#436cff"),
            Color.parseColor("#7c19c9")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        final ScaleRecyclerView recyclerView = (ScaleRecyclerView) findViewById(R.id.main_recyclerView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        manager.supportsPredictiveItemAnimations();
        recyclerView.setLayoutManager(manager);

        final ColorListAdapter mAdapter = new ColorListAdapter(this,mPaintColors,null);
        recyclerView.setAdapter(mAdapter);

    }
}
