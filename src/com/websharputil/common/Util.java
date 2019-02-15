package com.websharputil.common;

import android.app.Activity;

import android.app.ActivityManager;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 
 * @类名称：Util
 * @包名：com.websharp.mix.util
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2014-7-15 下午8:40:31
 * @版本 V1.0
 * @Copyright (c) 2014 by 苏州威博世网络科技有限公司.
 */
public class Util {

	public Util() {
	}

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 
	 * 方法�? startActivity 描述: 启动�?��activity
	 * 
	 * @param context
	 * @param target
	 * @param finish
	 *            void 抛出异常
	 */
	public static void startActivity(Context context, Class target,
			boolean finish) {
		Intent intent = new Intent(context, target);
		context.startActivity(intent);

		// ((Activity)
		// context).overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		if (finish)
			((Activity) context).finish();
		System.gc();
	}

	public static void startActivities(Context context, Intent[] intents,
			boolean finish) {
		// Intent intent = new Intent(context, target);
		// context.startActivity(intent);
		context.startActivities(intents);
		// ((Activity)
		// context).overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		if (finish)
			((Activity) context).finish();
		System.gc();
	}

	public static void startActivity(Context context, Class target, Bundle b,
			boolean finish) {
		Intent intent = new Intent(context, target);
		if (b != null) {
			intent.putExtras(b);
		}
		context.startActivity(intent);

		// ((Activity)
		// context).overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
		if (finish)
			((Activity) context).finish();
		System.gc();
	}

	public static void finishActivity(Context context) {
		((Activity) context).finish();
		// ((Activity)
		// context).overridePendingTransition(android.R.anim.slide_in_left,
		// android.R.anim.slide_out_right);
	}

	/**
	 * 
	 * 方法�? createToast 描述: 返回�?��Toast
	 * 
	 * @param context
	 * @param resourceId
	 * @param duration
	 * @return Toast 抛出异常
	 */
	public static Toast createToast(Context context, int resourceId,
			int duration) {
		return Toast.makeText(context, resourceId, duration);
	}

	public static Toast createToast(Context context, String message,
			int duration) {
		return Toast.makeText(context, message, duration);
	}

	/**
	 * 创建�?��alertDialog
	 * 
	 * @param context
	 * @param iconID
	 * @param title
	 * @param content
	 * @param view
	 * @param cancelable
	 * @return
	 */
	public static AlertDialog createDialog(final Context context,
			Integer iconID, int title, int content, View view,
			boolean cancelable, OnClickListener click) {
		return createDialog(context, iconID, title, content, view, cancelable,
				false, click);
	}

	/**
	 * 
	 * 方法�? createDialog 描述: TODO
	 * 
	 * @param context
	 * @param iconID
	 * @param title
	 * @param content
	 * @param view
	 * @param cancelable
	 * @param closeable
	 * @param click
	 * @return AlertDialog 抛出异常
	 */
	public static AlertDialog createDialog(final Context context,
			Integer iconID, int title, int content, View view,
			boolean cancelable, boolean closeable, OnClickListener click) {
		Builder dialogBuilder = new AlertDialog.Builder(context);
		if (iconID != null)
			dialogBuilder.setIcon(iconID);
		else {
			dialogBuilder.setIcon(android.R.color.transparent);
		}
		if (view != null)
			dialogBuilder.setView(view);
		if (cancelable) {
			dialogBuilder.setCancelable(true);
			dialogBuilder.setNegativeButton("取消", null);
		}
		if (title != -1)
			dialogBuilder.setTitle(title);
		if (content != -1)
			dialogBuilder.setMessage(content);
		if (closeable)
			dialogBuilder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((Activity) context).finish();
						}
					});
		else {
			dialogBuilder.setPositiveButton("确定", click);
		}
		AlertDialog dialog = dialogBuilder.create();
		return dialog;
	}

	/**
	 * 
	 * @param ctx
	 * @param editText
	 * @param textView
	 */
	public  void createDatePickerDialog(Context ctx,final TextView textView) {
		Calendar c = Calendar.getInstance(Locale.CHINA);
		DatePickerDialog d = new DatePickerDialog(
				ctx,
				DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,
				// 绑定监听器
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						String str = year
								+ "-"
								+ ((monthOfYear + 1) < 10 ? "0"
										+ (monthOfYear + 1) : (monthOfYear + 1))
								+ "-"
								+ (dayOfMonth < 10 ? "0" + dayOfMonth
										: dayOfMonth) + "";
//						if (editText != null)
//							editText.setText(str);
						if (textView != null)
							textView.setText(str);
					}
				}
				// 设置初始日期
				, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
				c.get(Calendar.DAY_OF_MONTH));
		d.show();
	}

}
