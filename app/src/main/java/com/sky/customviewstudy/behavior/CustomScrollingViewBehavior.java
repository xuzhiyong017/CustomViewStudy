package com.sky.customviewstudy.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author: xuzhiyong
 * @date: 2018/10/23 17:52
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class CustomScrollingViewBehavior extends AppBarLayout.ScrollingViewBehavior {

    public CustomScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return true;
    }

}
