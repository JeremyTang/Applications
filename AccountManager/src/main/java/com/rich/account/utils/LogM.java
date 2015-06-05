package com.rich.account.utils;

import android.util.Log;

public class LogM {

	private static final String TAG = LogM.class.getSimpleName();

	public static void onLogDebug(String msg) {
		Log.d(TAG, msg);
	}

	public static void onLogError(String msg) {
		Log.e(TAG, msg);
	}

}
