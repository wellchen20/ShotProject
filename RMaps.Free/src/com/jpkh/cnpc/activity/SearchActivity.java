package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.adapters.SearchHistoryListAdapter;
import com.jpkh.cnpc.protocol.bean.TaskPoint;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysConfig.WorkType;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.kml.PoiManager;
import com.robert.maps.applib.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity implements OnClickListener {

	private int searchType = 0;
	public class SearchType {
		public final static int SHOT = 0x03;
	}

	private AutoCompleteTextView main_search_edit;
	private TextView tv_search_history,main_search;
	private ListView lv_search_history;
	private LinearLayout ll_tv_search_clear_history;

	private PoiManager mPoiManager;
	private String keyWord;
	private ProgressDialog progDialog = null;// 搜索时进度条

	private List<String> mHistories = new ArrayList<String>();
	private SearchHistoryListAdapter mHistoryListAdapter;
	protected PointDBDao mPointDBDao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		StatusBarUtil.setTheme(this);
		mPointDBDao = new PointDBDao(this);
		mPoiManager = new PoiManager(SearchActivity.this);

		initViews();
		initDatas();
	}

	private void initViews() {
		main_search_edit = (AutoCompleteTextView) findViewById(R.id.main_search_edit);
		lv_search_history = (ListView) findViewById(R.id.lv_search_history);
		main_search = (TextView) findViewById(R.id.main_search);
		main_search.setOnClickListener(this);
		ll_tv_search_clear_history = (LinearLayout) findViewById(R.id.ll_tv_search_clear_history);
		ll_tv_search_clear_history.setOnClickListener(this);
		findViewById(R.id.ll_title_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.this.finish();
			}
		});;
		((TextView) findViewById(R.id.tv_title_title)).setText(getResources().getString(R.string.menu_search));
		searchType = SearchType.SHOT;
	}

	private void initDatas() {
		mHistories = mPointDBDao.selectAllHistory();
		mHistoryListAdapter = new SearchHistoryListAdapter(SearchActivity.this, mHistories);
		lv_search_history.setAdapter(mHistoryListAdapter);
		lv_search_history.setOnItemClickListener(itemClickListener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.main_search:
				keyWord = main_search_edit.getText().toString();
				searchButton();
//				hideSoftInput(main_search_edit);
				break;

			case R.id.ll_tv_search_clear_history:
				/*if (searchType == 0) {
					mPointDBDao.deleteHistory();
					mHistories = mPointDBDao.selectAllHistory();
					mHistoryListAdapter.setmHistorires(mHistories);
				} else {
					mPointDBDao.deleteHistoryByType(String.valueOf(searchType));
					mHistories = mPointDBDao.selectHistoryByType(String.valueOf(searchType));
					mHistoryListAdapter.setmHistorires(mHistories);
				}*/
				mPointDBDao.deleteHistory();
				mHistories = mPointDBDao.selectAllHistory();
				mHistoryListAdapter.setmHistorires(mHistories);
				break;
		}
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
			keyWord = mHistories.get(position);
			searchButton();
//			hideSoftInput(main_search_edit);
		}
	};


	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		if ("".equals(keyWord)) {
			Toast.makeText(SearchActivity.this,"请输入关键字",Toast.LENGTH_SHORT).show();
			return;
		} else {
			showProgressDialog();// 显示进度框
			switch (searchType) {
				case SearchType.SHOT:
					if (SysConfig.workType == WorkType.WORK_TYPE_NONE) {

					} else {
						TaskPoint taskPoint = null;
						if (SysConfig.workType == WorkType.WORK_TYPE_SHOT) {
							taskPoint = mPointDBDao.selectShotPointTotaskPoint(keyWord);
							if (taskPoint==null){
								taskPoint = mPointDBDao.selectDrillPointToTaskPoint(keyWord);
							}
						} else if (SysConfig.workType == WorkType.WORK_TYPE_DRILE) {

						}else if (SysConfig.workType == WorkType.WORK_TYPE_ARRANGE){
//							taskPoint = mPointDBDao.selectArrangePoint(keyWord);
						}
						if (taskPoint != null && !"".equals(taskPoint.stationNo)) {
							mPointDBDao.deleteHistoryByHistory(keyWord);
							mPointDBDao.insertHistory(String.valueOf(searchType), keyWord, TimeUtil.getCurrentTimeInString());
							dissmissProgressDialog();
							Intent intent = new Intent();
							intent.putExtra("type", searchType);
							intent.putExtra("isguide", false);
							intent.putExtra("shot", taskPoint.stationNo);
							setResult(RESULT_OK, intent);
							SearchActivity.this.finish();

						} else {
							Toast.makeText(this,"未查找到炮点",Toast.LENGTH_SHORT).show();
						}
					}
					break;
			}
			dissmissProgressDialog();
		}
	}



	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}
}
