package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.jpkh.cnpc.activity.adapters.TaskDrillsReceiveAdapter;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.bean.DrillPoint;
import com.jpkh.cnpc.protocol.bean.TaskPoint;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.OnlineSetEntity;
import com.robert.maps.applib.utils.DialogUtils;

import org.andnav.osm.util.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Subscriber;

public class ReceiveOnlineSetActivity extends Activity {

    TextView tv_num;
    TextView tv_title;
    TextView tv_type;
    ListView lv_view;
    Button btn_submit;
    ImageView iv_back;
    private PointDBDao mPointDBDao;
    private List<DrillPoint> mAllDrills = new ArrayList<>();//所有
    private List<DrillPoint> mIsDoneDrills = new ArrayList<>();//已完成
    private TaskDrillsReceiveAdapter drillsAdapter;
    private int task_total = 0;
    private SharedPreferences mPreferences;

    ProgressDialog progressDialog = null;
    private OnlineSetEntity mOnlineSetEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_model);
        StatusBarUtil.setTheme(this);
        setViews();
        initProgressDialog();
        initData();
        setAdapters();
        setListeners();
    }


    public void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在导入任务...");
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
                        Toast.makeText(ReceiveOnlineSetActivity.this,"暂无可领取任务！",Toast.LENGTH_SHORT).show();
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
                taskPoint = mPointDBDao.selectDrillPointToTaskPoint(mAllDrills.get(position).stationNo);
                if (taskPoint != null && !"".equals(taskPoint.stationNo)) {
                    Intent intent = new Intent();
                    intent.putExtra("stationNo", taskPoint.stationNo);
                    intent.putExtra("type",1);
                    setResult(200, intent);
                    finish();
                }
            }
        });
    }

    private void initData() {
        task_total = mAllDrills.size();
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
        mAllDrills = mPointDBDao.selectAllDrillPoint();
        mIsDoneDrills = mPointDBDao.selectDrillisDone();
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        tv_title.setText("在线设备列表");
        tv_type.setText("在线设备数量");
    }

    private void setAdapters() {
        drillsAdapter = new TaskDrillsReceiveAdapter(this, mAllDrills);
        lv_view.setAdapter(drillsAdapter);
    }

    public void processPoints(){
        String tokenId = mPreferences.getString(SysContants.TOKENID,"");
        String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        Log.e("getPoints", "tokenId: "+tokenId );
        Log.e("getPoints", "accessToken: "+accessToken );
        Map<String,String> map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
        getPoints(map);
    }

    public void refush(Map<String,String> map){
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).refreshAllDevice(map), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ReceiveOnlineSetActivity.this,"刷新最新点位失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
                getPoints(map);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Log.e("responseBody", "responseBody: "+responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                getPoints(map);
            }

        });
    }

    public void getPoints(Map<String,String> map){
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAllImeiLocationLocal(map), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (progressDialog!=null){
                    progressDialog.dismiss();
                }
                Toast.makeText(ReceiveOnlineSetActivity.this,"获取在线设备失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("onNext", "在线设备: "+result);
                    mOnlineSetEntity = new OnlineSetEntity();
                    mOnlineSetEntity = new Gson().fromJson(result,OnlineSetEntity.class);
                    if (mOnlineSetEntity.getData()!=null){
                        new MyTaskAsync().execute();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void refushAdapters(){
        if (SysConfig.workType == SysConfig.WorkType.WORK_TYPE_SHOT){
            mAllDrills.clear();
            mIsDoneDrills.clear();
            mAllDrills.addAll(mPointDBDao.selectAllDrillPoint());
            mIsDoneDrills.addAll(mPointDBDao.selectDrillisDone());
            drillsAdapter.notifyDataSetChanged();
        }
        initData();
    }

    class MyTaskAsync extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            for (int i = 0; i < mOnlineSetEntity.getData().size(); i++) {
                DrillPoint mDrillPoint = new DrillPoint();
                String stationNo = mOnlineSetEntity.getData().get(i).getStation();
                mDrillPoint.stationNo = mOnlineSetEntity.getData().get(i).getDeviceName();
//                mDrillPoint.lineNo = stationNo;
                mDrillPoint.spointNo = mOnlineSetEntity.getData().get(i).getImei();
                mDrillPoint.time = mOnlineSetEntity.getData().get(i).getGpsTime();
                if (stationNo!=null && mOnlineSetEntity.getData().get(i).getStaLat()!=null){
                    mDrillPoint.geoPoint = GeoPoint.from2DoubleString(mOnlineSetEntity.getData().get(i).getStaLat(), mOnlineSetEntity.getData().get(i).getStaLng());
                    mDrillPoint.lineNo =mOnlineSetEntity.getData().get(i).getStation();
                }else {
                    mDrillPoint.geoPoint = GeoPoint.from2DoubleString(mOnlineSetEntity.getData().get(i).getLat(), mOnlineSetEntity.getData().get(i).getLng());
                    mDrillPoint.lineNo = "";
                }
                  mPointDBDao.insertDrillPoint(mDrillPoint);
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
                    if (mOnlineSetEntity==null || mOnlineSetEntity.getData().size()<1){
                        Toast.makeText(ReceiveOnlineSetActivity.this,"暂无可领取任务！",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ReceiveOnlineSetActivity.this,"任务领取成功！",Toast.LENGTH_SHORT).show();
                    }
                }
                refushAdapters();
            }
        }
    }

    private Dialog dialog;
    public void showClearDataDialog(final int type) {
        dialog = DialogUtils.Alert(ReceiveOnlineSetActivity.this, "提示", "刷新在线设备？",
                new String[]{ReceiveOnlineSetActivity.this.getString(R.string.ok), ReceiveOnlineSetActivity.this.getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 清空数据库
                        mPointDBDao.deleteAllDrillPoint();
                        processPoints();
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
