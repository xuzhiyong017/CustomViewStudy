package com.sky.customviewstudy.utils;

import android.content.Context;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author: xuzhiyong
 * @date: 2018/8/6 10:27
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class SingleInstance {

    private WeakReference<Context> mContext;
    private static SingleInstance instance;

    private SingleInstance(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    public static SingleInstance getInstance(Context context) {
        if (instance == null) {
            instance = new SingleInstance(context);
        }
        return instance;
    }

    public void say() {
        Log.i("tag", "this is single instance");
        Log.i("tag", "：code：" + instance.hashCode());
    }

}
