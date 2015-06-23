package cn.gogo.maven.map.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import cn.gogo.maven.map.entity.ViewPoint;

/**
 * XML 数据解析
 * 
 * @author Jeremy
 * 
 */
public class XmlDataUtil {

	private static XmlDataUtil mDataParser = null;

	@SuppressWarnings("unused")
	private static final String TAG = XmlDataUtil.class.getSimpleName();

	public static XmlDataUtil getInstance() {
		if (mDataParser == null) {
			mDataParser = new XmlDataUtil();
		}
		return mDataParser;
	}

	private XmlDataUtil() {
	}

	/**
	 * 获取景点列表
	 * 
	 * @param is
	 * @return tyep (0:2小时路线；1：4小时路线；2：6小时路线)
	 */
	public List<ViewPoint> getMicroPointList(InputStream is, int type) {
		boolean isID = false, isLantitue = false, isLongitude = false, isTitle = false, isType = false;
		boolean isLevel = false, isRating = false, isAlbum = false;
		boolean isContnetURl = false, isAudioURl = false, isRemark = false, isImage = false;
		ArrayList<ViewPoint> mpList = new ArrayList<ViewPoint>();
		ArrayList<String> imageList = null;
		ViewPoint mp = null;
		int level = 50;
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			int event = parser.getEventType();
			parser.setInput(is, "utf-8");
			while (event != XmlPullParser.END_DOCUMENT) {
				// Log.d(TAG, "xml parsering");
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					// 判断获取路线
					switch (type) {
					case 0:
						level = 50;
						break;
					case 1:
						level = 100;
						break;
					case 2:
						level = 200;
						break;
					}
					break;
				case XmlPullParser.START_TAG:
					String sName = parser.getName();
					if (sName.equals("point")) {
						mp = new ViewPoint();
					} else if (sName.equals("id")) {
						isID = true;
					} else if (sName.equals("latitude")) {
						isLantitue = true;
					} else if (sName.equals("longitude")) {
						isLongitude = true;
					} else if (sName.equals("title")) {
						isTitle = true;
					} else if (sName.equals("type")) {
						isType = true;
					} else if (sName.equals("level")) {
						isLevel = true;
					} else if (sName.equals("rating")) {
						isRating = true;
					} else if (sName.equals("album")) {
						isAlbum = true;
						imageList = new ArrayList<String>();
					} else if (sName.equals("img")) {
						isImage = true;
					} else if (sName.equals("url")) {
						isContnetURl = true;
					} else if (sName.equals("audiodesc")) {
						isRemark = true;
					} else if (sName.equals("audiourl")) {
						isAudioURl = true;
					}
					break;
				case XmlPullParser.END_TAG:
					String eName = parser.getName();
					if (eName.equals("point")) {
						if (mp.getId() < level) {
							mpList.add(mp);
						}
					} else if (eName.equals("id")) {
						isID = false;
					} else if (eName.equals("latitude")) {
						isLantitue = false;
					} else if (eName.equals("longitude")) {
						isLongitude = false;
					} else if (eName.equals("title")) {
						isTitle = false;
					} else if (eName.equals("type")) {
						isType = false;
					} else if (eName.equals("level")) {
						isLevel = false;
					} else if (eName.equals("rating")) {
						isRating = false;
					} else if (eName.equals("album")) {
						isAlbum = false;
					} else if (eName.equals("img")) {
						isImage = false;
					} else if (eName.equals("url")) {
						isContnetURl = false;
					} else if (eName.equals("audiodesc")) {
						isRemark = false;
					} else if (eName.equals("audiourl")) {
						isAudioURl = false;
					}
					break;
				case XmlPullParser.TEXT:
					if (isID) {
						mp.setId(Integer.parseInt(parser.getText().trim()));
					} else if (isLantitue) {
						mp.setLantitude(Double.parseDouble(parser.getText()
								.trim()));
					} else if (isLongitude) {
						mp.setLongitude(Double.parseDouble(parser.getText()
								.trim()));
					} else if (isTitle) {
						mp.setTitle(parser.getText());
					} else if (isType) {
						mp.setType(parser.getText());
					} else if (isLevel) {
						mp.setLevel(Integer.parseInt(parser.getText().trim()));
					} else if (isRating) {
						mp.setRating(Integer.parseInt(parser.getText().trim()));
					} else if (isAlbum) {
						if (isImage) {
							imageList.add(parser.getText());
						}
					} else if (isContnetURl) {
						mp.setContent(parser.getText());
					} else if (isRemark) {
						mp.setDescription(parser.getText());
					} else if (isAudioURl) {
						mp.setAudio(parser.getText());
					}
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mpList;
	}

	/**
	 * 获取景点列表
	 * 
	 * @param is
	 * @return tyep (0:2小时路线；1：4小时路线；2：6小时路线)
	 */
	boolean isID = false, isLantitue = false, isLongitude = false,
			isName = false, isType = false, isJson = false;
	boolean isLevel = false, isRating = false, isPano = false;
	boolean isContnetURl = false, isAudioURl = false, isRemark = false,
			isImage = false;

	public List<ViewPoint> getViewPointList(Context context) {
		ArrayList<ViewPoint> mpList = new ArrayList<ViewPoint>();
		ViewPoint mp = null;
		try {
			XmlPullParser parser = XmlPullParserFactory.newInstance()
					.newPullParser();
			int event = parser.getEventType();
			parser.setInput(context.getAssets().open("point/viewpoint.xml"),
					"utf-8");
			while (event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String sName = parser.getName();
					if (sName.equals("scenicspot")) {
						mp = new ViewPoint();
					} else if (sName.equals("id")) {
						isID = true;
					} else if (sName.equals("lantitude")) {
						isLantitue = true;
					} else if (sName.equals("longitude")) {
						isLongitude = true;
					} else if (sName.equals("name")) {
						isName = true;
					} else if (sName.equals("type")) {
						isType = true;
					} else if (sName.equals("level")) {
						isLevel = true;
					} else if (sName.equals("rating")) {
						isRating = true;
					} else if (sName.equals("images")) {
						isImage = true;
					} else if (sName.equals("content")) {
						isContnetURl = true;
					} else if (sName.equals("audio")) {
						isAudioURl = true;
					} else if (sName.equals("panoramic")) {
						isPano = true;
					} else if (sName.equals("description")) {
						isRemark = true;
					} else if (sName.equals("json")) {
						isJson = true;
					}
					break;
				case XmlPullParser.END_TAG:
					String eName = parser.getName();
					if (eName.equals("scenicspot")) {
						mpList.add(mp);
					} else if (eName.equals("id")) {
						isID = false;
					} else if (eName.equals("lantitude")) {
						isLantitue = false;
					} else if (eName.equals("longitude")) {
						isLongitude = false;
					} else if (eName.equals("name")) {
						isName = false;
					} else if (eName.equals("type")) {
						isType = false;
					} else if (eName.equals("level")) {
						isLevel = false;
					} else if (eName.equals("rating")) {
						isRating = false;
					} else if (eName.equals("images")) {
						isImage = false;
					} else if (eName.equals("content")) {
						isContnetURl = false;
					} else if (eName.equals("audio")) {
						isAudioURl = false;
					} else if (eName.equals("panoramic")) {
						isPano = false;
					} else if (eName.equals("description")) {
						isRemark = false;
					} else if (eName.equals("json")) {
						isJson = false;
					}
					break;
				case XmlPullParser.TEXT:
					if (isID) {
						mp.setId(Integer.parseInt(parser.getText().trim()));
					} else if (isLantitue) {
						mp.setLantitude(Double.parseDouble(parser.getText()
								.trim()));
					} else if (isLongitude) {
						mp.setLongitude(Double.parseDouble(parser.getText()
								.trim()));
					} else if (isName) {
						mp.setTitle(parser.getText().trim());
					} else if (isType) {
						mp.setType(parser.getText().trim());
					} else if (isLevel) {
						mp.setLevel(Integer.parseInt(parser.getText().trim()));
					} else if (isRating) {
						mp.setRating(Integer.parseInt(parser.getText().trim()));
					} else if (isContnetURl) {
						mp.setContent(parser.getText().trim());
					} else if (isRemark) {
						mp.setDescription(parser.getText().trim());
					} else if (isAudioURl) {
						mp.setAudio(parser.getText().trim());
					} else if (isPano) {
						mp.setPanoramic(parser.getText().trim());
					} else if (isJson) {
						mp.setJson(parser.getText().trim());
					}
					break;
				}
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mpList;
	}
}
