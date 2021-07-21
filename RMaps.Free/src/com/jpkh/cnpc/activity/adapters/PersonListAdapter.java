package com.jpkh.cnpc.activity.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.PersonListEntity;

public class PersonListAdapter extends BaseAdapter{

	private Context mContext;
	private PersonListEntity mPersonListEntity ;

	public PersonListAdapter(Context context, PersonListEntity mPersonListEntity) {
		this.mContext = context;
		this.mPersonListEntity = mPersonListEntity;
	}


	@Override
	public int getCount() {
		return mPersonListEntity == null ? 0 : mPersonListEntity.getData().getList().size();
	}

	@Override
	public Object getItem(int position) {
		return mPersonListEntity.getData().getList().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,R.layout.item_person_list, null);
			holdView = new HoldView();
			holdView.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
			convertView.setTag(holdView);
		} 
		holdView = (HoldView) convertView.getTag();
		if (mPersonListEntity.getData().getList()!=null){
			holdView.tv_username.setText(mPersonListEntity.getData().getList().get(position).getUserName());
		}
		return convertView;
	}

	class HoldView {
		TextView tv_username;
	}
}
