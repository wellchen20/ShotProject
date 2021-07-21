package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.adapters.AlarmHistoryAdapter;
import com.jpkh.cnpc.activity.entity.AlarmHistoryEntity;
import com.jpkh.cnpc.activity.entity.AlarmImeiEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.OnlineSetEntity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class AlarmHistoryActivity extends Activity {
    TextView tv_num;
    TextView tv_title;
    TextView tv_type;
    ListView lv_view;
    Button btn_submit;
    ImageView iv_back;
    private AlarmHistoryAdapter adapter;
    private int task_total = 0;
    private SharedPreferences mPreferences;

    ProgressDialog progressDialog = null;
    private OnlineSetEntity mOnlineSetEntity;
    String imei;
    AlarmHistoryEntity mAlarmHistoryEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_alarm);
        StatusBarUtil.setTheme(this);
        imei = getIntent().getStringExtra("imei");
        setViews();
        initProgressDialog();
        initData();
        setListeners();
    }

    public void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中...");
    }

    private void setListeners() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initData() {
        processPoints();
    }

    private void setViews() {
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_title = findViewById(R.id.tv_title);
        tv_type = findViewById(R.id.tv_type);
        lv_view = (ListView) findViewById(R.id.lv_view);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        tv_title.setText("历史告警");
        tv_type.setText("告警数量");
        btn_submit.setVisibility(View.GONE);
    }

    private void setAdapters() {
        adapter = new AlarmHistoryAdapter(this, mAlarmHistoryEntity);
        lv_view.setAdapter(adapter);

    }

    public void processPoints(){
        if (progressDialog!=null){
            progressDialog.show();
        }
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        Log.e("getPoints", "tokenId: "+tokenId );
        Log.e("getPoints", "accessToken: "+accessToken );
        Map<String,String> map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
        refush(map);
    }

    public void refush(Map<String,String> map){
        AlarmImeiEntity mAlarmImeiEntity = new AlarmImeiEntity();
        mAlarmImeiEntity.setImei(imei);
        Gson gson = new Gson();
        String json = gson.toJson(mAlarmImeiEntity);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getHistoryImeis(map,body), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(AlarmHistoryActivity.this,"获取告警历史记录失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                try {
                    String result = responseBody.string().toString();
                    Log.e("responseBody", "告警历史记录: "+result);
                    mAlarmHistoryEntity = new Gson().fromJson(result,AlarmHistoryEntity.class);
                    task_total = mAlarmHistoryEntity.getData().getList().size();
                    tv_num.setText(task_total+"");
                    setAdapters();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

}
