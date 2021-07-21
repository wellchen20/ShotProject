package com.jpkh.cnpc.protocol.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.robert.maps.applib.AlarmPoint;
import com.robert.maps.applib.R;
import com.robert.maps.applib.view.TileView;
import com.robert.maps.applib.view.TileViewOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * 位置分享图层
 *
 * @author CW
 *
 */
public class AlarmPointOverlay extends TileViewOverlay {

	private Paint mPaint = new Paint();
	private Paint mPaintText = new Paint();
	private Bitmap mBitmap_press;
	private Bitmap mPerson;
	private int mPressWidth,mPressHeight,mPersonWidth,mPersonHeight;
	private AlarmPoint mAlarmPoint;
	protected OnItemTapListener<AlarmPoint> mOnItemTapListener;
	private List<AlarmPoint> pointArrayList = new ArrayList<>();
	private int mTapId;
	public static int NO_TAP = -9999;
	//	private LinearLayout msgbox = null;
	public void setTapIndex(int mTapIndex) {
		this.mTapId = mTapIndex;
	}
	public AlarmPointOverlay(Context ctx,OnItemTapListener<AlarmPoint> onItemTapListener){
		mPaint.setAntiAlias(true);
		mPaintText.setColor(Color.GREEN);
		mPaintText.setTextSize(ctx.getResources().getDimensionPixelSize(R.dimen.measuretool_label_size));
		mPaintText.setAntiAlias(true);
		this.mBitmap_press = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.point_online);
		this.mPerson = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.icon_alarm);
		mPressWidth = mBitmap_press.getWidth();
		mPressHeight = mBitmap_press.getHeight();
		mPersonWidth = mPerson.getWidth();
		mPersonHeight = mPerson.getHeight();
		this.mOnItemTapListener = onItemTapListener;
	}
	@Override
	protected void onDraw(Canvas c, TileView tileView) {
		com.robert.maps.applib.view.TileView.OpenStreetMapViewProjection pj = tileView.getProjection();
		Point curScreenCoords = new Point();
		if (pointArrayList!=null && pointArrayList.size()!=0){
			for (int i=0;i<pointArrayList.size();i++){
				pj.toPixels(pointArrayList.get(i).getPoint(), curScreenCoords);
				c.save();
				c.rotate(tileView.getBearing(), curScreenCoords.x, curScreenCoords.y);
				onDrawItem(c, pointArrayList.get(i),curScreenCoords);
				c.restore();
			}
		}
	}


	@Override
	protected void onDrawFinished(Canvas c, TileView tileView) {

	}



	public void addAlarmPoint(AlarmPoint point, TileView mapView){
		pointArrayList.add(point);
		mapView.invalidate();
	}

	public void refushAlarmPoint(List<AlarmPoint> mAlarmList, TileView mapView){
		pointArrayList.clear();
		pointArrayList.addAll(mAlarmList);
		mapView.invalidate();
	}

	public void onDrawItem(Canvas c, AlarmPoint point, Point p1){
		if (p1!=null && point!=null){
			c.drawBitmap(mBitmap_press,p1.x - (int)(mPressWidth/2), p1.y - (int)(mPressHeight / 2), mPaint);
			c.drawBitmap(mPerson,p1.x - (int)(mPersonWidth/2), p1.y - (int)(mPersonHeight+mPressHeight)/3*2, mPaint);
			c.drawText(point.getDeviceName(),p1.x+(int)(mPersonWidth/2),p1.y - (int)(mPersonHeight/2),mPaintText);
		}
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event, TileView mapView) {
		final int id = getMarkerAtPoint((int)event.getX(), (int)event.getY(), mapView);
		if (id > NO_TAP) {
			mapView.mAlarmMenuInfo.MarkerIndex = id;
			for (int i=0;i<pointArrayList.size();i++){
				if (pointArrayList.get(i).getId()==id){
					mTapId = i;
				}
			}
			mapView.mAlarmMenuInfo.mAlarmPoint = pointArrayList.get(mTapId);
			mapView.mAlarmMenuInfo.EventGeoPoint = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY(), mapView.getBearing());
			mapView.showContextMenu();
			return true;
		}


		return super.onSingleTapUp(event, mapView);
	}


	public int getMarkerAtPoint(final int eventX, final int eventY, TileView mapView){
		if(this.pointArrayList != null){
			final com.robert.maps.applib.view.TileView.OpenStreetMapViewProjection pj = mapView.getProjection();

			final Rect curMarkerBounds = new Rect();
			final Point mCurScreenCoords = new Point();


			for(int i = 0; i < this.pointArrayList.size(); i++){
				final AlarmPoint mItem = this.pointArrayList.get(i);
				pj.toPixels(mItem.getPoint(), mapView.getBearing(), mCurScreenCoords);

				final int left = (int)(mCurScreenCoords.x - (int) this.mPersonWidth / 2);
				final int right = (int)(mCurScreenCoords.x + (int) this.mPersonWidth / 2);
				final int top = (int)(mCurScreenCoords.y - (int) this.mPersonHeight / 2);
				final int bottom = (int)(mCurScreenCoords.y + (int) this.mPersonHeight / 2);

				curMarkerBounds.set(left, top, right, bottom);
				if(curMarkerBounds.contains(eventX, eventY))
					return mItem.getId();
			}
		}

		return NO_TAP;
	}

	protected boolean onTap(int id) {
		if(mTapId == id)
			mTapId = NO_TAP;
		else
			mTapId = id;

		if(this.mOnItemTapListener != null)
			return this.mOnItemTapListener.onItemTap(id, this.pointArrayList.get(id));
		else
			return false;
	}

	@SuppressWarnings("hiding")
	public static interface OnItemTapListener<DrillPoint>{
		public boolean onItemTap(final int aIndex, final AlarmPoint aItem);
	}

}