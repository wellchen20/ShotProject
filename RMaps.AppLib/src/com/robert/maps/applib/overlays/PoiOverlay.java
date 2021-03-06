package com.robert.maps.applib.overlays;

import org.andnav.osm.util.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.robert.maps.applib.R;
import com.robert.maps.applib.kml.PoiManager;
import com.robert.maps.applib.kml.PoiPoint;
import com.robert.maps.applib.utils.GpsCorrect;
import com.robert.maps.applib.view.TileView;
import com.robert.maps.applib.view.TileViewOverlay;

/**
 * 兴趣点图层
 * 
 * @author DRH
 *
 */
public class PoiOverlay extends TileViewOverlay {
	private Context mCtx;
	private PoiManager mPoiManager;
	private int mTapId;
	private GeoPoint mLastMapCenter;
	private int mLastZoom;
	private PoiListThread mThread;
	private RelativeLayout mT;
	private float mDensity;
	private boolean mNeedUpdateList = false;
	public static int NO_TAP = -9999;
	public Bitmap mBitmap_press;

	protected OnItemTapListener<PoiPoint> mOnItemTapListener;
	protected OnItemLongPressListener<PoiPoint> mOnItemLongPressListener;
	protected SparseArray<PoiPoint> mItemList;
	protected final Point mMarkerHotSpot;
	protected final int mMarkerWidth, mMarkerHeight;
	private boolean mCanUpdateList = true;

	public int getTapIndex() {
		return mTapId;
	}

	public void setTapIndex(int mTapIndex) {
		this.mTapId = mTapIndex;
	}
	
	public void UpdateList() {
		mNeedUpdateList = true;
	}

	public PoiOverlay(Context ctx, PoiManager poiManager,
			OnItemTapListener<PoiPoint> onItemTapListener, boolean hidepoi)
	{
		mCtx = ctx;
		mPoiManager = poiManager;
		mCanUpdateList = !hidepoi;
		mTapId = NO_TAP;

		Drawable marker = ctx.getResources().getDrawable(R.drawable.poi);
		this.mMarkerWidth = marker.getIntrinsicWidth();
		this.mMarkerHeight = marker.getIntrinsicHeight();
		this.mMarkerHotSpot = new Point(0, mMarkerHeight);

        this.mOnItemTapListener = onItemTapListener;

        mLastMapCenter = null;
        mLastZoom = -1;
        mThread = new PoiListThread();// 获取范围内兴趣点

		this.mT = (RelativeLayout) LayoutInflater.from(ctx).inflate(R.layout.poi_descr, null);
		this.mT.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mDensity = metrics.density;
		
		this.mBitmap_press = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.poi_marker_pressed);
	}
	
	public void clearPoiList() {
		if(mItemList == null)
			mItemList = new SparseArray<PoiPoint>();
		else
			mItemList.clear();
	}

	public void setGpsStatusGeoPoint(final int id, final GeoPoint geopoint, final String title, final String descr) {
		PoiPoint poi = new PoiPoint(id, title, descr, geopoint, 0, R.drawable.poi_satttelite);

		if(mItemList == null)
			mItemList = new SparseArray<PoiPoint>();

		mItemList.put(poi.getId(), poi);
		mCanUpdateList = false;
		mTapId = NO_TAP;
	}

	@Override
	public void onDraw(Canvas c, TileView mapView) {
		final com.robert.maps.applib.view.TileView.OpenStreetMapViewProjection pj = mapView.getProjection();
		final Point curScreenCoords = new Point();

		if (mCanUpdateList){
			boolean looseCenter = false;
			GeoPoint center = mapView.getMapCenter();
			GeoPoint lefttop = pj.fromPixels(0, 0);
			double deltaX = Math.abs(center.getLongitude() - lefttop.getLongitude());
			double deltaY = Math.abs(center.getLatitude() - lefttop.getLatitude());

			if (mLastMapCenter == null || mLastZoom != mapView.getZoomLevel())
				looseCenter = true;
			else if(0.7 * deltaX < Math.abs(center.getLongitude() - mLastMapCenter.getLongitude()) || 0.7 * deltaY < Math.abs(center.getLatitude() - mLastMapCenter.getLatitude()))
				looseCenter = true;

			if(looseCenter || mNeedUpdateList){
				mLastMapCenter = center;
				mLastZoom = mapView.getZoomLevel();
				mNeedUpdateList = false;

				mThread.setParams(1.5*deltaX, 1.5*deltaY);
				mThread.run();
			}
		}

		if (this.mItemList != null) {

			/*
			 * Draw in backward cycle, so the items with the least index are on
			 * the front.
			 */
			for (int i = 0; i < this.mItemList.size(); i++) {
				PoiPoint item = this.mItemList.valueAt(i);
//				if (mapView.getTileSource().ID.equals("googlemap_hd") || mapView.getTileSource().ID.equals("qqmap3_hd")
//						|| mapView.getTileSource().ID.equals("bdmap2") || mapView.getTileSource().ID.equals("gaodemap")
//						|| mapView.getTileSource().ID.equals("googleweixing") || mapView.getTileSource().ID.equals("googleweixing_gaode")
//						|| mapView.getTileSource().ID.equals("googleweixing_google") || mapView.getTileSource().ID.equals("qqmap2weixing_")
//						|| mapView.getTileSource().ID.equals("googlemap") || mapView.getTileSource().ID.equals("qqmap3")
//						|| mapView.getTileSource().ID.equals("sgmap") || mapView.getTileSource().ID.equals("googledixing")
//						|| mapView.getTileSource().ID.equals("mapabc") || mapView.getTileSource().ID.equals("tianditu")
//						|| mapView.getTileSource().ID.equals("opencyclemap") || mapView.getTileSource().ID.equals("opencyclemap2") 
//						|| mapView.getTileSource().ID.equals("opencyclemap3")) {
//					/**
//					 * 纠偏
//					 */
					/*double[] latlon = new double[2];
					GpsCorrect.transform(item.GeoPoint.getLatitude(), item.GeoPoint.getLongitude(), latlon);
					item.GeoPoint = GeoPoint.fromDouble(latlon[0], latlon[1]);*/
//				}
				
				pj.toPixels(item.GeoPoint, curScreenCoords);
				c.save();
				c.rotate(mapView.getBearing(), curScreenCoords.x, curScreenCoords.y);
				onDrawItem(c, item, curScreenCoords);
				c.restore();
				
//				if (mTapId == item.getId()) {
//					pj.toPixels(item.GeoPoint, curScreenCoords);
//					c.rotate(mapView.getBearing(), curScreenCoords.x, curScreenCoords.y);
//					onDrawItem(c, item, curScreenCoords);
//					c.drawBitmap(mBitmap_press, curScreenCoords.x - (int)(mBitmap_press.getWidth() / 2), curScreenCoords.y - (int)(mBitmap_press.getHeight()), new Paint());
//					c.restore();
//					c.save();
//					
//				}
			}
		}
	}

	protected void onDrawItem(Canvas c, final PoiPoint focusedItem, Point screenCoords) {
		
		final int left = screenCoords.x - this.mMarkerHotSpot.x;
		final int right = left + this.mMarkerWidth;
		final int top = screenCoords.y - this.mMarkerHotSpot.y;
		final int bottom = top + this.mMarkerHeight;
	
		Drawable marker = null;
		try {
			if (mTapId != focusedItem.getId()) {
				marker = mCtx.getResources().getDrawable(R.drawable.poi);
			} else {
				marker = mCtx.getResources().getDrawable(R.drawable.poigreen);
			}
		} catch (Exception e) {
			marker = mCtx.getResources().getDrawable(R.drawable.poi);
		}
		marker.setBounds(left, top, right, bottom);
	
		marker.draw(c);
	
//		if(OpenStreetMapViewConstants.DEBUGMODE){
//			final int pxUp = 2;
//			final int left2 = (int)(screenCoords.x + mDensity*(5 - pxUp));
//			final int right2 = (int)(screenCoords.x + mDensity*(38 + pxUp));
//			final int top2 = (int)(screenCoords.y - this.mMarkerHotSpot.y - mDensity*(pxUp));
//			final int bottom2 = (int)(top2 + mDensity*(33 + pxUp));
			Paint p = new Paint();
//			c.drawLine(left2, top2, right2, bottom2, p);
//			c.drawLine(right2, top2, left2, bottom2, p);
//				
			c.drawLine(screenCoords.x - 5, screenCoords.y - 5, screenCoords.x + 5, screenCoords.y + 5, p);
			c.drawLine(screenCoords.x - 5, screenCoords.y + 5, screenCoords.x + 5, screenCoords.y - 5, p);
//		}
	}

	public PoiPoint getPoiPoint(final int id){
		return this.mItemList.get(id);
	}

	public int getMarkerAtPoint(final int eventX, final int eventY, TileView mapView){
		if(this.mItemList != null){
			final com.robert.maps.applib.view.TileView.OpenStreetMapViewProjection pj = mapView.getProjection();

			final Rect curMarkerBounds = new Rect();
			final Point mCurScreenCoords = new Point();

			 
			for(int i = 0; i < this.mItemList.size(); i++){
				final PoiPoint mItem = this.mItemList.valueAt(i);
				pj.toPixels(mItem.GeoPoint, mapView.getBearing(), mCurScreenCoords);

				final int left = mCurScreenCoords.x - this.mMarkerHotSpot.x;
				final int right = left + this.mMarkerWidth;
				final int top = mCurScreenCoords.y - this.mMarkerHotSpot.y;
				final int bottom = top + this.mMarkerHeight;
				
//				final int pxUp = 2;
//				final int left = (int)(mCurScreenCoords.x + mDensity*(5 - pxUp));
//				final int right = (int)(mCurScreenCoords.x + mDensity*(38 + pxUp));
//				final int top = (int)(mCurScreenCoords.y - this.mMarkerHotSpot.y - mDensity*(pxUp));
//				final int bottom = (int)(top + mDensity*(33 + pxUp));

				curMarkerBounds.set(left, top, right, bottom);
				if(curMarkerBounds.contains(eventX, eventY))
					return mItem.getId();
			}
		}

		return NO_TAP;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event, TileView mapView) {
		final int id = getMarkerAtPoint((int)event.getX(), (int)event.getY(), mapView);
		if (id > NO_TAP) {
			mTapId = id;
			
			mapView.mPoiMenuInfo.MarkerIndex = id;
			mapView.mPoiMenuInfo.EventGeoPoint = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY(), mapView.getBearing());
			mapView.showContextMenu();
			
			return true;
		}
		
		return super.onSingleTapUp(event, mapView);
	}

	@Override
	public int onLongPress(MotionEvent event, TileView mapView) {
		final int id = getMarkerAtPoint((int)event.getX(), (int)event.getY(), mapView);
		mapView.mPoiMenuInfo.MarkerIndex = id;
		mapView.mPoiMenuInfo.EventGeoPoint = mapView.getProjection().fromPixels((int)event.getX(), (int)event.getY(), mapView.getBearing());
		
		if (id > NO_TAP) {
			mTapId = id;
			return 1;
			
		} else 
			mTapId = NO_TAP;

		return super.onLongPress(event, mapView);
	}

	protected boolean onTap(int id) {
		if(mTapId == id)
			mTapId = NO_TAP;
		else
			mTapId = id;

		if(this.mOnItemTapListener != null)
			return this.mOnItemTapListener.onItemTap(id, this.mItemList.get(id));
		else
			return false;
	}

	@SuppressWarnings("hiding")
	public static interface OnItemTapListener<PoiPoint>{
		public boolean onItemTap(final int aIndex, final PoiPoint aItem);
	}

	@SuppressWarnings("hiding")
	public static interface OnItemLongPressListener<PoiPoint>{
		public boolean onItemLongPress(final int aIndex, final PoiPoint aItem);
	}

	@Override
	protected void onDrawFinished(Canvas c, TileView osmv) {
	}


	private class PoiListThread extends Thread {
		private double mdeltaX;
		private double mdeltaY;

		public void setParams(double deltaX, double deltaY){
			mdeltaX = deltaX;
			mdeltaY = deltaY;
		}

		@Override
		public void run() {
			mItemList = mPoiManager.getPoiListNotHidden(mLastZoom, mLastMapCenter, mdeltaX, mdeltaY);
			super.run();
		}
	}
}