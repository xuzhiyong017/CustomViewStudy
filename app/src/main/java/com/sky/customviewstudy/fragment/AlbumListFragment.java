package com.sky.customviewstudy.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.customviewstudy.R;
import com.sky.customviewstudy.bean.CategoryModel;
import com.sky.customviewstudy.bean.MultiModel;
import com.sky.customviewstudy.utils.QueryProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuzhiyong
 * @date: 2018/10/26 17:35
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class AlbumListFragment extends BaseFragment {

    RecyclerView mAlbumList;
    private AlbumAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAlbumList = view.findViewById(R.id.album_list);
        mAlbumList.setLayoutManager(new LinearLayoutManager(context));
        mAlbumList.setAdapter(mAdapter = new AlbumAdapter(context));
        new QueryProcessor(context).queryAll(new QueryProcessor.QueryCallback() {
            @Override
            public void querySuccess(ArrayList<MultiModel> multiModels) {
                filterModel(multiModels);
            }

            @Override
            public void queryFailed() {

            }
        });
    }

    private void filterModel(final ArrayList<MultiModel> multiModels) {
        final List<MultiModel> videoList = new ArrayList<>();
        for (int i = 0; i < multiModels.size(); i++) {
            if(multiModels.get(i).getType() == 1){
                videoList.add(multiModels.get(i));
            }
        }

        mAlbumList.post(new Runnable() {
            @Override
            public void run() {
                mAdapter.addData(new CategoryModel("相机胶卷",multiModels.get(0).getPath(),multiModels.size()));
                mAdapter.addData(new CategoryModel("视频",videoList.get(0).getPath(),videoList.size()));
            }
        });

    }

    public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder>{

        List<CategoryModel> datas;
        private Context context;

        public AlbumAdapter(Context context) {
            super();
            this.context = context;
        }


        public void addData(CategoryModel categoryModel){
            if(datas == null){
                datas = new ArrayList<>();
            }

            datas.add(categoryModel);
            notifyDataSetChanged();
        }

        @Override
        public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AlbumViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.album_list,parent,false));
        }

        @Override
        public void onBindViewHolder(AlbumViewHolder holder, int position) {
            Glide.with(context).load(datas.get(position).coverUrl).into(holder.imageView);
            holder.mTitleView.setText(datas.get(position).title);
            holder.mNumView.setText(String.valueOf(datas.get(position).nums));
        }

        @Override
        public int getItemCount() {
            return datas == null ? 0:datas.size();
        }
    }


    public static class AlbumViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView mTitleView;
        public TextView mNumView;
        public View list_item_root;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            list_item_root = itemView.findViewById(R.id.list_item_root);
            imageView = itemView.findViewById(R.id.image_list);
            mTitleView = itemView.findViewById(R.id.title_tv);
            mNumView = itemView.findViewById(R.id.num);
        }
    }
}
