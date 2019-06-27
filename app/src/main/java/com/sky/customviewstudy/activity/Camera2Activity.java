package com.sky.customviewstudy.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CustomAppBarBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.customviewstudy.R;
import com.sky.customviewstudy.adapter.GalleryAdapter;
import com.sky.customviewstudy.adapter.PicAdapter;
import com.sky.customviewstudy.bean.MultiModel;
import com.sky.customviewstudy.fragment.AlbumListFragment;
import com.sky.customviewstudy.item.PicListItemDecoration;
import com.sky.customviewstudy.utils.QueryProcessor;
import com.sky.customviewstudy.utils.ScreenUtils;
import com.sky.customviewstudy.widget.NpaLinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;

public class Camera2Activity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener
                                    ,View.OnClickListener{

    public RecyclerView mRecyclerView;
    public GalleryAdapter mAdapter;
    public RecyclerView checkedPhotosRV;
    public PicAdapter mSelectedAdapter;

    public AppBarLayout mAppbarLayout;
    public CoordinatorLayout mMainContent;
    private CollapsingToolbarLayout collapsing_toolbar;
    private View flyt_min_layout;
    private View album_layout;
    private View rlyt_switch;
    private View empty_guide_view;
    private View player_wrapper;
    private TextView title_tv;
    private ImageView up_arrow;
    private TextView next_step;
    private View closeView;

    public int maxHeight ;
    public int minHeight ;
    public int checkRecycleHeight;
    public int headerHeight;
    private boolean isChooseVideo = false;
    private boolean isOpenMenu = false;
    private Fragment mAlbumFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera2);

        mAlbumFragment = new AlbumListFragment();
        mAppbarLayout = findViewById(R.id.appbar);
        mMainContent = findViewById(R.id.main_content);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        flyt_min_layout = findViewById(R.id.flyt_min_layout);
        empty_guide_view = findViewById(R.id.empty_guide_view);
        player_wrapper = findViewById(R.id.player_wrapper);
        title_tv = findViewById(R.id.title_tv);
        up_arrow = findViewById(R.id.up_arrow);
        next_step = findViewById(R.id.next_step);
        album_layout = findViewById(R.id.album_layout);
        rlyt_switch = findViewById(R.id.rlyt_switch);
        closeView = findViewById(R.id.close_view);

        rlyt_switch.setOnClickListener(this);
        next_step.setOnClickListener(this);
        closeView.setOnClickListener(this);

        mAppbarLayout.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.x88) + ScreenUtils.getWidth(this);
        mAppbarLayout.requestLayout();

        mRecyclerView = findViewById(R.id.recycleView);
        mRecyclerView.addItemDecoration(new PicListItemDecoration(4,5,false));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mRecyclerView.setAdapter(mAdapter = new GalleryAdapter(this,null));


        checkedPhotosRV = findViewById(R.id.checkedPhotosRV);
        checkedPhotosRV.setLayoutManager(new NpaLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        checkedPhotosRV.setAdapter(mSelectedAdapter = new PicAdapter(this,ScreenUtils.getWidth(this)));


        mAdapter.setOnSelectListener(new GalleryAdapter.OnSelectListener() {
            @Override
            public void OnSelected(MultiModel model) {
                if(model.getType() == 1){
                    isChooseVideo = true;
                }else{
                    mSelectedAdapter.addPicture(model);
                    isChooseVideo = false;
                }
                notifyAdapterSizeChange();
            }

            @Override
            public void OnUnSelect(MultiModel model) {
                mSelectedAdapter.removePicture(model);
                notifyAdapterSizeChange();
            }
        });

        mSelectedAdapter.setOnSelectListener(new GalleryAdapter.OnSelectListener() {
            @Override
            public void OnSelected(MultiModel model) {

            }

            @Override
            public void OnUnSelect(MultiModel model) {
                mAdapter.removeModel(model);
                notifyAdapterSizeChange();
            }
        });

        mAppbarLayout.addOnOffsetChangedListener(this);


        notifyAdapterSizeChange();

        mAppbarLayout.post(new Runnable() {
            @Override
            public void run() {
                initHeight();
            }
        });

        loadData();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.rlyt_switch){
            switchDataSource();
        }else if(id == R.id.next_step){
            //下一步
            Log.d("camera2",mMainContent.getChildAt(mMainContent.getChildCount()-1).toString());
        }else if(id == R.id.close_view){
            finish();
        }
    }


    //切换数据源
    private void switchDataSource() {
        if(!isOpenMenu){

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_bottom,R.anim.slide_out_to_bottom)
                    .add(R.id.album_layout,mAlbumFragment).commitAllowingStateLoss();
            up_arrow.animate().rotation(-180f).start();
            closeView.setVisibility(View.INVISIBLE);
            closeView.setEnabled(false);
            next_step.setVisibility(View.INVISIBLE);
            next_step.setEnabled(false);
            album_layout.bringToFront();
            isOpenMenu = !isOpenMenu;
        }else{
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_from_bottom,R.anim.slide_out_to_bottom)
                    .remove(mAlbumFragment).commitAllowingStateLoss();
            up_arrow.animate().rotation(0f).start();
            closeView.setVisibility(View.VISIBLE);
            closeView.setEnabled(true);
            next_step.setVisibility(View.VISIBLE);
            next_step.setEnabled(true);
            isOpenMenu = !isOpenMenu;
        }
    }

    //size 改变监听
    private void notifyAdapterSizeChange() {

        if(isChooseVideo){
            mSelectedAdapter.clearData();
            getBehavior().setCanScroll(true);
            mAppbarLayout.setExpanded(true,false);
            player_wrapper.setVisibility(View.VISIBLE);
            empty_guide_view.setVisibility(View.INVISIBLE);
            checkedPhotosRV.setVisibility(View.INVISIBLE);
            collapsing_toolbar.post(new Runnable() {
                @Override
                public void run() {
                    collapsing_toolbar.setMinimumHeight(headerHeight);
                    ((CollapsingToolbarLayout.LayoutParams)flyt_min_layout.getLayoutParams()).setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF);

                }
            });

        }else{
            checkedPhotosRV.setVisibility(View.VISIBLE);
            player_wrapper.setVisibility(View.INVISIBLE);
            collapsing_toolbar.setMinimumHeight(minHeight);
            ((CollapsingToolbarLayout.LayoutParams)flyt_min_layout.getLayoutParams()).setCollapseMode(CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN);
            if(mSelectedAdapter.getItemCount() == 0){
                getBehavior().setCanScroll(false);
                mAppbarLayout.setExpanded(true,true);
                checkedPhotosRV.getLayoutParams().height = checkRecycleHeight;
                checkedPhotosRV.requestLayout();
                empty_guide_view.setVisibility(View.VISIBLE);
            }else if(mSelectedAdapter.getItemCount() == 1){
                getBehavior().setCanScroll(true);
                checkedPhotosRV.getLayoutParams().height = checkRecycleHeight;
                checkedPhotosRV.requestLayout();
                mAppbarLayout.setExpanded(true,true);
                empty_guide_view.setVisibility(View.INVISIBLE);
            }else{
                empty_guide_view.setVisibility(View.INVISIBLE);
                getBehavior().setCanScroll(false);
                checkedPhotosRV.getLayoutParams().height = minHeight -headerHeight;
                checkedPhotosRV.requestLayout();
                mAppbarLayout.setExpanded(false,true);

            }
        }


    }

    private void initHeight() {
        if(maxHeight == 0){
            maxHeight = mAppbarLayout.getHeight();
            minHeight = getResources().getDimensionPixelOffset(R.dimen.x292);
            headerHeight = getResources().getDimensionPixelOffset(R.dimen.x88);
            checkRecycleHeight = maxHeight - headerHeight;
        }
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
        if(isChooseVideo) return;
        float frence =Math.abs(verticalOffset)/ (float)(maxHeight - minHeight);
        checkedPhotosRV.getLayoutParams().height =verticalOffset + checkRecycleHeight;
        Log.d("onOffsetChanged","height="+appBarLayout.getHeight()+"  offset="+verticalOffset+ " frence="+frence
        +" checkedPhotosRVHeight="+checkedPhotosRV.getHeight());


        if(checkedPhotosRV.getChildCount() >= 1){
            mSelectedAdapter.updateSize(checkedPhotosRV.getChildAt(0),1-frence);
        }

    }


    public CustomAppBarBehavior getBehavior() {
        if (this.mAppbarLayout != null && (this.mAppbarLayout.getLayoutParams() instanceof CoordinatorLayout.LayoutParams)) {
            CoordinatorLayout.LayoutParams dVar = (CoordinatorLayout.LayoutParams) this.mAppbarLayout.getLayoutParams();
            if (dVar.getBehavior() instanceof CustomAppBarBehavior) {
                return (CustomAppBarBehavior) dVar.getBehavior();
            }
        }
        return null;

    }

}
