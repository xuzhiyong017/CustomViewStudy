package com.sky.customviewstudy.widget;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.sky.customviewstudy.adapter.ColorListAdapter;

import java.util.Collections;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_DRAG;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;

/**
 * @author: xuzhiyong
 * @date: 2018/10/29 16:46
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class ItemMoveTouchHelperCallback extends ItemTouchHelper.Callback {

    private ColorListAdapter mAdapter;
    RecyclerView recyclerView;

    public ItemMoveTouchHelperCallback(RecyclerView recyclerView, ColorListAdapter picAdapter){
        mAdapter = picAdapter;
        this.recyclerView = recyclerView;
    }

    /**
     * 官方文档的说明如下：
     * o control which actions user can take on each view, you should override getMovementFlags(RecyclerView, ViewHolder)
     * and return appropriate set of direction flags. (LEFT, RIGHT, START, END, UP, DOWN).
     * 返回我们要监控的方向，上下左右，我们做的是上下拖动，要返回都是UP和DOWN
     * 关键坑爹的是下面方法返回值只有1个，也就是说只能监控一个方向。
     * 不过点入到源码里面有惊喜。源码标记方向如下：
     *  public static final int UP = 1     0001
     *  public static final int DOWN = 1 << 1; （位运算：值其实就是2）0010
     *  public static final int LEFT = 1 << 2   左 值是3
     *  public static final int RIGHT = 1 << 3  右 值是8
     */
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //也就是说返回值是组合式的
        //makeMovementFlags (int dragFlags, int swipeFlags)，看下面的解释说明
        int swipFlag=0;
        //如果也监控左右方向的话，swipFlag=ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
        int dragflag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        //等价于：0001&0010;多点触控标记触屏手指的顺序和个数也是这样标记哦
        return  makeMovementFlags(dragflag,swipFlag);

        /**
         * 备注：由getMovementFlags可以联想到setMovementFlags，不过文档么有这个方法，但是：
         * 有 makeMovementFlags (int dragFlags, int swipeFlags)
         * Convenience method to create movement flags.便捷方法创建moveMentFlag
         * For instance, if you want to let your items be drag & dropped vertically and swiped left to be dismissed,
         * you can call this method with: makeMovementFlags(UP | DOWN, LEFT);
         * 这个recyclerview的文档写的简直完美，示例代码都弄好了！！！
         * 如果你想让item上下拖动和左边滑动删除，应该这样用： makeMovementFlags(UP | DOWN, LEFT)
         */

        //拓展一下：如果只想上下的话：makeMovementFlags（UP | DOWN, 0）,标记方向的最小值1
    }



    /**
     * 官方文档的说明如下
     * If user drags an item, ItemTouchHelper will call onMove(recyclerView, dragged, target). Upon receiving this callback,
     * you should move the item from the old position (dragged.getAdapterPosition()) to new position (target.getAdapterPosition())
     * in your adapter and also call notifyItemMoved(int, int).
     * 拖动某个item的回调，在return前要更新item位置，调用notifyItemMoved（draggedPosition，targetPosition）
     * viewHolde:正在拖动item
     * target：要拖到的目标
     * @return true 表示消费事件
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();//得到拖动ViewHolder的position
        int toPosition = target.getAdapterPosition();//得到目标ViewHolder的position
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(mAdapter.getDatas(), i, i + 1);
//                PicAdapter.PicItemHolder viewHolder2 = (PicAdapter.PicItemHolder) recyclerView.getChildViewHolder(this.recyclerView.getChildAt(i));
//                if (viewHolder2 != null) {
//                    viewHolder2.pic_num.setText(String.valueOf(viewHolder2.getAdapterPosition() + 1));
//                }
//            }
//
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(mAdapter.getDatas(), i, i - 1);
//                PicAdapter.PicItemHolder viewHolder1 = (PicAdapter.PicItemHolder) recyclerView.getChildViewHolder(this.recyclerView.getChildAt(i));
//                if (viewHolder1 != null) {
//                    viewHolder1.pic_num.setText(String.valueOf(viewHolder1.getAdapterPosition() + 1));
//                }
//            }
//        }


//        Collections.swap(mAdapter.getDatas(), fromPosition, toPosition);
        //直接按照文档来操作啊，这文档写得太给力了,简直完美！
        mAdapter.notifyItemMoved(fromPosition, toPosition);

        return true;
    }



    /**
     * 谷歌官方文档说明如下：
     * 这个看了一下主要是做左右拖动的回调
     * When a View is swiped, ItemTouchHelper animates it until it goes out of bounds, then calls onSwiped(ViewHolder, int).
     * At this point, you should update your adapter (e.g. remove the item) and call related Adapter#notify event.
     * @param viewHolder
     * @param direction
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //暂不处理
    }



    /**
     * 官方文档如下：返回true 当前tiem可以被拖动到目标位置后，直接”落“在target上，其他的上面的tiem跟着“落”，
     * 所以要重写这个方法，不然只是拖动的tiem在动，target tiem不动，静止的
     * Return true if the current ViewHolder can be dropped over the the target ViewHolder.
     * @param recyclerView
     * @param current
     * @param target
     * @return
     */
    @Override
    public boolean canDropOver(RecyclerView recyclerView, RecyclerView.ViewHolder current, RecyclerView.ViewHolder target) {
        return true;
    }


    /**
     * 官方文档说明如下：
     * Returns whether ItemTouchHelper should start a drag and drop operation if an item is long pressed.
     * 是否开启长按 拖动
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        //return true后，可以实现长按拖动排序和拖动动画了
        return true;
    }

    private View mItemView;

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if(viewHolder != null && viewHolder.itemView != null){
            mItemView = viewHolder.itemView;
        }

        if (mItemView == null) {
            return;
        }

        switch (actionState){
            case ACTION_STATE_IDLE:
            case ACTION_STATE_SWIPE:
                mItemView.setScaleX(1.0f);
                mItemView.setScaleY(1.0f);
                break;
            case ACTION_STATE_DRAG:
                mItemView.setScaleX(1.2f);
                mItemView.setScaleY(1.2f);
                break;

        }

    }
}
