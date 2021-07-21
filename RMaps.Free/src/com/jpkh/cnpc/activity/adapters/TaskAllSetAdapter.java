package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.AllSetEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2018/7/12.
 */

public class TaskAllSetAdapter extends RecyclerView.Adapter<TaskAllSetAdapter.ViewHolder> {
    Context context;
    AllSetEntity mAllSetEntity;
    int isProtect = -1;
    int isSelect = -1;
    // 存储勾选框状态的map集合
    private Map<Integer, Boolean> map = new HashMap<>();
    public TaskAllSetAdapter(Context context, AllSetEntity mAllSetEntity){
        this.context = context;
        this.mAllSetEntity = mAllSetEntity;
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        if (isSelect!=-1){
            for (int i = 0; i < mAllSetEntity.getData().getList().size(); i++) {
                map.put(i, true);
            }
        }else{
            for (int i = 0; i < mAllSetEntity.getData().getList().size(); i++) {
                map.put(i, false);
            }
        }

    }

    public void setData(AllSetEntity mAllSetEntity){
        this.mAllSetEntity = mAllSetEntity;
        initMap();
    }
    public void setIsProtect(int isProtect){
        this.isProtect = isProtect;
    }
    public void  setIsSelect(int isSelect ){
        this.isSelect = isSelect;
        initMap();
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void setOnCheckedChangeClick(View view, int position,boolean flag);
    }

    private TaskAllSetAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(TaskAllSetAdapter.OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_receive, parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tv_stationNo.setText(mAllSetEntity.getData().getList().get(position).getDeviceName());
        if (null!=mAllSetEntity.getData().getList().get(position).getDeviceState()&&mAllSetEntity.getData().getList().get(position).getDeviceState().equals("1")){
            holder.tv_isSet.setVisibility(View.VISIBLE);
            holder.tv_noSet.setVisibility(View.GONE);
        }else {
            holder.tv_isSet.setVisibility(View.GONE);
            holder.tv_noSet.setVisibility(View.VISIBLE);
        }
        if (isProtect!=-1){
            holder.cb_isSet.setVisibility(View.VISIBLE);
        }else {
            holder.cb_isSet.setVisibility(View.GONE);
            holder.cb_isSet.setChecked(false);
        }
        /*if (isSelect!=-1){
            holder.cb_isSet.setChecked(true);
        }else {
            holder.cb_isSet.setChecked(false);
        }*/
        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.cb_isSet.setChecked(map.get(position));
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition()-1;
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition()-1;
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });

            holder.cb_isSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //用map集合保存
                    map.put(position, isChecked);
                    int pos = holder.getLayoutPosition()-1;
                    mOnItemClickLitener.setOnCheckedChangeClick(holder.cb_isSet,position,isChecked);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mAllSetEntity!=null){
            return mAllSetEntity.getData().getList().size();
        }else {
            return 0;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_stationNo;
        CheckBox cb_isSet;
        TextView tv_isSet;
        TextView tv_noSet;
        public ViewHolder(View view) {
            super(view);
            tv_stationNo = view.findViewById(R.id.tv_stationNo);
            cb_isSet = view.findViewById(R.id.cb_isSet);
            tv_isSet = view.findViewById(R.id.tv_isSet);
            tv_noSet = view.findViewById(R.id.tv_noSet);
        }
    }
}
