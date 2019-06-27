package com.sky.customviewstudy.bean;

import android.support.annotation.NonNull;

/**
 * @author: xuzhiyong
 * @date: 2018/10/22 14:59
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class MultiModel implements Comparable<MultiModel>{

    private String path;
    private String date;
    private int type;
    private long duration;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int compareTo(@NonNull MultiModel o) {
        return (int)(o.duration - duration);
    }

}
