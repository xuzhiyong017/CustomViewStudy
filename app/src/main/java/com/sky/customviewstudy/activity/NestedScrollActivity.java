package com.sky.customviewstudy.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.view.SlideUpView;

public class NestedScrollActivity extends AppCompatActivity {

    private RecyclerView recycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scroll);
        recycleView = findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setNestedScrollingEnabled(true);
        recycleView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = View.inflate(parent.getContext(),R.layout.recycle_view_item,null);
                return new SlideUpView.MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((SlideUpView.MyHolder) holder).textView.setText("位置"+position);
            }

            @Override
            public int getItemCount() {
                return 30;
            }
        });
    }
}
