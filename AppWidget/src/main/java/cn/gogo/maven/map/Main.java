package cn.gogo.maven.map;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.gogo.maven.map.data.ApplicationData;
import cn.gogo.maven.map.data.XmlDataUtil;
import cn.gogo.maven.map.entity.ViewPoint;
import cn.gogo.maven.map.pool.ThreadManager;
import cn.gogo.maven.map.util.ImageConvert;
import cn.gogo.maven.map.util.Utils;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.events.MapTouchedEvent;
import com.ls.widgets.map.events.ObjectTouchEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.interfaces.OnMapTouchListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;

/**
 * @author Jeremy
 * @category 景区手绘地图导航
 */
@SuppressLint("HandlerLeak")
@SuppressWarnings("unused")
public class Main extends Activity {

	private static final String TAG = Main.class.getSimpleName();

	// 地图缩放级别
	private static final int MAX_LEVEL = 12;
	private static final int MIN_LEVEL = 11;
	private static final int initLevel = 11;
	private static final int Message_point_success = 1;

	private int pointLayoutId = 1;

	private ThreadManager tManager;

	private FrameLayout mLayout;
	// 手绘地图
	private MapWidget mapWidget;
	// 地图配置
	private OfflineMapConfig mapConfig;
	// 景点集合
	private ArrayList<ViewPoint> mViewList;
	private Layer mLayer;

	private int screenWidth;
	private int screenHeight;

	private int primaryLevel = 0;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Message_point_success:
				configViewPointForMapView(mViewList);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
		configMapView();
		readViewPoint();
	}

	@Override
	protected void onResume() {
		if (mViewList != null && mViewList.size() > 0) {
			if (!Utils.getPointVisilableValue(this)) {
				mLayer.clearAll();
				mapWidget.centerMap();
			}
			int level = Utils.getPointLevelValue(this);
		}
		super.onResume();
	}

	/**
	 * 界面初始化
	 */
	private void init() {
		screenWidth = getResources().getDisplayMetrics().widthPixels;
		screenHeight = getResources().getDisplayMetrics().heightPixels;

		primaryLevel = Utils.getPointLevelValue(this);
		tManager = ThreadManager.getInstance();

		mLayout = (FrameLayout) findViewById(R.id.introduce_layout);

	}

	// 初始化手绘地图
	private void configMapView() {
		removeMapLogo();
		String mapPath = Environment.getExternalStorageDirectory()
				+ File.separator + "map";
		Log.d(TAG, "mapPath = " + mapPath);
		mapWidget = new MapWidget(this, "map", initLevel);
		mapWidget.setZoomButtonsVisible(false);
		mapConfig = mapWidget.getConfig();
		mapConfig.setMinZoomLevelLimit(MIN_LEVEL);
		mapConfig.setMaxZoomLevelLimit(MAX_LEVEL);
		mapConfig.setMapCenteringEnabled(true);
		mapConfig.setPinchZoomEnabled(true);
		mLayout.addView(mapWidget);
		mapWidget.createLayer(pointLayoutId);
		mLayer = mapWidget.getLayerById(pointLayoutId);
		mapWidget.centerMap();
		mapWidget.setOnMapScrolledListener(mapSrollListener);
		mapWidget.setOnMapTouchListener(mapTouchListener);
	}

	/**
	 * 初始化景点集合
	 */
	private void readViewPoint() {
		Log.d(TAG, "initPoiPointArray()");
		tManager.execute(new Runnable() {
			public void run() {
				try {
					mViewList = (ArrayList<ViewPoint>) XmlDataUtil
							.getInstance().getViewPointList(Main.this);
					mHandler.sendEmptyMessage(Message_point_success);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 初始化地图景区点导航
	 */
	private void configViewPointForMapView(ArrayList<ViewPoint> list) {
		Location location = new Location("");
		mLayer.clearAll();
		if (list != null) {
			// Log.e(TAG, "initMapObject()");
			for (int i = 0; i < list.size(); i++) {
				Drawable drawable = null;
				if (i == 0) {
					drawable = getResources().getDrawable(
							R.drawable.widget_poi_start);
				} else if (i == list.size() - 1) {
					drawable = getResources().getDrawable(
							R.drawable.widget_poi_end);
				} else {
					drawable = getResources().getDrawable(R.drawable.widget_poi_blue);
					drawable = ImageConvert.getInstance(this).convertDrawable(
							drawable, i + "");
				}

				MapObject mObject = new MapObject(i, drawable, new Point(0, 0),
						PivotFactory.createPivotPoint(drawable,
								PivotPosition.PIVOT_CENTER), true, false);
				mLayer.addMapObject(mObject);
				location.setLatitude(list.get(i).getLantitude() + 0.00151);
				location.setLongitude(list.get(i).getLongitude() + 0.006260);
				mObject.moveTo(location);
			}

		}

	}

	/**
	 * Button 监听
	 */

	private OnClickListener mBtnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 地图触摸监听
	 */
	private OnMapTouchListener mapTouchListener = new OnMapTouchListener() {
		public void onTouch(MapWidget map, MapTouchedEvent event) {
			// ArrayList<ObjectTouchEvent> touchList = event
			// .getTouchedObjectEvents();
			// if (overlayView != null && overlayView.isVisible()) {
			// overlayView.hide();
			// }
			// if (touchList.size() > 0) {
			// ObjectTouchEvent ote = touchList.get(0);
			// int screenX = event.getScreenX();
			// int screenY = event.getScreenY();
			//
			// if (overlayView == null) {
			// View view = LayoutInflater.from(MapWidgetActivity.this)
			// .inflate(R.layout.popup_map_point, null);
			// overlayView = new MapOverlay(MapWidgetActivity.this,
			// mLayout, view);
			// }
			//
			// if (ote.getLayerId() == pointLayoutId) {
			// try {
			// int index = Integer.parseInt(ote.getObjectId() + "");
			// overlayView.setPopupInfo(spotList.get(index));
			// overlayView.show(mLayout, screenX, screenY);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
			// }
		}
	};

	/**
	 * 地图滑动监听
	 */
	private OnMapScrollListener mapSrollListener = new OnMapScrollListener() {
		public void onScrolledEvent(MapWidget arg0, MapScrolledEvent arg1) {
			handlerOnMapScroll(arg0, arg1);
		}
	};

	/**
	 * 去掉地图水印
	 */
	public void removeMapLogo() {
		Class<?> c = null;
		try {
			// 反射找到Resources类
			c = Class.forName("com.ls.widgets.map.utils.Resources");
			Object obj = c.newInstance();
			// 找到Logo 属性，是一个数组
			Field field = c.getDeclaredField("LOGO");
			field.setAccessible(true);
			// 将LOGO字段设置为null
			field.set(obj, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 泡泡弹窗随界面滑动
	 * 
	 * @param map
	 * @param event
	 */
	private void handlerOnMapScroll(MapWidget map, MapScrolledEvent event) {
		int x = event.getDX();
		int y = event.getDY();
		// if (overlayView != null && overlayView.isVisible()) {
		// overlayView.moveBy(x, y);
		// }
	}

}
