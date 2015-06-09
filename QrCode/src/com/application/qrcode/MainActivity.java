package com.application.qrcode;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class MainActivity extends Activity {

	private static final String BAIDU = "http://www.baidu.com";
	public static String APP_ID = "300008880753";// –¬∆÷¡‘»À”Œœ∑
	public static String APP_KEY = "9BB253542D429931AFA8F4D9C013808B";

	private static int width = 200;
	private static int height = 200;

	private ImageView mImage;
	private EditText mEdit;
	private Button mCreateButton;

	private QrcodeHandler mHandler;

	private String source;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		width = getResources().getDisplayMetrics().widthPixels / 2;
		height = width;

		mImage = (ImageView) findViewById(R.id.image_qrcode);
		mEdit = (EditText) findViewById(R.id.edit_qrcode);

		findViewById(R.id.btn_qrcode).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						QrcodeActiviey.class));
			}
		});

		mCreateButton = (Button) findViewById(R.id.btn_create);
		mCreateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				// Bitmap b = encodeQrcodeBitmap();
				// if (b != null) {
				// mImage.setImageBitmap(b);
			}
		});

	}

	final class QrcodeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private Bitmap encodeQrcodeBitmap() {
		source = mEdit.getText().toString();
		MultiFormatWriter mfw = new MultiFormatWriter();
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		try {
			BitMatrix bm = mfw.encode(TextUtils.isEmpty(source) ? BAIDU
					: source, BarcodeFormat.QR_CODE, width, height, hints);
			return toBitmap(bm);
		} catch (WriterException e) {
			e.printStackTrace();
		}

		return null;
	}

	private Bitmap toBitmap(BitMatrix bitMatrix) {
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK
						: Color.WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
