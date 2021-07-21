package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.AlarmTaskListEntity;

/***
 * 告警任务列表
 *
 * @author TNT
 *
 */
public class AlarmTaskListAdapter extends BaseAdapter implements
		ListAdapter {
	public Context context = null;
	public LayoutInflater layoutInflater = null;
	public int nSelected = 0;
	public AlarmTaskListEntity mAlarmList;

	public AlarmTaskListAdapter(Context context,
								AlarmTaskListEntity mAlarmList) {
		this.context = context;
		this.mAlarmList = mAlarmList;
		layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setSelectedItem(int nIndex) {
		nSelected = nIndex;
	}

	public int getSelectedItem() {
		return nSelected;
	}

	@Override
	public int getCount() {
		return mAlarmList.getData().size();
	}

	@Override
	public Object getItem(int position) {
		return mAlarmList.getData().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.list_alarm_task, null);
			holder.textView1 = convertView.findViewById(R.id.textView1);
			holder.textView2 = convertView.findViewById(R.id.textView2);
			holder.textView3 = convertView.findViewById(R.id.textView3);
			holder.textView4 = convertView.findViewById(R.id.textView4);
//			holder.tv_title = convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}

/*		if (position==0){
			holder.tv_title.setVisibility(View.VISIBLE);
		}*/
		holder.textView1.setText((position + 1) + "");
		holder.textView2.setText(mAlarmList.getData().get(position).getAlarm().getDeviceName());
		holder.textView3.setText(mAlarmList.getData().get(position).getAlarm().getAlarmTime());
		holder.textView4.setText(mAlarmList.getData().get(position).getAlarm().getAlarmName());

		/*if (nSelected == position) {
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(true);
		} else {
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(false);
		}*/

		return convertView;
	}

	class ViewHolder{

		TextView textView1;

		TextView textView2;

		TextView textView3;

		TextView textView4;

		TextView tv_title;

	}

}
