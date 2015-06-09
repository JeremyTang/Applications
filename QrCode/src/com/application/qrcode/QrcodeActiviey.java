package com.application.qrcode;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.application.qrcode.camera.CameraManager;
import com.application.qrcode.decoding.CaptureActivityHandler;
import com.application.qrcode.decoding.InactivityTimer;
import com.application.qrcode.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

public class QrcodeActiviey extends Activity implements Callback {
	private String TAG = "CaptureActivity";
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private ImageView imageView;
	SurfaceView surfaceView;
	private LinearLayout no_qrcode;
	private static final String secret_Key = "mpost";
	private static final String ACTION_LOGIN = "9";

	// private static String URL_REGEX =
	// "http://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=+]*)?";
	private static final String URL_REGEX = "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";

	private Pattern pattern = Pattern.compile(URL_REGEX);

	// private String my_url="http://mail.10086.cn/";
	// private String test_url="http://mail.10086.cn/?columId=133&fla=";

	private TextView result_title;

	private Button qrcode_scan_open_web;

	private String temp_result;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode_scan);
		CameraManager.init(getApplication());

		imageView = (ImageView) findViewById(R.id.qrcode_scan_line);
		no_qrcode = (LinearLayout) findViewById(R.id.no_qrcode);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		result_title = (TextView) findViewById(R.id.result_title);
		qrcode_scan_open_web = (Button) findViewById(R.id.qrcode_scan_open_web);

		qrcode_scan_open_web.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String url = "http://www.domainname.com";
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);

			}
		});

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		Button mButtonBack = (Button) findViewById(R.id.button_back);
		mButtonBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QrcodeActiviey.this.finish();

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d("liaoguang", "onResume================");
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
		// imageView.clearAnimation();
	}

	@Override
	protected void onPause() {
		Log.d("liaoguang", "onPause================");
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		imageView.clearAnimation();
		surfaceView = null;
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		Log.d("liaoguang", "onDestroy================");
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);

			Rect frame = CameraManager.get().getFramingRect();
			if (frame != null) {

				LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
						frame.right - frame.left,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				param.setMargins(0, frame.top, 0, 0);

				imageView.setLayoutParams(param);
				
				TranslateAnimation animation = new TranslateAnimation(0, 0, 0,
						frame.bottom - frame.top - imageView.getHeight());

				animation.setDuration(2600);
				// animation.setRepeatMode(Animation.RESTART);
				animation.setRepeatCount(Animation.INFINITE);
				animation
						.setInterpolator(new AccelerateDecelerateInterpolator());
				imageView.startAnimation(animation);

			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		// viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();

		// txtResult.setText(obj.getBarcodeFormat().toString() + ":"
		// + obj.getText());
		// txtResult.setText(obj.getBarcodeFormat().toString() + ":"
		// + PATTERN.matcher(obj.getText()).matches());

		temp_result = obj.getText();

		if (pattern.matcher(obj.getText()).matches()) {// 濡傛灉鏄綉鍧�
			if (temp_result.contains("www.rdcmpassport.com")) {
				doScanAction();
			} else {
				Map<String, String[]> valueMap = new HashMap<String, String[]>();

				try {
					URL url = new URL(obj.getText());
					RequestUtil.parseParameters(valueMap, url.getQuery(),
							"UTF-8");

					result_title
							.setText(getString(R.string.umcsdk_qrcode_scan_result_title2));

					qrcode_scan_open_web.setVisibility(View.VISIBLE);
					// txtResult.setText(getString(R.string.qrcode_scan_addtext)
					// +" "+ obj.getText());
					// no_qrcode.setVisibility(View.VISIBLE);

					SpannableString spanText = new SpannableString(
							getString(R.string.umcsdk_qrcode_scan_addtext)
									+ " " + obj.getText());
					spanText.setSpan(new UnderlineSpan(), 3, spanText.length(),
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
					// 璁剧疆楂樹寒鏍峰紡涓�
					spanText.setSpan(new ForegroundColorSpan(getResources()
							.getColor(R.color.color14)), 3, spanText.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					// txtResult.append(spanText);
					txtResult.setText(spanText);
					no_qrcode.setVisibility(View.VISIBLE);

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} else {
			result_title
					.setText(getString(R.string.umcsdk_qrcode_scan_result_title));
			txtResult.setText(obj.getText());
			no_qrcode.setVisibility(View.VISIBLE);
		}

	}

	private void doScanAction() {
		Uri uri = Uri.parse(temp_result);
		String from = uri.getQueryParameter("from");
		if (ACTION_LOGIN.equals(from)) {
			String uuid = uri.getQueryParameter("uuid");
			Intent dataIntent = new Intent();
			// dataIntent.putExtra(MainActivity.EXTRA_SCAN_RESULT, uuid);
			// setResult(MainActivity.RESULT_CODE_SCAN_QRCODE, dataIntent);
			this.finish();
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.umcsdk_beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}