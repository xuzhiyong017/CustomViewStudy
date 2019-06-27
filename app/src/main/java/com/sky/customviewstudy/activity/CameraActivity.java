package com.sky.customviewstudy.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CustomAppBarBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.daimajia.easing.BaseEasingMethod;
import com.daimajia.easing.Glider;
import com.daimajia.easing.Skill;
import com.sky.customviewstudy.R;
import com.sky.customviewstudy.adapter.GalleryAdapter;
import com.sky.customviewstudy.adapter.PicAdapter;
import com.sky.customviewstudy.bean.MultiModel;
import com.sky.customviewstudy.item.PicListItemDecoration;
import com.sky.customviewstudy.listener.DragBarTouchListener;
import com.sky.customviewstudy.listener.PhotoPickFragmentV13;
import com.sky.customviewstudy.listener.PhotoPickFragmentV14;
import com.sky.customviewstudy.listener.PhotoPickFragmentV318;
import com.sky.customviewstudy.listener.PhotoPickFragmentV319;
import com.sky.customviewstudy.listener.PhotoPickFragmentV320;
import com.sky.customviewstudy.listener.PhotoPickFragmentV322;
import com.sky.customviewstudy.listener.RecycleTouchListener;
import com.sky.customviewstudy.utils.QueryProcessor;
import com.sky.customviewstudy.utils.ScreenUtils;
import com.sky.customviewstudy.widget.NpaLinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;

public class CameraActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    public RecyclerView mRecyclerView;
    public GalleryAdapter mAdapter;
    public RecyclerView checkedPhotosRV;
    public PicAdapter mSelectedAdapter;

    public AppBarLayout mAppbarLayout;
    public CoordinatorLayout mMainContent;
    private View flyt_min_layout;
    private View drag_bar;

    public float maxHeight;
    public float minHeight;
    private float checkPhotoHeight;
    private float width;
    public boolean i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_camera);

        mAppbarLayout = findViewById(R.id.appbar);
        mMainContent = findViewById(R.id.main_content);
        flyt_min_layout = findViewById(R.id.flyt_min_layout);
        drag_bar = findViewById(R.id.drag_bar);
        mAppbarLayout.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.x100) + ScreenUtils.getWidth(this)
        +getResources().getDimensionPixelOffset(R.dimen.x64);

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.addItemDecoration(new PicListItemDecoration(4,5,false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(mAdapter = new GalleryAdapter(this,null));

        drag_bar.setOnTouchListener(new DragBarTouchListener(this));
        mRecyclerView.setOnTouchListener(new RecycleTouchListener(this));

        checkedPhotosRV = findViewById(R.id.checkedPhotosRV);
        checkedPhotosRV.setLayoutManager(new NpaLinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        checkedPhotosRV.setAdapter(mSelectedAdapter = new PicAdapter(this,ScreenUtils.getWidth(this)));
        checkedPhotosRV.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.x160);


        mAdapter.setOnSelectListener(new GalleryAdapter.OnSelectListener() {
            @Override
            public void OnSelected(MultiModel model) {
                mSelectedAdapter.addPicture(model);
            }

            @Override
            public void OnUnSelect(MultiModel model) {
                mSelectedAdapter.removePicture(model);
            }
        });

        mAppbarLayout.addOnOffsetChangedListener(this);
        f().setCanScroll(true);

        loadData();
    }

    private void loadData() {
        new QueryProcessor(this).queryAll(new QueryProcessor.QueryCallback() {
            @Override
            public void querySuccess(final ArrayList<MultiModel> multiModels) {
                Collections.sort(multiModels);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.replaceData(multiModels);
                    }
                });
            }

            @Override
            public void queryFailed() {

            }
        });
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        a(verticalOffset);
        Log.d("onOffsetChanged","onOffsetChanged="+verticalOffset);
        float frect = (float) (Math.abs(verticalOffset)) /(appBarLayout.getHeight() - flyt_min_layout.getHeight());
//        b(frect);
        a(verticalOffset);
    }

    public void a(int verticalOffset){
        if ((verticalOffset < 0) && (verticalOffset > -this.mAppbarLayout.getTotalScrollRange()))
        {
            for (int i = 1; i < 5; i++) {
                if(mRecyclerView.getChildCount() > i){
                    View childAt = mRecyclerView.getChildAt(mRecyclerView.getChildCount()-i);
                    if(childAt != null){
                        int height  = mMainContent.getHeight() - mAppbarLayout.getHeight();
                        if(c(mRecyclerView.getChildAdapterPosition(childAt))){
                            childAt.setPadding(0,0,0,(mRecyclerView.getHeight()-height)+i);
                        }else{
                            childAt.setPadding(0,0,0,0);
                        }
                    }
                }
            }
        }
    }

    public void initHeight() {
        if (this.maxHeight == 0.0f) {
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.x100);
            int dimensionPixelOffset2 = getResources().getDimensionPixelOffset(R.dimen.x64);
            int f = ScreenUtils.getWidth(this);
            this.checkPhotoHeight = (float) getResources().getDimensionPixelOffset(R.dimen.x160);
            this.maxHeight = (float) ((dimensionPixelOffset + f) + dimensionPixelOffset2);
            this.minHeight = (((float) dimensionPixelOffset) + this.checkPhotoHeight) + ((float) dimensionPixelOffset2);
            this.width = (float) f;
        }
    }

    public void a(float f) {
        initHeight();
        if (!isInitState()) {
            CustomAppBarBehavior f2 = f();
            if (f2 != null) {
                f2.setCanScroll(false);
            }
//            if (AdvEditUtil.b()) {
//                this.mVideoSDKPlayerView.setVisibility(8);
//            } else {
//                this.mPlayer.setVisibility(8);
//            }
//            this.mPlayerWrapper.setVisibility(8);
        }
        b(f);
    }

    public void l() {
        initHeight();
        b((this.maxHeight - ((float) this.mAppbarLayout.getHeight())) / (this.maxHeight - this.minHeight), 400);
    }

    public void k()
    {
        initHeight();
        b((this.mAppbarLayout.getHeight() - this.minHeight) / (this.maxHeight - this.minHeight), 400L);
    }

    public void a(float f, long j) {
        this.i = false;
        initHeight();
        CustomAppBarBehavior f2 = f();
        if (f2 != null && f2.getTopAndBottomOffset() == 0) {
            f2.setCanScroll(false);
        }
        if (this.mAdapter.getSelectedList() > 0 || this.mSelectedAdapter.getItemCount() > 0) {
//            this.mEmptyGuideView.setVisibility(4);
        } else {
//            F();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, 1.0f});
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.setDuration(j);
        ofFloat.addUpdateListener(new PhotoPickFragmentV13(this));
        ofFloat.addListener(new PhotoPickFragmentV14(this));
        ofFloat.start();
//        this.A = true;
//        if (AdvEditUtil.b()) {
//            this.mVideoSDKPlayerView.setVisibility(8);
//        } else {
//            this.mPlayer.setVisibility(8);
//        }
//        this.mPlayerWrapper.setVisibility(8);
//        o.b(((GifshowActivity) getActivity()).a(), "expand", new Object[0]);
    }


    public void b(float f, long j) {
        this.i = true;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f, 1.0f});
        ofFloat.setInterpolator(new LinearInterpolator());
        ofFloat.setDuration(j);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                b(((Float)animation.getAnimatedValue()).floatValue());
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        ofFloat.start();
//        this.A = true;
//        if (AdvEditUtil.b()) {
//            this.mVideoSDKPlayerView.setVisibility(8);
//        } else {
//            this.mPlayer.setVisibility(8);
//        }
//        this.mPlayerWrapper.setVisibility(8);
//        if (getActivity() != null) {
//            o.b(((GifshowActivity) getActivity()).a(), "collapse", new Object[0]);
//        }
    }


    public void b(float f) {
        this.mAppbarLayout.getLayoutParams().height = (int) (this.maxHeight - ((this.maxHeight - this.minHeight) * f));
        this.mAppbarLayout.requestLayout();
        this.checkedPhotosRV.getLayoutParams().height = (int) (this.width - ((this.width - this.checkPhotoHeight) * f));
        this.checkedPhotosRV.requestLayout();
        PicAdapter checkedPhotoAdapterV3 = mSelectedAdapter;
        for (int i = 0; i < checkedPhotosRV.getChildCount(); i++) {
            checkedPhotoAdapterV3.updateSize(checkedPhotosRV.getChildAt(i), 1.0f - f);
        }
//        checkedPhotoAdapterV3.c = 1.0f - getBehavior;
//        if (checkedPhotoAdapterV3.c == 1.0f) {
//            checkedPhotoAdapterV3.getBehavior.a(checkedPhotoAdapterV3.b);
//            checkedPhotoAdapterV3.g.a(checkedPhotoAdapterV3.getBehavior, checkedPhotoAdapterV3.b);
//            if (checkedPhotoAdapterV3.getItemCount() > 1) {
//                checkedPhotoAdapterV3.g.setVisibility(0);
//            }
//        } else if (checkedPhotoAdapterV3.c == 0.0f) {
//            checkedPhotoAdapterV3.getBehavior.a(null);
//            checkedPhotoAdapterV3.g.setVisibility(4);
//        }
//        this.mPagerIndicator.setAlpha(1.0f - getBehavior);
    }

    public boolean isInitState() {
        initHeight();
        return (((float) this.mAppbarLayout.getHeight()) == this.maxHeight || ((float) this.mAppbarLayout.getHeight()) == this.minHeight) ? false : true;
    }



    public CustomAppBarBehavior f() {
        if (this.mAppbarLayout != null && (this.mAppbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams)) {
            CoordinatorLayout.LayoutParams dVar = (CoordinatorLayout.LayoutParams) this.mAppbarLayout.getLayoutParams();
            if (dVar.getBehavior() instanceof CustomAppBarBehavior) {
                return (CustomAppBarBehavior) dVar.getBehavior();
            }
        }
        return null;

    }

    final boolean c(int i) {
        return i > ((i() + -1) * 4) + -1;
    }

    final int i() {
        return (int) Math.ceil((double) ((((float) mSelectedAdapter.getItemCount()) * 1.0f) / 4.0f));
    }

    public void v()
    {
        CustomAppBarBehavior localCustomAppBarBehavior = f();
        if (localCustomAppBarBehavior != null)
        {
            localCustomAppBarBehavior.setCanScroll(true);
            Skill localSkill = Skill.Linear;
            float[] arrayOfFloat = new float[2];
            arrayOfFloat[0] = 0.0F;
            arrayOfFloat[1] = this.mAppbarLayout.getTotalScrollRange() + localCustomAppBarBehavior.getTopAndBottomOffset();
            ValueAnimator localValueAnimator = Glider.glide(localSkill, 250.0F, ValueAnimator.ofFloat(arrayOfFloat), new BaseEasingMethod.EasingListener[0]);
            localValueAnimator.setDuration(250L);
            localValueAnimator.addListener(new PhotoPickFragmentV318(this,localCustomAppBarBehavior));
            localValueAnimator.addUpdateListener(new PhotoPickFragmentV319(this, localCustomAppBarBehavior));
            localValueAnimator.start();
            localCustomAppBarBehavior.onNestedScroll(this.mMainContent, this.mAppbarLayout, this.mRecyclerView, 0, 0, 0, 0, 0);
        }
    }

    public void w()
    {
        CustomAppBarBehavior localCustomAppBarBehavior = f();
        if (localCustomAppBarBehavior != null)
        {
            localCustomAppBarBehavior.setCanScroll(true);
            Skill localSkill = Skill.Linear;
            float[] arrayOfFloat = new float[2];
            arrayOfFloat[0] = 0.0F;
            arrayOfFloat[1] = -localCustomAppBarBehavior.getTopAndBottomOffset();
            ValueAnimator localValueAnimator = Glider.glide(localSkill, 250.0F, ValueAnimator.ofFloat(arrayOfFloat), new BaseEasingMethod.EasingListener[0]);
            localValueAnimator.setDuration(250L);
            localValueAnimator.addListener(new PhotoPickFragmentV320(this, localCustomAppBarBehavior));
            localValueAnimator.addUpdateListener(new PhotoPickFragmentV322(this, localCustomAppBarBehavior));
            localValueAnimator.start();
//            this.A = true;
        }
    }

}
