package com.rich.account.framework;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

public class MeizuAccountKikat {

	public static final String ACCOUNT_TYPE = "com.rich.account.type";
	public static final String ACCOUNT_TIME = "com.rich.account.time";

	private static AccountManager aManager;
	private static MeizuAccountKikat mInstance;

	private MeizuAccountKikat(Context context) {
		aManager = AccountManager.get(context);
	}

	public static MeizuAccountKikat newInstance(Context context) {
		if (mInstance == null) {
			mInstance = new MeizuAccountKikat(context);
		}
		return mInstance;
	}

	public Account[] getAccounts() {
		return aManager.getAccounts();
	}

	public Account[] getAccounts(String accountType) {
		return aManager.getAccountsByType(accountType);
	}

	public boolean addAccount(String accountName) {
		Account account = new Account(accountName, ACCOUNT_TYPE);
		Bundle bundle = new Bundle();
		bundle.putString(ACCOUNT_TIME, System.currentTimeMillis() + "");
		return aManager.addAccountExplicitly(account, null, bundle);
	}

	public AccountManagerFuture<Boolean> deleteAccount(Account account,
			AccountManagerCallback<Boolean> callback, Handler handler) {
		return aManager.removeAccount(account, callback, handler);
	}

}
