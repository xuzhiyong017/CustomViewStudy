package com.sky.customviewstudy.scheduler;

import android.util.Log;

/**
 * @author: xuzhiyong
 * @date: 2019/10/9 9:28
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class CountScheduler extends Scheduler {

    int count = 0;

    public CountScheduler(long maxTime) {
        super(maxTime);
    }

    public CountScheduler(long triggerTime, long maxTime) {
        super(triggerTime, maxTime);
    }

    public void add(){
        count++;
        schedule();
    }

    @Override
    protected void onTrigger() {
        Log.d("CountScheduler","count="+count);
    }

    @Override
    public void cancel() {
        super.cancel();
        count = 0;
    }
}
