package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.adapters.SearchHistoryListAdapter;
import com.jpkh.cnpc.activity.entity.AllSetEntity;
import com.jpkh.cnpc.activity.entity.InfoEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.utils.TimeUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class SearchSetActivity extends Activity implements View.OnClickListener {
    private AutoCompleteTextView main_search_edit;
    private TextView tv_search_history,main_search;
    private ListView lv_search_history;
    private LinearLayout ll_tv_search_clear_history;
    private List<String> mHistories = new ArrayList<String>();
    private SearchHistoryListAdapter mHistoryListAdapter;
    protected PointDBDao mPointDBDao;
    private String keyWord;
    HashMap<String,String> map;
    AllSetEntity mAllSetEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_set);
        StatusBarUtil.setTheme(this);
        setViews();
        initDate();
        setListeners();
    }

    private void initDate() {
        mPointDBDao = new PointDBDao(this);
        mHistories = mPointDBDao.selectAllHistory();
        mHistoryListAdapter = new SearchHistoryListAdapter(this, mHistories);
        lv_search_history.setAdapter(mHistoryListAdapter);
        lv_search_history.setOnItemClickListener(itemClickListener);
        map = (HashMap<String, String>) getIntent().getSerializableExtra("map");
    }

    private void setViews() {
        main_search_edit = (AutoCompleteTextView) findViewById(R.id.main_search_edit);
        lv_search_history = (ListView) findViewById(R.id.lv_search_history);
        main_search = (TextView) findViewById(R.id.main_search);
        ll_tv_search_clear_history = (LinearLayout) findViewById(R.id.ll_tv_search_clear_history);
        ((TextView) findViewById(R.id.tv_title_title)).setText(getResources().getString(R.string.menu_search));
        main_search.setOnClickListener(this);
        ll_tv_search_clear_history.setOnClickListener(this);
    }


    private void setListeners() {
        findViewById(R.id.ll_title_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });;
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
                mPointDBDao.deleteHistory();
                mHistories = mPointDBDao.selectAllHistory();
                mHistoryListAdapter.setmHistorires(mHistories);
                break;
        }
    }

    /**
     * 点击搜索按钮
     */
    public void searchButton() {
        if ("".equals(keyWord)) {
            Toast.makeText(this,"请输入关键字",Toast.LENGTH_SHORT).show();
            return;
        } else {
            getAll();
        }
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            keyWord = mHistories.get(position);
            searchButton();
//			hideSoftInput(main_search_edit);
        }
    };

    public void getAll(){
        InfoEntity mInfoEntity = new InfoEntity();
        mInfoEntity.setDeviceName(keyWord);
        Gson gson = new Gson();
        String json = gson.toJson(mInfoEntity);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAll(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(SearchSetActivity.this,"获取全部设备失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    mPointDBDao.deleteHistoryByHistory(keyWord);
                    mPointDBDao.insertHistory("100", keyWord, TimeUtil.getCurrentTimeInString());
                    String result = responseBody.string().toString();
                    mAllSetEntity = new Gson().fromJson(result, AllSetEntity.class);
                    Log.e("responseBody", "responseBody: "+result);
                    Intent intent = new Intent();
                    intent.putExtra("mAllSetEntity",mAllSetEntity);
                    if (mAllSetEntity.getData().getList().size()!=0){
                        setResult(0,intent);
                        finish();
                    }else {
                        Toast.makeText(SearchSetActivity.this,"未查找到设备",Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        finish();
    }
}
