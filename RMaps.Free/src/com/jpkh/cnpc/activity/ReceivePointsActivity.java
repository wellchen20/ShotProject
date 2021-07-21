package com.jpkh.cnpc.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.adapters.TaskShotsReceiveAdapter;
import com.jpkh.cnpc.activity.entity.AppImei;
import com.jpkh.cnpc.activity.entity.ShotEntity;
import com.jpkh.cnpc.activity.entity.StationPageEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.bean.ShotPoint;
import com.jpkh.cnpc.protocol.bean.TaskPoint;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.utils.DialogUtils;

import org.andnav.osm.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

public class ReceivePointsActivity extends Activity {

    ListView lv_view;
    Button btn_submit;
    ImageView iv_back;
    TextView tv_num;
    TextView tv_title;
    TextView tv_type;
    private PointDBDao mPointDBDao;
    private List<ShotPoint> mAllShots = new ArrayList<>();//所有
    private List<ShotPoint> mIsDoneShots = new ArrayList<>();//已完成
    private TaskShotsReceiveAdapter shotsAdapter;
    private SharedPreferences mPreferences;
    ProgressDialog progressDialog = null;
    private String tel;
    ShotEntity shotEntity ;
    int task_total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_model);
        StatusBarUtil.setTheme(this);
        setViews();
        getIMEI();
        initProgressDialog();
        initData();
        setAdapters();
        setListeners();
    }

    String appImei = "";
    /*获取android 唯一标志*/
    public void getIMEI() {
        try {
            //获取android 唯一标志
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            appImei = telephonyManager.getDeviceId();
            if(TextUtils.isEmpty(appImei)){
                appImei = Settings.System.getString(
                        getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            Log.e("AppImei", "AppImei: "+appImei );

        }catch (Exception e){

        }

    }
    public void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("开始导入任务...");
    }
    private void setListeners() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_DRILE){
                    showClearDataDialog(0);
                }else if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_SHOT){
                    showClearDataDialog(1);

                }else {
                    if (progressDialog!=null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                        Toast.makeText(ReceivePointsActivity.this,"暂无可领取任务！",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskPoint taskPoint = null;
                taskPoint = mPointDBDao.selectShotPointTotaskPoint(mAllShots.get(position).stationNo);
                if (taskPoint != null && !"".equals(taskPoint.stationNo)) {
                    Intent intent = new Intent();
                    intent.putExtra("stationNo", taskPoint.stationNo);
                    intent.putExtra("type",0);
                    setResult(200, intent);
                    finish();
                }
            }
        });
    }

    private void initData() {
        task_total = mAllShots.size();
        tv_num.setText(task_total+"");
    }

    private void setViews() {
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_title = findViewById(R.id.tv_title);
        tv_type = findViewById(R.id.tv_type);
        lv_view = (ListView) findViewById(R.id.lv_view);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mPointDBDao = new PointDBDao(this);
        mAllShots = mPointDBDao.selectAllShotPoint();
        mIsDoneShots = mPointDBDao.selectShotPointisDone();
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        tel = mPreferences.getString(SysContants.TEL, "");
        tv_title.setText("桩号列表");
        tv_type.setText("桩号数量");
    }

    private void setAdapters() {
        if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_SHOT) {
            shotsAdapter = new TaskShotsReceiveAdapter(this, mAllShots);
            lv_view.setAdapter(shotsAdapter);
        }

    }
    String batchNumber;
    Map<String,String> map;
    public void getPoints(){
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
        batchNumber = new Date().getTime()+"";
        AppImei imei = new AppImei();
        imei.setAppImei(appImei);
        imei.setPageNum(1);
        imei.setPageSize(5000);
        imei.setBatchNumber(batchNumber);
        Gson gson = new Gson();
        String json = gson.toJson(imei);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAllStationPage(map,body), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toast.makeText(ReceivePointsActivity.this,"获取失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                try {
                    String result = responseBody.string();
                    Log.e("onNext", "获取全部桩号: "+result);
                    StationPageEntity stationPageEntity = new StationPageEntity();
                    stationPageEntity = new Gson().fromJson(result,StationPageEntity.class);
                    if (stationPageEntity.getCode()==200){
                        inserAllStations(stationPageEntity);
                        if(stationPageEntity.getData().getPages()>=2){
                            for (int i=2;i<=stationPageEntity.getData().getPages();i++){
                                getAllStationFor(i);
                            }
                        }
                    }else if (stationPageEntity.getCode()==500){
                        if (progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ReceivePointsActivity.this,stationPageEntity.getMsg(),Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void inserAllStations(StationPageEntity stationPageEntity){
        new Thread(){
            @Override
            public void run() {
                if (stationPageEntity.getData().getList()!=null){
                    List<ShotPoint> shotPointList = new ArrayList<>();
                    for (int i = 0; i < stationPageEntity.getData().getList().size(); i++) {
                        ShotPoint shotPoint = new ShotPoint();
                        shotPoint.stationNo = stationPageEntity.getData().getList().get(i).getStation();
                        shotPoint.lineNo = stationPageEntity.getData().getList().get(i).getStation();
                        //shotPoint.spointNo = stationPageEntity.getData().getList().get(i).getNearestImei();
                        shotPoint.Alt = 0;
                        shotPoint.geoPoint = GeoPoint.from2DoubleString(stationPageEntity.getData().getList().get(i).getLat(), stationPageEntity.getData().getList().get(i).getLng());
                        shotPointList.add(shotPoint);
                    }
                    mPointDBDao.insertAllShotPoint(shotPointList);
                }
            }
        }.start();
    }

    public void getAllStationFor(int num){
        AppImei imei = new AppImei();
        imei.setAppImei(appImei);
        imei.setPageNum(num);
        imei.setPageSize(5000);
        imei.setBatchNumber(batchNumber);
        Gson gson = new Gson();
        String json = gson.toJson(imei);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAllStationPage(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("onNext", num+"|获取全部桩号: "+result);
                    StationPageEntity stationPageEntity = new StationPageEntity();
                    stationPageEntity = new Gson().fromJson(result,StationPageEntity.class);
                    inserAllStations(stationPageEntity);;
                } catch (IOException e) {
                    Toast.makeText(ReceivePointsActivity.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
                }finally {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refushAdapters();
                        }
                    },2000);
                }
            }
        });
    }


    public void refushAdapters(){
        if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_SHOT){
            mAllShots.clear();
            mIsDoneShots.clear();
            mAllShots.addAll(mPointDBDao.selectAllShotPoint());
            mIsDoneShots.addAll(mPointDBDao.selectShotPointisDone());
            shotsAdapter.notifyDataSetChanged();
        }
        initData();
    }

    class MyTaskAsync extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            if (shotEntity.getData()!=null){
                if (shotEntity.getData().getUpdateList()!=null){
                    for (int i = 0; i < shotEntity.getData().getUpdateList().size(); i++) {
                        ShotPoint shotPoint = new ShotPoint();
                        shotPoint.stationNo = shotEntity.getData().getUpdateList().get(i).getStation();
                        shotPoint.lineNo = shotEntity.getData().getUpdateList().get(i).getStation();
                        shotPoint.spointNo = shotEntity.getData().getUpdateList().get(i).getNearestImei();
                        shotPoint.Alt = 0;
                        shotPoint.geoPoint = GeoPoint.from2DoubleString(shotEntity.getData().getUpdateList().get(i).getLat(), shotEntity.getData().getUpdateList().get(i).getLng());
                        mPointDBDao.updateShotPoint(shotPoint);
                    }
                }

                if (shotEntity.getData().getAddlist()!=null && shotEntity.getData().getAddlist().size()>0){
                    mPointDBDao.deleteAllShot();
                    for (int i = 0; i < shotEntity.getData().getAddlist().size(); i++) {
                        ShotPoint shotPoint = new ShotPoint();
                        shotPoint.stationNo = shotEntity.getData().getAddlist().get(i).getStation();
                        shotPoint.lineNo = shotEntity.getData().getAddlist().get(i).getStation();
                        shotPoint.spointNo = shotEntity.getData().getAddlist().get(i).getNearestImei();
                        shotPoint.Alt = 0;
                        shotPoint.geoPoint = GeoPoint.from2DoubleString(shotEntity.getData().getAddlist().get(i).getLat(), shotEntity.getData().getAddlist().get(i).getLng());
//                            Log.e("getAddlist", "[ "+i+"]"+shotEntity.getData().getAddlist().get(i).toString());
                        mPointDBDao.insertShotPoint(shotPoint);
                    }
                }

                if (shotEntity.getData().getDelList()!=null){
                    for (int i = 0; i < shotEntity.getData().getDelList().size(); i++) {
                        mPointDBDao.deleteShot(shotEntity.getData().getDelList().get(i).getStation());
                    }
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean){
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_SHOT){
                    /*暂时放在这里*/
                    Toast.makeText(ReceivePointsActivity.this,"任务领取成功！",Toast.LENGTH_SHORT).show();
                }
                refushAdapters();
            }
        }
    }

    private Dialog dialog;
    public void showClearDataDialog(final int type) {
        dialog = DialogUtils.Alert(ReceivePointsActivity.this, "提示", "刷新所有桩号？",
                new String[]{ReceivePointsActivity.this.getString(R.string.ok), ReceivePointsActivity.this.getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPoints();
                        dialog.dismiss();
                        progressDialog.show();
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
