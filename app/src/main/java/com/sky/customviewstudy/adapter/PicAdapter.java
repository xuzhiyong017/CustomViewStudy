package com.sky.customviewstudy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.customviewstudy.R;
import com.sky.customviewstudy.bean.MultiModel;
import com.sky.customviewstudy.bean.SizeModel;
import com.sky.customviewstudy.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuzhiyong
 * @date: 2018/10/22 16:57
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class PicAdapter extends RecyclerView.Adapter<PicAdapter.PicItemHolder> {

    private Context context;
    private List<MultiModel> datas;

    public PicAdapter(Context context,int contentHeight) {
        super();
        this.context = context;
        this.itemMaxWidth = contentHeight;
        this.itemMaxHeight = contentHeight;
        this.itemWidth = context.getResources().getDimensionPixelOffset(R.dimen.checked_photo_list_height);
        this.itemHeight = this.itemWidth;
        this.imageWidth = context.getResources().getDimensionPixelSize(R.dimen.checked_photo_list_avatar_height);
        this.imageHeight = this.imageWidth;
        this.marginLeft = a(context,20);
        this.marginTop = this.marginLeft;


    }

    public static int a(Context paramContext, float paramFloat)
    {
        return (int)(0.5F + paramFloat * paramContext.getResources().getDisplayMetrics().density);
    }

    public void addPicture(MultiModel model){
        if(datas == null){
            datas = new ArrayList<>();
        }

        datas.add(model);
        notifyDataSetChanged();
    }

    public void removePicture(MultiModel model) {
        if(datas != null){
            datas.remove(model);
            notifyDataSetChanged();
        }
    }

    @Override
    public PicItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.picture_select_item, parent,false);
        return new PicItemHolder(view);
    }

    @Override
    public void onBindViewHolder(PicItemHolder holder, int position) {
        final MultiModel model = datas.get(position);
        Glide.with(context).load(model.getPath()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectListener != null){
                    removePicture(model);
                    selectListener.OnUnSelect(model);
                }
            }
        });
        holder.pic_num.setText(""+(position+1));

        holder.itemView.setTag(position);

    }

    @Override
    public int getItemCount() {
        return datas == null ? 0:datas.size();
    }

    public void clearData() {
        if(datas != null){
            datas.clear();
            notifyDataSetChanged();
        }
    }


    public class PicItemHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private ImageView picture_delete;
        private TextView pic_num;
        public PicItemHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.picture);
            picture_delete = itemView.findViewById(R.id.picture_delete);
            pic_num = itemView.findViewById(R.id.pic_num);


        }
    }

    public float c = 1.0f;

    private float itemMaxHeight;
    private float itemMaxWidth;
    private float itemWidth;
    private float itemHeight;
    private float imageWidth;
    private float imageHeight;
    private float marginLeft;
    private float marginTop;


    public  void updateSize(View view, float f) {
        int i;
        View picDeleteView = view.findViewById(R.id.picture_delete);
        View picNumView = view.findViewById(R.id.pic_num);
        picDeleteView.setAlpha(1.0f - f);
        picNumView.setAlpha(1.0f - f);
        ImageView picView = (ImageView) view.findViewById(R.id.picture);
        int currentItemWidth = (int) ((((float) (this.itemMaxWidth - this.itemWidth)) * f) + ((float) this.itemWidth));
        int currentItemHeight = (int) ((((float) (this.itemMaxHeight - this.itemHeight)) * f) + ((float) this.itemHeight));
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new RelativeLayout.LayoutParams(currentItemWidth, currentItemHeight));
        } else {
            view.getLayoutParams().width = currentItemWidth;
            view.getLayoutParams().height = currentItemHeight;
        }

        float intrinsicWidth = -1.0f;
        float intrinsicHeight = -1.0f;

        if (intrinsicWidth == -1.0f && view.getTag() != null) {
            MultiModel cVar = (MultiModel) datas.get(((Integer) view.getTag()).intValue());
            if (cVar != null) {
                SizeModel sizeModel = BitmapUtil.getImageSize(cVar.getPath());
                intrinsicWidth = (float) sizeModel.width;
                intrinsicHeight = (float) sizeModel.height;
            } else {
                return;
            }
        }
        if (intrinsicHeight / intrinsicWidth > ((float) (this.itemMaxHeight / this.itemMaxWidth))) {
            i = (int) this.itemMaxWidth;
            currentItemWidth = (int) ((intrinsicWidth * ((float) i)) / intrinsicHeight);
            currentItemHeight = i;
        } else {
            i = (int) this.itemMaxHeight;
            currentItemHeight = (int) ((intrinsicHeight * ((float) i)) / intrinsicWidth);
            currentItemWidth = i;
        }
        i = (int) ((((float) (currentItemWidth - this.imageWidth)) * f) + ((float) this.imageWidth));
        int i4 = (int) ((((float) (currentItemHeight - this.imageHeight)) * f) + ((float) this.imageHeight));
        int i5 = (int) ((((float) (((this.itemMaxHeight - currentItemWidth) / 2) - this.marginLeft)) * f) + ((float) this.marginLeft));
        currentItemHeight = (int) ((((float) (((this.itemMaxWidth - currentItemHeight) / 2) - this.marginTop)) * f) + ((float) this.marginTop));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) picView.getLayoutParams();
        layoutParams.setMargins(i5, currentItemHeight, 0, 0);
        layoutParams.width = i;
        layoutParams.height = i4;
        picView.requestLayout();
        picView.invalidate();
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) picDeleteView.getLayoutParams();
        layoutParams2.setMargins(i5 - (layoutParams2.width / 2), currentItemHeight - (layoutParams2.height / 2), 0, 0);
        picDeleteView.requestLayout();
    }

    private GalleryAdapter.OnSelectListener selectListener;

    public void setOnSelectListener(GalleryAdapter.OnSelectListener listener){
        selectListener = listener;
    }
}
