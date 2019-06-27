package com.sky.customviewstudy.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.sky.customviewstudy.bean.MultiModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * @author: xuzhiyong
 * @date: 2018/10/22 14:57
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class QueryProcessor {

    //弱引用,防止内存泄漏
    private WeakReference<Context> context;
    private ContentResolver resolver;
    private Cursor cursor = null;

    public QueryProcessor(Context context) {
        this.context = new WeakReference<>(context);
        resolver = context.getContentResolver();
    }

    /**
     * 查询所有图片和视频
     *
     * @param callback 查询完毕后的回调接口
     */
    public void queryAll(final QueryCallback callback) {
        if (context == null) {
            return;
        }
        //开辟一个新线程执行耗时查询
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<MultiModel> multiModels = new ArrayList<>();
                queryImages(multiModels, callback);
                queryVideos(multiModels, callback);
                callback.querySuccess(multiModels);
                cursor.close();
            }
        }).start();
    }

    /**
     * 查询所有图片
     *
     * @param multiModels 存放查询到的图片和视频的集合
     * @param callback    查询完毕后的回调接口
     */
    private void queryImages(ArrayList<MultiModel> multiModels, final QueryCallback callback) {
        cursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null
                , null, null);
        if (cursor == null) {
            callback.queryFailed();
            return;
        }
        while (cursor.moveToNext()) {
            MultiModel multiModel = new MultiModel();
            multiModel.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
            multiModel.setDate(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)));
            multiModel.setType(0);
            multiModels.add(multiModel);
        }
    }

    /**
     * 查询所有视频
     *
     * @param multiModels 存放查询到的图片和视频的集合
     * @param callback    查询完毕后的回调接口
     */
    private void queryVideos(ArrayList<MultiModel> multiModels, final QueryCallback callback) {
        cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null
                , null, null);
        if (cursor == null) {
            callback.queryFailed();
            return;
        }
        while (cursor.moveToNext()) {
            MultiModel multiModel = new MultiModel();
            multiModel.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
            multiModel.setDate(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED)));
            multiModel.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION)));
            multiModel.setType(1);
            multiModels.add(multiModel);
        }
    }

    /**
     * 查询照片和视频结束后的回调接口
     */
    public interface QueryCallback {
        void querySuccess(ArrayList<MultiModel> multiModels);

        void queryFailed();
    }
}

