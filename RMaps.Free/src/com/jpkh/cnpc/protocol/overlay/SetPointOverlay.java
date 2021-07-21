package com.jpkh.cnpc.protocol.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.robert.maps.applib.OnlineSetEntity;
import com.robert.maps.applib.R;
import com.robert.maps.applib.view.TileView;
import com.robert.maps.applib.view.TileViewOverlay;

/**
 * 设备图层
 *
 * @author CW
 *
 */
public class SetPointOverlay extends TileViewOverlay {

	private Paint mPaint = new Paint();
	private Paint mPaintText = new Paint();
	private Bitmap mBitmap_press;
	private Bitmap mPerson;
	private int mPressWidth,mPressHeight,mPersonWidth,mPersonHeight;
	private int mTapId;
	public static int NO_TAP = -9999;
	OnlineSetEntity mOnlineSetEntity;
	//	private LinearLayout msgbox = null;
	public SetPointOverlay(Context ctx){
		mPaint.setAntiAlias(true);
		mPaintText.setColor(Color.GREEN);
		mPaintText.setTextSize(ctx.getResources().getDimensionPixelSize(R.dimen.measuretool_label_size));
		mPaintText.setAntiAlias(true);
		this.mBitmap_press = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.point_online);
		this.mPerson = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.set);
		mPressWidth = mBitmap_press.getWidth();
		mPressHeight = mBitmap_press.getHeight();
		mPersonWidth = mPerson.getWidth();
		mPersonHeight = mPerson.getHeight();
	}
	@Override
	protected void onDraw(Canvas c, TileView tileView) {
		TileView.OpenStreetMapViewProjection pj = tileView.getProjection();
		Point curScreenCoords = new Point();
		Log.e("onDraw", "onDraw");
		if (mOnlineSetEntity!=null && mOnlineSetEntity.getData().size()!=0){
			for (int i=0;i<mOnlineSetEntity.getData().size();i++){
				pj.toPixels(mOnlineSetEntity.getData().get(i).getPoint(), curScreenCoords);
				c.save();
				c.rotate(tileView.getBearing(), curScreenCoords.x, curScreenCoords.y);
				onDrawItem(c, mOnlineSetEntity.getData().get(i),curScreenCoords);
				c.restore();
			}
		}
	}


	@Override
	protected void onDrawFinished(Canvas c, TileView tileView) {

	}

	public void refushAlarmPoint(OnlineSetEntity mOnlineSetEntity, TileView mapView){
		this.mOnlineSetEntity = mOnlineSetEntity;
		mapView.invalidate();
	}

	public void onDrawItem(Canvas c, OnlineSetEntity.DataBean point, Point p1){
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
			mapView.mSetMenuInfo.MarkerIndex = id;
			mapView.mSetMenuInfo.mOnlineSetEntity = this.mOnlineSetEntity;
			mapView.mSetMenuInfo.EventGeoPoint = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY(), mapView.getBearing());
			mapView.showContextMenu();
			Log.e("onSingleTapUp", "onSingleTapUp: "+"222" );
			return true;
		}


		return super.onSingleTapUp(event, mapView);
	}


	public int getMarkerAtPoint(final int eventX, final int eventY, TileView mapView){
		if(this.mOnlineSetEntity != null){
			final TileView.OpenStreetMapViewProjection pj = mapView.getProjection();

			final Rect curMarkerBounds = new Rect();
			final Point mCurScreenCoords = new Point();


			for(int i = 0; i < this.mOnlineSetEntity.getData().size(); i++){
				final OnlineSetEntity.DataBean mItem = this.mOnlineSetEntity.getData().get(i);
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


}