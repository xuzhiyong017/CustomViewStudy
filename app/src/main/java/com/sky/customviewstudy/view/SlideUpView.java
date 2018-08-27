package com.sky.customviewstudy.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sky.customviewstudy.R;

/**
 * @author: xuzhiyong
 * @date: 2018/8/27 18:10
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class SlideUpView extends FrameLayout {

    private static final String TAG = "SlideUpView";
    private View slide_view;
    private ViewDragHelper viewDragHelper;

    public SlideUpView(Context context) {
        this(context, null);
    }

    public SlideUpView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideUpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context) {
        View.inflate(context, R.layout.slide_up_view,this);
        slide_view = findViewById(R.id.slide_view);

        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DraggableViewCallback(this));

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }


    private static class DraggableViewCallback extends ViewDragHelper.Callback {

        private static float Y_MIN_VELOCITY = 300;//竖直方向关闭最小值 px

        SlideUpView slideUpView;

        public DraggableViewCallback(SlideUpView slideUpView) {
            Y_MIN_VELOCITY = slideUpView.getHeight() / 3;
            this.slideUpView = slideUpView;
        }


        /**
         * 子控件位置改变时触发（包括X和Y轴方向）
         *
         * @param left position.
         * @param top  position.
         * @param dx   change in X position from the last call.
         * @param dy   change in Y position from the last call.
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            slideUpView.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 子控件竖直方向位置改变时触发
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //不能滑出顶部
            return Math.max(top, 0);
        }

        /**
         * 子控件水平方向位置改变时触发
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //屏蔽掉水平方向
            return 0;
        }

        /**
         * 手指松开时触发
         *
         * @param releasedChild the captured child view now being released.
         * @param xVel          X velocity of the pointer as it left the screen in pixels per second.
         * @param yVel          Y velocity of the pointer as it left the screen in pixels per second.
         */
        @Override
        public void onViewReleased(View releasedChild, float xVel, float yVel) {
            super.onViewReleased(releasedChild, xVel, yVel);
            Log.d(TAG, "onViewReleased");
            int top = releasedChild.getTop(); //获取子控件Y值
            int left = releasedChild.getLeft(); //获取子控件X值

            if (Math.abs(left) <= Math.abs(top)) {//若为竖直滑动
                triggerOnReleaseActionsWhileVerticalDrag(top);
            }
        }

        @Override
        public boolean tryCaptureView(View view, int pointerId) {
            return true;
        }

        /**
         * 计算竖直方向的滑动
         */
        private void triggerOnReleaseActionsWhileVerticalDrag(float yVel) {
            if (yVel > 0 && yVel >= Y_MIN_VELOCITY) {
                slideUpView.closedToBottom();
                Log.d(TAG, "ReleaseVerticalDrag" + ", closeToBottom");
            } else {
                slideUpView.onReset();
                Log.d(TAG, "ReleaseVerticalDrag" + ", onReset");
            }
        }
    }

    private void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
    }

    private void closedToBottom() {
    }

    private void onReset() {
    }
}
