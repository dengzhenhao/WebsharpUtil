package com.websharputil.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static boolean IsWifi(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifiNetInfo.isConnected()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 
	 * 方法�? hasInternet 描述: 得到手机是否联网
	 * 
	 * @param context
	 * @return boolean 抛出异常
	 */
	public static boolean HasInternet(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null || cm.getActiveNetworkInfo() == null) {
			return false;
		} else {
			// 如果仅仅是用来判断网络连接
			// 则可以使用
			return cm.getActiveNetworkInfo().isAvailable();
			// NetworkInfo[] info = cm.getAllNetworkInfo();
			// if (info != null) {
			// for (int i = 0; i < info.length; i++) {
			// if (info[i].getState() == NetworkInfo.State.CONNECTED) {
			// return true;
			// }
			// }
			// }
		}
		// return false;

	}

	/**
	 * 
	 * 方法�? getLocalIpAddress 描述: 返回IP
	 * 
	 * @return String 抛出异常
	 */
	public static String GetLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断是否打开gps精确定位
	 * @param context
	 * @return
	 */
	public static boolean IsGpsLocationOpen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		return gps;
	}

	/**
	 * 判断是否打开wifi/移动网络信号的定位
	 * @param context
	 * @return
	 */
	public static boolean IsNetworkLocationOpen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		return network;
	}

}
