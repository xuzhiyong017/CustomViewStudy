package com.sky.customviewstudy.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.customviewstudy.R;
import com.sky.customviewstudy.bean.MultiModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xuzhiyong
 * @date: 2018/10/22 14:45
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {


    private Context context;
    //数据源,所有的图片/视频
    private List<MultiModel> data;
    //已选中的图片/视频
    private List<MultiModel> selectedModels;
    //记录已选中的图片在数据源中的索引位置
    private List<Integer> selectedPosList;
    private int height;


    public GalleryAdapter(Context context, List<MultiModel> data) {
        this.context = context;
        this.data = data;
        this.selectedModels = new ArrayList<>();
        this.selectedPosList = new ArrayList<>();
    }

    public int getSelectedList(){
        return selectedModels == null ? 0: selectedModels.size();
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        height = (parent.getWidth() - 5 * 3)/4;
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(height,height));
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GalleryViewHolder holder, int position) {
        MultiModel model = data.get(position);

        Glide.with(context).load(model.getPath()).into(holder.importPreview);
        //使用Fresco加载图片
//        FrescoHelper.INSTANCE.bindImage(holder.importPreview, "file://" + model.getPath()
//                , new ResizeOptions((int)UIUtils.dip2Px(context, 88.5f)
//                        , (int)UIUtils.dip2Px(context, 88.5f))
//                , Priority.HIGH, null);
        //判断是图片还是视频,若是视频需要在右下角显示时长
        if (model.getType() == 1) {
            holder.imageSelect.setVisibility(View.GONE);
            holder.importVideoDuration.setVisibility(View.VISIBLE);
            holder.mPlayer.setVisibility(View.VISIBLE);
            holder.importVideoDuration.setText(timeParse(model.getDuration()));
        } else {
            holder.importVideoDuration.setVisibility(View.GONE);
            holder.imageSelect.setVisibility(View.VISIBLE);
            holder.mPlayer.setVisibility(View.GONE);
        }
        //当子项出现（或重新出现）到屏幕内时，需要判断其是否已经被选择，若已经被选择则需要将其置为对应位置；若不是被选择的图片/视频，需要取消其右上角的数字显示（在这里有坑，不判断的话在新刷出来的子项里会有之前选择过的图片的数字在新的图片的右上角，原因尚未明确）
        if (selectedModels.contains(model)) {
            if(model.getType() == 0){
                holder.imageSelect.setSelected(true);
                holder.mCoverImage.setVisibility(View.VISIBLE);
            }else{
                holder.mCoverVideo.setVisibility(View.VISIBLE);
            }
        } else {
            if(model.getType() == 0){
                holder.imageSelect.setSelected(false);
                holder.mCoverImage.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 三个参数的onBind方法，实际上onBind方法首先都是回调三参的onBind，若payloads里面没有数据，则再回调两个参数的onBind。notifyItemChanged方法也是如此，首先回调的是三参的onBind。
     **/
    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }

    }

    @Override
    public int getItemCount() {
        return data == null? 0:data.size();
    }

    /**
     * 将以毫秒为单位的时长转化为分钟形式(如02:30为二分三十秒)
     * @param duration 视频时长
     * @return xx:xx
     */
    private String timeParse(long duration) {
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;
        if( second < 10 ){
            time += "0" ;
        }
        time += second ;
        return time ;
    }

    public void replaceData(ArrayList<MultiModel> multiModels) {
        if(data == null){
            data = new ArrayList<>(multiModels.size());
        }

        data.clear();
        data.addAll(multiModels);
        notifyDataSetChanged();
    }

    public void removeModel(MultiModel model) {
        if(selectedModels != null){
            int index = selectedModels.indexOf(model);
            if(index > -1){
                selectedModels.remove(model);
                selectedPosList.remove(index);
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 素材导入界面RecyclerView的ViewHolder
     */
    class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //RecyclerView子项最外层布局
        private ImageView importPreview;
        private ImageView imageSelect;
        private View mCoverImage;
        private View mCoverVideo;
        private View mPlayer;
        //RecyclerView子项右下角显示的视频的时长
        private TextView importVideoDuration;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            importPreview = itemView.findViewById(R.id.iv_import_preview);
            imageSelect = itemView.findViewById(R.id.image_select);
            importVideoDuration = itemView.findViewById(R.id.tv_import_video_duration);
            mPlayer = itemView.findViewById(R.id.img_video_play);
            mCoverImage = itemView.findViewById(R.id.pic_cover);
            mCoverVideo = itemView.findViewById(R.id.video_cover);
            initListener();
        }

        private void initListener() {
            importPreview.setOnClickListener(this);
            imageSelect.setOnClickListener(this);

        }

        /**
         * item右上角点击事件的逻辑
         * @param view
         */
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_import_preview: {
                    MultiModel model = data.get(getAdapterPosition());
                    if (model.getType() == 0) { //图片处理
                        if(selectedModels.indexOf(model) == -1){
                            if(selectedModels.size() >0){
                                if(selectedModels.get(0).getType() == 1){
                                    clearSelected();
                                }
                            }
                            selectedModels.add(model);
                            selectedPosList.add(getAdapterPosition());
                            imageSelect.setSelected(true);
                            mCoverImage.setVisibility(View.VISIBLE);
                            if(selectListener != null){
                                selectListener.OnSelected(model);
                            }
                        }

                    } else if (model.getType() == 1 && selectedModels.size() <= 1){ //视频处理
                        //播放视频
                        clearSelected();
                        selectedModels.add(model);
                        selectedPosList.add(getAdapterPosition());
                        if(selectListener != null){
                            selectListener.OnSelected(model);
                        }
                    }
                    break;
                }
                case R.id.image_select:
                    MultiModel model = data.get(getAdapterPosition());
                    if(model.getType() == 0){
                        if(selectedModels.indexOf(model) == -1){
                            selectedModels.add(model);
                            selectedPosList.add(getAdapterPosition());
                            imageSelect.setSelected(true);
                            mCoverImage.setVisibility(View.VISIBLE);
                            if(selectListener != null){
                                selectListener.OnSelected(model);
                            }
                        }else{
                            int index = selectedModels.indexOf(model);
                            selectedModels.remove(model);
                            selectedPosList.remove(index);
                            imageSelect.setSelected(false);
                            mCoverImage.setVisibility(View.GONE);
                            if(selectListener != null){
                                selectListener.OnUnSelect(model);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void clearSelected() {
        if(selectedModels != null){

            selectedModels.clear();
            if(selectedPosList != null){
                selectedPosList.clear();
            }
            notifyDataSetChanged();
        }
    }

    private OnSelectListener selectListener;

    public void setOnSelectListener(OnSelectListener listener){
        selectListener = listener;
    }

    public interface OnSelectListener{
        public void OnSelected(MultiModel model);
        public void OnUnSelect(MultiModel model);
    }
}
