package com.rich.account.framework;

import com.rich.account.utils.LogM;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

public class MeizuAccountAuthenticator extends AbstractAccountAuthenticator {

	private final Context mContext;

	public MeizuAccountAuthenticator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse arg0, String arg1,
			String arg2, String[] arg3, Bundle arg4)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		LogM.onLogDebug("addAcount()");
		return null;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, Bundle arg2) throws NetworkErrorException {
		// TODO Auto-generated method stub
		LogM.onLogDebug("confirmCredentials()");
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
		// TODO Auto-generated method stub
		LogM.onLogDebug("editProperties()");
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse arg0, Account arg1,
			String arg2, Bundle arg3) throws NetworkErrorException {
		// TODO Auto-generated method stub
		LogM.onLogDebug("getAuthToken()");
		return null;
	}

	@Override
	public String getAuthTokenLabel(String arg0) {
		// TODO Auto-generated method stub
		LogM.onLogDebug("getAuthTokenLabel()");
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
			String[] arg2) throws NetworkErrorException {
		// TODO Auto-generated method stub
		LogM.onLogDebug("hasFeatures()");
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, String arg2, Bundle arg3)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		LogM.onLogDebug("updateCredentials()");
		return null;
	}

}
