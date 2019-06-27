package android.support.design.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author: xuzhiyong
 * @date: 2018/10/23 15:13
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class CustomAppBarBehavior extends AppBarLayout.Behavior{

    public boolean canScroll  = true;

    public CustomAppBarBehavior(){
    }

    public CustomAppBarBehavior(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if(this.canScroll){
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        }
    }


    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if(this.canScroll){
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
            if ((consumed[1] == 0) && (dy < 0) && (super.getTopBottomOffsetForScrollingSibling() != -child.getTotalScrollRange())){
                consumed[1] = dy;
                super.onNestedScroll(coordinatorLayout, child, target, 0, 0, 0, dy, type);
            }
        }
    }



    public final void setCanScroll(boolean canScroll) {
        if (this.canScroll != canScroll){
            this.canScroll = canScroll;
        }
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        return true;
    }

}
