package com.jpkh.cnpc.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatCheckBox;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.navi.model.NaviLatLng;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.InterFace.ITaskShot;
import com.jpkh.cnpc.activity.adapters.AlarmListAdapter;
import com.jpkh.cnpc.activity.adapters.AlarmTaskListAdapter;
import com.jpkh.cnpc.activity.adapters.ZhuangHaoAroundSelectAdapter;
import com.jpkh.cnpc.activity.entity.AlarmTaskEntity;
import com.jpkh.cnpc.activity.entity.AlarmTaskListEntity;
import com.jpkh.cnpc.activity.entity.AppImei;
import com.jpkh.cnpc.activity.entity.FirstEntity;
import com.jpkh.cnpc.activity.entity.HandleMsgEntity;
import com.jpkh.cnpc.activity.entity.ImeiEntity;
import com.jpkh.cnpc.activity.entity.MatchEntity;
import com.jpkh.cnpc.activity.entity.ProtectImeisEntity;
import com.jpkh.cnpc.activity.entity.RemarkEntity;
import com.jpkh.cnpc.activity.entity.SetLastestEntity;
import com.jpkh.cnpc.activity.entity.SetProEntity;
import com.jpkh.cnpc.activity.entity.StationEntity;
import com.jpkh.cnpc.activity.entity.StationInfoEntity;
import com.jpkh.cnpc.activity.utils.OpenServiceBroadcast;
import com.jpkh.cnpc.activity.utils.ShotTaskEntity;
import com.jpkh.cnpc.protocol.overlay.DrillPointOverlay;
import com.jpkh.cnpc.service.ConnService;
import com.jpkh.utils.zbar.CaptureActivity;
import com.jpkh.utils.zxing.camera.MipcaActivityCapture;
import com.jpkh.cnpc.activity.entity.SetEntity;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.activity.utils.DownLoadManager;
import com.jpkh.cnpc.activity.utils.FileUtils;
import com.jpkh.cnpc.activity.utils.UpdataInfoParser;
import com.jpkh.cnpc.broadcast.OpenMapBroadcast;
import com.jpkh.cnpc.broadcast.OpenMapBroadcast.IShowMapId;
import com.jpkh.cnpc.protocol.bean.AlarmInfo;
import com.jpkh.cnpc.protocol.bean.DrillPoint;
import com.jpkh.cnpc.protocol.bean.ShotPoint;
import com.jpkh.cnpc.protocol.bean.TaskPoint;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysConfig.WorkType;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.protocol.overlay.AlarmPointOverlay;
import com.jpkh.cnpc.protocol.overlay.ShotPointOverlay;
import com.jpkh.cnpc.protocol.overlay.WorkAreaOverlay;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.jpkh.utils.entity.UpdateInfo;
import com.jpkh.utils.entity.WorkAreaEntity;
import com.jpkh.utils.entity.WorkAreaPoint;
import com.robert.maps.applib.AlarmPoint;
import com.robert.maps.applib.MainPreferences;
import com.robert.maps.applib.downloader.AreaSelectorActivity;
import com.robert.maps.applib.downloader.FileDownloadListActivity;
import com.robert.maps.applib.kml.PoiListActivity;
import com.robert.maps.applib.kml.PoiManager;
import com.robert.maps.applib.kml.PoiPoint;
import com.robert.maps.applib.kml.TrackListActivity;
import com.robert.maps.applib.kml.XMLparser.PredefMapsParser;
import com.robert.maps.applib.overlays.MyLocationOverlay;
import com.robert.maps.applib.overlays.SearchResultOverlay;
import com.robert.maps.applib.overlays.TileOverlay;
import com.robert.maps.applib.tileprovider.TileProviderFileBase;
import com.robert.maps.applib.tileprovider.TileSource;
import com.robert.maps.applib.tileprovider.TileSourceBase;
import com.robert.maps.applib.utils.CrashReportHandler;
import com.robert.maps.applib.utils.DialogUtils;
import com.robert.maps.applib.utils.GpsCorrect;
import com.robert.maps.applib.utils.GpsCorrect.Gps;
import com.robert.maps.applib.utils.LogFileUtil;
import com.robert.maps.applib.utils.RException;
import com.robert.maps.applib.utils.SQLiteMapDatabase;
import com.robert.maps.applib.utils.SimpleThreadFactory;
import com.robert.maps.applib.utils.Ut;
import com.robert.maps.applib.view.IMoveListener;
import com.robert.maps.applib.view.MapView;
import com.robert.maps.applib.view.TileView;
import com.robert.maps.applib.view.TileViewOverlay;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.util.TypeConverter;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

@SuppressLint({"NewApi", "HandlerLeak"})
public class MainActivity extends SlidingFragmentActivity implements ITaskShot, IShowMapId{
	private SensorManager mOrientationSensorManager;
	public class RequestCode {
		public final static int MAPSET = 0x01;
		public final static int SCAN_BIND = 0x02;
		public final static int POIS = 0x03;
		public final static int COLLECT_POI = 0x04;
		public final static int IMPORT_POI = 0x05;
		public final static int TRACKS = 0x06;
		public final static int EDIT_TRACK = 0x07;
		public final static int IMPORT_TRACK = 0x08;
		public final static int POI_GUIDE = 0x09;
		public final static int TASK_GUIDE = 0x0a;
		public final static int STOP_GUIDE = 0x0b;
		public final static int SHOTPOINT_ISDONE = 0X0e;
		public final static int PROJECT_MAP = 0x0f;
		public final static int SEARCH = 0x10;
		public final static int OUTMAP = 0x11;
		public final static int START_TRACK = 0x12;
		public final static int PAUSE_TRACK = 0x13;
		public final static int STOP_TRACK = 0x14;
		public final static int CLOSE_TRACK = 0x15;
		public final static int STOP_GUIDING = 0x16;
		public final static int SCAN = 0x17;
		public final static int CHECKED = 0x18;
	}

	private static final String MAPNAME = "MapName";
	private static final String ACTION_SHOW_POINTS = "com.robert.maps.action.SHOW_POINTS";

	private MapView mMap;
	private ImageView iv_person, map_location, tv_is_login,tv_scan,iv_task,iv_scan; // ?????????????????????
	private TextView map_menu_search;
	//	private AppCompatCheckBox checkBox;
	private AppCompatCheckBox cb_zh;
	private AppCompatCheckBox cb_set;
	private TileSource mTileSource;
	private PoiManager mPoiManager;
	private Handler mCallbackHandler = new MainActivityCallbackHandler();
	private MoveListener mMoveListener = new MoveListener();
	private PowerManager.WakeLock myWakeLock;
	private SharedPreferences uiState;

	// Overlays
	private TileOverlay mTileOverlay = null;
	private boolean mShowOverlay = false;
	private String mMapId = null;
	private String mOverlayId = "";
	private MyLocationOverlay mMyLocationOverlay;
	private ShotPointOverlay mShotPointOverlay;
	private DrillPointOverlay mDrillPointOverlay;
	private WorkAreaOverlay mWorkAreaOverlay;
	private AlarmPointOverlay mAlarmPointOverlay;
	private boolean mAutoFollow = true;
	private String mGpsStatusName = "";
	private float mLastSpeed, mLastBearing;
	private boolean mCompassEnabled;
	private boolean mDrivingDirectionUp;
	private ExecutorService mThreadPool = Executors.newSingleThreadExecutor(new SimpleThreadFactory("MainActivity.Search"));
	public static boolean isChangeMap = false;

	private BroadcastReceiver gpsReceiver;
	ProgressDialog pd;    //??????????????????
	String ACTION_SHARE_POS = "action_share_pos";
	String ACTION_POINT_LOC = "action_point_loc";
	String REFUSH_STATUS_PHONE = "refush_status_phone";
	String CNPC_DATA_ALARM = "cnpc.data.alarm";
	String CNPC_LIST_ALARM = "cnpc.list.alarm";
	String CNPC_DATA_TASK = "cnpc.data.task";
	String CNPC_SET_INSERT = "cnpc_set_insert";
	//??????mlocationClient??????
	public AMapLocationClient locationClient;
	//??????mLocationOption??????
	public AMapLocationClientOption locationOption = null;
	String guidName = "";

	private SharedPreferences mPreferences;
	protected PointDBDao mPointDBDao;
	private ImageView iv_speach;
	private ImageView iv_search;
	private ImageView iv_close_voice;
	// ????????????????????????????????????true
	protected boolean enableOffline = false;
	private LinearLayout ll_search;
	private FrameLayout layout;
	private PopupWindow map_popupWindow;
	private TextView tv_online;
	private TextView tv_alarm_today;
	private TextView tv_alarm_now;
	private WorkAreaEntity mAreaEntity;
	private ImageView iv_map;
	String json;
	List<AlarmPoint> mAlarmList = new ArrayList<>();
	List<AlarmTaskEntity> alarmTaskArray = new ArrayList<>();
	FirstEntity firstEntity;
	Map<String,String> mPreferencesMap = new HashMap<>();
	AlarmInfo alarmInfo;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Window window = getWindow();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setNavigationBarColor(Color.TRANSPARENT);
		OpenMapBroadcast.setShowMapId(this);
		mPointDBDao = new PointDBDao(this);
		mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
		SysConfig.workType = getData(SysContants.WORK_TYPE, SysConfig.workType);
		initRightMenu();
		initMap();
		initWorkArea();
		initLocation();
		initAlarm(false);
		getIMEI();
		startLocation();
		// ????????????????????????
		if (!OpenStreetMapViewConstants.DEBUGMODE)
			CrashReportHandler.attach(this);
		mOrientationSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		CreateContentView();
		initSet();
		initPopu();
		mPoiManager = new PoiManager(this);
		mMap.setMoveListener(mMoveListener);
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		uiState = getPreferences(Activity.MODE_PRIVATE);
		// Init
		mCompassEnabled = uiState.getBoolean("CompassEnabled", false);
		mAutoFollow = uiState.getBoolean("AutoFollow", true);

		mMap.getController().setCenter(new GeoPoint(uiState.getInt("Latitude", 39909604), uiState.getInt("Longitude", 116397228)));
//		mGPSFastUpdate = pref.getBoolean("pref_gpsfastupdate", true);
		mAutoFollow = uiState.getBoolean("AutoFollow", true);
		setAutoFollow(mAutoFollow, true);

		this.mMyLocationOverlay = new MyLocationOverlay(this);
//		this.mShotPointOverlay = new ShotPointOverlay(this, mPointDBDao, null, false);
		mDrillPointOverlay = new DrillPointOverlay(MainActivity.this,mPointDBDao,null,false);
		this.mAlarmPointOverlay = new AlarmPointOverlay(this,null);
		//addWorkArea();????????????
		// loadOverlay
		FillOverlays();
		checkOnlineSetUpdate();
		mDrivingDirectionUp = pref.getBoolean("pref_drivingdirectionup", true);

		final int screenOrientation = Integer.parseInt(pref.getString("pref_screen_orientation", "-1"));
		setRequestedOrientation(screenOrientation);

		final boolean fullScreen = pref.getBoolean("pref_showstatusbar", true);
		if (fullScreen)
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		else
			getWindow()
					.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


		// GPS????????????
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
		intentFilter.addAction(ACTION_SHARE_POS);
		intentFilter.addAction(ACTION_POINT_LOC);
		intentFilter.addAction(REFUSH_STATUS_PHONE);
		intentFilter.addAction(CNPC_DATA_ALARM);
		intentFilter.addAction(CNPC_LIST_ALARM);
		intentFilter.addAction(CNPC_DATA_TASK);
		intentFilter.addAction(CNPC_SET_INSERT);
		gpsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(REFUSH_STATUS_PHONE)) {
					refreshViews();
				} else if (intent.getAction().equals(ACTION_POINT_LOC)) {
					String stationNo = intent.getStringExtra("stationNo");
					int type = intent.getIntExtra("type",0);
					Log.e("type", "type: "+type );
					TaskPoint taskPoint = null;
					if (type==0){
						taskPoint = mPointDBDao.selectShotPointTotaskPoint(stationNo);
					}else if (type==1){
						taskPoint = mPointDBDao.selectDrillPointToTaskPoint(stationNo);
//						Log.e("taskPoint", "taskPoint: "+taskPoint.lineNo+"|"+taskPoint.spointNo+"|"+taskPoint.stationNo+"|"+taskPoint.Id);
					}
					if (taskPoint != null && !"".equals(taskPoint.stationNo)) {
						mMap.getController().setCenter(taskPoint.geoPoint);
						if (type==0) {
							if (mShotPointOverlay!=null){
								mMap.getTileView().mShotMenuInfo.EventGeoPoint = taskPoint.geoPoint;
								mMap.getTileView().mShotMenuInfo.MarkerIndex = taskPoint.Id;
								mMap.getTileView().mShotMenuInfo.stationNo = taskPoint.stationNo;
								mShotPointOverlay.setTapIndex(taskPoint.Id);
							}else {
								Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
							}

						}else if (type==1){
							if (mDrillPointOverlay!=null){
								mMap.getTileView().mDrillMenuInfo.EventGeoPoint = taskPoint.geoPoint;
								mMap.getTileView().mDrillMenuInfo.MarkerIndex = taskPoint.Id;
								mMap.getTileView().mDrillMenuInfo.stationNo = taskPoint.stationNo;
								mDrillPointOverlay.setTapIndex(taskPoint.Id);
							}else {
								Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
							}
						}
						mMap.getTileView().showContextMenu();
						mMap.Refresh();

					}
				}else if (intent.getAction().equals(CNPC_DATA_ALARM)){
					AlarmInfo alarmInfo = (AlarmInfo) intent.getSerializableExtra("alarmInfo");
					if (alarmInfo!=null){
						GeoPoint geoAlarm = GeoPoint.from2DoubleString(alarmInfo.getLat(),alarmInfo.getLng());
						showAlarm(alarmInfo);//??????????????????
						mMap.getController().setCenter(geoAlarm);
					}
					initSet();//??????????????????
				}else if (intent.getAction().equals(CNPC_LIST_ALARM)){
					AlarmPoint mAlarmPoint = (AlarmPoint) intent.getSerializableExtra("alarmPoint");
					if (mAlarmPoint!=null){
						if (mAlarmPointOverlay!=null){
							mMap.getTileView().mAlarmMenuInfo.EventGeoPoint = mAlarmPoint.getPoint();
							mMap.getTileView().mAlarmMenuInfo.mAlarmPoint = mAlarmPoint;
							mMap.getTileView().showContextMenu();
						}
						mMap.getController().setCenter(mAlarmPoint.getPoint());
					}
				}else if (intent.getAction().equals(CNPC_DATA_TASK)){
					AlarmTaskEntity mAlarmTaskEntity = (AlarmTaskEntity) intent.getSerializableExtra("task");
					if (mAlarmTaskEntity!=null){
						alarmTaskArray.add(mAlarmTaskEntity);
					}
				}else if (intent.getAction().equals(CNPC_SET_INSERT)){
					int status = intent.getIntExtra("status",1);
					if (status==0){
						Toast.makeText(MainActivity.this,"??????????????????????????????",Toast.LENGTH_LONG).show();
					}
					if (progressDialog!=null && progressDialog.isShowing()){
						progressDialog.dismiss();
					}
					FillOverlays();
					mMap.Refresh();
				}
			}
		};
		registerReceiver(gpsReceiver, intentFilter);

		if (!uiState.getString("app_version", "").equalsIgnoreCase(Ut.getAppVersion(this))) {
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
		}

		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();

		if (ACTION_SHOW_POINTS.equalsIgnoreCase(queryAction)) {
			ActionShowPoints(queryIntent);
		} else if (Intent.ACTION_VIEW.equalsIgnoreCase(queryAction)) {
			Uri uri = queryIntent.getData();
			if (uri.getScheme().equalsIgnoreCase("geo")) {
				final String latlon = uri.getEncodedSchemeSpecificPart().replace("?" + uri.getEncodedQuery(), "");
				if (latlon.equals("0,0")) {

				} else {
					GeoPoint point = GeoPoint.fromDoubleString(latlon);
					setAutoFollow(false);
					mMap.getController().setCenter(point);
				}
			}
		} else if ("SHOW_MAP_ID".equalsIgnoreCase(queryAction)) {
			final Bundle bundle = queryIntent.getExtras();
			mMapId = bundle.getString(MAPNAME);
			if (bundle.containsKey("center")) {
				try {
					final GeoPoint geo = GeoPoint.fromDoubleString(bundle.getString("center"));
					mMap.getController().setCenter(geo);
				} catch (Exception e) {
				}
			}

			if (bundle.containsKey("zoom")) {
				try {
					final int zoom = Integer.valueOf(bundle.getString("zoom"));
					mMap.getController().setZoom(zoom);
					Editor editor = uiState.edit();
					editor.putInt("ZoomLevel", mMap.getZoomLevel());
					editor.commit();
				} catch (Exception e) {
				}
			}
			queryIntent.setAction("");
		}
//		checkVersion();//????????????
	}

	private void checkOnlineSetUpdate() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("???????????????...");
		progressDialog.setCancelable(false);
		if (progressDialog!=null){
			progressDialog.show();
		}
	}

	/*??????????????????*/
	private void showAlarmTask(AlarmTaskListEntity mAlarmList) {

		final AlarmTaskListAdapter adapter1 = new AlarmTaskListAdapter(
				MainActivity.this, mAlarmList);
		if (popupWindow!=null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
		popupWindow = new PopupWindow();
		int height_pop = getWindowManager().getDefaultDisplay().getHeight();
		int width_pop = getWindowManager().getDefaultDisplay().getWidth();;
		popupWindow.setHeight(height_pop*3/4);
		popupWindow.setWidth(width_pop*9/10);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pop_up);
		animation.setFillAfter(true);//android?????????????????????????????????
		View view = LayoutInflater.from(this).inflate(R.layout.layout_alarm_list,null);
		popupWindow.setContentView(view);
		popupWindow.showAtLocation(findViewById(R.id.tv_main),Gravity.CENTER,0,0);
		view.startAnimation(animation);
		openPopu();
		ListView lv_alarm = view.findViewById(R.id.lv_alarm);
		TextView tv_title = view.findViewById(R.id.tv_title);
		ImageView iv_close = view.findViewById(R.id.iv_close);
		tv_title.setText("????????????");
		lv_alarm.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv_alarm.setAdapter(adapter1);

		lv_alarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				closePopup(false);
				Intent intent = new Intent(ACTION_POINT_LOC);
				intent.putExtra("stationNo",mAlarmList.getData().get(position).getAlarm().getDeviceName());
				intent.putExtra("type",1);
				sendBroadcast(intent);
			}
		});

		iv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

//???????????????
		/*Collections.sort(mAlarmList.getData(), new Comparator<AlarmTaskListEntity.DataBean>() {
			public int compare(AlarmTaskListEntity.DataBean o1, AlarmTaskListEntity.DataBean o2) {
				if (o1.getAlarm().getAlarmTime() == null || o2.getAlarm().getAlarmTime() == null)
					return 0;
				return o2.getAlarm().getAlarmTime().compareTo(o1.getAlarm().getAlarmTime());
			}});*/

		// ??????????????????
		/*int defIndex = 0;
		adapter1.setSelectedItem(defIndex);
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(adapter1, defIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								adapter1.setSelectedItem(which);
								adapter1.notifyDataSetChanged();
								adapter1.notifyDataSetInvalidated();
							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {

								Intent intent = new Intent(ACTION_POINT_LOC);
								intent.putExtra("stationNo",mAlarmList.getData().get(adapter1.getSelectedItem()).getAlarm().getDeviceName());
								intent.putExtra("type",1);
								sendBroadcast(intent);
							}
						}).setNegativeButton(R.string.cancel, null).create()
				.show();*/

	}
	/*?????????????????????*/
	private void initSet() {
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).getBasicInfo(mPreferencesMap), new Subscriber<ResponseBody>() {

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(MainActivity.this,"????????????????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					String result = responseBody.string();
					Log.e("onNext", "??????????????????: "+result);
					SetEntity setEntity =  new Gson().fromJson(result,SetEntity.class);
					tv_online.setText(setEntity.getData().getCountImei()+"");
					tv_alarm_today.setText(setEntity.getData().getCountTodayAlarm()+"");
					tv_alarm_now.setText(setEntity.getData().getCountAlarmDevice()+"");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	/*???????????????????????????*/
	private void initAlarm(boolean isShow) {
		mAlarmList.clear();
		String tokenId = mPreferences.getString(SysContants.TOKENID,"");
		String accessToken = mPreferences.getString(SysContants.ACCESSTOKEN,"");
		mPreferencesMap.put("tokenId",tokenId);
		mPreferencesMap.put("accessToken",accessToken);
		Log.e("tokenId", "tokenId: "+tokenId);
		Log.e("accessToken", "accessToken: "+accessToken );
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).firstGetAllImeis(mPreferencesMap), new Subscriber<ResponseBody>() {

			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(MainActivity.this,"?????????????????????????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					String result = responseBody.string();
					Log.e("onNext", "?????????????????????: "+result);
					firstEntity = new Gson().fromJson(result,FirstEntity.class);
					for (int i=0;i<firstEntity.getData().size();i++){
						AlarmPoint alarmPoint = new AlarmPoint();
						alarmPoint.setId(new Random().nextInt(1000000000));
						alarmPoint.setIds(firstEntity.getData().get(i).getId());
						alarmPoint.setAddress(firstEntity.getData().get(i).getAddress());
						alarmPoint.setAlarmName(firstEntity.getData().get(i).getAlarmName());
						alarmPoint.setAlarmTime(firstEntity.getData().get(i).getAlarmTime());
						alarmPoint.setAlarmType(firstEntity.getData().get(i).getAlarmType());
						alarmPoint.setDeviceName(firstEntity.getData().get(i).getDeviceName());
						if(firstEntity.getData().get(i).getStation()==null || firstEntity.getData().get(i).getStation().equals("")){
							alarmPoint.setPoint(GeoPoint.from2DoubleString(firstEntity.getData().get(i).getLat(),firstEntity.getData().get(i).getLng()));
						}else {
							alarmPoint.setPoint(GeoPoint.from2DoubleString(firstEntity.getData().get(i).getStation_lat(),firstEntity.getData().get(i).getStation_lng()));
						}
						alarmPoint.setImei(firstEntity.getData().get(i).getImei());
						alarmPoint.setHandleStatus(firstEntity.getData().get(i).getHandleStatus());
						alarmPoint.setStation(firstEntity.getData().get(i).getStation());
						alarmPoint.setStation_lat(firstEntity.getData().get(i).getStation_lat());
						alarmPoint.setStation_lng(firstEntity.getData().get(i).getStation_lng());
						mAlarmList.add(alarmPoint);
					}
					mAlarmPointOverlay = null;
					mAlarmPointOverlay = new AlarmPointOverlay(MainActivity.this,null);
					FillOverlays();
					mAlarmPointOverlay.refushAlarmPoint(mAlarmList,mMap.getTileView());
					if (isShow){
						if (mAlarmList.size()!=0){
							showAlarmDlg();
						}else {
							Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_SHORT).show();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
	}

	/*????????????????????????*/
	public void getTaskNotRc(){
		AppImei mAppImei = new AppImei();
		mAppImei.setAppImei(appImei);
		Gson gson = new Gson();
		String json = gson.toJson(mAppImei);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).getMessageHasNotRecieve(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					String result = responseBody.string().toString();
					Log.e("getMessageHasNotRecieve", "getMessageHasNotRecieve: "+result);
					AlarmTaskListEntity mTaskEntity = new Gson().fromJson(result,AlarmTaskListEntity.class);
					if (mTaskEntity.getData().size()>0){
						showAlarmTask(mTaskEntity);
					}else {
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	String appImei = "";
	/*??????android ????????????*/
	public void getIMEI() {
		try {
			//??????android ????????????
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

	public String getAppImei(){
		return appImei;
	}
	/*????????????????????????*/
	private void addWorkArea(){
		if (mWorkAreaOverlay==null){
			mWorkAreaOverlay = new WorkAreaOverlay(this);
			if (mAreaEntity!=null && mAreaEntity.getWork_area()!=null){
				for (int i=0;i<mAreaEntity.getWork_area().size();i++){
					mWorkAreaOverlay.addPoint(new WorkAreaPoint(i+"",GeoPoint.fromDouble(mAreaEntity.getWork_area().get(i).getLat(),mAreaEntity.getWork_area().get(i).getLon())),mMap.getTileView());
				}
			}
		}

	}

	/*?????????????????????????????????*/
	private void showAlarm(AlarmInfo alarmInfo){
		//...
		AlarmPoint alarmPoint = new AlarmPoint();
		alarmPoint.setId(new Random().nextInt(1000000000));
		alarmPoint.setIds(alarmInfo.getId());
		alarmPoint.setAddress(alarmInfo.getAddress());
		alarmPoint.setAlarmName(alarmInfo.getAlarmName());
		alarmPoint.setAlarmTime(alarmInfo.getAlarmTime());
		alarmPoint.setAlarmType(alarmInfo.getAlarmType());
		alarmPoint.setDeviceName(alarmInfo.getDeviceName());
		alarmPoint.setPoint(GeoPoint.from2DoubleString(alarmInfo.getLat(),alarmInfo.getLng()));
		alarmPoint.setImei(alarmInfo.getImei());
		alarmPoint.setHandleStatus(alarmInfo.getHandleStatus());
		alarmPoint.setStation(alarmInfo.getStation());
		alarmPoint.setStation_lat(alarmInfo.getStation_lat());
		alarmPoint.setStation_lng(alarmInfo.getStation_lng());
		mAlarmList.add(alarmPoint);
		mAlarmPointOverlay.refushAlarmPoint(mAlarmList,mMap.getTileView());
		if (alarmListAdapter!=null){
			alarmListAdapter.notifyDataSetChanged();
		}
		//??????????????????
		if (mAlarmPointOverlay!=null){
			closePopup(false);
			mMap.getTileView().mAlarmMenuInfo.EventGeoPoint = alarmPoint.getPoint();
			mMap.getTileView().mAlarmMenuInfo.mAlarmPoint = alarmPoint;
			mMap.getTileView().showContextMenu();
		}
	}

	//???????????????????????????
	private void initWorkArea() {
		//project/private/points/input
		String path = Ut.getRMapsProjectPrivatePointsInputDir(this).getPath()+"/area.txt";
		File areaFile = new File(path);
		json = FileUtils.readTxt(path);
		Log.e("initWorkArea", "initWorkArea: "+json );
		if (json!=null && areaFile.exists()){
//			mAreaEntity = JSON.parseObject(json,WorkAreaEntity.class);
			mAreaEntity = new Gson().fromJson(json,WorkAreaEntity.class);
		}
	}

	private static int makeDropDownMeasureSpec(int measureSpec) {
		int mode;
		if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
			mode = View.MeasureSpec.UNSPECIFIED;
		} else {
			mode = View.MeasureSpec.EXACTLY;
		}
		return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
	}

	/*private void initPopu() {
		//????????????
		layout = (FrameLayout) LayoutInflater.from(MainActivity.this).inflate(
				R.layout.layout_guide, null);
		tv_title = layout.findViewById(R.id.tv_title);
		tv_content = layout.findViewById(R.id.tv_content);
		iv_close_voice = layout.findViewById(R.id.iv_close_voice);
		iv_close_voice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				voice_popupWindow.dismiss();
				stop();
			}
		});
		// ?????????popupWindow
		voice_popupWindow = new PopupWindow(layout, getWindowManager().getDefaultDisplay().getWidth()*9/10,350);
		View contentView = voice_popupWindow.getContentView();
		//??????????????????PopupWindow???????????????????????????0
		contentView.measure(makeDropDownMeasureSpec(voice_popupWindow.getWidth()),
				makeDropDownMeasureSpec(voice_popupWindow.getHeight()));
		voice_popupWindow.setOutsideTouchable(false);
	}*/

	/*private void initVoice() {
		iv_speach = (ImageView) findViewById(R.id.iv_speach);
		//?????????????????????????????????AnimationDrawable
		AnimationDrawable animationDrawable = (AnimationDrawable) iv_speach.getBackground();
		//?????????????????????
		if(!animationDrawable.isRunning()){
			//???????????????
			animationDrawable.start();
		}
		// ??????sdk??????1.1 ?????????EventManager??????
		asr = EventManagerFactory.create(this, "asr");
		// ??????sdk??????1.3 ??????????????????????????????
		asr.registerListener(this); //  EventListener ??? onEvent??????
		iv_speach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				start();
//				int offsetX = Math.abs(voice_popupWindow.getContentView().getMeasuredWidth()-ll_search.getWidth()) / 2;
				int offsetX = Math.abs(getWindowManager().getDefaultDisplay().getWidth()-voice_popupWindow.getContentView().getMeasuredWidth()) / 2;
				int offsetY = 10;
//				PopupWindowCompat.showAsDropDown(voice_popupWindow, ll_search, offsetX, offsetY, Gravity.START);//??????????????????
				PopupWindowCompat.showAsDropDown(voice_popupWindow, findViewById(R.id.iv_person), offsetX, offsetY, Gravity.START);//??????????????????
			}
		});


		if (enableOffline) {
			loadOfflineEngine(); // ??????????????????????????????, ?????? ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH ???????????????
		}
	}*/



	private void initRightMenu() {
		Fragment leftMenuFragment = new PersonalFragment();
		setBehindContentView(R.layout.left_menu_frame);
		getSupportFragmentManager().beginTransaction().replace(R.id.id_left_menu_frame, leftMenuFragment).commit();
		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		// ???????????????????????????
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		// ?????????????????????????????????
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// ??????????????????????????????
		menu.setFadeDegree(0.35f);
		menu.setSecondaryShadowDrawable(R.drawable.shadow);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//??????????????????
	}


	public void showMenu() {
		getSlidingMenu().showMenu();
	}

	public void closeMenu(){
		getSlidingMenu().toggle();
	}


	/**
	 * ???????????????
	 *
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void initLocation() {
		//?????????client
		locationClient = new AMapLocationClient(this.getApplicationContext());
		locationOption = getDefaultOption();
		//??????????????????
		locationClient.setLocationOption(locationOption);
		// ??????????????????
		locationClient.setLocationListener(locationListener);

	}

	/**
	 * ?????????????????????
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private AMapLocationClientOption getDefaultOption() {
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//????????????????????????????????????????????????????????????????????????????????????????????????????????????
		mOption.setGpsFirst(false);//?????????????????????gps??????????????????????????????????????????????????????
		mOption.setHttpTimeOut(30000);//???????????????????????????????????????????????????30?????????????????????????????????
		mOption.setInterval(2000);//???????????????????????????????????????2???
		mOption.setNeedAddress(true);//????????????????????????????????????????????????????????????true
		mOption.setOnceLocation(false);//?????????????????????????????????????????????false
		mOption.setOnceLocationLatest(false);//???????????????????????????wifi??????????????????false.???????????????true,?????????????????????????????????????????????????????????
		AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//????????? ????????????????????????????????????HTTP??????HTTPS????????????HTTP
		mOption.setSensorEnable(false);//????????????????????????????????????????????????false
		mOption.setWifiScan(true); //???????????????????????????wifi??????????????????true??????????????????false??????????????????????????????????????????????????????????????????????????????????????????????????????
		mOption.setLocationCacheEnable(true); //???????????????????????????????????????????????????true
		mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//??????????????????????????????????????????????????????????????????????????????????????????????????????
		return mOption;
	}

	/**
	 * ????????????
	 */
	boolean isGaode = false;
	AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation location) {
			if (null != location) {
				location_x = location.getLatitude();
				location_y = location.getLongitude();
				Gps gps = GpsCorrect.GCJ02_TO_GPS84(new Gps(location.getLatitude(), location.getLongitude()));
				location.setLatitude(gps.lat);
				location.setLongitude(gps.lon);
				unRectifyLocation = location;
				mMyLocationOverlay.setLocation(location);
				mGpsStatusName = location.getProvider(); // + " 2 " + (cnt >= 0 ? cnt : 0);
//			setTitle(); // ?????????????????????

				mLastSpeed = location.getSpeed();
//			LogFileUtil.saveFileToSDCard("Main:latitude" + loc.getLatitude() + " longitude:" + loc.getLongitude());

				if (location != null && location.getLatitude() != 0) {
					if (isTracked) {
						trackIndex++;
					}
					if (mAutoFollow) {
//					if (mDrivingDirectionUp)
//						if (loc.getSpeed() > 0.5)
//							mMap.setBearing(loc.getBearing());
						// ???????????????????????????
						mMap.getController().setCenter(TypeConverter.locationToGeoPoint(location));
						if (isTracked) {
							if (trackIndex == 5) {
								if (Build.VERSION.SDK_INT != 23) {
									mMap.Refresh();
								}
								trackIndex = 0;
							}
						}
					} else {
						if (isTracked) {
							if (trackIndex == 5) {
								if (Build.VERSION.SDK_INT != 23) {
								}
								trackIndex = 0;
							}
						}
						// ????????????
						mMap.Refresh();
					}
				}
				setTitle();


				StringBuffer sb = new StringBuffer();
				//errCode??????0????????????????????????????????????????????????????????????????????????????????????????????????
				if (location.getErrorCode() == 0) {
					sb.append("????????????" + "\n");
					sb.append("????????????: " + location.getLocationType() + "\n");
					sb.append("???    ???    : " + location.getLongitude() + "\n");
					sb.append("???    ???    : " + location.getLatitude() + "\n");
					sb.append("???    ???    : " + location.getAccuracy() + "???" + "\n");
					sb.append("?????????    : " + location.getProvider() + "\n");

					sb.append("???    ???    : " + location.getSpeed() + "???/???" + "\n");
					sb.append("???    ???    : " + location.getBearing() + "\n");
					// ?????????????????????????????????????????????
					sb.append("???    ???    : " + location.getSatellites() + "\n");
					sb.append("???    ???    : " + location.getCountry() + "\n");
					sb.append("???            : " + location.getProvince() + "\n");
					sb.append("???            : " + location.getCity() + "\n");
					sb.append("???????????? : " + location.getCityCode() + "\n");
					sb.append("???            : " + location.getDistrict() + "\n");
					sb.append("?????? ???   : " + location.getAdCode() + "\n");
					sb.append("???    ???    : " + location.getAddress() + "\n");
					sb.append("?????????    : " + location.getPoiName() + "\n");
					//?????????????????????
//					sb.append("????????????: " + Utils.formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
				} else {
					//????????????
					sb.append("????????????" + "\n");
					sb.append("?????????:" + location.getErrorCode() + "\n");
					sb.append("????????????:" + location.getErrorInfo() + "\n");
					sb.append("????????????:" + location.getLocationDetail() + "\n");
				}
				sb.append("***??????????????????***").append("\n");
				sb.append("* WIFI?????????").append(location.getLocationQualityReport().isWifiAble() ? "??????" : "??????").append("\n");
				sb.append("* GPS?????????").append(getGPSStatusString(location.getLocationQualityReport().getGPSStatus())).append("\n");
				sb.append("* GPS?????????").append(location.getLocationQualityReport().getGPSSatellites()).append("\n");
				sb.append("* ???????????????" + location.getLocationQualityReport().getNetworkType()).append("\n");
				sb.append("* ???????????????" + location.getLocationQualityReport().getNetUseTime()).append("\n");
				sb.append("****************").append("\n");
				address = location.getAddress();
				//???????????????????????????
//				sb.append("????????????: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

				//?????????????????????
				String result = sb.toString();


//				Log.e("result", result);
			} else {
//				tvResult.setText("???????????????loc is null");
				Log.e("result", "???????????????loc is null");
			}
		}
	};

	/**
	 * ??????GPS??????????????????
	 * @param statusCode GPS?????????
	 * @return
	 */
	private String getGPSStatusString(int statusCode) {
		String str = "";
		switch (statusCode) {
			case AMapLocationQualityReport.GPS_STATUS_OK:
				str = "GPS????????????";
				break;
			case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
				str = "???????????????GPS Provider???????????????GPS??????";
				break;
			case AMapLocationQualityReport.GPS_STATUS_OFF:
				str = "GPS?????????????????????GPS?????????????????????";
				break;
			case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
				str = "?????????????????????????????????GPS???????????????????????????GPS????????????????????????????????????";
				break;
			case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
				str = "??????GPS???????????????????????????gps????????????";
				break;
		}
		return str;
	}

	// ????????????????????????????????????????????????
	private void resetOption() {
		// ????????????????????????????????????
		locationOption.setNeedAddress(true);
		/**
		 * ????????????????????????GPS?????????????????????30??????GPS?????????????????????????????????????????????
		 * ??????????????????????????????????????????????????????????????????????????????
		 */
		locationOption.setGpsFirst(true);
		// ????????????????????????
		locationOption.setLocationCacheEnable(false);
		// ????????????????????????
		locationOption.setOnceLocation(false);
		//????????????????????????wifi????????????????????????true,?????????????????????????????????????????????????????????
		locationOption.setOnceLocationLatest(false);
		//???????????????????????????
		locationOption.setSensorEnable(true);
		//??????????????????wifi????????????????????????false?????????????????????????????????????????????????????????????????????????????????????????????????????????
		String strInterval = "1000";
		if (!TextUtils.isEmpty(strInterval)) {
			try {
				// ???????????????????????????????????????,????????????1000???????????????1000?????????1000???
				locationOption.setInterval(Long.valueOf(strInterval));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		String strTimeout = "30000";
		if (!TextUtils.isEmpty(strTimeout)) {
			try {
				// ??????????????????????????????
				locationOption.setHttpTimeOut(Long.valueOf(strTimeout));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ????????????
	 *
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void startLocation() {
		//????????????????????????????????????????????????
		resetOption();
		// ??????????????????
		locationClient.setLocationOption(locationOption);
		// ????????????
		locationClient.startLocation();
	}

	/**
	 * ????????????
	 *
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private void stopLocation() {
		// ????????????
		locationClient.stopLocation();
	}

	private void destroyLocation() {
		if (null != locationClient) {
			/**
			 * ??????AMapLocationClient????????????Activity???????????????
			 * ???Activity???onDestroy??????????????????AMapLocationClient???onDestroy
			 */
			locationClient.onDestroy();
			locationClient = null;
			locationOption = null;
		}
	}

	/**
	 * DENSITY_LOW??=??120
	 * DENSITY_MEDIUM??=??160????//?????????????
	 * DENSITY_TV??=??213????????????//TV??????????
	 * DENSITY_HIGH??=??240????
	 * DENSITY_XHIGH??=??320????
	 * DENSITY_400??=??400????
	 * DENSITY_XXHIGH??=??480????
	 * DENSITY_XXXHIGH??=??640??
	 */
	private void initMap() {
		int mapSize = 256;
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		switch (screenDensity) {
			case DisplayMetrics.DENSITY_LOW:
				mapSize = 120;
				break;
			case DisplayMetrics.DENSITY_MEDIUM:
				mapSize = 160;
				break;
			case DisplayMetrics.DENSITY_HIGH:
				mapSize = 240;
				break;
			default:
				mapSize = 320;
				break;
		}
		TileSourceBase.MAPTILE_SIZEPX = getData("tilesize", mapSize);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		final String queryAction = intent.getAction();
		if (ACTION_SHOW_POINTS.equalsIgnoreCase(queryAction)) {
			ActionShowPoints(intent);
		} else if ("SHOW_MAP_ID".equalsIgnoreCase(queryAction)) {
			final Bundle bundle = intent.getExtras();
			mMapId = bundle.getString(MAPNAME);
			if (bundle.containsKey("center")) {
				try {
					final GeoPoint geo = GeoPoint.fromDoubleString(bundle.getString("center"));
					mMap.getController().setCenter(geo);
				} catch (Exception e) {
				}
			}
			if (bundle.containsKey("zoom")) {
				try {
					final int zoom = Integer.valueOf(bundle.getString("zoom"));
					mMap.getController().setZoom(zoom);
					SharedPreferences uiState = getPreferences(Activity.MODE_PRIVATE);
					Editor editor = uiState.edit();
					editor.commit();
					editor.putInt("ZoomLevel", mMap.getZoomLevel());
				} catch (Exception e) {
				}
			}
		}else if ("notice_clear".equals(queryAction)){
			ConnService.no_count = 0;
		}
	}

	// ??????????????????
	private void CreateContentView() {
		setContentView(R.layout.main);
		// ??????????????????
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		final int sideBottom = Integer.parseInt(pref.getString("pref_zoomctrl", "1"));
		final boolean showTitle = pref.getBoolean("pref_showtitle", true);
		final boolean showAutoFollow = pref.getBoolean("pref_show_autofollow_button", true);

		if (!showTitle)
			findViewById(R.id.screen).setVisibility(View.GONE);
		// ??????
		mMap = (MapView) findViewById(R.id.map_area);
		iv_person = (ImageView) findViewById(R.id.iv_person);
		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_map = (ImageView) findViewById(R.id.iv_map);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		ll_search = (LinearLayout) findViewById(R.id.ll_search);
//		checkBox = (AppCompatCheckBox) findViewById(R.id.map_workarea);
		iv_task = (ImageView) findViewById(R.id.iv_task);
		tv_alarm_now = (TextView) findViewById(R.id.tv_alarm_now);
		tv_alarm_today = (TextView) findViewById(R.id.tv_alarm_today);
		tv_online = (TextView) findViewById(R.id.tv_online);
		cb_zh = (AppCompatCheckBox) findViewById(R.id.cb_zh);
		cb_set = (AppCompatCheckBox) findViewById(R.id.cb_set);

		iv_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showMenu();
			}
		});

		/*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Log.e("onCheckedChanged", "onCheckedChanged" );
				if (isChecked){
					addWorkArea();
					FillOverlays();
					mMap.Refresh();
				}else {
					mWorkAreaOverlay = null;
					FillOverlays();
					mMap.Refresh();
				}
			}
		});*/

		cb_zh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					if (mShotPointOverlay == null) {
						mShotPointOverlay = new ShotPointOverlay(MainActivity.this, mPointDBDao, null, false);
					}
					FillOverlays();
					mMap.Refresh();
				}else {
					mShotPointOverlay=null;
					FillOverlays();
					mMap.Refresh();
				}
			}
		});

		cb_set.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked){
					if (mDrillPointOverlay == null){
						mDrillPointOverlay = new DrillPointOverlay(MainActivity.this,mPointDBDao,null,false);
					}
					FillOverlays();
					mMap.Refresh();

				}else {
					mDrillPointOverlay = null;
					FillOverlays();
					mMap.Refresh();
				}
			}
		});

		iv_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				map_popupWindow.showAsDropDown(iv_map,110,0);
			}
		});

		iv_scan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
				intent.putExtra("stationNo","");
				startActivityForResult(intent,RequestCode.SCAN_BIND);
			}
		});


		// ????????????
		tv_is_login = (ImageView) findViewById(R.id.tv_is_login);

		iv_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initAlarm(true);
			}
		});

		iv_task.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getTaskNotRc();
			}
		});
		refreshViews();
		/*map_zoom_in = (ImageView) findViewById(R.id.map_zoom_in);
		map_zoom_in.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMap.getTileView().setZoomLevel(mMap.getTileView().getZoomLevel() + 1);
				if (mMoveListener != null)
					mMoveListener.onZoomDetected();
			}
		});
		map_zoom_in.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
				final int zoom = Integer.parseInt(pref.getString("pref_zoommaxlevel", "17"));
				if (zoom > 0) {
					mMap.getTileView().setZoomLevel(zoom - 1);
					if (mMoveListener != null)
						mMoveListener.onZoomDetected();
				}
				return true;
			}
		});
		map_zoom_full = (ImageView) findViewById(R.id.map_zoom_full);
		map_zoom_full.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (SysConfig.isOnlineMap) {
					mMap.getController().setZoom(12);
					mMap.Refresh();
				} else {
					String name = mTileSource.ID;
					try {
						File folder = Ut.getRMapsMapsDir(MainActivity.this);
						SQLiteMapDatabase cacheDatabase = new SQLiteMapDatabase();
						cacheDatabase.setFile(folder.getAbsolutePath() + "/" + name);
						int[] zooms = cacheDatabase.getZoom();
						if (zooms != null) {
							TileProviderFileBase provider = new TileProviderFileBase(MainActivity.this);
							mMap.getController().setZoom(zooms[0]);
							mMap.Refresh();
							provider.Free();
						}
					} catch (Exception e) {
					}
				}

			}
		});
		map_zoom_out = (ImageView) findViewById(R.id.map_zoom_out);
		map_zoom_out.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMap.getTileView().setZoomLevel(mMap.getTileView().getZoomLevel() - 1);
				if (mMoveListener != null)
					mMoveListener.onZoomDetected();
			}
		});
		map_zoom_out.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
				final int zoom = Integer.parseInt(pref.getString("pref_zoomminlevel", "10"));
				if (zoom > 0) {
					mMap.getTileView().setZoomLevel(zoom - 1);
					if (mMoveListener != null)
						mMoveListener.onZoomDetected();
				}
				return true;
			}
		});*/
		map_location = (ImageView) findViewById(R.id.map_location);
		map_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setAutoFollow(true);
				setLastKnownLocation();
			}
		});
		map_location.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				setAutoFollow(true);
				setLastKnownLocation();

				if (unRectifyLocation != null) {
					PoiPoint point = new PoiPoint();
					point.Title = "??????";
					point.GeoPoint = GeoPoint.fromDouble(unRectifyLocation.getLatitude(), unRectifyLocation.getLongitude());
				}
				return false;
			}
		});
		map_menu_search = (TextView) findViewById(R.id.map_menu_search);
		map_menu_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, SearchActivity.class);
				startActivityForResult(intent, RequestCode.SEARCH);
			}
		});

		// ?????????????????????
		registerForContextMenu(mMap);
	}

	public void refreshViews() {
		if (ConnService.is_connect){
			tv_is_login.setImageResource(R.drawable.online_m);
		}else {
			tv_is_login.setImageResource(R.drawable.offline_m);
			Toast.makeText(MainActivity.this,"??????????????????????????????????????????",Toast.LENGTH_SHORT).show();
		}
	}

	TextView tv_Yandex;
	TextView tv_BING;
	TextView mcmap;
	private void initPopu() {
		//????????????
		layout = (FrameLayout) LayoutInflater.from(MainActivity.this).inflate(
				R.layout.layout_map_select, null);
		tv_Yandex = layout.findViewById(R.id.tv_Yandex);
		tv_BING = layout.findViewById(R.id.tv_BING);
		mcmap = layout.findViewById(R.id.mcmap);
		// ?????????popupWindow
		map_popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
		View contentView = map_popupWindow.getContentView();
		//??????????????????PopupWindow???????????????????????????0
		contentView.measure(makeDropDownMeasureSpec(map_popupWindow.getWidth()),
				makeDropDownMeasureSpec(map_popupWindow.getHeight()));
		map_popupWindow.setOutsideTouchable(true);

		tv_Yandex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				isGaode = false;
				exchangeMap("Yandex");
			}
		});

		tv_BING.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				isGaode = false;
				exchangeMap("bing");
			}
		});

		mcmap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				isGaode = false;
				exchangeMap("msmaphybrid");
			}
		});
	}

	private void exchangeMap(String mapName) {//????????????
		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences uiState = getPreferences(Activity.MODE_PRIVATE);
		mMapId = mapName;
		mOverlayId = uiState.getString("OverlayID", "");
		mShowOverlay = uiState.getBoolean("ShowOverlay", true);
		setTileSource(mMapId, mOverlayId, mShowOverlay);
		FillOverlays();
		setTitle();
		map_popupWindow.dismiss();
	}

	public static boolean isTracked = false;

	// ??????????????????
	private void FillOverlays() {
		this.mMap.getOverlays().clear();
		// ????????????
		if (mTileOverlay != null)
			this.mMap.getOverlays().add(mTileOverlay);

		// ????????????
		this.mMap.getOverlays().add(mMyLocationOverlay);
		// ????????????
		if (SysConfig.workType == WorkType.WORK_TYPE_SHOT) {
			if (mShotPointOverlay != null) {
				this.mMap.getOverlays().add(mShotPointOverlay);
			}
		}
		if (mWorkAreaOverlay!=null){
			this.mMap.getOverlays().add(mWorkAreaOverlay);
		}
		if (mAlarmPointOverlay!=null){
			this.mMap.getOverlays().add(mAlarmPointOverlay);
		}
		if (mDrillPointOverlay!=null){
			this.mMap.getOverlays().add(mDrillPointOverlay);
		}
	}

	private void setAutoFollow(boolean autoFollow) {
		setAutoFollow(autoFollow, false);
	}

	private void setAutoFollow(boolean autoFollow, final boolean supressToast) {
		mAutoFollow = autoFollow;
	}

	/**
	 * ??????????????????????????????????????????????????????
	 *
	 */
	private void setLastKnownLocation() {
		final GeoPoint p = mMyLocationOverlay.getLastGeoPoint();
		if (p != null)
			mMap.getController().setCenter(p);
		else {
			// ?????????GPS????????????
			final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			final Location loc1 = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			final Location loc2 = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			boolean boolGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean boolNetworkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			String str = "";
			Location loc = null;

			if(loc1 == null && loc2 != null)
				loc = loc2;
			else if (loc1 != null && loc2 == null)
				loc = loc1;
			else if (loc1 == null && loc2 == null)
				loc = null;
			else
				loc = loc1.getTime() > loc2.getTime() ? loc1 : loc2;

			if(boolGpsEnabled){}
			else if(boolNetworkEnabled)
				str = getString(R.string.message_gpsdisabled);
			else if(loc == null)
				str = getString(R.string.message_locationunavailable);
			else
				str = getString(R.string.message_lastknownlocation);

			if (loc == null) {
				str = "???????????????";
			}
			if(str.length() > 0)
				Toast.makeText(this, str, Toast.LENGTH_LONG).show();

			if(loc != null) {
				mMap.getController().setCenter(TypeConverter.locationToGeoPoint(loc));
//				unRectifyLocation = loc;//???????????? ???????????????
				Log.e("loc", loc.getLongitude()+"||"+loc.getLatitude() );
			}
		}
	}

	private void setTitle(){
		try {
			final TextView leftText = (TextView) findViewById(R.id.left_text);
			if(leftText != null) {
				String overlayName = "";
				if(mMap.getTileSource() != null && mMap.getTileSource().MAP_TYPE != TileSourceBase.MIXMAP_PAIR)
					if(mMap.getTileSource().getTileSourceBaseOverlay() != null)
						overlayName = " / " + mMap.getTileSource().getTileSourceBaseOverlay().NAME;
				leftText.setText(mMap.getTileSource().NAME + overlayName);
			}

			final TextView gpsText = (TextView) findViewById(R.id.gps_text);
			if(gpsText != null){
				gpsText.setText(mGpsStatusName);
			}

			final TextView rightText = (TextView) findViewById(R.id.right_text);
			if(rightText != null){
				final double zoom = mMap.getZoomLevelScaled();
				if(zoom > mMap.getTileSource().ZOOM_MAXLEVEL) {
					rightText.setText(""+(mMap.getTileSource().ZOOM_MAXLEVEL+1)+"+");
				} else {
					rightText.setText(""+(1 + Math.round(zoom)));
				}
			}
		} catch (Exception e) {
		}
	}

	@SuppressLint("InvalidWakeLockTag")
	@Override
	protected void onResume() {

		keyBackClickCount = 0;

		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		final SharedPreferences uiState = getPreferences(Activity.MODE_PRIVATE);

		if (!isChangeMap) {
			if(mMapId == null)
				mMapId = uiState.getString(MAPNAME, TileSource.MAPNIK);
			if (mMapId.endsWith(".sqlitedb")) {
				File file = new File(Ut.getRMapsMapsDir(MainActivity.this).getAbsolutePath()+ "/" + mMapId.substring(8));
				if (!file.exists()) {
					SysConfig.isOnlineMap = true;
					mMapId = TileSource.MAPNIK;
					setData(SysContants.ISONLINEMAP, SysConfig.isOnlineMap);
				}
			}
			mOverlayId = uiState.getString("OverlayID", "");
			mShowOverlay = uiState.getBoolean("ShowOverlay", true);
			isGaode = uiState.getBoolean("isGaode",false);
			setTileSource(mMapId, mOverlayId, mShowOverlay);

			mMap.getController().setZoom(uiState.getInt("ZoomLevel", 16));
			setTitle();
			mMapId = null;
		} else {
			isChangeMap = false;
		}

		refreshViews();
		FillOverlays();
		mOrientationSensorManager.registerListener(mListener, mOrientationSensorManager
				.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
		if (pref.getBoolean("pref_keepscreenon", true)) {
			myWakeLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
					PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "RMaps");
			myWakeLock.acquire();
		} else {
			myWakeLock = null;
		}

		super.onResume();
	}



	public interface MainMSG {
		public int MSG_SHOW_TASK_STOP = 0x041;
		public int MSG_UPDATA_TOP_TASK_DATA = 0x043;
		public int MSG_IS_KUITAN = 0x044;
		public int MSG_KUITAN_FAIL = 0x045;
	}

	@Override
	protected void onPause() {
		final GeoPoint point = mMap.getMapCenter();

		// ??????????????????
		SharedPreferences uiState = getPreferences(Activity.MODE_PRIVATE);
		Editor editor = uiState.edit();
		if(mTileSource != null) {
			editor.putString("MapName", mTileSource.ID);
			try {
				editor.putString("OverlayID", mTileOverlay == null ? mTileSource.getOverlayName() : mTileOverlay.getTileSource().ID);
			} catch (Exception e) {
			}
		}
		editor.putBoolean("ShowOverlay", mShowOverlay);
		editor.putInt("Latitude", point.getLatitudeE6());
		editor.putInt("Longitude", point.getLongitudeE6());
		editor.putInt("ZoomLevel", mMap.getZoomLevel());
		editor.putBoolean("CompassEnabled", mCompassEnabled);
		editor.putBoolean("AutoFollow", mAutoFollow);
		editor.putString("app_version", Ut.getAppVersion(this));
		editor.putBoolean("isGaode",isGaode);
//		editor.putString("targetlocation", mMyLocationOverlay.getTargetLocation() == null ? "" : mMyLocationOverlay.getTargetLocation().toDoubleString());
		editor.commit();

		uiState = getSharedPreferences("MapName", Activity.MODE_PRIVATE);
		editor = uiState.edit();
		if(mTileSource != null)
			editor.putString("MapName", mTileSource.ID);
		editor.putInt("Latitude", point.getLatitudeE6());
		editor.putInt("Longitude", point.getLongitudeE6());
		editor.putInt("ZoomLevel", mMap.getZoomLevel());
		editor.putBoolean("CompassEnabled", mCompassEnabled);
		editor.putBoolean("AutoFollow", mAutoFollow);
		editor.putBoolean("isGaode",isGaode);
		editor.commit();

		// ????????????
		if (myWakeLock != null)
			myWakeLock.release();

		if(mOrientationSensorManager != null)
			mOrientationSensorManager.unregisterListener(mListener);
		if(mTileSource != null)

			mTileSource.Free();
		mTileSource = null;
		mPoiManager.FreeDatabases();

		if(mTileOverlay != null)
			mTileOverlay.Free();

//		asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
		Log.i("ActivityMiniRecog","On pause");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		for (TileViewOverlay osmvo : mMap.getOverlays())
			osmvo.Free();
		if(mTileSource != null)
			mTileSource.Free();
		mTileSource = null;
		mMap.setMoveListener(null);
		mThreadPool.shutdown();

		if (gpsReceiver != null) {
			unregisterReceiver(gpsReceiver);
		}
		destroyLocation();
		// ??????SDK??????4.2 ??????????????????
		/*asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
		if (enableOffline) {
			unloadOfflineEngine(); // ??????????????????????????????, ?????? ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH ???????????????
		}

		// ??????SDK??????5.2 ?????????????????????
		// ?????????registerListener?????????????????????????????????????????????
		asr.unregisterListener(this);*/
//		stopService(new Intent(MainActivity.this,ConnService.class));
		LoginActivity.isLogin = false;
//		sendBroadcast(new Intent(this, OpenServiceBroadcast.class));
		Log.e("Main","Main_CLOSED");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// ?????????
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_option_menu, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Menu submenu = menu.findItem(R.id.mapselector).getSubMenu();
		submenu.clear();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		File folder = Ut.getRMapsMapsDir(MainActivity.this);
		if (folder.exists()) {
			File[] files = folder.listFiles();
			if (files != null)
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().toLowerCase().endsWith(".mnm")
							|| files[i].getName().toLowerCase().endsWith(".tar")
							|| files[i].getName().toLowerCase().endsWith(".sqlitedb")) {
//						String name = Ut.FileName2ID(files[i].getName());
						String name = files[i].getName();
						if (name.toLowerCase().endsWith("sqlitedb")) {
							String string = pref.getString(TileSourceBase.PREF_USERMAP_ + name + "_name", "");
							if (string == null || "".equals(string)) {
								final Editor editor = pref.edit();
								editor.putBoolean(TileSourceBase.PREF_USERMAP_ + name + "_enabled", true);
								editor.putString(TileSourceBase.PREF_USERMAP_ + name + "_name", (String) name.subSequence(0, name.length() - 9));
								editor.putString(TileSourceBase.PREF_USERMAP_ + name + "_projection", "1");
								editor.putString(TileSourceBase.PREF_USERMAP_ + name + "_baseurl", folder.getAbsolutePath() + "/" + name);
								editor.putBoolean(TileSourceBase.PREF_USERMAP_+name+"_isoverlay", mTileSource.LAYER);
								editor.commit();

								try {
									SQLiteMapDatabase cacheDatabase = new SQLiteMapDatabase();
									cacheDatabase.setFile(folder.getAbsolutePath() + "/" + files[i].getName());
									int[] zooms = cacheDatabase.getZoom();
									if (zooms != null) {
										TileProviderFileBase provider = new TileProviderFileBase(MainActivity.this);
										provider.CommitIndex(TileSourceBase.PREF_USERMAP_ + name, 0, 0, zooms[0], zooms[1]);
										provider.Free();
									}
								} catch (Exception e) {
								}
							}
						}
						if (pref.getBoolean("pref_usermaps_" + name + "_enabled", false) && !pref.getBoolean("pref_usermaps_" + name + "_isoverlay", false)) {
							MenuItem item = submenu.add(R.id.isoverlay, Menu.NONE, Menu.NONE, pref.getString("pref_usermaps_" + name + "_name", files[i].getName()));
							item.setTitleCondensed("usermap_" + name);
						}
					}
				}
		}

		Cursor c = mPoiManager.getGeoDatabase().getMixedMaps();
		if(c != null) {
			if(c.moveToFirst()) {
				do {
					if (pref.getBoolean("PREF_MIXMAPS_" + c.getInt(0) + "_enabled", true) && c.getInt(2) < 3) {
						MenuItem item = submenu.add(c.getString(1));
						item.setTitleCondensed("mixmap_" + c.getInt(0));
					}
				} while(c.moveToNext());
			}
			c.close();
		}

		final SAXParserFactory fac = SAXParserFactory.newInstance();
		SAXParser parser = null;
		try {
			parser = fac.newSAXParser();
			if(parser != null){
				final InputStream in = getResources().openRawResource(R.raw.predefmaps);
				parser.parse(in, new PredefMapsParser(submenu, pref));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		final GeoPoint point = mMap.getMapCenter();

		if(item.getItemId() == R.id.area_selector) {
			startActivity(new Intent(this, AreaSelectorActivity.class)
					.putExtra("new", true)
					.putExtra(MAPNAME, mTileSource.ID)
					.putExtra("Latitude", point.getLatitudeE6())
					.putExtra("Longitude", point.getLongitudeE6())
					.putExtra("ZoomLevel", mMap.getZoomLevel()));
			return true;
		} else if(item.getItemId() == R.id.downloadprepared) {
			startActivity(new Intent(this, FileDownloadListActivity.class));
			return true;
		}

		else if (item.getItemId() == R.id.poilist) {
			startActivityForResult((new Intent(this, PoiListActivity.class))
					.putExtra("lat", point.getLatitude())
					.putExtra("lon", point.getLongitude())
					.putExtra("title", "POI"), RequestCode.POIS);
			return true;
		} else if (item.getItemId() == R.id.tracks) {
			startActivityForResult(new Intent(this, TrackListActivity.class), RequestCode.TRACKS);
			return true;
		}
		else if (item.getItemId() == R.id.settings) {
			startActivityForResult(new Intent(this, MainPreferences.class), RequestCode.MAPSET);
			return true;
		} else if (item.getItemId() == R.id.task_settings) {
//			startActivity(new Intent(this, TaskSettingActivity.class));
			return true;
		} else if (item.getItemId() == R.id.about) {
			showDialog(R.id.about);
			return true;
		} else if (item.getItemId() == R.id.mapselector) {
			return true;
		}
		else if (item.getItemId() == R.id.exit) {
			onPause();
			Process.killProcess(Process.myPid());
			System.exit(10);
			return true;
		} else {
			final String mapid = (String)item.getTitleCondensed();
			setTileSource(mapid, "", true);

			if(mTileSource.MAP_TYPE == TileSource.PREDEF_ONLINE) {
//				mTracker.setCustomVar(1, "MAP", mapid);
//				mTracker.trackPageView("/maps");
			}
			FillOverlays();
			setTitle();

			if (unRectifyLocation != null) {
				Location location = unRectifyLocation;
				mMyLocationOverlay.setLocation(location);
			}

			return true;
		}

	}

	/**
	 * ??????????????????
	 *
	 * @param aMapId
	 * @param aOverlayId
	 * @param aShowOverlay
	 */
	private void setTileSource(String aMapId, String aOverlayId, boolean aShowOverlay) {
		final String mapId = aMapId == null ? (mTileSource == null ? TileSource.MAPNIK : mTileSource.ID) : aMapId;
		final String overlayId = aOverlayId == null ? mOverlayId : aOverlayId;
		final String lastMapID = mTileSource == null ? TileSource.MAPNIK : mTileSource.ID;

		if(mTileSource != null) mTileSource.Free();

		if(overlayId != null && !overlayId.equalsIgnoreCase("") && aShowOverlay) {
			mOverlayId = overlayId;
			mShowOverlay = true;
			try {
				mTileSource = new TileSource(this, mapId, overlayId);

			} catch (RException e) {
				mTileSource = null;
				addMessage(e);
			} catch (Exception e) {
				mTileSource = null;
				addMessage(new RException(R.string.error_other, e.getMessage()));
			}
		} else {
			if(mTileOverlay != null) {
				mTileOverlay.Free();
				mTileOverlay = null;
			}

			try {
				mTileSource = new TileSource(this, mapId, aShowOverlay);

				mShowOverlay = aShowOverlay;
				if(mapId != lastMapID)
					mOverlayId = "";
			} catch (RException e) {
				mTileSource = null;
				addMessage(e);
			} catch (Exception e) {
				mTileSource = null;
				addMessage(new RException(R.string.error_other, e.getMessage()));
			}
		}

		if(mTileSource != null) {
			final TileSource tileSource = mTileSource.getTileSourceForTileOverlay();
			if(tileSource != null) {
				if(mTileOverlay == null)
					mTileOverlay = new TileOverlay(mMap.getTileView(), true);
				mTileOverlay.setTileSource(tileSource);
			} else if(mTileOverlay != null) {
				mTileOverlay.Free();
				mTileOverlay = null;
			}
		} else {
			try {
				mTileSource = new TileSource(this, TileSource.MAPNIK);
			} catch (SQLiteException e) {
			} catch (RException e) {
			}
		}

		mMap.setTileSource(mTileSource);
		FillOverlays();

		if(mMyLocationOverlay != null && mTileSource != null)
			mMyLocationOverlay.correctScale(mTileSource.MAPTILE_SIZE_FACTOR, mTileSource.GOOGLESCALE_SIZE_FACTOR);

	}

	/**
	 * ??????Error
	 *
	 * @param e
	 */
	private void addMessage(RException e) {

		LogFileUtil.saveFileToSDCard(e.getStringRes(this));

	}

	/*??????station??????????????????*/
	private TaskPoint getStationByStation(TaskPoint mTaskPoint) {
		StationEntity stationEntity = new StationEntity();
		stationEntity.setStation(mTaskPoint.stationNo);
		Gson gson = new Gson();
		String json = gson.toJson(stationEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).getStationById(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string().toString();
					Log.e("getStationByStation", "getStationByStation: "+result);
					if (result.contains("success")){
						StationInfoEntity entity = new StationInfoEntity();
						entity = new Gson().fromJson(result, StationInfoEntity.class);
						mTaskPoint.spointNo = entity.getData().getNearestImei();
					}else {
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
		return mTaskPoint;
	}

	PopupWindow popupWindow;
	private Animation animation;
	/*????????????????????????*/
	public void showPopuwindow(final TaskPoint mTaskPoint,int type){
		popupWindow = new PopupWindow();
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pop_up);
		animation.setFillAfter(true);//android?????????????????????????????????
		View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_popup_window,null);
		popupWindow.setContentView(view);
		popupWindow.showAtLocation(findViewById(R.id.tv_main),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
		view.startAnimation(animation);

		TextView tv_stationNo = view.findViewById(R.id.tv_stationNo);
		TextView tv_lon = view.findViewById(R.id.tv_lon);
		TextView tv_lat = view.findViewById(R.id.tv_lat);
		TextView tv_imei = view.findViewById(R.id.tv_imei);
		TextView tv_match = view.findViewById(R.id.tv_match);
		TextView tv_clear = view.findViewById(R.id.tv_clear);
		TextView tv_station =view.findViewById(R.id.tv_station);
		TextView tv_stationNum = view.findViewById(R.id.tv_stationNum);
		ImageView tv_leave = view.findViewById(R.id.tv_leave);
		View views = view.findViewById(R.id.view);
		LinearLayout ll_guide = view.findViewById(R.id.ll_guide);
		LinearLayout ll_match = view.findViewById(R.id.ll_match);
		LinearLayout ll_dis_match = view.findViewById(R.id.ll_dis_match);
		LinearLayout ll_stationNum = view.findViewById(R.id.ll_stationNum);

		tv_stationNo.setText(mTaskPoint.stationNo);
		tv_lon.setText(mTaskPoint.geoPoint.getLongitude()+"");
		tv_lat.setText(mTaskPoint.geoPoint.getLatitude()+"");

		if (type==1){//??????
			tv_station.setText("?????????:");
			tv_match.setText("????????????");
			tv_imei.setText(mTaskPoint.spointNo);
			ll_stationNum.setVisibility(View.VISIBLE);
			//
			HttpUtil.init( HttpUtil.getService(RetrofitService.class).getDeviceInfoByImei(mPreferencesMap,mTaskPoint.spointNo), new Subscriber<ResponseBody>() {
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
						String result = responseBody.string();
						Log.e("responseBody", "responseBody: "+result);
						if (result.contains("success")){
							//???????????????
							SetLastestEntity lastestEntity = new Gson().fromJson(result,SetLastestEntity.class);
							if (lastestEntity.getData().getStatus().equals("1")){
								DrillPoint mDrillPoint = new DrillPoint();
								String stationNo = lastestEntity.getData().getStation();
								mDrillPoint.stationNo = lastestEntity.getData().getDeviceName();
								mDrillPoint.Id = mTaskPoint.Id;
//                mDrillPoint.lineNo = stationNo;
								mDrillPoint.spointNo = lastestEntity.getData().getImei();
								mDrillPoint.time = "";
								if (stationNo!=null && lastestEntity.getData().getStaLat()!=null){
									mDrillPoint.geoPoint = GeoPoint.from2DoubleString(lastestEntity.getData().getStaLat(), lastestEntity.getData().getStaLng());
									mDrillPoint.lineNo =lastestEntity.getData().getStation();
								}else {
									mDrillPoint.geoPoint = GeoPoint.from2DoubleString(lastestEntity.getData().getLat(), lastestEntity.getData().getLng());
									mDrillPoint.lineNo = "";
								}
								mPointDBDao.updateDrillPoint(mDrillPoint);

								//??????????????????
								int size = mPointDBDao.selectDrillbyKeyWordToTaskPoint(mTaskPoint.spointNo).size();
								Log.e("size", "size: "+size );
								if (size>0){
									TaskPoint taskPoint = mPointDBDao.selectDrillbyKeyWordToTaskPoint(mTaskPoint.spointNo).get(0);
									if (taskPoint.lineNo==null || "".equals(taskPoint.lineNo)){
										ll_dis_match.setVisibility(View.GONE);
										views.setVisibility(View.GONE);
										tv_stationNum.setText("?????????");
									}else {
										ll_dis_match.setVisibility(View.VISIBLE);
										tv_clear.setText("????????????");
										views.setVisibility(View.VISIBLE);
										tv_stationNum.setText(taskPoint.lineNo);
									}
								}
							}else if (lastestEntity.getData().getStatus().equals("2")){
								ll_dis_match.setVisibility(View.GONE);
								views.setVisibility(View.GONE);
								tv_stationNum.setText("?????????");
								Toast.makeText(MainActivity.this,"?????????????????????????????????????????????",Toast.LENGTH_LONG).show();
							}
						}else {

						}

					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			});
			//

		}else if (type==0){//??????
			StationEntity stationEntity = new StationEntity();
			stationEntity.setStation(mTaskPoint.stationNo);
			Gson gson = new Gson();
			String json = gson.toJson(stationEntity);
			Log.e("json", "json: "+json);
			RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
			HttpUtil.init( HttpUtil.getService(RetrofitService.class).getStationById(mPreferencesMap,body), new Subscriber<ResponseBody>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable e) {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					Log.e("onError", "onError: "+e.getMessage());
				}

				@Override
				public void onNext(ResponseBody responseBody) {
					try {
						if (progressDialog!=null){
							progressDialog.dismiss();
						}
						String result = responseBody.string().toString();
						Log.e("getStationByStation", "getStationByStation: "+result);
						if (result.contains("success")){
							StationInfoEntity entity = new StationInfoEntity();
							entity = new Gson().fromJson(result, StationInfoEntity.class);
							mTaskPoint.spointNo = entity.getData().getNearestImei();
							if (null==mTaskPoint.spointNo || "".equals(mTaskPoint.spointNo)){
								ll_dis_match.setVisibility(View.GONE);
								views.setVisibility(View.GONE);
								tv_imei.setText("?????????");
							}else {
								tv_imei.setText(mTaskPoint.spointNo);
								ll_dis_match.setVisibility(View.VISIBLE);
								tv_clear.setText("??????");
								views.setVisibility(View.VISIBLE);
							}
						}else {
							Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
						}

					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(MainActivity.this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
					}

				}

			});

		}

		/*????????????*/
		tv_leave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		/*????????????*/
		ll_guide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Message message = new Message();
				message.what = RequestCode.TASK_GUIDE;
				message.obj = mTaskPoint;
				mCallbackHandler.sendMessage(message);
				if (popupWindow!=null){
					popupWindow.dismiss();
				}
			}
		});
		/*????????????/??????*/
		ll_match.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (type==0){
					dialog = DialogUtils.Alert(MainActivity.this, "???????????????", "?????????????????????",
							new String[]{"??????", "????????????"},
							new OnClickListener[]{new OnClickListener() {

								@Override
								public void onClick(View v) {
									//??????
//									Intent intent = new Intent(MainActivity.this,MipcaActivityCapture.class);
									Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
									intent.putExtra("stationNo",mTaskPoint.stationNo);
									startActivityForResult(intent,RequestCode.SCAN);
									dialog.dismiss();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.dismiss();
									dialog = DialogUtils.AlertEdit(MainActivity.this, "???????????????IMEI", "",
											new String[]{"??????", "??????"},
											new OnClickListener[]{new OnClickListener() {

												@Override
												public void onClick(View v) {
													//??????
													View view = dialog.getCurrentFocus();
													EditText editText = view.findViewById(R.id.dialog_message);
													editText.setHint("???????????????imei");
													String imei = editText.getText().toString();
													Log.e("imei", "imei: "+imei);
													matchingImei(imei,mTaskPoint.stationNo);
													dialog.dismiss();
												}
											}, new OnClickListener() {

												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											},new OnClickListener() {
												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											}
											});
									dialog.show();
								}
							}
							});
					dialog.show();

					if (popupWindow!=null){
						popupWindow.dismiss();
					}
				}else if (type==1){
					startSearchAround(mTaskPoint);//????????????
					if (popupWindow!=null){
						popupWindow.dismiss();
					}
				}

			}
		});

		/*??????*/
		ll_dis_match.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = DialogUtils.Alert(MainActivity.this, "??????", "???????????????",
						new String[]{"??????", "??????"},
						new OnClickListener[]{new OnClickListener() {

							@Override
							public void onClick(View v) {
								//????????????
								if (type==0){
									disMatchByStation(mTaskPoint.stationNo);
								}else if (type==1){
									disMatchByImei(mTaskPoint.spointNo,mTaskPoint.stationNo);
								}
								dialog.dismiss();
								if (popupWindow!=null){
									popupWindow.dismiss();
								}
							}
						}, new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						}
						});
				dialog.show();
			}
		});

	}
	/*??????????????????*/
	private void disMatchByStation(String stationNo) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("????????????...");
		if (progressDialog!=null){
			progressDialog.show();
		}
		StationEntity mStationEntity = new StationEntity();
		mStationEntity.setStation(stationNo);
		Gson gson = new Gson();
		String json = gson.toJson(mStationEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).delMatchByStation(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string().toString();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						//???????????????
						ShotPoint shotPoint = mPointDBDao.selectShotPoint(stationNo);
						shotPoint.spointNo=null;
						mPointDBDao.updateShotPoint(shotPoint);
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}
	/*??????imei?????????*/
	private void disMatchByImei(String imei,String deviceName) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("????????????...");
		if (progressDialog!=null){
			progressDialog.show();
		}
		ImeiEntity mImeiEntity = new ImeiEntity();
		mImeiEntity.setNearestImei(imei);
		Gson gson = new Gson();
		String json = gson.toJson(mImeiEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).delMatchByImei(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string().toString();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						DrillPoint mDrillPoint = mPointDBDao.selectDrillPoint(deviceName);
						mDrillPoint.lineNo="";
						mPointDBDao.updateDrillPoint(mDrillPoint);
//						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
						Set<String> imeiArray = new HashSet<>();
						imeiArray.add(imei);
						ProtectImeisEntity protectImeisEntity = new ProtectImeisEntity();
						protectImeisEntity.setImeis(imeiArray);
						protectImeisEntity.setType("0");
						Gson gson = new Gson();
						String json = gson.toJson(protectImeisEntity);
						setProtect(json,protectImeisEntity.getType());
					}else {
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/*??????????????????*/
	public void showAlarmPopuwindow(final AlarmPoint mAlarmPoint){
		popupWindow = new PopupWindow();
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pop_up);
		animation.setFillAfter(true);//android?????????????????????????????????
		View view = LayoutInflater.from(this).inflate(R.layout.layout_popup_alarm,null);
		popupWindow.setContentView(view);
		popupWindow.showAtLocation(findViewById(R.id.tv_main),Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,0,0);
		view.startAnimation(animation);

		TextView tv_DeviceName = view.findViewById(R.id.tv_DeviceName);
		TextView tv_lon = view.findViewById(R.id.tv_lon);
		TextView tv_lat = view.findViewById(R.id.tv_lat);
		ImageView tv_leave = view.findViewById(R.id.tv_leave);
		LinearLayout ll_guide = view.findViewById(R.id.ll_guide);
		LinearLayout ll_deal = view.findViewById(R.id.ll_deal);
		TextView tv_AlarmName = view.findViewById(R.id.tv_AlarmName);
		TextView tv_AlarmTime = view.findViewById(R.id.tv_AlarmTime);
		TextView tv_Address = view.findViewById(R.id.tv_Address);
		TextView tv_stationNo = view.findViewById(R.id.tv_stationNo);

		tv_DeviceName.setText(mAlarmPoint.getDeviceName());
		tv_lon.setText(mAlarmPoint.getPoint().getLongitude()+"");
		tv_lat.setText(mAlarmPoint.getPoint().getLatitude()+"");
		tv_AlarmName.setText(mAlarmPoint.getAlarmName());
		tv_AlarmTime.setText(mAlarmPoint.getAlarmTime());
		tv_Address.setText(mAlarmPoint.getAddress());
		if (mAlarmPoint.getStation()==null || mAlarmPoint.getStation().equals("")){
			tv_stationNo.setText("?????????");
		}else {
			tv_stationNo.setText(mAlarmPoint.getStation());
		}
		tv_leave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});

		ll_guide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				guideToAlarm(mAlarmPoint);
				if (popupWindow!=null){
					popupWindow.dismiss();
				}
			}
		});



		ll_deal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showRemarkDialog(mAlarmPoint);
				if (popupWindow!=null){
					popupWindow.dismiss();
				}
			}
		});
	}

	/*????????????*/
	public void showRemarkDialog(AlarmPoint mAlarmPoint) {
		dialog = DialogUtils.AlertEdit(MainActivity.this, "?????????????????????", "",
				new String[]{"??????", "??????"},
				new OnClickListener[]{new OnClickListener() {

					@Override
					public void onClick(View v) {
						//??????
						View view = dialog.getCurrentFocus();
						EditText editText = view.findViewById(R.id.dialog_message);
						editText.setHint("?????????????????????");
						String remark = editText.getText().toString();
						addRemark(mAlarmPoint.getIds(),mAlarmPoint.getImei(),remark);
						dialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				},new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				}
				});
		dialog.show();
	}

	/*????????????*/
	private void addRemark(String id,String imei,String remark) {
		RemarkEntity remarkEntity = new RemarkEntity();
		remarkEntity.setId(id);
		remarkEntity.setRemark(remark);
		String remarkJson = new Gson().toJson(remarkEntity);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), remarkJson);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).toModifyRemark(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string().toString();
					Log.e("remark", "remark: "+result);
					if (result.contains("success")){
						dealAlarm(id,imei);
//						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/*????????????*/
	private void dealAlarm(String id,String imei) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("????????????...");
		if (progressDialog!=null){
			progressDialog.show();
		}
		HandleMsgEntity mHandleMsgEntity = new HandleMsgEntity();
		mHandleMsgEntity.setId(id);
		mHandleMsgEntity.setImei(imei);
		mHandleMsgEntity.setHandleStatus("1");
		Gson gson = new Gson();
		String json = gson.toJson(mHandleMsgEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).toChangeHandleStatus(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string().toString();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						initAlarm(false);
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(MainActivity.this,"????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	/*
	*
	* ??????????????????
	* Uri gmmIntentUri = Uri.parse("google.navigation:q="+endGps.lat+","+endGps.lon);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
		mapIntent.setPackage("com.google.android.apps.maps");
		if (mapIntent.resolveActivity(getPackageManager()) != null) {
			startActivity(mapIntent);
		}
	*
	* */
	/*??????????????????*/
	public void guideToAlarm(AlarmPoint mAlarmPoint){
		NaviLatLng mStartLatlng = new NaviLatLng(location_x,location_y);
		Gps endGps = GpsCorrect.GPS84_TO_GCJ02(new Gps(mAlarmPoint.getPoint().getLatitude(),mAlarmPoint.getPoint().getLongitude()));
		Log.e("endGps", "endGps: "+endGps.lat+"|"+endGps.lon);
		NaviLatLng mEndLatlng = new NaviLatLng(endGps.lat,endGps.lon);
		Log.e("guideToAlarm", "guideToAlarm: "+mAlarmPoint.getPoint().getLatitude()+"|"+mAlarmPoint.getPoint().getLongitude() );
		final Intent intent = new Intent(MainActivity.this, RouteNaviActivity.class);
		intent.putExtra("mStartLatlng",mStartLatlng);
		intent.putExtra("mEndLatlng",mEndLatlng);
		intent.putExtra("guidName",mAlarmPoint.getDeviceName());
		dialog = DialogUtils.Alert(MainActivity.this, "??????", "?????????????????????",
				new String[]{"??????", "??????"},
				new OnClickListener[]{new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("flag",0);
//									startActivityForResult(intent,RequestCode.STOP_GUIDING);
						startActivity(intent);
						dialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						intent.putExtra("flag",1);
//									startActivityForResult(intent,RequestCode.STOP_GUIDING);
						startActivity(intent);
						dialog.dismiss();
					}
				}
				});
		dialog.show();
	}



	/**
	 * ?????????menu
	 * ????????????????????????
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(menuInfo instanceof TileView.ShotMenuInfo) {
			final TileView.ShotMenuInfo info = (TileView.ShotMenuInfo) menuInfo;
			if(info.EventGeoPoint != null) {
				if(info.MarkerIndex > ShotPointOverlay.NO_TAP) {
					TaskPoint shotPoint = mPointDBDao.selectShotPoint(info.MarkerIndex);
					showPopuwindow(shotPoint,0);
					mMap.getTileView().mShotMenuInfo.EventGeoPoint = null;
				}
			}
		}else if (menuInfo instanceof TileView.DrillMenuInfo){
			final TileView.DrillMenuInfo info = (TileView.DrillMenuInfo) menuInfo;
			if(info.MarkerIndex > AlarmPointOverlay.NO_TAP) {
				//...
				TaskPoint drillPoint = mPointDBDao.selectDrillPoint(info.MarkerIndex);
				showPopuwindow(drillPoint,1);
				mMap.getTileView().mDrillMenuInfo.EventGeoPoint = null;
			}
		}else if (menuInfo instanceof TileView.AlarmMenuInfo){
			final TileView.AlarmMenuInfo info = (TileView.AlarmMenuInfo) menuInfo;
			if(info.MarkerIndex > AlarmPointOverlay.NO_TAP) {
				//...
				showAlarmPopuwindow(info.mAlarmPoint);
				mMap.getTileView().mAlarmMenuInfo.EventGeoPoint = null;
			}
		}
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() == R.id.isoverlay) {

			final String mapid = (String)item.getTitleCondensed();
			setTileSource(mapid, "", true);

			if(mTileSource.MAP_TYPE == TileSource.PREDEF_ONLINE) {
			}
			FillOverlays();
			setTitle();

			if (unRectifyLocation != null) {
				Location location = unRectifyLocation;
				mMyLocationOverlay.setLocation(location);
			}

		}

		final ContextMenuInfo menuInfo = item.getMenuInfo();
		if(menuInfo != null && menuInfo instanceof TileView.PoiMenuInfo) {
			((TileView.PoiMenuInfo) menuInfo).EventGeoPoint = null;
		} else if(menuInfo != null && menuInfo instanceof TileView.ShotMenuInfo) {
			((TileView.ShotMenuInfo) menuInfo).EventGeoPoint = null;
		}else if (menuInfo != null && menuInfo instanceof TileView.AlarmMenuInfo){
			((TileView.AlarmMenuInfo) menuInfo).EventGeoPoint = null;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RequestCode.STOP_GUIDING){
			Log.e("guidName", "guidName" );
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = RequestCode.STOP_GUIDE;
					msg.obj = guidName;
					mCallbackHandler.sendMessage(msg);
				}
			}).start();
		}else if (resultCode==200){
			closeMenu();
			String stationNo = data.getStringExtra("stationNo");
			int type = data.getIntExtra("type",0);
			Log.e("onActivityResult", "type: "+type );
			Intent intentx = new Intent(ACTION_POINT_LOC);
			intentx.putExtra("stationNo",stationNo);
			intentx.putExtra("type",type);
			sendBroadcast(intentx);
		}else if (requestCode == RequestCode.SEARCH){
			if (data!=null){
				String satation = data.getStringExtra("shot");
				if (SysConfig.workType ==  WorkType.WORK_TYPE_SHOT){
					TaskPoint taskPoint = mPointDBDao.selectShotPointTotaskPoint(satation);
					if (taskPoint!=null){
						if (mShotPointOverlay!=null){
							mMap.getController().setCenter(taskPoint.geoPoint);
							mMap.getTileView().mShotMenuInfo.EventGeoPoint = taskPoint.geoPoint;
							mMap.getTileView().mShotMenuInfo.MarkerIndex = taskPoint.Id;
							mMap.getTileView().mShotMenuInfo.stationNo = taskPoint.stationNo;
							mShotPointOverlay.setTapIndex(taskPoint.Id);
						}else {
							Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
						}
					}else {
						taskPoint = mPointDBDao.selectDrillPointToTaskPoint(satation);
						if (mDrillPointOverlay!=null){
							mMap.getController().setCenter(taskPoint.geoPoint);
							mMap.getTileView().mDrillMenuInfo.EventGeoPoint = taskPoint.geoPoint;
							mMap.getTileView().mDrillMenuInfo.MarkerIndex = taskPoint.Id;
							mMap.getTileView().mDrillMenuInfo.stationNo = taskPoint.stationNo;
							mDrillPointOverlay.setTapIndex(taskPoint.Id);
						}else {
							Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
						}
					}


				}
				mMap.getTileView().showContextMenu();
			}

		}else if (requestCode == RequestCode.CHECKED){
			if (resultCode == RESULT_OK){
				String stationNo = data.getStringExtra("stationNo");
				if (stationNo!=null && SysConfig.workType ==  WorkType.WORK_TYPE_SHOT){
					ShotPoint shotPoint = mPointDBDao.selectShotPoint(stationNo);
					shotPoint.isDone = true;
					mPointDBDao.updateShotPoint(shotPoint);
					mShotPointOverlay.UpdateList();
					mMap.Refresh();
				}
			}

		}else if (requestCode == RequestCode.SCAN){
			if (resultCode == RESULT_OK){
				String result = data.getStringExtra("resultString");
				Log.e("imei", "imei: "+result );
				String stationNo = data.getStringExtra("stationNo");
				showMatchDialog(result,stationNo);
			}
		}else if (requestCode == RequestCode.SCAN_BIND){
			if (resultCode == RESULT_OK){
				String imei = data.getStringExtra("resultString");
				Log.e("imei", "imei: "+imei );
				int size = mPointDBDao.selectDrillbyKeyWordToTaskPoint(imei).size();
				Log.e("size", "size: "+size );
				if (size>0){
					TaskPoint taskPoint = mPointDBDao.selectDrillbyKeyWordToTaskPoint(imei).get(0);
					if (taskPoint!=null){
						if (mDrillPointOverlay!=null){
							mMap.getController().setCenter(taskPoint.geoPoint);
							mMap.getTileView().mDrillMenuInfo.EventGeoPoint = taskPoint.geoPoint;
							mMap.getTileView().mDrillMenuInfo.MarkerIndex = taskPoint.Id;
							mMap.getTileView().mDrillMenuInfo.stationNo = taskPoint.stationNo;
							mDrillPointOverlay.setTapIndex(taskPoint.Id);
							mMap.getTileView().showContextMenu();
							mMap.Refresh();
						}else {
							Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
						}
					}
				}else {
					getDeviceInfoByImei(imei);
					//Toast.makeText(MainActivity.this,"????????????????????????????????????????????????",Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private void getDeviceInfoByImei(String imei) {
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).getDeviceInfoByImei(mPreferencesMap,imei), new Subscriber<ResponseBody>() {
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
					String result = responseBody.string();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						//???????????????
						SetLastestEntity lastestEntity = new Gson().fromJson(result,SetLastestEntity.class);
						if (lastestEntity.getData().getStatus().equals("1")){
							DrillPoint mDrillPoint = new DrillPoint();
							String stationNo = lastestEntity.getData().getStation();
							mDrillPoint.stationNo = lastestEntity.getData().getDeviceName();
//                mDrillPoint.lineNo = stationNo;
							mDrillPoint.spointNo = lastestEntity.getData().getImei();
							mDrillPoint.time = "";
							if (stationNo!=null && lastestEntity.getData().getStaLat()!=null){
								mDrillPoint.geoPoint = GeoPoint.from2DoubleString(lastestEntity.getData().getStaLat(), lastestEntity.getData().getStaLng());
								mDrillPoint.lineNo =lastestEntity.getData().getStation();
							}else {
								mDrillPoint.geoPoint = GeoPoint.from2DoubleString(lastestEntity.getData().getLat(), lastestEntity.getData().getLng());
								mDrillPoint.lineNo = "";
							}
							mPointDBDao.insertDrillPoint(mDrillPoint);

							//??????????????????
							int size = mPointDBDao.selectDrillbyKeyWordToTaskPoint(imei).size();
							Log.e("size", "size: "+size );
							if (size>0){
								TaskPoint taskPoint = mPointDBDao.selectDrillbyKeyWordToTaskPoint(imei).get(0);
								if (taskPoint!=null){
									if (mDrillPointOverlay!=null){
										mMap.getController().setCenter(taskPoint.geoPoint);
										mMap.getTileView().mDrillMenuInfo.EventGeoPoint = taskPoint.geoPoint;
										mMap.getTileView().mDrillMenuInfo.MarkerIndex = taskPoint.Id;
										mMap.getTileView().mDrillMenuInfo.stationNo = taskPoint.stationNo;
										mDrillPointOverlay.setTapIndex(taskPoint.Id);
										mMap.getTileView().showContextMenu();
										mMap.Refresh();
									}else {
										Toast.makeText(MainActivity.this,"?????????????????????",Toast.LENGTH_LONG).show();
									}
								}
							}
						}else if (lastestEntity.getData().getStatus().equals("2")){
							Toast.makeText(MainActivity.this,"?????????????????????????????????????????????",Toast.LENGTH_LONG).show();
						}
					}else {

					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	private void showMatchDialog(String imei,String stationNo){
		dialog = DialogUtils.Alert(MainActivity.this, "?????????????", imei,
				new String[]{"??????", "??????"},
				new OnClickListener[]{new OnClickListener() {

					@Override
					public void onClick(View v) {
						//??????
						matchingImei(imei,stationNo);
						dialog.dismiss();
					}
				}, new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();

					}
				}
				});
		dialog.show();
	}
	private void matchingImei(String imei,String stationNo) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("????????????...");
		if (progressDialog!=null){
			progressDialog.show();
		}
		MatchEntity mMatchEntity = new MatchEntity();
		mMatchEntity.setNearestImei(imei);
		mMatchEntity.setStation(stationNo);
		Gson gson = new Gson();
		String json = gson.toJson(mMatchEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).matchingStationAndImeiByHand(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						//???????????????
						ShotPoint shotPoint = mPointDBDao.selectShotPoint(stationNo);
						shotPoint.spointNo = imei;
						mPointDBDao.updateShotPoint(shotPoint);
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	private void matchingStation(String imei,String stationNo,String deviceName) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("????????????...");
		if (progressDialog!=null){
			progressDialog.show();
		}
		MatchEntity mMatchEntity = new MatchEntity();
		mMatchEntity.setNearestImei(imei);
		mMatchEntity.setStation(stationNo);
		Gson gson = new Gson();
		String json = gson.toJson(mMatchEntity);
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).matchingStationAndImeiByHand(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				if (progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					if (progressDialog!=null){
						progressDialog.dismiss();
					}
					String result = responseBody.string();
					Log.e("responseBody", "responseBody: "+result);
					if (result.contains("success")){
						//???????????????
						DrillPoint mDrillPoint = mPointDBDao.selectDrillPoint(deviceName);
						mDrillPoint.lineNo = stationNo;
						mPointDBDao.updateDrillPoint(mDrillPoint);
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
						/*
						//????????????
						Set<String> imeiArray = new HashSet<>();
						imeiArray.add(imei);
						ProtectImeisEntity protectImeisEntity = new ProtectImeisEntity();
						protectImeisEntity.setImeis(imeiArray);
						protectImeisEntity.setType("1");
						protectImeisEntity.setDelay("1");
						Gson gson = new Gson();
						String json = gson.toJson(protectImeisEntity);
						setProtect(json,protectImeisEntity.getType());*/
					}else {
						Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_LONG).show();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	public void setProtect(String json,String type){
		Log.e("json", "json: "+json);
		RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		HttpUtil.init( HttpUtil.getService(RetrofitService.class).changeDeviceState(mPreferencesMap,body), new Subscriber<ResponseBody>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
				Log.e("onError", "onError: "+e.getMessage());
			}

			@Override
			public void onNext(ResponseBody responseBody) {
				try {
					String result = responseBody.string().toString();
					Log.e("responseBody", "responseBody: "+result);
					SetProEntity proEntity = new Gson().fromJson(result,SetProEntity.class);
					if (proEntity.getCode()==200){
						if (type.equals("1")){
							DecimalFormat df = new DecimalFormat("0.0");
							double min = Double.parseDouble(proEntity.getData())/60;
							df.format(min);
							Toast.makeText(MainActivity.this,"??????,????????????"+min+"?????????????????????",Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(MainActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
						}

					}


				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});
	}

	Dialog dialog;
	private class MainActivityCallbackHandler extends Handler {
		@Override
		public void handleMessage(final Message msg) {
			final int what = msg.what;
			if (what == Ut.MAPTILEFSLOADER_SUCCESS_ID) {
				mMap.Refresh();
			} else if (what == R.id.user_moved_map) {
				// setAutoFollow(false);
			} else if (what == R.id.set_title) {
				setTitle();
			} else if (what == R.id.add_yandex_bookmark) {
				showDialog(R.id.add_yandex_bookmark);
			} else if (what == Ut.ERROR_MESSAGE) {
				if (msg.obj != null)
					Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
			}  else if (what == RequestCode.TASK_GUIDE) {
				if (msg.obj != null) {
					TaskPoint taskPoint = (TaskPoint) msg.obj;
					lastPoint = taskPoint.geoPoint;
					NaviLatLng mStartLatlng = new NaviLatLng(location_x,location_y);
					Gps endGps = GpsCorrect.GPS84_TO_GCJ02(new Gps(lastPoint.getLatitude(),lastPoint.getLongitude()));
					Log.e("endGps", "endGps: "+endGps.lat+"|"+endGps.lon);
					NaviLatLng mEndLatlng = new NaviLatLng(endGps.lat,endGps.lon);
					final Intent intent = new Intent(MainActivity.this, RouteNaviActivity.class);
					intent.putExtra("mStartLatlng",mStartLatlng);
					intent.putExtra("mEndLatlng",mEndLatlng);
					intent.putExtra("guidName",taskPoint.stationNo);
					guidName = taskPoint.stationNo;
					dialog = DialogUtils.Alert(MainActivity.this, "??????", "?????????????????????",
							new String[]{"??????", "??????"},
							new OnClickListener[]{new OnClickListener() {

								@Override
								public void onClick(View v) {
									intent.putExtra("flag",0);
//									startActivityForResult(intent,RequestCode.STOP_GUIDING);
									startActivity(intent);
									dialog.dismiss();
								}
							}, new OnClickListener() {

								@Override
								public void onClick(View v) {
									intent.putExtra("flag",1);
//									startActivityForResult(intent,RequestCode.STOP_GUIDING);
									startActivity(intent);
									dialog.dismiss();
								}
							}
							});
					dialog.show();
					lastPoint = null;
				}
			} else if (what == RequestCode.SHOTPOINT_ISDONE) {
				if (msg.obj != null && !"".equals(msg.obj)) {
					ShotPoint shotPoint = mPointDBDao.selectShotPoint((String) msg.obj);
					shotPoint.isDone = true;
					mPointDBDao.updateShotPoint(shotPoint);
					mShotPointOverlay.UpdateList();
					mMap.Refresh();
				}
			} else if (what == UPDATA_NONEED) {
				Toast.makeText(getApplicationContext(), "???????????????",
						Toast.LENGTH_SHORT).show();
			}else if (what == UPDATA_CLIENT) {
				showUpdataDialog();
			}else if (what == GET_UNDATAINFO_ERROR) {
				//???????????????
//				Toast.makeText(getApplicationContext(), "?????????????????????????????????", Toast.LENGTH_LONG).show();
				Log.e("update", "?????????????????????????????????");
			}else if (what == DOWN_ERROR) {
				//??????apk??????
				Toast.makeText(getApplicationContext(), "?????????????????????", Toast.LENGTH_LONG).show();
				pd.dismiss();
			}
		}
	}


	public static String GPGGA = "";
	public static String GNGGA = "";
	public static String GPGSA = "";
	public static Location unRectifyLocation;
	public static String address;
	public static int trackIndex = 0;
	public static final String[] INITALL_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
	double location_x;
	double location_y;

	private class MoveListener implements IMoveListener {

		public void onMoveDetected() {
			if(mAutoFollow)
				setAutoFollow(false);
		}

		public void onZoomDetected() {
			setTitle();
		}

		@Override
		public void onCenterDetected() {
		}

	}


	private float updateBearing(float newBearing) {
		float dif = newBearing - mLastBearing;
		// find difference between new and current position
		if (Math.abs(dif) > 180)
			dif = 360 - dif;
		// if difference is bigger than 180 degrees,
		// it's faster to rotate in opposite direction
		if (Math.abs(dif) < 1)
			return mLastBearing;
		// if difference is less than 1 degree, leave things as is
		if (Math.abs(dif) >= 90)
			return mLastBearing = newBearing;
		// if difference is bigger than 90 degress, just update it
		mLastBearing += 90 * Math.signum(dif) * Math.pow(Math.abs(dif) / 90, 2);
		// bearing is updated proportionally to the square of the difference
		// value
		// sign of difference is paid into account
		// if difference is 90(max. possible) it is updated exactly by 90
		while (mLastBearing > 360)
			mLastBearing -= 360;
		while (mLastBearing < 0)
			mLastBearing += 360;
		// prevent bearing overrun/underrun
		return mLastBearing;
	}

	/**
	 * ?????????
	 *
	 * @param queryIntent
	 */
	private void ActionShowPoints(Intent queryIntent) {
		final ArrayList<String> locations = queryIntent.getStringArrayListExtra("locations");
		if(!locations.isEmpty()){
			GeoPoint point = null;
			int id = -1;
			Iterator<String> it = locations.iterator();
			while(it.hasNext()) {
				final String [] fields = it.next().split(";");
				String locns = "", title = "", descr = "";
				if(fields.length > 0) locns = fields[0];
				if(fields.length > 1) title = fields[1];
				if(fields.length > 2) descr = fields[2];

				point = GeoPoint.fromDoubleString(locns);
			}
			setAutoFollow(false);
			if(point != null)
				mMap.getController().setCenter(point);
		}
	}

	private int keyBackClickCount = 0;

	@Override
	public void onBackPressed() {
		if (SearchResultOverlay.mSearchGeoPoints != null
				&& SearchResultOverlay.mSearchGeoPoints.size() > 0) {
			SearchResultOverlay.mSearchDescrs.clear();
			SearchResultOverlay.mSearchGeoPoints.clear();
		}

		closePopup(true);
//		mShotPointOverlay.setTapIndex(ShotPointOverlay.NO_TAP);
		mMap.Refresh();

		switch (keyBackClickCount++) {
			case 0:
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
				LoginActivity.isLogin = false;
				super.onBackPressed();
				/*stopService(new Intent(MainActivity.this,ConnService.class));
				System.exit(0);*/
				break;
			default:
				break;
		}

	}
	private GeoPoint lastPoint;
	private ProgressDialog progressDialog = null;

	@Override
	public void setTaskEntity(String station) {
		if (SysConfig.workType == WorkType.WORK_TYPE_SHOT) {
			ShotPoint shotPoint = mPointDBDao.selectShotPoint(station);
			mShotPointOverlay.setTapIndex(shotPoint.getId());
			mMap.getController().setCenter(shotPoint.geoPoint);
			mMap.Refresh();
		}
	}

	@Override
	public void ShowMap(String mapId, String center, String zoom) {
		mMapId = mapId;
		if(center != null && !center.equalsIgnoreCase("")){
			try {
				final GeoPoint geo = GeoPoint.fromDoubleString(center);
				mMap.getController().setCenter(geo);
			} catch (Exception e) {
			}
		}
		if(zoom != null && !zoom.equalsIgnoreCase("")){
			try {
				final int mapzoom = Integer.valueOf(zoom);
				mMap.getController().setZoom(mapzoom);
				Editor editor = uiState.edit();
				editor.putInt("ZoomLevel", mMap.getZoomLevel());
				editor.commit();
			} catch (Exception e) {
			}
		}
		setTileSource(mMapId, "", true);
		if(mTileSource.MAP_TYPE == TileSource.PREDEF_ONLINE) {
//			mTracker.setCustomVar(1, "MAP", mMapId);
//			mTracker.trackPageView("/maps");
		}
		FillOverlays();
		setTitle();

		mMap.Refresh();
		isChangeMap = true;
	}

	/*private void gpsIsOpen() {
		mLocationListener.getBestProvider();
	}*/

	//????????????????????????
	public class CheckVersionTask implements Runnable {
		InputStream is;
		public void run() {
			try {
				String path = SysConfig.UPDATEURL+"/api/android/version";
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int responseCode = conn.getResponseCode();
				if (responseCode == 200) {
					// ?????????????????????????????????
					is = conn.getInputStream();
				}
				info = UpdataInfoParser.getUpdataInfo(is);
				Log.e("version", "run: "+info.getVersion() +"||"+localVersion);
				if (info.getVersion().equals(localVersion)) {
					Log.e("version", "???????????????");
					Message msg = new Message();
					msg.what = UPDATA_NONEED;
					mCallbackHandler.sendMessage(msg);
					// LoginMain();
				} else {
					Log.e("version", "?????????????????? ");
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					mCallbackHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				mCallbackHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
	private final int UPDATA_NONEED = -1;
	private final int UPDATA_CLIENT = -2;
	private final int GET_UNDATAINFO_ERROR = -3;
	private final int DOWN_ERROR = -4;
	private UpdateInfo info;
	private String localVersion;
	//?????????????????????
	protected void showUpdataDialog() {
		AlertDialog.Builder builer = new AlertDialog.Builder(this);
		builer.setTitle("????????????");
		builer.setMessage(info.getDescription());

		builer.setPositiveButton("??????", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Log.i("version", "??????apk,??????");
				downLoadApk();
			}
		});
		builer.setNegativeButton("??????", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//do sth
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}
	//?????????????????????
	protected void downLoadApk() {
		pd = new  ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		pd.setMessage("??????????????????");
		pd.show();
		new Thread(){
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(SysConfig.UPDATEURL+info.getUrl(), pd);
					installApk(file);
					pd.dismiss(); //???????????????????????????
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					mCallbackHandler.sendMessage(msg);
					e.printStackTrace();
				}
			}}.start();
	}

	//??????apk
	protected void installApk(File file) {
		Intent intent = new Intent();
		//????????????
		intent.setAction(Intent.ACTION_VIEW);

		intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		//?????????????????????
		intent.setDataAndType(getUriForFile(MainActivity.this,file), "application/vnd.android.package-archive");
		startActivity(intent);
	}

	public Uri getUriForFile(Context context, File file) {
		if (context == null || file == null) {
			throw new NullPointerException();
		}
		Uri uri;
		if (Build.VERSION.SDK_INT >= 24) {
			uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.robert.maps.fileprovider", file);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}

	//??????????????????
	public void checkVersion(){
		try {
			localVersion = getVersionName();
			CheckVersionTask cv = new CheckVersionTask();
			new Thread(cv).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//??????????????????
	private String getVersionName() throws Exception {
		//getPackageName()???????????????????????????0???????????????????????????
		PackageManager packageManager = getPackageManager();
		PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),
				0);
		return packInfo.versionName;
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

	public boolean getData(String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	public void setData(String key, Object o) {
		if (o != null) {
			Editor editor = mPreferences.edit();
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

	protected void showMessage(String msg) {
		Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private final SensorEventListener mListener = new SensorEventListener() {
		private int iOrientation = -1;

		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		@SuppressLint("WrongConstant")
		public void onSensorChanged(SensorEvent event) {
			if (iOrientation < 0) {
				iOrientation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
						.getDefaultDisplay().getOrientation();
			}

			mMyLocationOverlay.setmBearing(event.values[SensorManager.DATA_X] + 90 * iOrientation);

			if (mCompassEnabled) {
//				if (mNorthDirectionUp) {
				if (mDrivingDirectionUp == false || mLastSpeed == 0) {
					mMap.setBearing(updateBearing(event.values[0]) + 90 * iOrientation);
				}
//				}
			} else {
				mMap.setBearing(0);
			}

			mMap.Refresh();
		}
	};

	public void openPopu(){
		WindowManager.LayoutParams params=getWindow().getAttributes();
		params.alpha=0.7f;
		getWindow().setAttributes(params);
	}

	/**
	 * ????????????
	 */
	private void closePopup(boolean flag)
	{
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
			WindowManager.LayoutParams params=getWindow().getAttributes();
			params.alpha=1f;
			getWindow().setAttributes(params);
		}else {
			if (flag){
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
			}

		}
	}
	/***
	 * ??????????????????
	 */
	AlarmListAdapter alarmListAdapter;
	private void showAlarmDlg() {
		alarmListAdapter = new AlarmListAdapter(
				MainActivity.this, mAlarmList, MainActivity.unRectifyLocation);
		if (popupWindow!=null && popupWindow.isShowing()){
			popupWindow.dismiss();
		}
		popupWindow = new PopupWindow();
		int height_pop = getWindowManager().getDefaultDisplay().getHeight();
		int width_pop = getWindowManager().getDefaultDisplay().getWidth();
		popupWindow.setHeight(height_pop*3/4);
		popupWindow.setWidth(width_pop*9/10);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pop_up);
		animation.setFillAfter(true);//android?????????????????????????????????
		View view = LayoutInflater.from(this).inflate(R.layout.layout_alarm_list,null);
		popupWindow.setContentView(view);
		popupWindow.showAtLocation(findViewById(R.id.tv_main),Gravity.CENTER,0,0);
		view.startAnimation(animation);
		openPopu();
		ListView lv_alarm = view.findViewById(R.id.lv_alarm);
		ImageView iv_close = view.findViewById(R.id.iv_close);
		lv_alarm.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv_alarm.setAdapter(alarmListAdapter);

		lv_alarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				closePopup(false);
				Intent intent = new Intent(CNPC_LIST_ALARM);
				intent.putExtra("alarmPoint",mAlarmList.get(position));
				sendBroadcast(intent);
			}
		});

		iv_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

	}

	//????????????
	public void startSearchAround(TaskPoint mTaskPoint){
		new ZhuangHaoAroundQueryTask(
				GeoPoint.fromDouble(unRectifyLocation.getLatitude(),
						unRectifyLocation.getLongitude()), 0,mTaskPoint).execute("");
	}

	public class ZhuangHaoAroundQueryTask extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog progressDialog = null;
		List<ShotPoint> lstShotPoints = new ArrayList<ShotPoint>();
		GeoPoint centerPoint2d = null;
		double dis = 100;
		TaskPoint mTaskPoint;
		double[] arrDis = new double[] { 0.002, 0.005, 0.01, 0.05, 0.1,
				0.5 };
		public ZhuangHaoAroundQueryTask(GeoPoint center, double dis,TaskPoint mTaskPoint) {
			this.centerPoint2d = center;
			this.mTaskPoint = mTaskPoint;
			if (dis > 0) {
				this.dis = dis;
			}
		}

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(MainActivity.this);
			progressDialog.setMessage("??????????????????...");
			progressDialog.setCancelable(false);
			progressDialog.show();
			super.onPreExecute();
		}
		@Override
		protected Boolean doInBackground(String... strings) {
			SparseArray<ShotPoint> sparseArray = new SparseArray<ShotPoint>();
			sparseArray = mPointDBDao.selectShotListNotHidden(16, centerPoint2d, 0.001, 0.001);
			for (int i = 0; i < sparseArray.size(); i++) {
				lstShotPoints.add(sparseArray.get(sparseArray.keyAt(i)));
			}

			return lstShotPoints != null && lstShotPoints.size() > 0;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			if (!result) {
				showMessage("?????????????????????????????????");
				dialog = DialogUtils.AlertEdit(MainActivity.this, "???????????????????????????", "",
						new String[]{"??????", "??????"},
						new OnClickListener[]{new OnClickListener() {
							@Override
							public void onClick(View v) {
								//??????
								View view = dialog.getCurrentFocus();
								EditText editText = view.findViewById(R.id.dialog_message);
								String station = editText.getText().toString();
								Log.e("station", "station: "+station);
								matchingStation(mTaskPoint.spointNo,station,mTaskPoint.stationNo);
								dialog.dismiss();
							}
						}, new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						},new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						}
						});
				dialog.show();
			} else {
				showTaskAroundSelectDlg(lstShotPoints,mTaskPoint);

			}
		}
	}

	/***
	 * ??????????????????????????? shotpoints
	 */
	private void showTaskAroundSelectDlg(final List<ShotPoint> shotPoints,TaskPoint mTaskPoint) {
		final ZhuangHaoAroundSelectAdapter adapter = new ZhuangHaoAroundSelectAdapter(
				MainActivity.this, shotPoints, MainActivity.unRectifyLocation);

		// ??????????????????
		int defIndex = 0;
		adapter.setSelectedItem(defIndex);
		new AlertDialog.Builder(this)
				.setSingleChoiceItems(adapter, defIndex,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								// TODO Auto-generated method stub
								adapter.setSelectedItem(which);
								adapter.notifyDataSetChanged();
								adapter.notifyDataSetInvalidated();
							}
						})
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
												int which) {
								ShotPoint shotPoint = shotPoints.get(adapter
										.getSelectedItem());
								mMap.getController().setCenter(shotPoint.geoPoint);
								String station = shotPoint.stationNo;
								Log.e("station", station);
								if (station!=null){
									matchingStation(mTaskPoint.spointNo,station,mTaskPoint.stationNo);
								}
							}
						}).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialogs,
								int which) {
				dialog = DialogUtils.AlertEdit(MainActivity.this, "???????????????", "",
						new String[]{"??????", "??????"},
						new OnClickListener[]{new OnClickListener() {

							@Override
							public void onClick(View v) {
								//??????
								View view = dialog.getCurrentFocus();
								EditText editText = view.findViewById(R.id.dialog_message);
								editText.setHint("???????????????");
								String station = editText.getText().toString();
								Log.e("station", "station: "+station);
								matchingStation(mTaskPoint.spointNo,station,mTaskPoint.stationNo);
								dialog.dismiss();
							}
						}, new OnClickListener() {

							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						},new OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						}
						});
				dialog.show();

			}
		}).create()
				.show();
	}

	//------------------------------------------------------????????????-------------------------------------------------------------------

	/*VoiceEntity voiceEntity;
	@Override
	public void onEvent(String name, String params, byte[] data, int offset, int length) {
		String logTxt = "name: " + name;
		if (params != null && !params.isEmpty()) {
			logTxt += " ;params :" + params;
		}
		if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
			if (params != null && params.contains("\"nlu_result\"")) {
				if (length > 0 && data.length > 0) {
					logTxt += ", ?????????????????????" + new String(data, offset, length);
				}
			}
		} else if (data != null) {
			logTxt += " ;data length=" + data.length;
		}
		Log.e("logTxt", logTxt);
		voiceEntity = new VoiceEntity();
		voiceEntity = JSON.parseObject(params,VoiceEntity.class);
		if (voiceEntity!=null && voiceEntity.getResult_type()!=null){
			if (voiceEntity.getResult_type().equals("partial_result")){
				String content = voiceEntity.getBest_result().toString();
				Log.e("partial_result", "partial_result: "+content );
				tv_title.setVisibility(View.INVISIBLE);
				tv_content.setText(content);
			}else if (voiceEntity.getResult_type().equals("final_result")){
				String final_content = voiceEntity.getBest_result().toString();
				Log.e("final_result", "final_result: "+final_content );
				tv_content.setText(final_content);
				if (final_content.contains("??????") && final_content.length()>2){
//					String stationNum = final_content.split("???")[1];
					String regex = "[^0-9]";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(final_content);
					String stationNum = m.replaceAll("").trim();
//                    m.find();
//					stationNum = m.group();
					Log.e("voice", "stationNum: "+stationNum);
					if (stationNum!=null){
						ShotPoint shotPoint = mPointDBDao.selectShotPoint(stationNum);
						if (shotPoint!=null){
							Intent intentx = new Intent(ACTION_POINT_LOC);
							intentx.putExtra("stationNo",stationNum);
							sendBroadcast(intentx);
						}else {
							tv_content.setText("???????????????");
						}
					}

				}else if (final_content.contains("??????") && final_content.length()>2){
					String stationNum = final_content.split("???")[1];
					if (stationNum!=null) {
						ShotPoint shotPoint = mPointDBDao.selectShotPoint(stationNum);
						if (shotPoint!=null){
							Message message = new Message();
							message.what = RequestCode.TASK_GUIDE;
							message.obj = shotPoint;
							mCallbackHandler.sendMessage(message);
						}else {
							tv_content.setText("???????????????");
						}
					}
				}else {
					tv_content.setText("???????????????????????????");
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						voice_popupWindow.dismiss();
						tv_title.setVisibility(View.VISIBLE);
						tv_title.setText("???????????????");
						tv_content.setText("?????????+?????????  \n?????????+????????? ");
					}
				},1500);
			}

		}else if (voiceEntity!=null && voiceEntity.getError()!=0){
			voice_popupWindow.dismiss();
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText("???????????????");
			tv_content.setText("?????????+?????????  \n?????????+????????? ");
		}

	}



	*//**
	 * enableOffline??????true?????????onCreate?????????
	 * ??????SDK???????????????1.4 ??????????????????(???????????????)
	 *//*
	private void loadOfflineEngine() {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put(SpeechConstant.DECODER, 2);
		params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
		asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
	}

	*//**
	 * enableOffline???true?????????onDestory???????????????loadOfflineEngine??????
	 * ??????SDK??????5.1 ????????????????????????(???????????????)
	 *//*
	private void unloadOfflineEngine() {
		asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
	}
	*//**
	 * ??????SDK??????2.2 ??????????????????
	 * ??????????????????
	 * ????????????????????????
	 *//*
	private void start() {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		String event = null;
		event = SpeechConstant.ASR_START; // ??????????????????event

		if (enableOffline) {
			params.put(SpeechConstant.DECODER, 2);
		}
		// ??????SDK??????2.1 ??????????????????
		params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
		// params.put(SpeechConstant.NLU, "enable");
		// params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // ?????????
		// params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
		// params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
		// params.put(SpeechConstant.PID, 1537); // ?????????????????????????????????

		*//* ????????????????????????????????? *//*
		// params.put(SpeechConstant.PID, 8002);
		// ???????????????????????????pid???8002????????????????????????????????? 1537  ?????????8001??????8002???????????????????????????????????????
		// params.put(SpeechConstant.LMID,1068); // ???????????????????????????????????????ID???https://ai.baidu.com/smartasr/model
		// ????????????ID???????????????appId????????????????????????
		*//* ????????????????????????????????? *//*

		String json = null; // ????????????????????????json
		json = new JSONObject(params).toString(); // ???????????????????????????????????????json
		asr.send(event, json, null, 0, 0);
		Log.e("json", json );
	}

	*//**
	 * ??????????????????
	 *  ??????SDK??????4.1 ??????????????????
	 *//*
	private void stop() {
		Log.e("ASR_STOP", "???????????????ASR_STOP");
		asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
	}
*/
}
