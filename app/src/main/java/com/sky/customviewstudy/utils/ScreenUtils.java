package com.sky.customviewstudy.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * @author: xuzhiyong
 * @date: 2018/10/23 18:26
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class ScreenUtils {

    public static int getWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}
