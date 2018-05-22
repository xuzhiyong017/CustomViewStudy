package com.sky.customviewstudy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.customviewstudy.R;


/**
 * 颜色列表Adapter
 *
 * @author panyi
 */
public class ColorListAdapter extends RecyclerView.Adapter<ViewHolder> {
    public static final int TYPE_COLOR = 1;
    public static final int TYPE_MORE = 2;
    private final Context mContext;

    public interface IColorListAction{
        void onColorSelected(final int position, final int color);
        void onMoreSelected(final int position);
    }
    private int[] colorsData;
    private int curPosition = 0;

    private IColorListAction mCallback;


    public ColorListAdapter(Context context, int[] colors, IColorListAction action) {
        super();
        this.mContext = context;
        this.colorsData = colors;
        this.mCallback = action;

        curPosition = 0;
    }

    public class ColorViewHolder extends ViewHolder {
        View colorPanelView;
        View whiteViewTop;

        public ColorViewHolder(View itemView) {
            super(itemView);
            this.colorPanelView = itemView.findViewById(R.id.color_panel_view);
            this.whiteViewTop = itemView.findViewById(R.id.wihte_border_top);
            this.colorPanelView.setFocusable(true);
            this.colorPanelView.setFocusableInTouchMode(true);

        }
    }// end inner class

    public class MoreViewHolder extends ViewHolder {
        View moreBtn;
        public MoreViewHolder(View itemView) {
            super(itemView);
            this.moreBtn = itemView.findViewById(R.id.color_panel_more);
        }

    }//end inner class

    @Override
    public int getItemCount() {
        return colorsData.length ;
    }

    @Override
    public int getItemViewType(int position) {
        return colorsData.length == position ? TYPE_MORE : TYPE_COLOR;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        ViewHolder viewHolder = null;
        if (viewType == TYPE_COLOR) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.view_color_panel, parent,false);
            viewHolder = new ColorViewHolder(v);
        } else if (viewType == TYPE_MORE) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_color_more_panel,parent,false);
            viewHolder = new MoreViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if(type == TYPE_COLOR){
            onBindColorViewHolder((ColorViewHolder)holder,position);
        }else if(type == TYPE_MORE){
            onBindColorMoreViewHolder((MoreViewHolder)holder,position);
        }
    }

    private void onBindColorViewHolder(final ColorViewHolder holder,final int position){
       //白色边框控制
        if(position == 1){
            holder.whiteViewTop.setVisibility(View.VISIBLE);
        }else{
            holder.whiteViewTop.setVisibility(View.GONE);
        }
        holder.colorPanelView.setBackgroundColor(colorsData[position]);
        holder.colorPanelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.requestFocus();
                if(mCallback!=null){
                    mCallback.onColorSelected(position,colorsData[position]);
//                    notifyItemChanged(curPosition);
                    curPosition = position;
//                    notifyItemChanged(curPosition);
                }
            }
        });

//        if(position == curPosition){
//            holder.itemView.setScaleX(1.2f);
//            holder.itemView.setScaleY(1.2f);
//        }else{
//            holder.itemView.setScaleX(1f);
//            holder.itemView.setScaleY(1f);
//        }
    }

    private void onBindColorMoreViewHolder(final MoreViewHolder holder,final int position){
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback!=null){
                    mCallback.onMoreSelected(position);
                }
            }
        });
    }

}
