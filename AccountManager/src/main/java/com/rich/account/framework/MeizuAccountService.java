package com.rich.account.framework;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MeizuAccountService extends Service {

	private MeizuAccountAuthenticator mAcountAuthenticator;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mAcountAuthenticator = new MeizuAccountAuthenticator(this);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mAcountAuthenticator.getIBinder();
	}

}
