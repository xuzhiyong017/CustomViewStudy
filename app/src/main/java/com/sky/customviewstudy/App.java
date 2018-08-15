package com.sky.customviewstudy;

import android.app.Application;

import com.sky.customviewstudy.utils.CrashHandler;
import com.squareup.leakcanary.LeakCanary;

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        CrashHandler.getInstance().init(this);

    }
}
