package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.PersonListEntity;
import com.jpkh.cnpc.activity.entity.VisibleEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.utils.DialogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class PersionDetailsActivity extends Activity {

    ImageView iv_back;
    TextView tv_name;
    TextView tv_time;
    RadioGroup r_group;
    RadioButton rb_vb;
    RadioButton rb_invb;
    Button btn_del;
    PersonListEntity mPersonEntity;
    int position;
    private SharedPreferences mPreferences;
    Map<String,String> map;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_details);
        StatusBarUtil.setTheme(this);
        setViews();
        initData();
        setListeners();
    }

    private void setViews() {
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.tv_name);
        tv_time = findViewById(R.id.tv_time);
        r_group = findViewById(R.id.r_group);
        rb_vb = findViewById(R.id.rb_vb);
        rb_invb = findViewById(R.id.rb_invb);
        btn_del = findViewById(R.id.btn_del);
    }

    private void initData() {
        mPersonEntity = (PersonListEntity) getIntent().getSerializableExtra("mPersonEntity");
        position = getIntent().getIntExtra("position",0);
        if (mPersonEntity!=null){
            tv_name.setText(mPersonEntity.getData().getList().get(position).getUserName());
            tv_time.setText(mPersonEntity.getData().getList().get(position).getCreateTime());
            if (mPersonEntity.getData().getList().get(position).getStatus()==0){
                rb_vb.setChecked(true);
                rb_invb.setChecked(false);
            }else {
                rb_vb.setChecked(false);
                rb_invb.setChecked(true);
            }
        }

        getMap();

    }

    private void setListeners() {
        r_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_vb:
                        changeStatus(map,"0");//启用
                        break;
                    case R.id.rb_invb:
                        changeStatus(map,"1");//停用
                        break;
                }
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDelDataDialog();
            }
        });
    }

    private void getMap() {
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
    }

    private void changeStatus(Map<String, String> map,String status) {
        VisibleEntity visibleEntity = new VisibleEntity();
        visibleEntity.setId(mPersonEntity.getData().getList().get(position).getId());
        visibleEntity.setStatus(status);
        String json = new Gson().toJson(visibleEntity);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).toChangeAppUserStatus(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(PersionDetailsActivity.this,"启用/停用失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    if (result.contains("507")){
                        Toast.makeText(PersionDetailsActivity.this,"无权限",Toast.LENGTH_SHORT).show();
                    }else if(result.contains("200")){
                        Log.e("status", "status: "+result);
                        Toast.makeText(PersionDetailsActivity.this,"启用/停用成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PersionDetailsActivity.this,"启用/停用失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void deleteUser(Map<String, String> map) {
        VisibleEntity visibleEntity = new VisibleEntity();
        visibleEntity.setId(mPersonEntity.getData().getList().get(position).getId());
        String json = new Gson().toJson(visibleEntity);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).delAppUser(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(PersionDetailsActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    if (result.contains("507")){
                        Toast.makeText(PersionDetailsActivity.this,"无权限",Toast.LENGTH_SHORT).show();
                    }else if(result.contains("200")){
                        Log.e("del", "del: "+result);
                        Toast.makeText(PersionDetailsActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(PersionDetailsActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    public void showDelDataDialog() {
        dialog = DialogUtils.Alert(this, "提示", "确定删除账号？",
                new String[]{getString(R.string.ok), getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // 清空数据库
                        dialog.dismiss();
                        deleteUser(map);
                    }
                },
                        new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        }
                });
        dialog.show();
    }

}
