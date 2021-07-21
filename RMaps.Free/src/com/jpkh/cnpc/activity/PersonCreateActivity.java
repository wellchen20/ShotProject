package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.LoginEntity;
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

public class PersonCreateActivity extends Activity {
    EditText et_name;
    EditText et_pwd;
    Button btn_submit;
    Map<String,String> map;
    ImageView iv_back;
    private SharedPreferences mPreferences;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_create);
        StatusBarUtil.setTheme(this);
        getMap();
        setViews();
        setListeners();
    }

    private void setViews() {
        et_name = findViewById(R.id.et_name);
        et_pwd = findViewById(R.id.et_pwd);
        btn_submit = findViewById(R.id.btn_submit);
        iv_back = findViewById(R.id.iv_back);
    }

    private void getMap() {
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
    }

    private void setListeners() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateDataDialog();

            }
        });
    }

    public void createUser(){
        LoginEntity entity = new LoginEntity();
        entity.setUserName(et_name.getText().toString());
        entity.setUserPwd(et_pwd.getText().toString());
        Gson gson = new Gson();
        String json = gson.toJson(entity);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).addAppUser(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(PersonCreateActivity.this,"创建人员失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    Log.e("PersonListEntity", "PersonListEntity: "+result);
                    if (result.contains("200")){
                        Toast.makeText(PersonCreateActivity.this,"创建人员成功",Toast.LENGTH_SHORT).show();
                    }else if (result.contains("507")){
                        Toast.makeText(PersonCreateActivity.this,"无权限",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(PersonCreateActivity.this,"创建人员失败",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void showCreateDataDialog() {
        dialog = DialogUtils.Alert(this, "提示", "确定创建账号？",
                new String[]{getString(R.string.ok), getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (TextUtils.isEmpty(et_name.getText().toString()) || TextUtils.isEmpty(et_pwd.getText().toString())){
                            Toast.makeText(PersonCreateActivity.this,"用户名或密码为空",Toast.LENGTH_SHORT).show();
                        }else {
                            createUser();
                        }

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
