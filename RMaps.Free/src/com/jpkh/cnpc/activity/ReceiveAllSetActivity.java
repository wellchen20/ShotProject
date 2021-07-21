package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.google.gson.Gson;
import com.jpkh.cnpc.R;

import com.jpkh.cnpc.activity.adapters.TaskAllSetAdapter;
import com.jpkh.cnpc.activity.entity.AllSetEntity;
import com.jpkh.cnpc.activity.entity.InfoEntity;
import com.jpkh.cnpc.activity.entity.ProtectImeisEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.utils.StatusBarUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class ReceiveAllSetActivity extends Activity implements PullToRefreshListener {

    PullToRefreshRecyclerView pullToRefreshView;
    TaskAllSetAdapter adapter;
    AllSetEntity mAllSetEntity;
    ImageView iv_back;
    ImageView iv_find;
    Button btn_submit;
    TextView tv_title;
    TextView tv_type;
    TextView tv_num;
    TextView tv_yesSet;
    TextView tv_noSet;
    TextView tv_complete;
    TextView tv_selectAll;
    int isProtect = -1;
    private SharedPreferences mPreferences;
    int count = 10000;
    Set<String> imeiArray = new HashSet<>();
    ProtectImeisEntity protectImeisEntity;
    HashMap<String,String> map;
    int flag = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_task);
        StatusBarUtil.setTheme(this);
        setViews();
        setListeners();
        setAdapters();
        processPoints();
    }

    private void setListeners() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pullToRefreshView.setLayoutManager(layoutManager);
        //是否开启下拉刷新功能
        pullToRefreshView.setPullRefreshEnabled(true);
        //是否开启上拉加载功能
        pullToRefreshView.setLoadingMoreEnabled(false);
        //设置是否显示上次刷新的时间
        pullToRefreshView.displayLastRefreshTime(true);
        pullToRefreshView.setRefreshLimitHeight(60);
        //设置刷新回调
        pullToRefreshView.setPullToRefreshListener(this);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tv_yesSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProtect= 1;
                adapter.setIsProtect(isProtect);
                adapter.notifyDataSetChanged();
                tv_yesSet.setVisibility(View.GONE);
                tv_noSet.setVisibility(View.GONE);
                tv_complete.setVisibility(View.VISIBLE);
                tv_selectAll.setVisibility(View.VISIBLE);
            }
        });

        tv_noSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProtect= 0;
                adapter.setIsProtect(isProtect);
                adapter.notifyDataSetChanged();
                tv_yesSet.setVisibility(View.GONE);
                tv_noSet.setVisibility(View.GONE);
                tv_complete.setVisibility(View.VISIBLE);
                tv_selectAll.setVisibility(View.VISIBLE);
            }
        });

        tv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                protectImeisEntity = new ProtectImeisEntity();
                protectImeisEntity.setImeis(imeiArray);
                protectImeisEntity.setType(isProtect+"");
                Gson gson = new Gson();
                String json = gson.toJson(protectImeisEntity);
                setProtect(json);
            }
        });

        tv_selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag==1){
                    adapter.setIsSelect(flag);
                    adapter.notifyDataSetChanged();
                    flag = -1;
                    for (int i = 0;i<mAllSetEntity.getData().getList().size();i++){
                        imeiArray.add(mAllSetEntity.getData().getList().get(i).getImei());
                    }
                    Log.e("imeiArray", "size: "+imeiArray.size() );
                }else if (flag==-1){
                    adapter.setIsSelect(flag);
                    adapter.notifyDataSetChanged();
                    flag = 1;
                    imeiArray.clear();
                    Log.e("imeiArray", "size: "+imeiArray.size() );
                }
            }
        });

        iv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveAllSetActivity.this,SearchSetActivity.class);
                intent.putExtra("map",map);
                startActivityForResult(intent,0);
            }
        });
    }

    private void setViews() {
        pullToRefreshView = findViewById(R.id.pullToRefreshView);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_type = findViewById(R.id.tv_type);
        tv_num = findViewById(R.id.tv_num);
        btn_submit = findViewById(R.id.btn_submit);
        tv_yesSet = findViewById(R.id.tv_yesSet);
        tv_noSet = findViewById(R.id.tv_noSet);
        tv_complete = findViewById(R.id.tv_complete);
        tv_selectAll = findViewById(R.id.tv_selectAll);
        iv_find = findViewById(R.id.iv_find);
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        tv_title.setText("设备列表");
        tv_type.setText("设备数量");
        btn_submit.setVisibility(View.GONE);
    }

    private void setAdapters() {
        adapter = new TaskAllSetAdapter(this,mAllSetEntity);
        adapter.setHasStableIds(true);
        pullToRefreshView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new TaskAllSetAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String imei = mAllSetEntity.getData().getList().get(position).getImei();
                Intent intent = new Intent(ReceiveAllSetActivity.this,AlarmHistoryActivity.class);
                intent.putExtra("imei",imei);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }

            @Override
            public void setOnCheckedChangeClick(View view, int position, boolean flag) {
                String imei = mAllSetEntity.getData().getList().get(position).getImei();
                if (flag){
                    imeiArray.add(imei);
                    Log.e("imeiArray", "size: "+imeiArray.size() );
                }else if (flag == false){
                    imeiArray.remove(imei);
                    Log.e("imeiArray", "size: "+imeiArray.size() );
                }
            }
        });

    }

    public void processPoints(){
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        Log.e("getPoints", "tokenId: "+tokenId );
        Log.e("getPoints", "accessToken: "+accessToken );
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
        getAll();
    }

    public void getAll(){
        InfoEntity mInfoEntity = new InfoEntity();
        mInfoEntity.setDeviceName("");
        mInfoEntity.setImei("");
        mInfoEntity.setStation("");
        mInfoEntity.setPageNum(1);
        mInfoEntity.setPageSize(count*10);
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
                Toast.makeText(ReceiveAllSetActivity.this,"获取全部设备失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    mAllSetEntity = new Gson().fromJson(result,AllSetEntity.class);
                    tv_num.setText(mAllSetEntity.getData().getTotal()+"");
//                    count++;
                    adapter.setData(mAllSetEntity);
                    adapter.notifyDataSetChanged();
                    Log.e("responseBody", "responseBody: "+result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public void setProtect(String json){
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).changeDeviceState(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ReceiveAllSetActivity.this,"设防/撤防失败",Toast.LENGTH_SHORT).show();
                imeiArray.clear();
                Log.e("onError", "onError: "+e.getMessage());
                tv_noSet.setVisibility(View.VISIBLE);
                tv_yesSet.setVisibility(View.VISIBLE);
                tv_complete.setVisibility(View.GONE);
                tv_selectAll.setVisibility(View.GONE);
                adapter.setIsProtect(-1);
                adapter.setIsSelect(-1);
                flag = 1;
                getAll();
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    Log.e("responseBody", "responseBody: "+result);
                    Toast.makeText(ReceiveAllSetActivity.this,"设防/撤防成功",Toast.LENGTH_SHORT).show();
                    imeiArray.clear();
                    tv_noSet.setVisibility(View.VISIBLE);
                    tv_yesSet.setVisibility(View.VISIBLE);
                    tv_complete.setVisibility(View.GONE);
                    tv_selectAll.setVisibility(View.GONE);
                    adapter.setIsProtect(-1);
                    adapter.setIsSelect(-1);
                    flag = 1;
                    getAll();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onRefresh() {
        processPoints();
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshView.setRefreshComplete();
            }
        },1000);
    }

    @Override
    public void onLoadMore() {
//        processPoints();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshView.setLoadMoreComplete();
            }
        },1000);
    }

    @Override
    public void onBackPressed() {
        if (tv_complete.getVisibility()==View.VISIBLE){
            tv_complete.setVisibility(View.GONE);
            tv_selectAll.setVisibility(View.GONE);
            tv_yesSet.setVisibility(View.VISIBLE);
            tv_noSet.setVisibility(View.VISIBLE);
            imeiArray.clear();
            adapter.setIsProtect(-1);
            adapter.setIsSelect(-1);
            flag = 1;
            adapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0 && resultCode==0){
            mAllSetEntity = (AllSetEntity) data.getSerializableExtra("mAllSetEntity");
            adapter.setData(mAllSetEntity);
            adapter.notifyDataSetChanged();
        }
    }

}
