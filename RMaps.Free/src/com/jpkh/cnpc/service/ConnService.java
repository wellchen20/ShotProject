package com.jpkh.cnpc.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.LoginActivity;
import com.jpkh.cnpc.activity.MainActivity;
import com.jpkh.cnpc.activity.ReceiveOnlineSetActivity;
import com.jpkh.cnpc.activity.ReceivePointsActivity;
import com.jpkh.cnpc.activity.SplashActivity;
import com.jpkh.cnpc.activity.entity.AlarmTaskEntity;
import com.jpkh.cnpc.activity.entity.AppImei;
import com.jpkh.cnpc.activity.entity.ShotEntity;
import com.jpkh.cnpc.activity.entity.StationPageEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.activity.utils.OpenServiceBroadcast;
import com.jpkh.cnpc.protocol.bean.AlarmInfo;
import com.jpkh.cnpc.protocol.bean.DrillPoint;
import com.jpkh.cnpc.protocol.bean.LocationInfo;
import com.jpkh.cnpc.protocol.bean.ShotPoint;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.robert.maps.applib.OnlineSetEntity;

import org.andnav.osm.util.GeoPoint;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

import static com.jpkh.cnpc.activity.MainActivity.unRectifyLocation;

/**
 * ??????????????????
 * ?????????
 * 1.???MainActivity?????????Handler???????????????
 * 2.?????????Notification?????????????????????
 * 3.???Servie?????????START_STICKY???
 * 4.???????????? ???service???????????????????????????service???
 * */
public class ConnService extends Service {
    String channelID = "1";
    String channelName = "channel_name";
    public static int no_count= 0;
    NotificationManager notificationManager;
    JWebSocketClient client;
    JWebSocketClient client_task;
    String name ;
    AlarmInfo alarmInfo;
    AlarmTaskEntity mAlarmTaskEntity;
    public static boolean is_connect = false;
    String imei;
    private SharedPreferences mPreferences;
    private PointDBDao mPointDBDao;
    Map<String,String> map;
    String tokenId;
    String accessToken;
    ShotEntity shotEntity;
    OnlineSetEntity mOnlineSetEntity;

    boolean shotIsDone = false;
    boolean drillIsDone = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
        mPointDBDao = new PointDBDao(this);
        getIMEI();
        getMap();
        initHandler();
        getOnlineSets();
//        getAllStation();
        getAllStationByPage();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (unRectifyLocation!=null){
                    uploadLocation();
                }
            }
        },10000);
        return super.onStartCommand(intent, START_STICKY, startId);
    }

    public void getIMEI() {
        try {
            //??????android ????????????
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            imei = telephonyManager.getDeviceId();
            //android 10????????????????????????imei??? ??? android id??????
            if(TextUtils.isEmpty(imei)){
                imei = Settings.System.getString(
                        getContentResolver(), Settings.Secure.ANDROID_ID);
            }
            Log.e("AppImei", "AppImei: "+imei );

        }catch (Exception e){

        }

    }

    private void initHandler() {
        initSocketClient();
        initTaskClient();
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//??????????????????
        mHandler_location.postDelayed(locationBeatRunnable,LOCATION_BEAT_RATE);//????????????????????????
    }

    public void sendNotification() {
        no_count++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**
             * ??????channel
             * */
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);

            notificationManager.createNotificationChannel(channel);

            /**
             *  ???????????????????????????
             */
            Notification.Builder mBuilder =new Notification.Builder(this);
//            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            /**
             *  ??????Builder
             */
            //????????????
            Intent intent;
            if ( LoginActivity.isLogin ){
                intent = new Intent(this, MainActivity.class);
            }else {
                intent = new Intent(this, SplashActivity.class);
            }
            intent.setAction("notice_clear");

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            mBuilder.setChannelId(channelID)
                    .setAutoCancel(true)
                    .setContentTitle("??????"+no_count+"????????????")
                    //????????????
                    .setContentText("????????????????????????")
                    //???????????????
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_cnpc))
                    //???????????????
                    .setSmallIcon(R.drawable.news)
                    //??????????????????
                    .setWhen(System.currentTimeMillis())
                    //???????????????????????????
                    .setTicker("??????")
                    //???????????????????????????????????????????????????????????????????????????????????????
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);;
            //??????????????????
            notificationManager.notify(1, mBuilder.build());
        }else {
            /**
             *  ???????????????????????????
             */

            /**
             *  ???????????????????????????
             */

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            /**
             *  ??????Builder
             */
            //????????????
            Intent intent;
            if ( LoginActivity.isLogin ){
                intent = new Intent(this, MainActivity.class);
            }else {
                intent = new Intent(this, SplashActivity.class);
            }
            intent.setAction("notice_clear");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            mBuilder.setAutoCancel(true)
                    .setContentTitle("??????"+no_count+"????????????")
                    //????????????
                    .setContentText("????????????????????????")
                    //???????????????
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_cnpc))
                    //???????????????
                    .setSmallIcon(R.drawable.news)
                    //??????????????????
                    .setWhen(System.currentTimeMillis())
                    //???????????????????????????
                    .setTicker("??????")
                    //???????????????????????????????????????????????????????????????????????????????????????
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_ALL | Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent);;
            //??????????????????
            notificationManager.notify(1, mBuilder.build());
        }
    }


    @Override
    public void onDestroy() {
        Log.e("SERVICE","CLOSED");
        if (client!=null && !client.isClosed()){
            client.close();
        }
        if (client_task!=null && !client_task.isClosed()){
            client_task.close();
        }
       /* Intent localIntent = new Intent();
        localIntent.setClass(this, ConnService.class); //?????????????????????Service
        this.startService(localIntent);
        sendBroadcast(new Intent(this, OpenServiceBroadcast.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.startForegroundService(localIntent);
        } else {
            this.startService(localIntent);
        }
        Log.e("SERVICE","CLOSED");*/
        super.onDestroy();

    }


    private static final long HEART_BEAT_RATE = 10 * 1000;//??????10??????????????????????????????????????????
    private static final long LOCATION_BEAT_RATE = 10 * 1000 *60 ;//??????10??????????????????????????????
    private Handler mHandler = new Handler();
    private Handler mHandler_location = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override
        public void run() {
            if (client != null) {
                if (client.isClosed()) {
                    is_connect = false;
                    reconnectWs();
                }else {
                    is_connect = true;
                }
            } else {
                //??????client???????????????????????????websocket
                initSocketClient();
            }

            if (client_task != null) {
                if (client_task.isClosed()) {
                    is_connect = false;
                    reconnectTask();
                }else {
                    is_connect = true;
                }
            } else {
                //??????client???????????????????????????websocket
                initTaskClient();
            }
            sendHeart();//??????????????????
            //????????????????????????????????????
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    public void sendHeart(){
        Intent intentAlarm= new Intent("refush_status_phone");
        sendBroadcast(intentAlarm);
    }

    private Runnable locationBeatRunnable = new Runnable() {
        @Override
        public void run() {
            //??????????????????
            if (unRectifyLocation!=null){
                uploadLocation();
            }
//            getAllStation();
            getAllStationByPage();
            getOnlineSets();
            mHandler_location.postDelayed(this, LOCATION_BEAT_RATE);
        }
    };

    String connUrl = SysConfig.IP+":"+SysConfig.PORT;
    /*??????????????????websocket*/
    private void initSocketClient() {
        URI uri = URI.create("ws://"+connUrl+"/websocket/"+name+"-"+tokenId);
//        URI uri = URI.create("ws://"+connUrl+"/websocket/"+name);
        if (client==null ){
            client = new JWebSocketClient(uri) {
                @Override
                public void onMessage(String message) {
                    //message????????????????????????
                    String result = message;
                    Log.e("JWebSClientService", result);
                    alarmInfo =  new Gson().fromJson(result,AlarmInfo.class);
                    notice(alarmInfo);
                }

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
                    is_connect = true;
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    is_connect = false;
                }
            };
            try {
                client.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    /*??????????????????websocket*/
    private void initTaskClient() {
        URI uri = URI.create("ws://"+connUrl+"/messageWebsocket/"+imei+"-"+tokenId);
        //URI uri = URI.create("ws://"+connUrl+"/messageWebsocket/"+imei+"-"+tokenId);
        if (client_task==null){
            client_task = new JWebSocketClient(uri) {
                @Override
                public void onMessage(String message) {
                    //message????????????????????????
                    String result = message;
//                Log.e("initTaskClient", result);
                    mAlarmTaskEntity =  new Gson().fromJson(result,AlarmTaskEntity.class);
                    noticeTask(mAlarmTaskEntity);
                }

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    super.onOpen(handshakedata);
                    Log.e("task", "onOpen");
                    is_connect = true;
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    super.onClose(code, reason, remote);
                    Log.e("task", "close");
                    is_connect = false;
                }
            };
            try {
                client_task.connectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //????????????????????????UI???????????????
    public void notice(AlarmInfo alarmInfo){
        Intent intentAlarm= new Intent("cnpc.data.alarm");
        intentAlarm.putExtra("alarmInfo",alarmInfo);
        sendBroadcast(intentAlarm);
        //???????????????????????????
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//??????
            //???????????????????????????
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();  //????????????
                wl.release();  //?????????????????????
            }
            sendNotification();
        } else {
            sendNotification();
        }
    }

    //????????????????????????UI???????????????
    public void noticeTask(AlarmTaskEntity mAlarmTaskEntity){
        Intent intentAlarm= new Intent("cnpc.data.task");
        intentAlarm.putExtra("task",mAlarmTaskEntity);
        sendBroadcast(intentAlarm);
        //???????????????????????????
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.inKeyguardRestrictedInputMode()) {//??????
            //???????????????????????????
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            if (!pm.isScreenOn()) {
                @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
                wl.acquire();  //????????????
                wl.release();  //?????????????????????
            }
            sendNotification();
        } else {
            sendNotification();
        }
    }

    /**
     * ????????????
     */
    private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    //??????
                    client.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * ????????????
     */
    private void reconnectTask() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() {
            @Override
            public void run() {
                try {
                    //??????
                    client_task.reconnectBlocking();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void getMap(){
        tokenId = mPreferences.getString(SysContants.TOKENID,"");
        name = mPreferences.getString(SysContants.USERNAME,"")+"-"+new Random().nextInt(100000);
        accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
        Log.e("getPoints", "tokenId: "+tokenId );
        Log.e("getPoints", "accessToken: "+accessToken );
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
    }

    private void uploadLocation() {
        LocationInfo mLocationInfo = new LocationInfo();
        mLocationInfo.setAddress(MainActivity.address);
        mLocationInfo.setAppImei(imei);
        mLocationInfo.setLat(unRectifyLocation.getLatitude()+"");
        mLocationInfo.setLng(unRectifyLocation.getLongitude()+"");
        mLocationInfo.setHbTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        mLocationInfo.setLoginName(mPreferences.getString("username",""));
        Log.e("uploadLocation", MainActivity.address+"|"+imei+ "|"+unRectifyLocation.getLatitude()+"|"+unRectifyLocation.getLongitude());
        Gson gson = new Gson();
        String json = gson.toJson(mLocationInfo);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).saveAppLocation(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string().toString();
                    Log.e("responseBody", "responseBody: "+result);
                    if (result.contains("success")){

                    }else {
                        Log.e("conn", "location: "+"??????????????????" );
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    //??????????????????
    public void getAllStation(){
//        mPointDBDao.deleteAllShot();
        AppImei mAppImei = new AppImei();
        mAppImei.setAppImei(imei);
        Gson gson = new Gson();
        String json = gson.toJson(mAppImei);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getStationHasUpdated(map,body), new Subscriber<ResponseBody>() {
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
                    Log.e("onNext", "??????????????????: "+result);
                    shotEntity = new ShotEntity();
                    shotEntity = new Gson().fromJson(result,ShotEntity.class);
                    inserAllStations();
                } catch (IOException e) {
                    e.printStackTrace();
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",0);
                    sendBroadcast(intent);
                }
            }
        });
    }

    String batchNumber;
    public void getAllStationByPage(){
        batchNumber = new Date().getTime()+"";
        AppImei appImei = new AppImei();
        appImei.setAppImei(imei);
        appImei.setPageNum(1);
        appImei.setPageSize(5000);
        appImei.setBatchNumber(batchNumber);
        Gson gson = new Gson();
        String json = gson.toJson(appImei);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAllStationPage(map,body), new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("onError", "onError: "+e.getMessage());
                Intent intent = new Intent("cnpc_set_insert");
                intent.putExtra("status",0);
                sendBroadcast(intent);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("onNext", "??????????????????: "+result);
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
                        Intent intent = new Intent("cnpc_set_insert");
                        intent.putExtra("status",1);
                        sendBroadcast(intent);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",0);
                    sendBroadcast(intent);
                }finally {
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",1);
                    sendBroadcast(intent);
                }
            }
        });
    }

    public void getAllStationFor(int num){
        AppImei appImei = new AppImei();
        appImei.setAppImei(imei);
        appImei.setPageNum(num);
        appImei.setPageSize(5000);
        appImei.setBatchNumber(batchNumber);
        Gson gson = new Gson();
        String json = gson.toJson(appImei);
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
                    Log.e("onNext", num+"|??????????????????: "+result);
                    StationPageEntity stationPageEntity = new StationPageEntity();
                    stationPageEntity = new Gson().fromJson(result,StationPageEntity.class);
                    inserAllStations(stationPageEntity);;
                } catch (IOException e) {
                    e.printStackTrace();
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",0);
                    sendBroadcast(intent);
                }
            }
        });
    }

    public void inserAllStations(){
        new Thread(){
            @Override
            public void run() {
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

                shotIsDone = true;
                if (shotIsDone && drillIsDone){
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",1);
                    sendBroadcast(intent);
                }
            }
        }.start();

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

    public void getOnlineSets(){
        mPointDBDao.deleteAllDrillPoint();
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).getAllImeiLocationLocal(map), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.e("onError", "????????????????????????: "+e.getMessage());
                Intent intent = new Intent("cnpc_set_insert");
                intent.putExtra("status",0);
                sendBroadcast(intent);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("onNext", "????????????: "+result);
                    mOnlineSetEntity = new OnlineSetEntity();
                    mOnlineSetEntity = new Gson().fromJson(result,OnlineSetEntity.class);
                    if (mOnlineSetEntity.getData()!=null){
                        inserOnlineSets();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",0);
                    sendBroadcast(intent);
                }
            }

        });
    }

    public void inserOnlineSets() {
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < mOnlineSetEntity.getData().size(); i++) {
                    DrillPoint mDrillPoint = new DrillPoint();
                    String stationNo = mOnlineSetEntity.getData().get(i).getStation();
                    mDrillPoint.stationNo = mOnlineSetEntity.getData().get(i).getDeviceName();
                    mDrillPoint.lineNo = stationNo;
                    mDrillPoint.spointNo = mOnlineSetEntity.getData().get(i).getImei();
                    mDrillPoint.time = mOnlineSetEntity.getData().get(i).getGpsTime();
                    if (stationNo != null && mOnlineSetEntity.getData().get(i).getStaLat() != null) {
                        mDrillPoint.geoPoint = GeoPoint.from2DoubleString(mOnlineSetEntity.getData().get(i).getStaLat(), mOnlineSetEntity.getData().get(i).getStaLng());
                        mDrillPoint.lineNo =mOnlineSetEntity.getData().get(i).getStation();
                    } else {
                        mDrillPoint.geoPoint = GeoPoint.from2DoubleString(mOnlineSetEntity.getData().get(i).getLat(), mOnlineSetEntity.getData().get(i).getLng());
                        mDrillPoint.lineNo = "";
                    }
                    mPointDBDao.insertDrillPoint(mDrillPoint);
                }
                drillIsDone = true;

                if (shotIsDone && drillIsDone){
                    Intent intent = new Intent("cnpc_set_insert");
                    intent.putExtra("status",1);
                    sendBroadcast(intent);
                }
            }
        }.start();

    }
}


