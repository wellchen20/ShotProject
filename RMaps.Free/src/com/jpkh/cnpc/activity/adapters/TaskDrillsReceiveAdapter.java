package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.protocol.bean.DrillPoint;

import java.util.List;

public class TaskDrillsReceiveAdapter extends BaseAdapter{

	private Context mContext;
	private List<DrillPoint> mDrillPoints ;
	public TaskDrillsReceiveAdapter(Context context, List<DrillPoint> mDrillPoints) {
		this.mContext = context;
		this.mDrillPoints = mDrillPoints;
	}


	@Override
	public int getCount() {
		return mDrillPoints == null ? 0 : mDrillPoints.size();
	}

	@Override
	public Object getItem(int position) {
		return mDrillPoints.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,R.layout.item_online_receive, null);
			holdView = new HoldView();
			holdView.tv_stationNo = (TextView) convertView.findViewById(R.id.tv_stationNo);
			holdView.tv_bindNo = (TextView) convertView.findViewById(R.id.tv_bindNo);
			holdView.tv_unbind = (TextView) convertView.findViewById(R.id.tv_unbind);
			convertView.setTag(holdView);
		} 
		holdView = (HoldView) convertView.getTag();
		if (mDrillPoints!=null){
			holdView.tv_stationNo.setText(mDrillPoints.get(position).stationNo);
			if (!mDrillPoints.get(position).lineNo.equals("")){
				holdView.tv_bindNo.setVisibility(View.VISIBLE);
				holdView.tv_unbind.setVisibility(View.GONE);
				holdView.tv_bindNo.setText(mDrillPoints.get(position).lineNo);
			}else {
				holdView.tv_bindNo.setVisibility(View.GONE);
				holdView.tv_unbind.setVisibility(View.VISIBLE);
			}
		}
		return convertView;
	}

	class HoldView {
		TextView tv_stationNo;
		TextView tv_bindNo;
		TextView tv_unbind;
	}
}
