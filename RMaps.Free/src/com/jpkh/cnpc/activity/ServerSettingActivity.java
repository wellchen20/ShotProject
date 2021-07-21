package com.jpkh.cnpc.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jpkh.cnpc.R;
import com.jpkh.cnpc.protocol.constants.SysConfig;
import com.jpkh.cnpc.protocol.constants.SysContants;
import com.jpkh.utils.StatusBarUtil;
/**
 * 注册页
 * 
 */
public class ServerSettingActivity extends Activity implements OnClickListener {

	private EditText tv_ip_1, tv_ip_2, tv_ip_3, tv_ip_4, tv_port;
	private Button btn_ok;
	private SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_setting);
		mPreferences = getSharedPreferences(SysContants.SYSCONFIG, MODE_PRIVATE);
		StatusBarUtil.setTheme(this);
		initViews();
		eventViews();
		initDates();
	}

	private void initViews() {
		tv_ip_1 = (EditText) findViewById(R.id.tv_ip_1);
		tv_ip_2 = (EditText) findViewById(R.id.tv_ip_2);
		tv_ip_3 = (EditText) findViewById(R.id.tv_ip_3);
		tv_ip_4 = (EditText) findViewById(R.id.tv_ip_4);
		tv_port = (EditText) findViewById(R.id.tv_port);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		findViewById(R.id.ll_title_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ServerSettingActivity.this.finish();
			}
		});;
		((TextView) findViewById(R.id.tv_title_title)).setText("项目配置");
	}

	private void eventViews() {
		btn_ok.setOnClickListener(this);
	}
	
	private void initDates() {
		String[] ip = SysConfig.IP.split("\\.");
		tv_ip_1.setText(ip[0]);
		tv_ip_2.setText(ip[1]);
		tv_ip_3.setText(ip[2]);

		tv_ip_4.setText(ip[3]);
		tv_port.setText(SysConfig.PORT + "");
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			serverSave();
			break;
		}
	}

	private void serverSave() {
		if (saveIP()) {
			if (savePort()) {
				showMessage("设置成功，请重启软件生效");
				setData(SysContants.WIFI_IP, SysConfig.IP);
				setData(SysContants.WIFI_PORT, SysConfig.PORT);
			}
		}
	}
	
	private boolean saveIP() {
		boolean isSuccess = false;
		String ip_1 = tv_ip_1.getText().toString()
				.trim();
		String ip_2 = tv_ip_2.getText().toString()
				.trim();
		String ip_3 = tv_ip_3.getText().toString()
				.trim();
		String ip_4 = tv_ip_4.getText().toString()
				.trim();
		if (!ip_1.equalsIgnoreCase("") && !ip_2.equalsIgnoreCase("") && !ip_3.equalsIgnoreCase("") && !ip_4.equalsIgnoreCase("")
				&& Integer.parseInt(ip_1) > 0 && Integer.parseInt(ip_1) < 256 && Integer.parseInt(ip_2) >= 0 && Integer.parseInt(ip_2) < 256
				&& Integer.parseInt(ip_3) >= 0 && Integer.parseInt(ip_3) < 256 && Integer.parseInt(ip_4) >= 0 && Integer.parseInt(ip_4) < 255) {
			
			SysConfig.IP = new StringBuffer().append(ip_1).append(".")
					.append(ip_2).append(".").append(ip_3).append(".").append(ip_4).toString();
			isSuccess  = true;
		} else {
			showMessage("ip无效，请重新输入");
			isSuccess = false;
		}
		return isSuccess;
	}
	
	private boolean savePort() {
		boolean isSuccess = false;
		String info = tv_port.getText().toString()
				.trim();
		if (!info.equalsIgnoreCase("")) {
			try {
				SysConfig.PORT = Integer.parseInt(info);
				isSuccess  = true;
			} catch (Exception e) {
				isSuccess = false;
			}
		} else {
			showMessage("请输入端口号");
			isSuccess = false;
		}
		return isSuccess;
	}


	private void showMessage(String value) {
		Toast.makeText(ServerSettingActivity.this,value,Toast.LENGTH_LONG).show();
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
}
