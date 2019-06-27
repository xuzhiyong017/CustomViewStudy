package com.sky.customviewstudy.widget;

/**
 * @author: xuzhiyong
 * @date: 2019/2/13 10:08
 * @Email: 18971269648@163.com
 * @description: 描述
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class SimpleDragView extends FrameLayout {
    public static final Companion Companion = new Companion();
    public static final int SCALE_TYPE_ANIMATOR = 0;
    public static final int SCALE_TYPE_LAYOUT = 1;
    private HashMap _$_findViewCache;
    private DragAnimOptions animOptions;
    private LayoutParams containerParams;
    private FrameLayout containerView;
    private ViewGroup contentView;
    private FrameLayout defaultContainerView;
    private GestureDetector detector;
    private boolean doingDragAnim;
    private float downX;
    private float downY;
    private DragCallBack dragCallBack;
    private final LinkedList<DragElement> dragViewElements;
    private final HashMap<Integer, DragViewAttrs> dragViewsAttrPreMap;
    private boolean enableDrag;
    private AnimatorSet finishAnimatorSet;
    private boolean needAddToContainer;
    private ObservableTouchEvent observableTouchEvent;
    private final SimpleDragView$onGestureListener$1 onGestureListener;
    private AnimatorSet recoverAnimatorSet;
    private float yDistanceTotal;

     public static  class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u000e\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0014\u0010\u000f\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u001a\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0014\"\u0004\b\u0019\u0010\u0016R\u001a\u0010\u001a\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0014\"\u0004\b\u001c\u0010\u0016R\u001a\u0010\u001d\u001a\u00020\u0012X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0014\"\u0004\b\u001f\u0010\u0016¨\u0006 "}, d2 = {"Lcom/sup/android/uikit/widget/SimpleDragView$DragAnimOptions;", "", "()V", "addToContainerOrder", "", "getAddToContainerOrder", "()I", "setAddToContainerOrder", "(I)V", "animationContainerView", "Landroid/widget/FrameLayout;", "getAnimationContainerView", "()Landroid/widget/FrameLayout;", "setAnimationContainerView", "(Landroid/widget/FrameLayout;)V", "backGroundColorRes", "getBackGroundColorRes", "distanceYThreshold", "", "getDistanceYThreshold", "()F", "setDistanceYThreshold", "(F)V", "minAlpha", "getMinAlpha", "setMinAlpha", "scaleTotalDistanceY", "getScaleTotalDistanceY", "setScaleTotalDistanceY", "velocityYThreshold", "getVelocityYThreshold", "setVelocityYThreshold", "uikit_release"}, k = 1, mv = {1, 1, 10})
    public static final class DragAnimOptions {
        public static ChangeQuickRedirect changeQuickRedirect;
        private int addToContainerOrder;
        private FrameLayout animationContainerView;
        private final int backGroundColorRes = 17170443;
        private float distanceYThreshold = 200.0f;
        private float minAlpha = 0.4f;
        private float scaleTotalDistanceY = 500.0f;
        private float velocityYThreshold = 500.0f;

        public final float getScaleTotalDistanceY() {
            return this.scaleTotalDistanceY;
        }

        public final void setScaleTotalDistanceY(float f) {
            this.scaleTotalDistanceY = f;
        }

        public final int getBackGroundColorRes() {
            return this.backGroundColorRes;
        }

        public final float getMinAlpha() {
            return this.minAlpha;
        }

        public final void setMinAlpha(float f) {
            this.minAlpha = f;
        }

        public final float getDistanceYThreshold() {
            return this.distanceYThreshold;
        }

        public final void setDistanceYThreshold(float f) {
            this.distanceYThreshold = f;
        }

        public final float getVelocityYThreshold() {
            return this.velocityYThreshold;
        }

        public final void setVelocityYThreshold(float f) {
            this.velocityYThreshold = f;
        }

        public final FrameLayout getAnimationContainerView() {
            return this.animationContainerView;
        }

        public final void setAnimationContainerView(FrameLayout frameLayout) {
            this.animationContainerView = frameLayout;
        }

        public final int getAddToContainerOrder() {
            return this.addToContainerOrder;
        }

        public final void setAddToContainerOrder(int i) {
            this.addToContainerOrder = i;
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H&J\b\u0010\b\u001a\u00020\u0003H&J\b\u0010\t\u001a\u00020\u0003H&J\b\u0010\n\u001a\u00020\u0003H&J\b\u0010\u000b\u001a\u00020\u0003H&J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&¨\u0006\u000e"}, d2 = {"Lcom/sup/android/uikit/widget/SimpleDragView$DragCallBack;", "", "onDrag", "", "dx", "", "dy", "totalDistanceY", "onDragToEndAnimEnd", "onDragToEndAnimStart", "onRecoverAnimEnd", "onRecoverAnimStart", "shouldDragIntercept", "", "uikit_release"}, k = 1, mv = {1, 1, 10})
    public interface DragCallBack {
        void onDrag(float f, float f2, float f3);

        void onDragToEndAnimEnd();

        void onDragToEndAnimStart();

        void onRecoverAnimEnd();

        void onRecoverAnimStart();

        boolean shouldDragIntercept(float f, float f2);
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u0006\"\u0004\b\u000b\u0010\bR\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0014\"\u0004\b\u0018\u0010\u0016R\u001a\u0010\u0019\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u0014\"\u0004\b\u001a\u0010\u0016R\u001a\u0010\u001b\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0014\"\u0004\b\u001c\u0010\u0016R\u001a\u0010\u001d\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001e\u0010\u0014\"\u0004\b\u001f\u0010\u0016R\u001a\u0010 \u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%¨\u0006&"}, d2 = {"Lcom/sup/android/uikit/widget/SimpleDragView$DragElement;", "", "()V", "dragBeginLocation", "Landroid/graphics/Rect;", "getDragBeginLocation", "()Landroid/graphics/Rect;", "setDragBeginLocation", "(Landroid/graphics/Rect;)V", "dragEndLocation", "getDragEndLocation", "setDragEndLocation", "dragView", "Landroid/view/View;", "getDragView", "()Landroid/view/View;", "setDragView", "(Landroid/view/View;)V", "isEnableScaleX", "", "()Z", "setEnableScaleX", "(Z)V", "isEnableScaleY", "setEnableScaleY", "isEnableTransX", "setEnableTransX", "isEnableTransY", "setEnableTransY", "needAddToContainer", "getNeedAddToContainer", "setNeedAddToContainer", "scaleType", "", "getScaleType", "()I", "setScaleType", "(I)V", "uikit_release"}, k = 1, mv = {1, 1, 10})
    public static final class DragElement {
        public static ChangeQuickRedirect changeQuickRedirect;
        private Rect dragBeginLocation;
        private Rect dragEndLocation;
        private View dragView;
        private boolean isEnableScaleX = true;
        private boolean isEnableScaleY = true;
        private boolean isEnableTransX = true;
        private boolean isEnableTransY = true;
        private boolean needAddToContainer;
        private int scaleType;

        public final View getDragView() {
            return this.dragView;
        }

        public final void setDragView(View view) {
            this.dragView = view;
        }

        public final Rect getDragBeginLocation() {
            return this.dragBeginLocation;
        }

        public final void setDragBeginLocation(Rect rect) {
            this.dragBeginLocation = rect;
        }

        public final Rect getDragEndLocation() {
            return this.dragEndLocation;
        }

        public final void setDragEndLocation(Rect rect) {
            this.dragEndLocation = rect;
        }

        public final int getScaleType() {
            return this.scaleType;
        }

        public final void setScaleType(int i) {
            this.scaleType = i;
        }

        public final boolean getNeedAddToContainer() {
            return this.needAddToContainer;
        }

        public final void setNeedAddToContainer(boolean z) {
            this.needAddToContainer = z;
        }

        public final boolean isEnableTransX() {
            return this.isEnableTransX;
        }

        public final void setEnableTransX(boolean z) {
            this.isEnableTransX = z;
        }

        public final boolean isEnableTransY() {
            return this.isEnableTransY;
        }

        public final void setEnableTransY(boolean z) {
            this.isEnableTransY = z;
        }

        public final boolean isEnableScaleX() {
            return this.isEnableScaleX;
        }

        public final void setEnableScaleX(boolean z) {
            this.isEnableScaleX = z;
        }

        public final boolean isEnableScaleY() {
            return this.isEnableScaleY;
        }

        public final void setEnableScaleY(boolean z) {
            this.isEnableScaleY = z;
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0012\"\u0004\b\u0017\u0010\u0014R\u001c\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u001b\"\u0004\b\u001c\u0010\u001d¨\u0006\u001e"}, d2 = {"Lcom/sup/android/uikit/widget/SimpleDragView$DragViewAttrs;", "", "()V", "layoutParams", "Landroid/view/ViewGroup$LayoutParams;", "getLayoutParams", "()Landroid/view/ViewGroup$LayoutParams;", "setLayoutParams", "(Landroid/view/ViewGroup$LayoutParams;)V", "parent", "Landroid/view/ViewGroup;", "getParent", "()Landroid/view/ViewGroup;", "setParent", "(Landroid/view/ViewGroup;)V", "pivotX", "", "getPivotX", "()F", "setPivotX", "(F)V", "pivotY", "getPivotY", "setPivotY", "stubView", "Landroid/view/View;", "getStubView", "()Landroid/view/View;", "setStubView", "(Landroid/view/View;)V", "uikit_release"}, k = 1, mv = {1, 1, 10})
    public static final class DragViewAttrs {
        public static ChangeQuickRedirect changeQuickRedirect;
        private LayoutParams layoutParams;
        private ViewGroup parent;
        private float pivotX;
        private float pivotY;
        private View stubView;

        public final ViewGroup getParent() {
            return this.parent;
        }

        public final void setParent(ViewGroup viewGroup) {
            this.parent = viewGroup;
        }

        public final float getPivotX() {
            return this.pivotX;
        }

        public final void setPivotX(float f) {
            this.pivotX = f;
        }

        public final float getPivotY() {
            return this.pivotY;
        }

        public final void setPivotY(float f) {
            this.pivotY = f;
        }

        public final LayoutParams getLayoutParams() {
            return this.layoutParams;
        }

        public final void setLayoutParams(LayoutParams layoutParams) {
            this.layoutParams = layoutParams;
        }

        public final View getStubView() {
            return this.stubView;
        }

        public final void setStubView(View view) {
            this.stubView = view;
        }
    }

    @Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007H&¨\u0006\b"}, d2 = {"Lcom/sup/android/uikit/widget/SimpleDragView$ObservableTouchEvent;", "", "onTouch", "", "v", "Landroid/view/View;", "event", "Landroid/view/MotionEvent;", "uikit_release"}, k = 1, mv = {1, 1, 10})
    public interface ObservableTouchEvent {
        void onTouch(View view, MotionEvent motionEvent);
    }

    @JvmOverloads
    public SimpleDragView(Context context) {
        this(context, null, 0, 6, null);
    }

    @JvmOverloads
    public SimpleDragView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public void _$_clearFindViewByIdCache() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7323, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7323, new Class[0], Void.TYPE);
            return;
        }
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (PatchProxy.isSupport(new Object[]{new Integer(i)}, this, changeQuickRedirect, false, 7322, new Class[]{Integer.TYPE}, View.class)) {
            return (View) PatchProxy.accessDispatch(new Object[]{new Integer(i)}, this, changeQuickRedirect, false, 7322, new Class[]{Integer.TYPE}, View.class);
        }
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            view = findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    @JvmOverloads
    public /* synthetic */ SimpleDragView(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i2 & 2) != 0) {
            attributeSet = (AttributeSet) null;
        }
        if ((i2 & 4) != 0) {
            i = 0;
        }
        this(context, attributeSet, i);
    }

    @JvmOverloads
    public SimpleDragView(Context context, AttributeSet attributeSet, int i) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super(context, attributeSet, i);
        this.downX = FloatCompanionObject.INSTANCE.getMIN_VALUE();
        this.downY = FloatCompanionObject.INSTANCE.getMIN_VALUE();
        this.dragViewElements = new LinkedList();
        this.animOptions = new DragAnimOptions();
        this.dragViewsAttrPreMap = new HashMap();
        Activity scanForActivity = scanForActivity(context);
        ViewGroup viewGroup = scanForActivity != null ? (ViewGroup) scanForActivity.findViewById(16908290) : null;
        if (!(viewGroup instanceof ViewGroup)) {
            viewGroup = null;
        }
        this.contentView = viewGroup;
        this.defaultContainerView = new FrameLayout(context);
        FrameLayout frameLayout = this.defaultContainerView;
        if (frameLayout != null) {
            frameLayout.setId(R.id.container_drag_view);
        }
        this.containerParams = new FrameLayout.LayoutParams(-1, -1);
        this.onGestureListener = new SimpleDragView$onGestureListener$1(this);
    }

    public final boolean getEnableDrag() {
        return this.enableDrag;
    }

    public final void setEnableDrag(boolean z) {
        this.enableDrag = z;
    }

    public final ObservableTouchEvent getObservableTouchEvent() {
        return this.observableTouchEvent;
    }

    public final void setObservableTouchEvent(ObservableTouchEvent observableTouchEvent) {
        this.observableTouchEvent = observableTouchEvent;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (PatchProxy.isSupport(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7304, new Class[]{MotionEvent.class}, Boolean.TYPE)) {
            return ((Boolean) PatchProxy.accessDispatch(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7304, new Class[]{MotionEvent.class}, Boolean.TYPE)).booleanValue();
        }
        ObservableTouchEvent observableTouchEvent = this.observableTouchEvent;
        if (observableTouchEvent != null) {
            observableTouchEvent.onTouch(this, motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void dispatchDraw(Canvas canvas) {
        if (PatchProxy.isSupport(new Object[]{canvas}, this, changeQuickRedirect, false, 7305, new Class[]{Canvas.class}, Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[]{canvas}, this, changeQuickRedirect, false, 7305, new Class[]{Canvas.class}, Void.TYPE);
            return;
        }
        try {
            super.dispatchDraw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static /* synthetic */ void config$default(SimpleDragView simpleDragView, LinkedList linkedList, DragCallBack dragCallBack, DragAnimOptions dragAnimOptions, int i, Object obj) {
        if ((i & 4) != 0) {
            dragAnimOptions = (DragAnimOptions) null;
        }
        simpleDragView.config(linkedList, dragCallBack, dragAnimOptions);
    }

    public final void config(LinkedList<DragElement> linkedList, DragCallBack dragCallBack, DragAnimOptions dragAnimOptions) {
        LinkedList<DragElement> linkedList2 = linkedList;
        DragCallBack dragCallBack2 = dragCallBack;
        DragAnimOptions dragAnimOptions2 = dragAnimOptions;
        if (PatchProxy.isSupport(new Object[]{linkedList2, dragCallBack2, dragAnimOptions2}, this, changeQuickRedirect, false, 7306, new Class[]{LinkedList.class, DragCallBack.class, DragAnimOptions.class}, Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[]{linkedList2, dragCallBack2, dragAnimOptions2}, this, changeQuickRedirect, false, 7306, new Class[]{LinkedList.class, DragCallBack.class, DragAnimOptions.class}, Void.TYPE);
            return;
        }
        Intrinsics.checkParameterIsNotNull(linkedList2, "dragElements");
        Intrinsics.checkParameterIsNotNull(dragCallBack2, "dragCallBack");
        this.detector = new GestureDetector(getContext(), this.onGestureListener);
        if (dragAnimOptions2 != null) {
            this.animOptions = dragAnimOptions2;
        }
        this.dragViewElements.clear();
        this.dragViewElements.addAll(linkedList2);
        this.dragCallBack = dragCallBack2;
    }

    private final boolean isConfigInvalid() {
        if (!PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7307, new Class[0], Boolean.TYPE)) {
            return (this.dragViewElements.isEmpty() || this.dragCallBack == null || !this.enableDrag) ? false : true;
        } else {
            return ((Boolean) PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7307, new Class[0], Boolean.TYPE)).booleanValue();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (PatchProxy.isSupport(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7308, new Class[]{MotionEvent.class}, Boolean.TYPE)) {
            return ((Boolean) PatchProxy.accessDispatch(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7308, new Class[]{MotionEvent.class}, Boolean.TYPE)).booleanValue();
        } else if (!isConfigInvalid()) {
            return false;
        } else {
            DragCallBack dragCallBack = this.dragCallBack;
            if (!(dragCallBack == null || motionEvent == null)) {
                int action = motionEvent.getAction();
                if (action != 0) {
                    if (!(action != 2 || this.downX == FloatCompanionObject.INSTANCE.getMIN_VALUE() || this.downY == FloatCompanionObject.INSTANCE.getMIN_VALUE() || !dragCallBack.shouldDragIntercept(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY) || this.doingDragAnim)) {
                        setDragBeginLocation();
                        addDragViewToContainer();
                        requestDisallowInterceptTouchEvent(true);
                        FrameLayout frameLayout = this.containerView;
                        if (frameLayout != null) {
                            frameLayout.setClickable(true);
                        }
                        this.doingDragAnim = true;
                        return true;
                    }
                } else if (this.finishAnimatorSet == null && this.recoverAnimatorSet == null) {
                    this.downX = motionEvent.getX();
                    this.downY = motionEvent.getY();
                    this.yDistanceTotal = 0.0f;
                    return false;
                } else {
                    AnimatorSet animatorSet = this.finishAnimatorSet;
                    if (animatorSet != null) {
                        animatorSet.end();
                    }
                    animatorSet = this.recoverAnimatorSet;
                    if (animatorSet != null) {
                        animatorSet.end();
                    }
                    this.downX = FloatCompanionObject.INSTANCE.getMIN_VALUE();
                    this.downY = FloatCompanionObject.INSTANCE.getMIN_VALUE();
                    return false;
                }
            }
            return false;
        }
    }

    private final void setDragBeginLocation() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7309, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7309, new Class[0], Void.TYPE);
            return;
        }
        this.needAddToContainer = false;
        for (DragElement dragElement : this.dragViewElements) {
            View dragView = dragElement.getDragView();
            if (dragView != null) {
                if (dragElement.getDragBeginLocation() == null) {
                    dragElement.setDragBeginLocation(ViewHelper.getBoundsInWindow(dragView));
                }
                Rect dragBeginLocation = dragElement.getDragBeginLocation();
                if (dragBeginLocation == null) {
                    Intrinsics.throwNpe();
                }
                dragView.setPivotX(((float) dragBeginLocation.width()) / 2.0f);
                dragBeginLocation = dragElement.getDragBeginLocation();
                if (dragBeginLocation == null) {
                    Intrinsics.throwNpe();
                }
                dragView.setPivotY(((float) dragBeginLocation.height()) / 2.0f);
                if (dragElement.getNeedAddToContainer()) {
                    this.needAddToContainer = true;
                }
            }
        }
    }

    private final void addDragViewToContainer() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7310, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7310, new Class[0], Void.TYPE);
            return;
        }
        if (this.animOptions.getAnimationContainerView() != null && this.needAddToContainer) {
            this.containerView = this.animOptions.getAnimationContainerView();
            FrameLayout frameLayout = this.containerView;
            this.containerParams = frameLayout != null ? frameLayout.getLayoutParams() : null;
        } else if (this.animOptions.getAnimationContainerView() == null && this.needAddToContainer) {
            this.containerView = this.defaultContainerView;
        }
        this.dragViewsAttrPreMap.clear();
        ViewGroup viewGroup = this.contentView;
        if (viewGroup != null) {
            Object obj = this.containerView;
            if (obj != null) {
                obj.setBackgroundColor(getResources().getColor(this.animOptions.getBackGroundColorRes()));
                FrameLayout frameLayout2 = this.defaultContainerView;
                ViewParent parent = frameLayout2 != null ? frameLayout2.getParent() : null;
                if (!(parent instanceof ViewGroup)) {
                    parent = null;
                }
                ViewGroup viewGroup2 = (ViewGroup) parent;
                if (viewGroup2 != null) {
                    viewGroup2.removeView(this.defaultContainerView);
                }
                View findViewById = viewGroup.findViewById(R.id.container_drag_view);
                if (!(findViewById instanceof View)) {
                    findViewById = null;
                }
                if (findViewById != null) {
                    ViewParent parent2 = findViewById.getParent();
                    if (!(parent2 instanceof ViewGroup)) {
                        parent2 = null;
                    }
                    ViewGroup viewGroup3 = (ViewGroup) parent2;
                    if (viewGroup3 != null) {
                        viewGroup3.removeView(findViewById);
                    }
                }
                if (Intrinsics.areEqual(obj, this.defaultContainerView)) {
                    viewGroup.addView((View) obj, this.containerParams);
                }
                for (DragElement dragElement : this.dragViewElements) {
                    if (dragElement.getNeedAddToContainer()) {
                        findViewById = dragElement.getDragView();
                        if (findViewById != null) {
                            Map map = this.dragViewsAttrPreMap;
                            Integer valueOf = Integer.valueOf(findViewById.getId());
                            DragViewAttrs dragViewAttrs = new DragViewAttrs();
                            ViewParent parent3 = findViewById.getParent();
                            if (!(parent3 instanceof ViewGroup)) {
                                parent3 = null;
                            }
                            dragViewAttrs.setParent((ViewGroup) parent3);
                            dragViewAttrs.setPivotX(findViewById.getPivotX());
                            dragViewAttrs.setPivotY(findViewById.getPivotY());
                            dragViewAttrs.setLayoutParams(findViewById.getLayoutParams());
                            map.put(valueOf, dragViewAttrs);
                        }
                    }
                }
                for (DragElement dragElement2 : this.dragViewElements) {
                    if (dragElement2.getNeedAddToContainer()) {
                        View dragView = dragElement2.getDragView();
                        if (dragView != null) {
                            Rect dragBeginLocation;
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dragView.getWidth(), dragView.getHeight());
                            if (dragElement2.getDragBeginLocation() == null) {
                                dragElement2.setDragBeginLocation(ViewHelper.getBoundsInWindow(dragView));
                            } else {
                                dragBeginLocation = dragElement2.getDragBeginLocation();
                                if (dragBeginLocation != null) {
                                    layoutParams.width = dragBeginLocation.width();
                                    layoutParams.height = dragBeginLocation.height();
                                }
                            }
                            Rect dragBeginLocation2 = dragElement2.getDragBeginLocation();
                            if (dragBeginLocation2 != null) {
                                dragBeginLocation = ViewHelper.getBoundsInWindow((View) obj);
                                layoutParams.setMargins(dragBeginLocation2.left - dragBeginLocation.left, dragBeginLocation2.top - dragBeginLocation.top, 0, 0);
                            }
                            parent = dragView.getParent();
                            if (!(parent instanceof ViewGroup)) {
                                parent = null;
                            }
                            viewGroup2 = (ViewGroup) parent;
                            if (viewGroup2 != null) {
                                int indexOfChild = viewGroup2.indexOfChild(dragView);
                                ViewStub viewStub = new ViewStub(getContext());
                                viewStub.setTag(Integer.valueOf(Integer.valueOf(dragView.getId()).hashCode()));
                                View view = viewStub;
                                viewGroup2.addView(view, indexOfChild);
                                viewGroup2.removeView(dragView);
                                DragViewAttrs dragViewAttrs2 = (DragViewAttrs) this.dragViewsAttrPreMap.get(Integer.valueOf(dragView.getId()));
                                if (dragViewAttrs2 != null) {
                                    dragViewAttrs2.setStubView(view);
                                }
                            }
                            if (this.animOptions.getAddToContainerOrder() == 1) {
                                obj.addView(dragView, 0, layoutParams);
                            } else {
                                obj.addView(dragView, layoutParams);
                            }
                        }
                    }
                }
            }
        }
    }

    public final void removeDragViewFromContainer() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7311, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7311, new Class[0], Void.TYPE);
            return;
        }
        if (this.containerView != null) {
            Iterator it = this.dragViewElements.iterator();
            while (true) {
                Object obj = null;
                if (!it.hasNext()) {
                    break;
                }
                View dragView = ((DragElement) it.next()).getDragView();
                if (dragView != null) {
                    DragViewAttrs dragViewAttrs = (DragViewAttrs) this.dragViewsAttrPreMap.get(Integer.valueOf(dragView.getId()));
                    if (dragViewAttrs != null) {
                        ViewParent parent = dragView.getParent();
                        if (!(parent instanceof ViewGroup)) {
                            parent = null;
                        }
                        ViewGroup viewGroup = (ViewGroup) parent;
                        if (viewGroup != null) {
                            viewGroup.removeView(dragView);
                        }
                        Object parent2 = dragViewAttrs.getParent();
                        if (parent2 != null) {
                            DragViewAttrs dragViewAttrs2 = (DragViewAttrs) this.dragViewsAttrPreMap.get(Integer.valueOf(dragView.getId()));
                            View stubView = dragViewAttrs2 != null ? dragViewAttrs2.getStubView() : null;
                            if (stubView != null) {
                                obj = stubView.getParent();
                            }
                            if (Intrinsics.areEqual(parent2, obj) && Intrinsics.areEqual(stubView.getTag(), Integer.valueOf(Integer.valueOf(dragView.getId()).hashCode()))) {
                                parent2.addView(dragView, parent2.indexOfChild(stubView), dragViewAttrs.getLayoutParams());
                                parent2.removeView(stubView);
                            }
                        }
                    }
                }
            }
            FrameLayout frameLayout = this.defaultContainerView;
            ViewParent parent3 = frameLayout != null ? frameLayout.getParent() : null;
            if (!(parent3 instanceof ViewGroup)) {
                parent3 = null;
            }
            ViewGroup viewGroup2 = (ViewGroup) parent3;
            if (viewGroup2 != null) {
                viewGroup2.removeView(this.defaultContainerView);
            }
        }
    }

    public final void destroyDefaultContainerView() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7312, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7312, new Class[0], Void.TYPE);
            return;
        }
        FrameLayout frameLayout = this.defaultContainerView;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        frameLayout = this.defaultContainerView;
        ViewParent parent = frameLayout != null ? frameLayout.getParent() : null;
        if (!(parent instanceof ViewGroup)) {
            parent = null;
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        if (viewGroup != null) {
            viewGroup.removeView(this.defaultContainerView);
        }
    }

    public final DragViewAttrs getDragViewPreAttr(View view) {
        if (PatchProxy.isSupport(new Object[]{view}, this, changeQuickRedirect, false, 7313, new Class[]{View.class}, DragViewAttrs.class)) {
            return (DragViewAttrs) PatchProxy.accessDispatch(new Object[]{r0}, this, changeQuickRedirect, false, 7313, new Class[]{View.class}, DragViewAttrs.class);
        }
        Intrinsics.checkParameterIsNotNull(r0, "view");
        return (DragViewAttrs) this.dragViewsAttrPreMap.get(Integer.valueOf(view.getId()));
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (PatchProxy.isSupport(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7314, new Class[]{MotionEvent.class}, Boolean.TYPE)) {
            return ((Boolean) PatchProxy.accessDispatch(new Object[]{motionEvent}, this, changeQuickRedirect, false, 7314, new Class[]{MotionEvent.class}, Boolean.TYPE)).booleanValue();
        } else if (!isConfigInvalid() || motionEvent == null) {
            return false;
        } else {
            GestureDetector gestureDetector = this.detector;
            if (gestureDetector != null) {
                gestureDetector.onTouchEvent(motionEvent);
            }
            if (motionEvent.getAction() == 1 || (motionEvent.getAction() == 3 && this.doingDragAnim)) {
                if (this.yDistanceTotal >= this.animOptions.getDistanceYThreshold()) {
                    doDragToEndAnimation();
                } else {
                    doRecoverAnimation();
                }
            }
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
    }

    private final void changePositionAndScale(float f, float f2) {
        float f3 = f;
        float f4 = f2;
        if (PatchProxy.isSupport(new Object[]{new Float(f3), new Float(f4)}, this, changeQuickRedirect, false, 7315, new Class[]{Float.TYPE, Float.TYPE}, Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[]{new Float(f3), new Float(f4)}, this, changeQuickRedirect, false, 7315, new Class[]{Float.TYPE, Float.TYPE}, Void.TYPE);
            return;
        }
        Iterator it = this.dragViewElements.iterator();
        while (true) {
            float f5 = 1.0f;
            if (it.hasNext()) {
                DragElement dragElement = (DragElement) it.next();
                View dragView = dragElement.getDragView();
                if (dragView != null) {
                    Rect dragBeginLocation = dragElement.getDragBeginLocation();
                    if (dragBeginLocation != null) {
                        float height;
                        float f6 = this.yDistanceTotal;
                        if (Math.abs(this.yDistanceTotal) > this.animOptions.getScaleTotalDistanceY()) {
                            f6 = this.animOptions.getScaleTotalDistanceY();
                        }
                        Rect dragEndLocation = dragElement.getDragEndLocation();
                        float f7 = 0.4f;
                        if (dragEndLocation != null) {
                            f7 = ((float) dragEndLocation.width()) / ((float) dragBeginLocation.width());
                            height = ((float) dragEndLocation.height()) / ((float) dragBeginLocation.height());
                        } else {
                            height = 0.4f;
                        }
                        if (f7 != 1.0f) {
                            float f8 = (float) 1;
                            f7 = f8 - (((f8 - f7) / this.animOptions.getScaleTotalDistanceY()) * Math.abs(f6));
                        } else {
                            f7 = 1.0f;
                        }
                        if (height != 1.0f) {
                            f5 = (float) 1;
                            f5 -= ((f5 - height) / this.animOptions.getScaleTotalDistanceY()) * Math.abs(f6);
                        }
                        if (dragElement.getScaleType() == 0) {
                            if (dragElement.isEnableTransX()) {
                                dragView.setTranslationX(dragView.getTranslationX() + f3);
                            }
                            if (dragElement.isEnableTransY()) {
                                dragView.setTranslationY(dragView.getTranslationY() + f4);
                            }
                            if (dragElement.isEnableScaleX()) {
                                dragView.setScaleX(f7);
                            }
                            if (dragElement.isEnableScaleY()) {
                                dragView.setScaleY(f5);
                            }
                        } else {
                            LayoutParams layoutParams = dragView.getLayoutParams();
                            if (layoutParams == null) {
                                throw new TypeCastException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                            }
                            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
                            dragView.setTranslationX(dragView.getTranslationX() + ((((float) (marginLayoutParams.width - ((int) (((float) dragBeginLocation.width()) * f7)))) / 2.0f) + f3));
                            dragView.setTranslationY(dragView.getTranslationY() + ((((float) (marginLayoutParams.height - ((int) (((float) dragBeginLocation.height()) * f5)))) / 2.0f) + f4));
                            marginLayoutParams.width = (int) (f7 * ((float) dragBeginLocation.width()));
                            marginLayoutParams.height = (int) (f5 * ((float) dragBeginLocation.height()));
                            dragView.requestLayout();
                        }
                    } else {
                        return;
                    }
                }
                return;
            }
            float minAlpha = ((float) 1) - (((1.0f - this.animOptions.getMinAlpha()) / this.animOptions.getScaleTotalDistanceY()) * Math.abs(this.yDistanceTotal));
            if (this.yDistanceTotal > this.animOptions.getScaleTotalDistanceY()) {
                minAlpha = this.animOptions.getMinAlpha();
            }
            FrameLayout frameLayout = this.containerView;
            if (frameLayout != null) {
                Drawable background = frameLayout.getBackground();
                if (background != null) {
                    background.setAlpha((int) (minAlpha * ((float) 255)));
                }
            }
            DragCallBack dragCallBack = this.dragCallBack;
            if (dragCallBack != null) {
                dragCallBack.onDrag(f3, f4, this.yDistanceTotal);
            }
            return;
        }
    }

    public final boolean doFinishAnimation() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7316, new Class[0], Boolean.TYPE)) {
            return ((Boolean) PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7316, new Class[0], Boolean.TYPE)).booleanValue();
        }
        DragCallBack dragCallBack = this.dragCallBack;
        if (!isConfigInvalid() || dragCallBack == null) {
            return false;
        }
        if (this.recoverAnimatorSet != null) {
            AnimatorSet animatorSet = this.recoverAnimatorSet;
            if (animatorSet != null) {
                animatorSet.end();
            }
        }
        if (this.finishAnimatorSet != null) {
            return true;
        }
        if (!this.doingDragAnim) {
            this.doingDragAnim = true;
            FrameLayout frameLayout = this.containerView;
            if (frameLayout != null) {
                frameLayout.setClickable(true);
            }
            dragCallBack.shouldDragIntercept(0.0f, 0.0f);
            setDragBeginLocation();
            addDragViewToContainer();
        }
        doDragToEndAnimation();
        return true;
    }

    private final void doDragToEndAnimation() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7317, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7317, new Class[0], Void.TYPE);
        } else if (isConfigInvalid() && this.finishAnimatorSet == null) {
            AnimatorSet animatorSet = this.finishAnimatorSet;
            if (animatorSet != null) {
                if (!animatorSet.isRunning()) {
                    animatorSet = null;
                }
                if (animatorSet != null) {
                    return;
                }
            }
            this.finishAnimatorSet = new AnimatorSet();
            animatorSet = this.recoverAnimatorSet;
            if (animatorSet != null) {
                if (!animatorSet.isRunning()) {
                    animatorSet = null;
                }
                if (animatorSet != null) {
                    animatorSet.end();
                }
            }
            DragCallBack dragCallBack = this.dragCallBack;
            if (dragCallBack != null) {
                dragCallBack.onDragToEndAnimStart();
            }
            ArrayList arrayList = new ArrayList();
            int i = 1;
            for (DragElement dragElement : this.dragViewElements) {
                if (dragElement.getDragEndLocation() == null || dragElement.getDragBeginLocation() == null) {
                    i = 0;
                } else {
                    View dragView = dragElement.getDragView();
                    if (dragView != null) {
                        Rect dragBeginLocation = dragElement.getDragBeginLocation();
                        if (dragBeginLocation == null) {
                            Intrinsics.throwNpe();
                        }
                        Rect dragEndLocation = dragElement.getDragEndLocation();
                        if (dragEndLocation == null) {
                            Intrinsics.throwNpe();
                        }
                        if (dragElement.getScaleType() == 0) {
                            float translationY = dragView.getTranslationY();
                            if (dragElement.isEnableTransY()) {
                                translationY = ((float) (dragEndLocation.top - dragBeginLocation.top)) + (((float) (dragEndLocation.height() - dragBeginLocation.height())) / 2.0f);
                            }
                            float f = translationY;
                            translationY = dragView.getTranslationX();
                            if (dragElement.isEnableTransX()) {
                                translationY = ((float) (dragEndLocation.left - dragBeginLocation.left)) + (((float) (dragEndLocation.width() - dragBeginLocation.width())) / 2.0f);
                            }
                            float scaleX = dragView.getScaleX();
                            if (dragElement.isEnableScaleX()) {
                                scaleX = ((float) dragEndLocation.width()) / (((float) dragBeginLocation.width()) * 1.0f);
                            }
                            arrayList.addAll(generateAnimator(dragView, dragView.getTranslationX(), translationY, dragView.getTranslationY(), f, dragView.getScaleX(), scaleX, dragView.getScaleY(), dragElement.isEnableScaleY() ? ((float) dragEndLocation.height()) / (1.0f * ((float) dragBeginLocation.height())) : dragView.getScaleY()));
                        } else {
                            Rect rect = new Rect(dragBeginLocation);
                            rect.right = dragView.getWidth() + rect.left;
                            rect.bottom = dragView.getHeight() + rect.top;
                            arrayList.addAll(generateLayoutAnimList(dragView, rect, dragEndLocation));
                        }
                    } else {
                        return;
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                animatorSet = this.finishAnimatorSet;
                Builder play = animatorSet != null ? animatorSet.play((Animator) arrayList.get(0)) : null;
                int size = arrayList.size();
                for (int i2 = 1; i2 < size; i2++) {
                    if (play != null) {
                        play.with((Animator) arrayList.get(i2));
                    }
                }
                FrameLayout frameLayout = this.containerView;
                if (frameLayout != null) {
                    int[] iArr = new int[2];
                    Drawable background = frameLayout.getBackground();
                    Intrinsics.checkExpressionValueIsNotNull(background, "containerView.background");
                    iArr[0] = background.getAlpha();
                    iArr[1] = 0;
                    ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                    ofInt.addUpdateListener(new SimpleDragView$$special$$inlined$apply$lambda$1(frameLayout));
                    if (play != null) {
                        play.with(ofInt);
                    }
                }
                animatorSet = this.finishAnimatorSet;
                if (animatorSet != null) {
                    animatorSet.setDuration(300);
                }
                animatorSet = this.finishAnimatorSet;
                if (animatorSet != null) {
                    animatorSet.setInterpolator(InterpolatorHelper.getCubicEaseOutInterpolator());
                }
                animatorSet = this.finishAnimatorSet;
                if (animatorSet != null) {
                    animatorSet.addListener(new SimpleDragView$doDragToEndAnimation$6(this));
                }
            }
            if (i != 0) {
                animatorSet = this.finishAnimatorSet;
                if (animatorSet != null) {
                    animatorSet.start();
                }
            } else {
                this.finishAnimatorSet = (AnimatorSet) null;
                doRecoverAnimation();
            }
        }
    }

    private final void doRecoverAnimation() {
        if (PatchProxy.isSupport(new Object[0], this, changeQuickRedirect, false, 7318, new Class[0], Void.TYPE)) {
            PatchProxy.accessDispatch(new Object[0], this, changeQuickRedirect, false, 7318, new Class[0], Void.TYPE);
        } else if (isConfigInvalid() && this.recoverAnimatorSet == null) {
            AnimatorSet animatorSet = this.recoverAnimatorSet;
            Builder builder = null;
            if (animatorSet != null) {
                if (!animatorSet.isRunning()) {
                    animatorSet = null;
                }
                if (animatorSet != null) {
                    return;
                }
            }
            this.recoverAnimatorSet = new AnimatorSet();
            animatorSet = this.finishAnimatorSet;
            if (animatorSet != null) {
                if (!animatorSet.isRunning()) {
                    animatorSet = null;
                }
                if (animatorSet != null) {
                    animatorSet.end();
                }
            }
            DragCallBack dragCallBack = this.dragCallBack;
            if (dragCallBack != null) {
                dragCallBack.onRecoverAnimStart();
            }
            ArrayList arrayList = new ArrayList();
            for (DragElement dragElement : this.dragViewElements) {
                View dragView = dragElement.getDragView();
                if (dragView == null) {
                    return;
                }
                if (dragElement.getScaleType() == 0) {
                    arrayList.addAll(generateAnimator(dragView, dragView.getTranslationX(), 0.0f, dragView.getTranslationY(), 0.0f, dragView.getScaleX(), 1.0f, dragView.getScaleY(), 1.0f));
                } else {
                    Rect dragBeginLocation = dragElement.getDragBeginLocation();
                    if (dragBeginLocation != null) {
                        Rect rect = new Rect(dragBeginLocation);
                        rect.right = dragView.getWidth() + rect.left;
                        rect.bottom = dragView.getHeight() + rect.top;
                        arrayList.addAll(generateLayoutAnimList(dragView, rect, dragBeginLocation));
                    }
                }
            }
            if (!arrayList.isEmpty()) {
                AnimatorSet animatorSet2 = this.recoverAnimatorSet;
                if (animatorSet2 != null) {
                    builder = animatorSet2.play((Animator) arrayList.get(0));
                }
                int size = arrayList.size();
                for (int i = 1; i < size; i++) {
                    if (builder != null) {
                        builder.with((Animator) arrayList.get(i));
                    }
                }
                FrameLayout frameLayout = this.containerView;
                if (!(frameLayout == null || frameLayout.getBackground() == null)) {
                    int[] iArr = new int[2];
                    Drawable background = frameLayout.getBackground();
                    Intrinsics.checkExpressionValueIsNotNull(background, "containerView.background");
                    iArr[0] = background.getAlpha();
                    iArr[1] = 0;
                    ValueAnimator ofInt = ValueAnimator.ofInt(iArr);
                    ofInt.addUpdateListener(new SimpleDragView$$special$$inlined$apply$lambda$2(frameLayout));
                    if (builder != null) {
                        builder.with(ofInt);
                    }
                }
                AnimatorSet animatorSet3 = this.recoverAnimatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.setDuration(300);
                }
                animatorSet3 = this.recoverAnimatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.setInterpolator(InterpolatorHelper.getCubicEaseOutInterpolator());
                }
                animatorSet3 = this.recoverAnimatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.addListener(new SimpleDragView$doRecoverAnimation$6(this));
                }
                animatorSet3 = this.recoverAnimatorSet;
                if (animatorSet3 != null) {
                    animatorSet3.start();
                }
            }
        }
    }

    private final List<ValueAnimator> generateAnimator(View view, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        View view2 = view;
        float f9 = f;
        float f10 = f2;
        float f11 = f3;
        float f12 = f4;
        float f13 = f5;
        float f14 = f6;
        float f15 = f7;
        float f16 = f8;
        Class[] clsArr = new Class[]{View.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE};
        if (PatchProxy.isSupport(new Object[]{view2, new Float(f9), new Float(f10), new Float(f11), new Float(f12), new Float(f13), new Float(f14), new Float(f15), new Float(f16)}, this, changeQuickRedirect, false, 7319, clsArr, List.class)) {
            Object[] objArr = new Object[]{view2, new Float(f9), new Float(f10), new Float(f11), new Float(f12), new Float(f13), new Float(f14), new Float(f15), new Float(f16)};
            return (List) PatchProxy.accessDispatch(objArr, this, changeQuickRedirect, false, 7319, new Class[]{View.class, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE}, List.class);
        }
        ArrayList arrayList = new ArrayList();
        if (view2 != null) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, new float[]{f9, f10});
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, new float[]{f11, f12});
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view2, View.SCALE_X, new float[]{f13, f14});
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view2, View.SCALE_Y, new float[]{f15, f16});
            arrayList.add(ofFloat);
            arrayList.add(ofFloat2);
            arrayList.add(ofFloat3);
            arrayList.add(ofFloat4);
        }
        return arrayList;
    }

    private final List<ValueAnimator> generateLayoutAnimList(View view, Rect rect, Rect rect2) {
        Rect rect3 = rect;
        Rect rect4 = rect2;
        if (PatchProxy.isSupport(new Object[]{view, rect3, rect4}, this, changeQuickRedirect, false, 7320, new Class[]{View.class, Rect.class, Rect.class}, List.class)) {
            return (List) PatchProxy.accessDispatch(new Object[]{view, rect3, rect4}, this, changeQuickRedirect, false, 7320, new Class[]{View.class, Rect.class, Rect.class}, List.class);
        }
        float f;
        float f2;
        ArrayList arrayList = new ArrayList();
        int width = rect.width();
        int height = rect.height();
        float translationX = view.getTranslationX() + ((float) rect3.left);
        float translationY = view.getTranslationY() + ((float) rect3.top);
        FloatRef floatRef = new FloatRef();
        if (translationY != ((float) rect4.top)) {
            f = (float) rect4.top;
            f2 = translationY;
        } else if (translationX != ((float) rect4.left)) {
            f = (float) rect4.left;
            f2 = translationX;
        } else {
            f2 = -1.0f;
            f = f2;
        }
        if (f2 == -1.0f && f == -1.0f) {
            return arrayList;
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f2, f});
        ofFloat.addUpdateListener(new SimpleDragView$generateLayoutAnimList$$inlined$apply$lambda$1(floatRef, f2, f, rect4, width, height, translationX, rect3, translationY, view));
        arrayList.add(ofFloat);
        return arrayList;
    }

    private final Activity scanForActivity(Context context) {
        Context context2 = context;
        if (PatchProxy.isSupport(new Object[]{context2}, this, changeQuickRedirect, false, 7321, new Class[]{Context.class}, Activity.class)) {
            return (Activity) PatchProxy.accessDispatch(new Object[]{context2}, this, changeQuickRedirect, false, 7321, new Class[]{Context.class}, Activity.class);
        }
        Activity activity = null;
        if (context2 != null) {
            Activity activity2 = !(context2 instanceof Activity) ? null : context2;
            if (activity2 != null) {
                activity = activity2;
            } else if (context2 instanceof ContextWrapper) {
                activity = scanForActivity(((ContextWrapper) context2).getBaseContext());
                return activity;
            }
        }
        return activity;
    }
}
