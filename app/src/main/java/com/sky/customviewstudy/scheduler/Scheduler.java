package com.sky.customviewstudy.scheduler;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * @author: xuzhiyong
 * @date: 2019/10/9 9:23
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public abstract class Scheduler {

    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private final long triggerTime;
    private final long maxTime;
    private final Runnable triggerTask = new Runnable() {
        @Override
        public void run() {
            if (System.currentTimeMillis()- lastScheduleTime >= triggerTime) {
                doTrigger();
            } else {
                MAIN_HANDLER.removeCallbacks(triggerTask);
                MAIN_HANDLER.postDelayed(triggerTask, triggerTime);
            }
        }
    };
    private final Runnable maxTask = new Runnable() {
        @Override
        public void run() {
            doTrigger();
        }
    };
    private boolean isFirstSchedule = true;
    private long lastScheduleTime;

    private void doTrigger() {
        MAIN_HANDLER.removeCallbacks(triggerTask);
        MAIN_HANDLER.removeCallbacks(maxTask);
        onTrigger();
        isFirstSchedule = true;
    }


    public Scheduler(long triggerTime, long maxTime) {
        this.triggerTime = triggerTime;
        if (maxTime < triggerTime) {
            maxTime = triggerTime;
        }
        this.maxTime = maxTime;
    }

    public Scheduler(long maxTime) {
        this.triggerTime = 0;
        this.maxTime = maxTime;
    }

    protected abstract void onTrigger();

    public void schedule() {
        if (triggerTime > 0) {
            long currentTime = System.currentTimeMillis();
            long dur = currentTime - lastScheduleTime;
            Log.d("CountScheduler","dur="+dur+" triggerTime="+triggerTime);
            if (dur > triggerTime) {
                MAIN_HANDLER.removeCallbacks(triggerTask);
                MAIN_HANDLER.postDelayed(triggerTask, triggerTime);
            }
            lastScheduleTime = currentTime;
        }
        if (isFirstSchedule) {
            isFirstSchedule = false;
            MAIN_HANDLER.postDelayed(maxTask, maxTime);
        }
    }

    public void cancel() {
        MAIN_HANDLER.removeCallbacks(triggerTask);
        MAIN_HANDLER.removeCallbacks(maxTask);
        isFirstSchedule = true;
        lastScheduleTime = 0;
    }
}
