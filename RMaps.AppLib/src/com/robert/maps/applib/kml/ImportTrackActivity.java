package com.robert.maps.applib.kml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.openintents.filemanager.util.FileUtils;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.robert.maps.applib.R;
import com.robert.maps.applib.kml.XMLparser.GpxTrackParser;
import com.robert.maps.applib.kml.XMLparser.KmlTrackParser;
import com.robert.maps.applib.utils.DialogUtils;
import com.robert.maps.applib.utils.SimpleThreadFactory;
import com.robert.maps.applib.utils.Ut;

public class ImportTrackActivity extends Activity {
	private ListView lv_import_track;
	
	private PoiManager mPoiManager;
	private List<File> trackFiles = new ArrayList<File>();
	private ImportFileListAdapter mTrackFileListAdapter;
	private Dialog dialog;
	private File selectedFile;

	private ProgressDialog dlgWait;
	protected ExecutorService mThreadPool = Executors.newSingleThreadExecutor(new SimpleThreadFactory("ImportTrack"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences settings = getPreferences(Activity.MODE_PRIVATE);
		this.setContentView(R.layout.importtrack);

		if (mPoiManager == null)
			mPoiManager = new PoiManager(this);

		initViews();
		initDatas();
	}

	private void initViews() {
		lv_import_track = (ListView) findViewById(R.id.lv_import_track);
		
		((Button) findViewById(R.id.ImportBtn))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (selectedFile == null) {
					DialogUtils.alertInfo(ImportTrackActivity.this, getResources().getString(R.string.reminder), "??????????????????????????????");
				} else {
					doImportTrack(selectedFile);// ???????????????
				}
			}
		});
		((Button) findViewById(R.id.discardButton))
		.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ImportTrackActivity.this.finish();
			}
		});
		findViewById(R.id.ll_title_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ImportTrackActivity.this.finish();
			}
		});;
		((TextView) findViewById(R.id.tv_title_title)).setText("????????????");
	}

	private void initDatas() {
		trackFiles = FileUtils.getTracksList(ImportTrackActivity.this);
		mTrackFileListAdapter = new ImportFileListAdapter(ImportTrackActivity.this, trackFiles);
		lv_import_track.setAdapter(mTrackFileListAdapter);
		lv_import_track.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				File file = trackFiles.get(position);
				selectedFile = file;
				
				mTrackFileListAdapter.setSelectedIndex(position);
				return;
				
			}
		});
		lv_import_track.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				dialog = DialogUtils.Alert(ImportTrackActivity.this, getResources().getString(R.string.reminder), 
						getResources().getString(R.string.question_delete, getText(R.string.poi)),
						new String[]{getResources().getString(R.string.ok), getResources().getString(R.string.cancel)},
						new OnClickListener[]{new OnClickListener() {
						
							@Override
							public void onClick(View v) {
								if (dialog != null && dialog.isShowing()) {
									dialog.dismiss();
								}
								
								final File file = trackFiles.get(position);
								file.delete();
								trackFiles = FileUtils.getInsterestsList(ImportTrackActivity.this);
								mTrackFileListAdapter.setmFiles(trackFiles);
							}
						}, new OnClickListener() {
						
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						}
				});
				return false;
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == R.id.dialog_wait) {
			dlgWait = new ProgressDialog(this);
			dlgWait.setMessage("Please wait while loading...");
			dlgWait.setIndeterminate(true);
			dlgWait.setCancelable(false);
			return dlgWait;
		}
		return null;
	}
	
	private void doImportTrack(final File file) {
		if(!file.exists()){
			Toast.makeText(this, "No such file", Toast.LENGTH_LONG).show();
			return;
		}

		showDialog(R.id.dialog_wait);

		this.mThreadPool.execute(new Runnable() {
			public void run() {
				SAXParserFactory fac = SAXParserFactory.newInstance();
				SAXParser parser = null;
				try {
					parser = fac.newSAXParser();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(parser != null){
					mPoiManager.beginTransaction();
					Ut.dd("Start parsing file " + file.getName());
					try {
						if(FileUtils.getExtension(file.getName()).equalsIgnoreCase(".kml"))
							parser.parse(file, new KmlTrackParser(mPoiManager));
						else if(FileUtils.getExtension(file.getName()).equalsIgnoreCase(".gpx"))
							parser.parse(file, new GpxTrackParser(mPoiManager));

						mPoiManager.commitTransaction();
					} catch (SAXException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mPoiManager.rollbackTransaction();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						mPoiManager.rollbackTransaction();
					} catch (IllegalStateException e) {
					} catch (OutOfMemoryError e) {
						mPoiManager.rollbackTransaction();
					}
					Ut.dd("Pois commited");
				}

				dlgWait.dismiss();
				ImportTrackActivity.this.finish();
			};
		});

	}


	@Override
	protected void onDestroy() {
		mThreadPool.shutdown();
		super.onDestroy();
		mPoiManager.FreeDatabases();
	}

}
