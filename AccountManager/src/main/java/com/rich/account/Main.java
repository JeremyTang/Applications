package com.rich.account;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rich.account.AccountManager.R;
import com.rich.account.framework.MeizuAccountAuthenticator;
import com.rich.account.framework.MeizuAccountKikat;
import com.rich.account.utils.LogM;

public class Main extends Activity implements OnClickListener {

	private Button mLogin, mLogout, mAccount, mAll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	private void initViews() {
		mLogin = (Button) findViewById(R.id.login);
		mLogout = (Button) findViewById(R.id.logout);
		mAccount = (Button) findViewById(R.id.account);
		mAll = (Button) findViewById(R.id.all);

		mLogin.setOnClickListener(this);
		mLogout.setOnClickListener(this);
		mAccount.setOnClickListener(this);
		mAll.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(com.rich.account.AccountManager.R.menu.main,
				menu);
		return true;
	}

	public void onClick(View arg0) {
		if (arg0 == mLogin) {
			MeizuAccountKikat.newInstance(this).addAccount(
					"Account_" + System.currentTimeMillis());
		} else if (arg0 == mLogout) {
			Account[] as = MeizuAccountKikat.newInstance(this).getAccounts(
					MeizuAccountKikat.ACCOUNT_TYPE);
			if (as.length > 0) {
				MeizuAccountKikat.newInstance(this).deleteAccount(
						as[as.length - 1], null, null);
			}
		} else if (arg0 == mAccount) {
			Account[] as = MeizuAccountKikat.newInstance(this).getAccounts(
					MeizuAccountKikat.ACCOUNT_TYPE);
			for (Account a : as) {
				LogM.onLogDebug("Account = " + a.name + " ---- " + a.type);
			}
		} else if (arg0 == mAll) {
			Account[] as = MeizuAccountKikat.newInstance(this).getAccounts();
			for (Account a : as) {
				LogM.onLogDebug("Account = " + a.name + " ---- " + a.type);
			}
		}
	}
}
