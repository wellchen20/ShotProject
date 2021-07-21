package com.jpkh.cnpc.activity;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.activity.entity.LoginEntity;
import com.jpkh.cnpc.activity.entity.LoginResult;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.HttpUtils;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysConfig.WorkType;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.service.ConnService;
import com.jpkh.utils.StatusBarUtil;
import com.robert.maps.applib.utils.DialogUtils;

import rx.Subscriber;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 登录页面
 *
 */
public class LoginActivity extends Activity {
	public static boolean isLogin = false;
	private EditText usernameEditText;
	private EditText passwordEditText;
	private ImageView iv_setting;
	private SharedPreferences mPreferences;

	private ProgressDialog pd;
	private ArrayAdapter<String> adapter;
	private String userName;
	private String passWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		StatusBarUtil.setTheme(this);
        initPb();
		mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
		userName = getIntent().getStringExtra("userName");
		passWord = getIntent().getStringExtra("passWord");
		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		iv_setting = findViewById(R.id.iv_setting);
		String name = getData(SysContants.USERNAME, "");
		String password = getData(SysContants.PASSWORD, "");
		if (name != null && name!="") {
			usernameEditText.setText(name);
            passwordEditText.setText(password);
            login(name,password);
            pd.show();
		}
		if (userName!=null && passWord!=null){
			usernameEditText.setText(userName);
			passwordEditText.setText(passWord);
		}
		findViewById(R.id.tv_login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd.show();
				login(usernameEditText.getText().toString(),passwordEditText.getText().toString());
			}
		});
		iv_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LoginActivity.this,ServerSettingActivity.class));
			}
		});

		if (SysConfig.IS_FIRST){
			showAutoStartDialog();
		}
	}

	public String md5(String string) {
		if (TextUtils.isEmpty(string)) {
			return "";
		}
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(string.getBytes());
			String result = "";
			for (byte b : bytes) {
				String temp = Integer.toHexString(b & 0xff);
				if (temp.length() == 1) {
					temp = "0" + temp;
				}
				result += temp;
			}
			return result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	/*登录*/
	private void login(String name,String password) {
		LoginEntity mLoginEntity = new LoginEntity();
		mLoginEntity.setUserName(name);
//		password =md5(password);
		Log.e("md5", "md5: "+password );
		mLoginEntity.setUserPwd(password);
		mLoginEntity.setChannel("app");
		Gson gson = new Gson();
		String json = gson.toJson(mLoginEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).login(body), new Subscriber<ResponseBody>() {

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
				pd.dismiss();
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					pd.dismiss();
					String result = responseBody.string();
					LoginResult mLoginResult = new Gson().fromJson(result,LoginResult.class);
					Log.e("result", "result: "+result );
					if(mLoginResult.getData().getCode().equals("400")){
					    Toast.makeText(LoginActivity.this,mLoginResult.getData().getMsg(),Toast.LENGTH_SHORT).show();
                    }else if (result.contains("非法访问")){
						Toast.makeText(LoginActivity.this,"非法访问,请求频率过高!",Toast.LENGTH_SHORT).show();
					}else if (mLoginResult.getData().getCode().equals("0")){
						isLogin = true;
                        LoginResult loginResult = new Gson().fromJson(result,LoginResult.class);
                        setData(SysContants.TOKENID, loginResult.getData().getTokenId());
                        setData(SysContants.ACCESSTOKEN, loginResult.getData().getAccessToken());
                        setData(SysContants.USERNAME,usernameEditText.getText().toString());
                        setData(SysContants.PASSWORD,passwordEditText.getText().toString());
                        SysConfig.workType = WorkType.WORK_TYPE_SHOT;
//					setData(SysContants.ISLOGIN, isLogin);
                        setData(SysContants.WORK_TYPE, SysConfig.workType);
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        Intent service_intent = new Intent(LoginActivity.this, ConnService.class);
                        startService(service_intent);
                        finish();
                    }else {
						Toast.makeText(LoginActivity.this,"登录失败!",Toast.LENGTH_SHORT).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void initPb() {
		pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
	}

	@Override
	protected void onDestroy() {
		if (pd!=null){
			pd.dismiss();
		}
		super.onDestroy();
	}


	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
	}

	private int keyBackClickCount = 0;

	@Override
	public void onBackPressed() {
		switch (keyBackClickCount++) {
		case 0:
			Toast.makeText(this,
					getResources().getString(R.string.press_again_exit),
					Toast.LENGTH_SHORT).show();
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					keyBackClickCount = 0;
				}
			}, 2000);
			break;
		case 1:
			setData(SysContants.WORK_TYPE, SysConfig.workType);
			setData(SysContants.ISFIRST, true);
			super.onBackPressed();
			break;
		default:
			break;
		}
	}


	// ---------------------------------------------------------------------------
	public String getData(String key, String defValue) {
		return mPreferences.getString(key, defValue);
	}

	public int getData(String key, int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	public long getData(String key, long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	public float getData(String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}
	protected void showMessage(String msg) {
		Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
	public boolean getData(String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	public void setData(String key, Object o) {
		if (o != null) {
			SharedPreferences.Editor editor = mPreferences.edit();
			if (o instanceof Boolean) {
				editor.putBoolean(key, (Boolean) o);
			} else if (o instanceof Integer) {
				editor.putInt(key, (Integer) o);
			} else if (o instanceof Long) {
				editor.putLong(key, (Long) o);
			} else if (o instanceof Float) {
				editor.putFloat(key, (Float) o);
			} else if (o instanceof String) {
				editor.putString(key, (String) o);
			}
			editor.commit();
		}
	}


	//----------------------------------------系统保活-----------------------------------------------

	@RequiresApi(Build.VERSION_CODES.M)
	public boolean isIgnoringBatteryOptimizations(Context context){
		boolean isIgnoring = false;
		PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

		if (powerManager != null)
			isIgnoring = powerManager.isIgnoringBatteryOptimizations("com.jpkh.cnpc");

		return isIgnoring;
	}

	public void requestIgnoreBatteryOptimizations(Context context){
		try {
			Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
			intent.setData(Uri.parse("package:" + "com.jpkh.cnpc"));
			context.startActivity(intent);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 跳转到指定应用的首页
	 */
	public void showActivity(String packageName,Context context){
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
	/**
	 *  跳转到指定应用的指定页面
	 * */
	public void showActivity(String packageName,String activityDir,Context context){
		Intent intent = new Intent();
		intent.setComponent(new ComponentName(packageName, activityDir));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	//判断手机厂商
	public String checkPhoneFirm(){
		String phoneState = Build.BRAND.toLowerCase(); //获取手机厂商
		if (phoneState.equals("huawei") || phoneState.equals("honor"))
			return SplashActivity.PhoneConstant.IS_HUAWEI;
		else if (phoneState.equals("xiaomi") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_XIAOMI;
		else if (phoneState.equals("oppo") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_OPPO;
		else if (phoneState.equals("vivo") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_VIVO;
		else if (phoneState.equals("meizu") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_MEIZU;
		else if (phoneState.equals("samsung") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_SAMSUNG;
		else if (phoneState.equals("letv") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_LETV;
		else if (phoneState.equals("smartisan") && Build.BRAND != null)
			return SplashActivity.PhoneConstant.IS_SMARTISAN;

		return "";
	}

	//前往设置管理
	public void  gotoWhiteListSetting(Context context){
		if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_HUAWEI)){
			try {
				showActivity("com.huawei.systemmanager","com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity",context);
			}catch (Exception e){
				showActivity("com.huawei.systemmanager",
						"com.huawei.systemmanager.optimize.bootstart.BootStartActivity",context);
			}
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_XIAOMI)){
			showActivity("com.miui.securitycenter",
					"com.miui.permcenter.autostart.AutoStartManagementActivity",context);
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_OPPO)){
			//oppo:操作步骤：权限隐私 -> 自启动管理 -> 允许应用自启动
			try {
				showActivity("com.coloros.phonemanager",context);
			} catch (Exception e) {
				try {
					showActivity("com.oppo.safe",context);
				} catch (Exception e2) {
					try {
						showActivity("com.coloros.oppoguardelf", context);
					} catch (Exception e3) {
						showActivity("com.coloros.safecenter", context);
					}
				}
			}
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_VIVO)){
			//vivo:操作步骤：权限管理 -> 自启动 -> 允许应用自启动
			showActivity("com.iqoo.secure", context);
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_MEIZU)){
			//魅族:操作步骤：权限管理 -> 后台管理 -> 点击应用 -> 允许后台运行
			showActivity("com.meizu.safe", context);
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_SAMSUNG)){
			//三星:操作步骤：自动运行应用程序 -> 打开应用开关 -> 电池管理 -> 未监视的应用程序 -> 添加应用
			try {
				showActivity("com.samsung.android.sm_cn",context);
			} catch (Exception e) {
				showActivity("com.samsung.android.sm",context);
			}
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_LETV)){
			//乐视:操作步骤：自启动管理 -> 允许应用自启动
			showActivity("com.letv.android.letvsafe","com.letv.android.letvsafe.AutobootManageActivity", context);
		}else if (checkPhoneFirm().equals(SplashActivity.PhoneConstant.IS_SMARTISAN)){
			//锤子:操作步骤：权限管理 -> 自启动权限管理 -> 点击应用 -> 允许被系统启动
			showActivity("com.smartisanos.security", context );
		}
	}

	Dialog dialog;
	/*进入自启动设置*/
	public void showAutoStartDialog() {
		dialog = DialogUtils.Alert(LoginActivity.this, "自启动", "建议您开启自启动和通知更好的接收消息！",
				new String[]{getString(R.string.ok), getString(R.string.cancel)},
				new View.OnClickListener[]{new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						//
						requestIgnoreBatteryOptimizations(LoginActivity.this);
						gotoWhiteListSetting(LoginActivity.this);
						setData(SysContants.FIRST_LOGIN, false);
						dialog.dismiss();
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
