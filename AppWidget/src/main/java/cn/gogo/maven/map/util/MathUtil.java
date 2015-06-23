package cn.gogo.maven.map.util;

import java.util.List;

import android.util.Log;
import cn.gogo.maven.map.data.ApplicationData;
import cn.gogo.maven.map.entity.ViewPoint;

/**
 * 有关数学计算的工具类
 * 
 * @author Jeremy
 * 
 */
public class MathUtil {

	private static final String TAG = MathUtil.class.getSimpleName();

	public static int getPointX(double latitude) {
		double x = (latitude - ApplicationData.primaryX) * 800
				/ ApplicationData.ratioX;
		return (int) Math.round(x);
	}

	public static int getPointY(double longitude) {
		double x = (longitude - ApplicationData.primaryY) * 1016
				/ ApplicationData.ratioX;
		return (int) Math.round(x);
	}

	public static int getCurPosition(List<ViewPoint> scenicList, String url) {
		int cur = -1;
		if (scenicList == null) {
			return cur;
		}
		for (int i = 0; i < scenicList.size(); i++) {
			if (url.equals(ApplicationData.URL_LOCATION_BASIC
					+ scenicList.get(i).getPanoramic())) {
				cur = i;
				break;
			}
		}
		return cur;
	}

	public static int getCurPositionByJson(List<ViewPoint> scenicList,
			String url) {
		int cur = -1;
		if (scenicList == null) {
			return cur;
		}
		for (int i = 0; i < scenicList.size(); i++) {
			Log.d(TAG, scenicList.get(i).getJson());
			if (url.equals(scenicList.get(i).getJson())) {
				cur = i;
				break;
			}
		}
		return cur;
	}
}
