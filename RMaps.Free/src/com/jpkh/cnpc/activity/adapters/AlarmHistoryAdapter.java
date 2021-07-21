package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.AlarmHistoryEntity;

public class AlarmHistoryAdapter extends BaseAdapter{

	private Context mContext;
	private AlarmHistoryEntity mAlarmHistoryEntity;

	public AlarmHistoryAdapter(Context context, AlarmHistoryEntity mAlarmHistoryEntity) {
		this.mContext = context;
		this.mAlarmHistoryEntity = mAlarmHistoryEntity;
	}


	@Override
	public int getCount() {
		return mAlarmHistoryEntity == null ? 0 : mAlarmHistoryEntity.getData().getList().size();
	}

	@Override
	public Object getItem(int position) {
		return mAlarmHistoryEntity.getData().getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_alarm_history, null);
			holdView = new HoldView();
			holdView.tv_deviceName = (TextView) convertView.findViewById(R.id.tv_deviceName);
			holdView.tv_imei = (TextView) convertView.findViewById(R.id.tv_imei);
			holdView.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
			holdView.tv_stationNo = (TextView) convertView.findViewById(R.id.tv_stationNo);
			holdView.tv_alarmTime = (TextView) convertView.findViewById(R.id.tv_alarmTime);
			holdView.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			holdView.tv_lng = (TextView) convertView.findViewById(R.id.tv_lng);
			holdView.tv_lat = (TextView) convertView.findViewById(R.id.tv_lat);
			holdView.tv_alarmName = (TextView) convertView.findViewById(R.id.tv_alarmName);
			holdView.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(holdView);
		} 
		holdView = (HoldView) convertView.getTag();
		if ( mAlarmHistoryEntity.getData().getList()!=null){
			holdView.tv_deviceName.setText( mAlarmHistoryEntity.getData().getList().get(position).getDeviceName()+"");
			holdView.tv_imei.setText( mAlarmHistoryEntity.getData().getList().get(position).getImei()+"");
			holdView.tv_type.setText( mAlarmHistoryEntity.getData().getList().get(position).getAlarmName()+"");
			holdView.tv_stationNo.setText( mAlarmHistoryEntity.getData().getList().get(position).getStation()+"");
			holdView.tv_alarmTime.setText( mAlarmHistoryEntity.getData().getList().get(position).getAlarmTime()+"");
			holdView.tv_address.setText( mAlarmHistoryEntity.getData().getList().get(position).getAddress()+"");
			holdView.tv_lng.setText( mAlarmHistoryEntity.getData().getList().get(position).getLng()+"");
			holdView.tv_lat.setText( mAlarmHistoryEntity.getData().getList().get(position).getLat()+"");
			holdView.tv_alarmName.setText( mAlarmHistoryEntity.getData().getList().get(position).getHandleBy()+"");
			if (mAlarmHistoryEntity.getData().getList().get(position).getHandleStatus().equals("1")){
				holdView.tv_status.setText("已处理");
			}else {
				holdView.tv_status.setText("未处理");
			}

		}
		return convertView;
	}

	class HoldView {
		TextView tv_deviceName;
		TextView tv_imei;
		TextView tv_type;
		TextView tv_stationNo;
		TextView tv_alarmTime;
		TextView tv_address;

		TextView tv_lng;
		TextView tv_lat;
		TextView tv_alarmName;
		TextView tv_status;
	}
}
