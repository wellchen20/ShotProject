package com.jpkh.cnpc.activity;


import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jpkh.cnpc.R;
import com.jpkh.cnpc.activity.entity.AppImei;
import com.jpkh.cnpc.activity.interfaces.HttpUtil;
import com.jpkh.cnpc.activity.interfaces.RetrofitService;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.cnpc.sqlite.PointDBDao;
import com.robert.maps.applib.utils.DialogUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;

/**
 * A simple {@link Fragment} subclass.
 * 个人中心
 */
public class PersonalFragment extends Fragment {

    ImageView btn_exit;
    View view;
    TextView tv_version;
    TextView tv_username;
    LinearLayout ll_version;
    LinearLayout ll_settting;
    LinearLayout ll_online;
    LinearLayout ll_task;
    LinearLayout ll_clear;
    LinearLayout ll_allset;
    LinearLayout ll_refush;
    LinearLayout ll_person_set;
    boolean is_record = false;
//    LinearLayout ll_around;
    protected PointDBDao mPointDBDao;
    Map<String,String> map;
    String ACTION_POINT_LOC = "action_point_loc";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        setViews();
        setListeners();
        process();
        return view;
    }

    private void setViews() {
        mPointDBDao = new PointDBDao(getActivity());
        btn_exit = view.findViewById(R.id.btn_exit);
        tv_version = view.findViewById(R.id.tv_version);
        tv_username = view.findViewById(R.id.tv_username);
        ll_version = view.findViewById(R.id.ll_version);
        ll_settting = view.findViewById(R.id.ll_settting);
        ll_online = view.findViewById(R.id.ll_online);
        ll_task = view.findViewById(R.id.ll_task);
        ll_clear = view.findViewById(R.id.ll_clear);
        ll_refush = view.findViewById(R.id.ll_refush);
        ll_allset = view.findViewById(R.id.ll_allset);
        ll_person_set = view.findViewById(R.id.ll_person_set);
//        ll_around = view.findViewById(R.id.ll_around);
        tv_username.setText(((MainActivity)getActivity()).getData(SysContants.USERNAME, ""));
        is_record = ((MainActivity)getActivity()).getData(SysContants.RECORD, false);

        // 获取packagemanager的实例
        PackageManager packageManager = getActivity().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try{
            PackageInfo packInfo = packageManager.getPackageInfo(getActivity().getPackageName(),0);
            String version = packInfo.versionName;
            tv_version.setText(version);
        }catch (Exception e){};

    }

    private void setListeners() {
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ll_version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"当前为最新版本",Toast.LENGTH_SHORT).show();
                showAutoStartDialog();
            }
        });

        ll_settting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), ServerSettingActivity.class));
            }
        });

        ll_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReceiveOnlineSetActivity.class);
                startActivityForResult(intent,200);
            }
        });

        ll_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReceivePointsActivity.class);
                startActivityForResult(intent,200);
            }
        });

        ll_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showClearDataDialog();
            }
        });

        ll_allset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ReceiveAllSetActivity.class));
            }
        });

        ll_refush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRefushDialog();
            }
        });

        ll_person_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PersonSetActivity.class));
            }
        });

    }

    /**
     * 注销用户
     */
    public void logout() {
        String userName = ((MainActivity)getActivity()).getData(SysContants.USERNAME, "");
        String passWord = ((MainActivity)getActivity()).getData(SysContants.PASSWORD, "");
        ((MainActivity)getActivity()).setData(SysContants.USERNAME, "");
        ((MainActivity)getActivity()).setData(SysContants.PASSWORD, "");
        getActivity().finish();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.putExtra("userName",userName);
        intent.putExtra("passWord",passWord);
        startActivity(intent);
    }

    private Dialog dialog;
    public void showClearDataDialog() {
        dialog = DialogUtils.Alert(getActivity(), "提示", "清空数据和缓存？",
                new String[]{getActivity().getString(R.string.ok), getActivity().getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        delStationCache();
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

    public void showRefushDialog() {
        dialog = DialogUtils.Alert(getActivity(), "提示", "是否刷新数据（慎用）？",
                new String[]{getActivity().getString(R.string.ok), getActivity().getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //
                        refush();
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

    public void process(){
        String tokenId = ((MainActivity)getActivity()).getData(SysContants.TOKENID, "");
        String accessToken =((MainActivity)getActivity()).getData(SysContants.ACCESSTOKEN, "");
        map = new HashMap<>();
        map.put("tokenId",tokenId);
        map.put("accessToken",accessToken);
    }

    public void refush(){
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).refreshAllDevice(map), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(),"刷新最新点位失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("result", "result: "+result);
                    if (result.contains("200")){
                        Toast.makeText(getActivity(),"刷新最新点位成功",Toast.LENGTH_SHORT).show();
                    }else if (result.contains("500")){
                        Toast.makeText(getActivity(),"请求频繁，请稍后刷新",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    public void delStationCache(){
        AppImei mAppImei = new AppImei();
        mAppImei.setAppImei(((MainActivity)getActivity()).getAppImei());
        Gson gson = new Gson();
        String json = gson.toJson(mAppImei);
        Log.e("json", "json: "+json);
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        HttpUtil.init( HttpUtil.getService(RetrofitService.class).delStationCacheNew(map,body), new Subscriber<ResponseBody>() {

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getActivity(),"清除失败",Toast.LENGTH_SHORT).show();
                Log.e("onError", "onError: "+e.getMessage());
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String result = responseBody.string();
                    Log.e("result", "result: "+result);
                    if (result.contains("200")){
                        // 清空数据库
                        mPointDBDao.deleteAllShot();
                        mPointDBDao.deleteAllDrillPoint();
                        dialog.dismiss();
                        Toast.makeText(getActivity(),"清除成功",Toast.LENGTH_SHORT).show();
                    }else if (result.contains("500")){
                        Toast.makeText(getActivity(),"请求频繁，请稍后刷新",Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
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

    public void showAutoStartDialog() {
        dialog = DialogUtils.Alert(getActivity(), "自启动", "建议您开启自启动和通知更好的接收消息！",
                new String[]{getString(R.string.ok), getString(R.string.cancel)},
                new View.OnClickListener[]{new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //
                        requestIgnoreBatteryOptimizations(getActivity());
                        gotoWhiteListSetting(getActivity());
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
