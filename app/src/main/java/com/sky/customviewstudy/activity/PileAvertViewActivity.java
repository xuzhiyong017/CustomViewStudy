package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.PileAvertView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PileAvertViewActivity extends AppCompatActivity {

    PileAvertView head_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pile_avert_view);
        head_view = findViewById(R.id.head_view);
        initHeadView();
    }

    private void initHeadView() {
        List<String> list = new ArrayList<>();
        list.add("https://oss-cn-hangzhou.aliyuncs.com/tsnrhapp/user/photos/95a06ddd254f96b7dfb82491888c5abf_0.png");
        list.add("https://oss-cn-hangzhou.aliyuncs.com/tsnrhapp/user/photos/95a06ddd254f96b7dfb82491888c5abf_0.png");
        list.add("https://oss-cn-hangzhou.aliyuncs.com/tsnrhapp/user/photos/95a06ddd254f96b7dfb82491888c5abf_0.png");
        head_view.setAvertImageListener(list, new PileAvertView.OnLoadListener<String>() {
            @Override
            public void showImage(String s, CircleImageView imageView) {
                Glide.with(PileAvertViewActivity.this).load(s).placeholder(R.drawable.beautygirl).into(imageView);
            }
        });
    }
}
