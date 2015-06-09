/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.application.qrcode.view;

import java.util.Collection;
import java.util.HashSet;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.application.qrcode.R;
import com.application.qrcode.camera.CameraManager;
import com.google.zxing.ResultPoint;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

	private static final int[] SCANNER_ALPHA = { 0, 64, 128, 192, 255, 192,
			128, 64 };
	private static final long ANIMATION_DELAY = 30L;
	private static final int OPAQUE = 0xFF;

	private final Paint paint;
	private Bitmap resultBitmap;
	private final int maskColor;
	private final int resultColor;
	private final int frameColor;
	private final int laserColor;
	private final int angleColor;

	private Collection<ResultPoint> possibleResultPoints;
	private Collection<ResultPoint> lastPossibleResultPoints;

	private Context context;

	private static final float LINE_WIDTH = 5.0f;
	private static final float LINE_HEIGHT = 20.0f;
	private static final float LINE_BORDER_WIDTH = 1.0f;

	private float linewidth;
	private float lineheight;
	private float border_width;

	// This constructor is used when the class is built from an XML resource.
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Initialize these once for performance rather than calling them every
		// time in onDraw().
		paint = new Paint();
		this.context = context;
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);
		frameColor = resources.getColor(R.color.viewfinder_frame);
		angleColor = resources.getColor(R.color.viewfinder_angle);
		laserColor = resources.getColor(R.color.viewfinder_laser);
		possibleResultPoints = new HashSet<ResultPoint>(5);
		linewidth = getRawSize(TypedValue.COMPLEX_UNIT_DIP, LINE_WIDTH);
		lineheight = getRawSize(TypedValue.COMPLEX_UNIT_DIP, LINE_HEIGHT);
		border_width = LINE_BORDER_WIDTH;

	}

	@Override
	public void onDraw(Canvas canvas) {
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}

		int width = canvas.getWidth();
		int height = canvas.getHeight();

		// paint.setColor(laserColor);
		// canvas.drawRect(frame.left, frame.top, frame.right, frame.bottom,
		// paint);

		// Draw the exterior (i.e. outside the framing rect) darkened
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		canvas.drawRect(0, 0, width, frame.top - border_width, paint);// 上
		canvas.drawRect(0, frame.top - border_width, frame.left - border_width,
				frame.bottom + border_width, paint);// 左

		canvas.drawRect(frame.right + border_width, frame.top - border_width,
				width, frame.bottom + border_width, paint);// 右
		canvas.drawRect(0, frame.bottom + border_width, width, height, paint);// 下

		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			// Draw a two pixel solid black border inside the framing rect
			paint.setColor(frameColor);
			canvas.drawRect(frame.left - border_width,
					frame.top - border_width, frame.right + border_width,
					frame.top, paint);// 上
			// //
			canvas.drawRect(frame.left - border_width,
					frame.top - border_width, frame.left, frame.bottom
							+ border_width, paint);// 左
			//
			canvas.drawRect(frame.right, frame.top - border_width, frame.right
					+ border_width, frame.bottom + border_width, paint);// 右
			//
			canvas.drawRect(frame.left - border_width, frame.bottom,
					frame.right + border_width, frame.bottom + border_width,
					paint);// 下

			paint.setColor(angleColor);
			canvas.drawRect(frame.left - border_width,
					frame.top - border_width,
					(linewidth + frame.left - border_width), (lineheight
							+ frame.top - border_width), paint);

			canvas.drawRect(frame.left - border_width,
					frame.top - border_width,
					(lineheight + frame.left - border_width), (linewidth
							+ frame.top - border_width), paint);

			canvas.drawRect(frame.right + border_width - linewidth, frame.top
					- border_width, frame.right + border_width, lineheight
					+ frame.top - border_width, paint);

			canvas.drawRect(frame.right + border_width - lineheight, frame.top
					- border_width, frame.right + border_width, frame.top
					- border_width + linewidth, paint);

			canvas.drawRect(frame.left - border_width, frame.bottom
					+ border_width - linewidth, frame.left - border_width
					+ lineheight, frame.bottom + border_width, paint);

			canvas.drawRect(frame.left - border_width, frame.bottom
					+ border_width - lineheight, frame.left - border_width
					+ linewidth, frame.bottom + border_width, paint);

			canvas.drawRect(frame.right + border_width - linewidth,
					frame.bottom + border_width - lineheight, frame.right
							+ border_width, frame.bottom + border_width, paint);

			canvas.drawRect(frame.right + border_width - lineheight,
					frame.bottom + border_width - linewidth, frame.right
							+ border_width, frame.bottom + border_width, paint);

		}
	}

	public void drawViewfinder() {
		resultBitmap = null;
		Rect frame = CameraManager.get().getFramingRect();
		if (frame == null) {
			return;
		}
		postInvalidate(frame.left, frame.top, frame.right, frame.bottom);
		// invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

	public float getRawSize(int unit, float size) {

		Resources r = context.getResources();

		return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}

}
