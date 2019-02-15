package com.websharputil.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.json.JSONObject;

import com.websharp.websharputil.R;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * 应用的一些基本信息
 * 
 * @author dengzh
 * 
 */
public class AppData extends Application {

	public static String NETWORK_ERROR = "NETWORK_ERROR";

	public static String TAG_ERROR_MSG = "false";

	public static String VERSION_NAME = "";
	public static int VERSION_CODE = -1;

	public static String PRE_PACKAGE_NAME = "";

	public static String PUSH_USERID = "";
	public static String PUSH_CHANNELID = "";

	public static int SCREEN_WIDTH = 0;
	public static int SCREEN_HEIGHT = 0;
	public static int SCREEN_DPI = 120;

	public static String APP_NAME = "";
	public static final String ROOT_SRC = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public static final String SDCARD_IMAGE_DIR = ROOT_SRC + "/" + APP_NAME
			+ "/image/";// 图片存放
	public static final String SDCARD_FONT_File = ROOT_SRC + "/" + APP_NAME
			+ "/image/app.ttf";// 字体存放

	/**
	 * 初始化一些基本的信息
	 * @param context
	 * @param appName
	 */
	public static void InitAppData(Context context, String appName) {
		APP_NAME = appName;
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			PRE_PACKAGE_NAME = packInfo.packageName;
			VERSION_NAME = packInfo.versionName;
			VERSION_CODE = packInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	

}
