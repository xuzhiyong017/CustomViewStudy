package com.sky.customviewstudy.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sky.customviewstudy.R;

public class AppBarLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar_layout);

        AppBarLayout appBarLayout = findViewById(R.id.appbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("AppBarLayoutActivity","offset="+verticalOffset);
//                if(verticalOffset < -100){
//                    toolbar_title.setVisibility(View.GONE);
//                }else{
//                    toolbar_title.setVisibility(View.VISIBLE);
//                }
            }
        });
    }
}
