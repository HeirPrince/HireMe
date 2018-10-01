package com.nassaty.hireme.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PermissionUtils {

	private static final String PERMISSIONS = "myPermissions";
	public static final String permission = "permission_type";

	SharedPreferences sharedPreferences;

	public PermissionUtils(Context context) {
		this.sharedPreferences = context.getSharedPreferences(PERMISSIONS, Context.MODE_PRIVATE);
	}

	public void isPermissionGranted(Boolean val){
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putBoolean(permission, val);
		editor.apply();
	}

	public Boolean isPermissionGranted(){
		Boolean isPermitted = sharedPreferences.getBoolean(permission, false);

		if (isPermitted)
			return true;
		else
			return false;
	}
}
