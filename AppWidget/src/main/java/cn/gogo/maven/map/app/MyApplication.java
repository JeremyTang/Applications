package cn.gogo.maven.map.app;

import android.app.Application;

public class MyApplication extends Application {
	@SuppressWarnings("unused")
	private static final String TAG = MyApplication.class.getSimpleName();
	// 实例
	private static MyApplication mInstance = null;

	// 屏幕宽高
	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;

	// 正在播放语音景点的ID
	public static int playingID = -1;

	public static MyApplication getInstance() {
		return mInstance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		SCREEN_WIDTH = this.getResources().getDisplayMetrics().widthPixels;
		SCREEN_HEIGHT = this.getResources().getDisplayMetrics().heightPixels;
	}
}