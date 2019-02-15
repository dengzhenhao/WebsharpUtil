package com.websharputil.common;


import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtil {

	public static final String PRE_IS_FIRST_LOGIN = "is_first_login";
	
	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(AppData.PRE_PACKAGE_NAME, Context.MODE_PRIVATE);
	}

	public static SharedPreferences.Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

	public static void setPref(Context context, String key, int value) {
		SharedPreferences.Editor editor = getEditor(context);
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setPref(Context context, String key, String value) {
		SharedPreferences.Editor editor = getEditor(context);
		editor.putString(key, value);
		editor.commit();
	}

	public static int getPref(Context context, String key, int defaultKey) {
		return getPreferences(context).getInt(key, defaultKey);
	}

	public static String getPref(Context context, String key, String defaultKey) {
		return getPreferences(context).getString(key, defaultKey);
	}

	public static boolean IsFirstUseSystem(Context context) {
		int li_Return = 0;
		try {
			li_Return = getPref(context, PRE_IS_FIRST_LOGIN, 0);
			if (li_Return != 1) {
				setPref(context, PRE_IS_FIRST_LOGIN, 1);
			}
		} catch (Exception e) {

		}
		return li_Return == 0 ? true : false;
	}
}
