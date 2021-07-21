package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.robert.maps.applib.AlarmPoint;

import java.util.List;

/***
 * 告警列表
 *
 * @author TNT
 *
 */
public class AlarmListAdapter extends BaseAdapter implements
		ListAdapter {
	public Context context = null;
	public LayoutInflater layoutInflater = null;
	public int nSelected = 0;
	public List<AlarmPoint> mAlarmList;
//	GeoPoint locGeoPoint;

	public AlarmListAdapter(Context context,
							List<AlarmPoint> mAlarmList, Location location) {
		this.context = context;
		this.mAlarmList = mAlarmList;
//		locGeoPoint = GeoPoint.fromDouble(location.getLatitude(), location.getLongitude());
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
		return mAlarmList.size();
	}

	@Override
	public Object getItem(int position) {
		return mAlarmList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.list_alarm_item, null);
		}

		AlarmPoint mAlarmPoint = mAlarmList.get(position);
//		String dis;
//		dis = (int) locGeoPoint.distanceTo(mAlarmPoint.getPoint())+"米";

		((TextView) convertView.findViewById(R.id.textView1))
				.setText((position + 1) + "");
		((TextView) convertView.findViewById(R.id.textView2))
				.setText(mAlarmPoint.getDeviceName());
		((TextView) convertView.findViewById(R.id.textView3))
				.setText(mAlarmPoint.getAlarmTime());
		((TextView) convertView.findViewById(R.id.textView4))
				.setText(mAlarmPoint.getAlarmName());

		// ((CheckedTextView) convertView.findViewById(android.R.id.text1))
		// .setEnabled(true);


		/*if (nSelected == position) {
//			 convertView.findViewById(R.id.radioButton1).setSelected(true);
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(true);
		} else {
//			 convertView.findViewById(R.id.radioButton1).setSelected(false);
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(false);
		}*/

		return convertView;
	}

}
