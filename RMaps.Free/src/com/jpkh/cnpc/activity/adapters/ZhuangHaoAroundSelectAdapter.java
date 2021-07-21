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
import com.jpkh.cnpc.protocol.bean.ShotPoint;

import org.andnav.osm.util.GeoPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/***
 * 桩号选择过滤
 * 
 * @author TNT
 * 
 */
public class ZhuangHaoAroundSelectAdapter extends BaseAdapter implements
		ListAdapter {
	public Context context = null;
	public LayoutInflater layoutInflater = null;
	public int nSelected = 0;
	public List<ShotPoint> lstTaskEntities = new ArrayList<ShotPoint>();

	public ZhuangHaoAroundSelectAdapter(Context context,
                                        List<ShotPoint> lstDatas, Location location) {
		this.context = context;
		GeoPoint locGeoPoint = GeoPoint.fromDouble(location.getLatitude(), location.getLongitude());
		layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (lstDatas != null && lstDatas.size() > 0) {
			this.lstTaskEntities.clear();
			for (ShotPoint data : lstDatas) {
				if (data.dis > 0) {
					data.remark = data.dis + " 米";
				} else {
					int dis = (int) locGeoPoint.distanceTo(data.geoPoint);
					data.dis = dis;
					data.remark = dis + " 米";
				}
				this.lstTaskEntities.add(data);

			}
			// 排序
			Collections.sort(lstTaskEntities, new Comparator<ShotPoint>() {

				@Override
				public int compare(ShotPoint lhs, ShotPoint rhs) {
					// TODO Auto-generated method stub
					return ((Integer) lhs.dis).compareTo((Integer) rhs.dis);
				}
			});
			lstDatas.clear();
			lstDatas.addAll(this.lstTaskEntities);
		}
	}

	public void setSelectedItem(int nIndex) {
		nSelected = nIndex;
	}

	public int getSelectedItem() {
		return nSelected;
	}

	@Override
	public int getCount() {
		return lstTaskEntities.size();
	}

	@Override
	public Object getItem(int position) {
		return lstTaskEntities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.list_pipei_zhuanghao_item, null);
		}

		if (position==0){
			convertView.findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
		}
		ShotPoint taskEntity = lstTaskEntities.get(position);

		// if (convertView.findViewById(R.id.radioButton1).getVisibility() !=
		// View.VISIBLE) {
		// convertView.findViewById(R.id.radioButton1).setVisibility(
		// View.VISIBLE);
		// }

			((TextView) convertView.findViewById(R.id.textView1))
					.setTextColor(Color.BLACK);
			((TextView) convertView.findViewById(R.id.textView2))
					.setTextColor(Color.BLACK);
			((TextView) convertView.findViewById(R.id.textView3))
					.setTextColor(Color.BLACK);

			((TextView) convertView.findViewById(R.id.textView1))
					.setText((position + 1) + "");
			((TextView) convertView.findViewById(R.id.textView2))
					.setText(taskEntity.stationNo);
			((TextView) convertView.findViewById(R.id.textView3))
					.setText(taskEntity.remark);

			// ((CheckedTextView) convertView.findViewById(android.R.id.text1))
			// .setEnabled(true);

		if (nSelected == position) {
			// convertView.findViewById(R.id.radioButton1).setSelected(true);
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(true);
		} else {
			// convertView.findViewById(R.id.radioButton1).setSelected(false);
			((CheckedTextView) convertView.findViewById(android.R.id.text1))
					.setChecked(false);
		}

		return convertView;
	}

}
