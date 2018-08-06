package com.sky.customviewstudy.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.sky.customviewstudy.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author: xuzhiyong
 * @date: 2018/7/27 16:22
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PileAvertView extends RelativeLayout {

    /**
     * 默认图片大小
     */
    private int imageSize = Math.round(dp2px(50));
    /**
     * 默认图片数量
     */
    private int imageMaxCount = 5;
    /**
     * 默认图片偏移百分比 0～1
     */
    private float imageOffset = 0.3f;

    public PileAvertView(Context context) {
        this(context,null);
    }

    public PileAvertView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PileAvertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getResources().obtainAttributes(attrs, R.styleable.AvatarListLayout);
        imageMaxCount = ta.getInt(R.styleable.AvatarListLayout_image_max_count, imageMaxCount);
        imageSize = (int) ta.getDimension(R.styleable.AvatarListLayout_image_size, imageSize);
        imageOffset = ta.getFloat(R.styleable.AvatarListLayout_image_offset, imageOffset);
        imageOffset = imageOffset > 1 ? 1 : imageOffset;
        ta.recycle();

    }



    public <T> void  setAvertImageListener(List<T> imageList, OnLoadListener<T> loadListener){
        List<T> visibleList = null;
        if (imageList.size() > imageMaxCount) {
            visibleList = imageList.subList(imageList.size() - 1 - imageMaxCount, imageList.size() - 1);
        }else{
            visibleList = imageList;
        }
        removeAllViews();
        int offset = imageSize - (int)(imageSize * imageOffset);
        for(int i = 0;i < visibleList.size();i++){
            CircleImageView imageView = new CircleImageView(getContext());
            imageView.setId(imageView.hashCode()+i);
            imageView.setBorderColor(Color.WHITE);
            imageView.setBorderWidth(Math.round(dp2px(3)));
            RelativeLayout.LayoutParams params = new LayoutParams(imageSize,imageSize);
            params.addRule(ALIGN_PARENT_LEFT);
            params.setMargins(i*offset,0,0,0);
            if(loadListener != null){
                loadListener.showImage(visibleList.get(i),imageView);
            }
            addView(imageView,params);
        }

    }


    public interface OnLoadListener<T>{
        public void showImage(T t,CircleImageView imageView);
    }


    private int dp2px(int dip) {
        Context context = this.getContext();
        Resources resources = context.getResources();
        int px = Math
                .round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        dip, resources.getDisplayMetrics()));
        return px;
    }
}
