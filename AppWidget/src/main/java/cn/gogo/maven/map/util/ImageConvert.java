package cn.gogo.maven.map.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * @author Administrator
 * @category 图片处理工具类
 */
public class ImageConvert {

	@SuppressWarnings("unused")
	private static final String TAG = ImageConvert.class.getSimpleName();

	private static ImageConvert imageUtils = null;

	private Context mContext;

	private static int text_size = 28;

	private int screenWidth = 480;

	public static ImageConvert getInstance(Context context) {
		if (imageUtils == null) {
			imageUtils = new ImageConvert(context);
		}
		return imageUtils;
	}

	/**
	 * 判断当前手机的分辨率
	 * 
	 * @param context
	 */
	private ImageConvert(Context context) {
		this.mContext = context;
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		switch (screenWidth) {
		case 480:
			text_size = 28;
			break;
		case 720:
			text_size = 42;
			break;
		case 1080:
			text_size = 60;
			break;
		}
	}

	/**
	 * 地图标记 重新绘制编号
	 * 
	 * @param drawable
	 * @param text
	 * @return
	 */
	public Drawable convertDrawable(Drawable drawable, String text) {
		BitmapDrawable bd = (BitmapDrawable) drawable;
		Bitmap bMap = bd.getBitmap().copy(Config.ARGB_8888, true);
		Canvas c = new Canvas(bMap);
		Paint paint = new Paint();
		paint.setTextSize(text_size);
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		if (text.length() > 1) {
			c.drawText(text, bMap.getWidth() / 2 - paint.measureText(text) / 2
					- 12, bMap.getHeight() / 2, paint);
		} else {
			c.drawText(text, bMap.getWidth() / 2 - text_size * text.length()
					/ 2, bMap.getHeight() / 2, paint);
		}
		Drawable d = new BitmapDrawable(mContext.getResources(), bMap);
		return d;
	}

	/**
	 * 重新绘制Bitmap大小 适配ImageView
	 * 
	 * @param map
	 * @return
	 */
	public Drawable convertBitmap(ImageView image, Bitmap map) {
		// Bitmap bm = Bitmap.createBitmap(map, 0, 0, 80, 40);
		return new BitmapDrawable(mContext.getResources(), map);
	}

}
