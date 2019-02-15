package com.websharputil.common;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

public class ServiceUtil {

	/**
	 * 
	 * 方法�? isServiceRunning 描述: 返回指定的服务是否在运行
	 * 
	 * @param context
	 * @param className
	 * @return boolean 抛出异常
	 */
	public static boolean isServiceRunning(Context context, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		Log.i("websharp", "service is running?==" + isRunning);
		return isRunning;
	}
}
