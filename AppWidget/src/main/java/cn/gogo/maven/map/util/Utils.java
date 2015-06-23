package cn.gogo.maven.map.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import cn.gogo.maven.map.data.ApplicationData;
import cn.gogo.maven.map.entity.ViewPoint;

/**
 * @author Jeremy
 * @category 工具类
 */
@SuppressWarnings("static-access")
public class Utils {

	public static boolean checkNetWork(Context context) {

		return false;
	}

	/**
	 * 格式化 语音播放时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(int time) {
		String minStr = "";
		String socStr = "";
		int min = time / 60;
		int soc = time % 60;
		if (min < 10) {
			minStr = "0" + min;
		} else {
			minStr = min + "";
		}
		if (soc < 10) {
			socStr = "0" + soc;
		} else {
			socStr = soc + "";
		}
		return minStr + ":" + socStr;
	}

	/**
	 * 设置景点标记可见参数
	 */

	public static void setPointVisilable(Context context) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		shared.edit().putBoolean(ApplicationData.KEY_POINT_SHOW, true).commit();
	}

	/**
	 * 设置景点标记不可见参数
	 */
	public static void setPointInVisilable(Context context) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		shared.edit().putBoolean(ApplicationData.KEY_POINT_SHOW, false)
				.commit();
	}

	/**
	 * 获取设置可见参数
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getPointVisilableValue(Context context) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		return shared.getBoolean(ApplicationData.KEY_POINT_SHOW, true);
	}

	/**
	 * 根据title 查询列表中的ScenicPoint 实例
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public static ViewPoint getScenicPointByName(List<ViewPoint> list,
			String name) {
		ViewPoint sp = null;
		if (list == null || name == null) {
			return null;
		}
		try {
			for (ViewPoint s : list) {
				if (s.getTitle().equals(name)) {
					sp = s;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sp;
	}

	/**
	 * 获取当前显示 旅游路线
	 * 
	 * @param context
	 * @return
	 */
	public static int getPointLevelValue(Context context) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		return shared.getInt(ApplicationData.KEY_POINT_LEVEL, 0);
	}

	/**
	 * 设置旅游路线
	 * 
	 * @param context
	 * @param type
	 */
	public static void setPointLevelValue(Context context, int type) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		shared.edit().putInt(ApplicationData.KEY_POINT_LEVEL, type).commit();
	}

	// public static List<SettingBean> getSettingItemList(Context context) {
	// // List<SettingBean> list = new ArrayList<SettingBean>();
	// // list.add(new SettingBean(R.drawable.menu_popup_setting, context
	// // .getResources().getString(R.string.menu_popup_setting_string)));
	// // list.add(new SettingBean(R.drawable.menu_popup_baisc, context
	// // .getResources().getString(R.string.menu_popup_basic_string)));
	// // list.add(new SettingBean(R.drawable.menu_popup_traffic, context
	// // .getResources().getString(R.string.menu_popup_traffic_string)));
	// // list.add(new SettingBean(R.drawable.menu_popup_list, context
	// // .getResources().getString(R.string.menu_popup_list_string)));
	// // list.add(new SettingBean(R.drawable.menu_popup_more, context
	// // .getResources().getString(R.string.menu_popup_more_string)));
	// // list.add(new SettingBean(R.drawable.menu_popup_sina, context
	// // .getResources().getString(R.string.menu_popup_sina_string)));
	// // return list;
	// }

	/**
	 * 判断字串是否为空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isStringEmpty(String string) {
		if (string == null) {
			return true;
		}
		return string.equals("");
	}

	/**
	 * 重置登录数据
	 * 
	 * @param context
	 */
	public static void reSetLoginParams(Context context) {
		SharedPreferences shared = context.getSharedPreferences(
				ApplicationData.URL_SHARED, context.MODE_PRIVATE);
		shared.edit().putString(ApplicationData.KEY_USER_ID, "").commit();
		shared.edit().putString(ApplicationData.KEY_USER_NAME, "").commit();
	}

	/**
	 * InputStream转换成String
	 * 
	 * @param is
	 * @return
	 */
	public static String convertInputStreamToString(InputStream is) {
		StringBuffer sb = new StringBuffer(20480);
		String str = "";
		try {
			// int number = 0;
			// byte[] by = new byte[1024];
			// while ((number = is.read(by)) != 0) {
			// str = new String(by);
			// }
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			while ((str = reader.readLine()) != null) {

				sb.append(str);
				// System.out.println("sb = " + sb.toString());
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(sb);
	}
}
