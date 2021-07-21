package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.adapters.PersonListAdapter;
import com.jpkh.cnpc.activity.entity.PersonListEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.utils.StatusBarUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class PersonSetActivity extends Activity{
    ListView lv_view;
    Button btn_add;
    ImageView iv_back;
    PersonListAdapter adapter;
    PersonListEntity mPersonListEntity;
    private SharedPreferences mPreferences;
    boolean hasQx = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_set);
        StatusBarUtil.setTheme(this);
        setViews();
        setListeners();
    }

    @Override
    protected void onResume() {
        getMap();
        super.onResume();
    }

    private void getMap() {
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        Map<String,String> map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
        getPerson(map);
    }

    private void getPerson(Map<String, String> map) {
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), "{}");
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).listAppUser(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(PersonSetActivity.this,"获取人员列表失败",Toast.LENGTH_SHORT).show();
                hasQx = false;
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    if (result.contains("507")){
                        hasQx = false;
                        Toast.makeText(PersonSetActivity.this,"无权限",Toast.LENGTH_SHORT).show();
                    }else if(result.contains("200")){
                        hasQx = true;
                        mPersonListEntity = new Gson().fromJson(result, PersonListEntity.class);
                        Log.e("PersonListEntity", "PersonListEntity: "+result);
                        setAdapters();
                    }else {
                        Toast.makeText(PersonSetActivity.this,"获取人员失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void setViews() {
        lv_view = findViewById(R.id.lv_view);
        btn_add = findViewById(R.id.btn_add);
        iv_back = findViewById(R.id.iv_back);
    }

    private void setAdapters() {
        adapter = new PersonListAdapter(PersonSetActivity.this,mPersonListEntity);
        lv_view.setAdapter(adapter);
    }

    private void setListeners() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PersonSetActivity.this,PersionDetailsActivity.class);
                intent.putExtra("mPersonEntity",mPersonListEntity);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PersonSetActivity.this,PersonCreateActivity.class));
            }
        });
    }
}
