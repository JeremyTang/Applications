package cn.gogo.maven.map.data;

public class ApplicationData {
	public static final double primaryX = 39.92245;
	public static final double primaryY = 116.392143;
	public static final double maxX = 39.91376;
	public static final double maxY = 116.401927;

	public static final double ratioX = maxX - primaryX;
	public static final double ratioY = maxY - primaryY;

	public static final String ACTION_MUSIC_SERVICE = "musicplayerservice";
	public static final String ACTION_MUSIC_RECEIVER = "musicplayerreceiver";
	public static final String ACTION_CONTROLL_RECEIVER = "musiccontrollreceiver";

	// 音乐播放器控制属性
	public static final int MUSIC_PLAYER = 1;
	public static final int MUSIC_STOP = 3;
	public static final int MUSIC_NEXT = 4;
	public static final int MUSIC_FRONT = 5;
	public static final int MUSIC_VALUES = 6;
	public static final int MUSIC_CHANGE = 7;
	public static final int MUSIC_JUMP = 8;

	// sharedprefrence URL
	public static final String URL_SHARED = "micro_shared_url";
	public static final String KEY_POINT_SHOW = "point_show_key";
	public static final String KEY_POINT_LEVEL = "point_level_key";

	public static final String KEY_USER_NAME = "user_name";
	public static final String KEY_USER_ID = "user_id";

	//全景界面ID
	public static final String KEY_POSITION = "curposition";

	public static final String KEY_FIRST_RUN = "first_run";

	// 访问Asset地址文件
	public static final String URL_LOCATION_BASIC = "file:///android_asset/";

	public static final double CENTER_X = 22.543534;
	public static final double CENTER_Y = 114.027034;

	public static final int ZOON_ADAPTER_TYPE = 0;
	public static final int MY_ZOON_ADAPTER_TYPE = 1;
}
