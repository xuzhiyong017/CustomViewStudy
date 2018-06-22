package com.sky.customviewstudy.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.sky.customviewstudy.damon.Wathcer;

import java.util.Timer;
import java.util.TimerTask;

public class PushService extends Service {
    private int number = 0;

    public PushService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Timer timer = new Timer();

        Wathcer wathcer = new Wathcer();
        wathcer.createSocketServer("");
        wathcer.connectServer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("PushServer","链接上了"+number++);
            }
        },1000L);

    }
}
