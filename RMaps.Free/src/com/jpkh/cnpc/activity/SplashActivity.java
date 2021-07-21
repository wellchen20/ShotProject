package com.jpkh.cnpc.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysConfig.WorkType;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.service.ConnService;
import com.robert.maps.applib.utils.DialogUtils;
import com.robert.maps.applib.utils.LogFileUtil;
import com.robert.maps.applib.utils.Ut;

import java.util.ArrayList;
import java.util.List;

/***
 * 启动界面
 * 
 * @author TNT
 * 
 */
public class SplashActivity extends Activity {
	
	private static final int sleepTime = 3 / 2 * 1000;
	private SharedPreferences preferences;
	public static int PERMISSION_REQ = 0x123456;
	class PhoneConstant{
		final static String  IS_HUAWEI = "isHuawei"; //华为
		final static String  IS_XIAOMI = "isXiaomi"; //小米
		final static String  IS_OPPO = "isOppo";  //oppo
		final static String  IS_VIVO = "isVivo"; //vivo
		final static String  IS_MEIZU = "isMeizu"; //魅族
		final static String  IS_SAMSUNG = "isSamsung"; //三星
		final static String  IS_LETV = "isLetv"; //乐视
		final static String  IS_SMARTISAN = "isSmartisan"; //锤子
	}
	Dialog dialog;

	private String[] mPermission = new String[] {
			Manifest.permission.CAMERA,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.READ_PHONE_STATE
	};

	private List<String> mRequestPermission = new ArrayList<String>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		dealNotice();
		preferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
		// 初始化项目目录
		Ut.getRMapsMapsDir(this);
		Ut.getRMapsProjectDir(this);
		Ut.getRMapsProjectPublicDir(this);
		Ut.getRMapsRMapsProjectPrivateDir(this);
		Ut.getRMapsProjectPrivatePointsDir(this);
		Ut.getRMapsProjectPrivatePointsInputDir(this);
		Ut.getRMapsProjectPrivatePointsOutputDir(this);
		Ut.getRMapsProjectPrivateTracksDir(this);
		Ut.getRMapsProjectPrivateTracksInputDir(this);
		Ut.getRMapsProjectPrivateTracksOutputDir(this);
		Ut.getRMapsProjectPrivateTasksDir(this);
		Ut.getRMapsProjectPrivateTasksInputDir(this);
		Ut.getRMapsProjectPrivateTasksOutputDir(this);
		LogFileUtil.getSize();
		// 更新参数
		refreshConfig();
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			for (String one : mPermission) {
				if (PackageManager.PERMISSION_GRANTED != this.checkSelfPermission(one)) {
					mRequestPermission.add(one);
				}
			}
			if (!mRequestPermission.isEmpty()) {
				this.requestPermissions(mRequestPermission.toArray(new String[mRequestPermission.size()]), PERMISSION_REQ);
				return ;
			}
		}
		startActiviy();
	}

	private void dealNotice() {
		if (getIntent().getAction().equals("notice_clear")){
			ConnService.no_count = 0;
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
		// 版本兼容
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
			return;
		}
		if (requestCode == PERMISSION_REQ) {
			for (int i = 0; i < grantResults.length; i++) {
				for (String one : mPermission) {
					if (permissions[i].equals(one) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
						mRequestPermission.remove(one);
					}
				}
			}
			startActiviy();
		}
	}

	private void refreshConfig() {

		try {
			LoginActivity.isLogin = preferences.getBoolean(SysContants.ISLOGIN, false);
			
			SysConfig.isOnlineMap = preferences.getBoolean(SysContants.ISONLINEMAP, true);
			SysConfig.isProjectShow = preferences.getBoolean(SysContants.ISPROJECTSHOW, false);
			SysConfig.shotSelectType = preferences.getInt(SysContants.SHOTSELECTTYPE,SysConfig.shotSelectType);
			
			SysConfig.isDSCloud = preferences.getBoolean(SysContants.ISCLOUD, SysConfig.isDSCloud);
			
			SysConfig.workType = preferences.getInt(SysContants.WORK_TYPE, WorkType.WORK_TYPE_NONE);
			SysConfig.APP_KEY = preferences.getString(SysContants.APPKEY, SysConfig.APP_KEY);
			SysConfig.IP = preferences.getString(SysContants.WIFI_IP, SysConfig.IP);
			SysConfig.PORT = preferences.getInt(SysContants.WIFI_PORT, SysConfig.PORT);
			
//			HttpContact.URL = "http://" + SysConfig.IP + ":" + SysConfig.PORT + "/huobao-service/";
			
			SysConfig.BZJ_ID = preferences.getString(SysContants.BZJ, SysConfig.BZJ_ID);
			SysConfig.SC_ID = preferences.getString(SysContants.SC, SysConfig.SC_ID);
			SysConfig.ZZJG_ID = preferences.getString(SysContants.ZZJG, SysConfig.ZZJG_ID);
			SysConfig.SC = new StringBuffer().append(SysConfig.HANDSET).append(SysConfig.SC_ID).toString();
			
			SysConfig.ShotproMax = preferences.getFloat(SysContants.SHOTPRO_MAX, SysConfig.ShotproMax);
			SysConfig.safe_Distance = preferences.getFloat(SysContants.SAFE_DISTANCE, SysConfig.safe_Distance);
			SysConfig.Readytimeout = preferences.getFloat(SysContants.READY_TIMEOUT, SysConfig.Readytimeout);
			SysConfig.PowerTimeout = preferences.getFloat(SysContants.POWER_TIMEOUT, SysConfig.PowerTimeout);
			SysConfig.GPS_UP_TIME_TIP = preferences.getInt(SysContants.GPS_UPTIME, SysConfig.GPS_UP_TIME_TIP);
			SysConfig.IS_FIRST = preferences.getBoolean(SysContants.FIRST_LOGIN,SysConfig.IS_FIRST);

		} catch (Exception e) {
		}
	}

	public void startActiviy() {
		if (mRequestPermission.isEmpty()) {
			new Handler().postDelayed(new Runnable() {
				@TargetApi(Build.VERSION_CODES.M)
				@Override
				public void run() {
					Intent intent01 = new Intent(SplashActivity.this, MainActivity.class);
					Intent intent02 = new Intent(SplashActivity.this, LoginActivity.class);
					if (LoginActivity.isLogin){
						startActivity(intent01);
					}else {
						startActivity(intent02);
					}
					SplashActivity.this.finish();
				}
			},1000);
		} else {
			//在Manifest中未要求任何权限时也会跳到这里
			Toast.makeText(this, "权限注册失败!", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					SplashActivity.this.finish();
				}
			}, 2000);
		}
	}

}
