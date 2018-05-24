package com.sky.customviewstudy;

import android.app.Application;

import com.sky.customviewstudy.utils.CrashHandler;

/**
 * Author: xuzhiyong
 * Time:2018/5/22 星期二
 * description:
 * projectName:CustomViewStudy
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
