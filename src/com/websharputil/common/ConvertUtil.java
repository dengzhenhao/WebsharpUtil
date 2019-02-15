package com.websharputil.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;

public class ConvertUtil {

	/**
	 * #0
	 */
	public static final String FORMAT_DECIMAL_FORMAT_1 = "#0";
	/**
	 * #0.0
	 */
	public static final String FORMAT_DECIMAL_FORMAT_2 = "#0.0";
	/**
	 * #0.00
	 */
	public static final String FORMAT_DECIMAL_FORMAT_3 = "#0.00";
	/**
	 * #0.000
	 */
	public static final String FORMAT_DECIMAL_FORMAT_4 = "#0.000";

	public static int ParsetStringToInt32(String str, int defaultValue) {
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static long ParsetStringToLong(String str, long defaultValue) {
		try {
			return Long.parseLong(str.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static double ParsetStringToDouble(String str, double defaultValue) {
		try {
			return Double.parseDouble(str.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static String ParsetDoubleStringToFormat(String str,String format) {
		try {
			return new DecimalFormat(format).format(Double.parseDouble(str.trim()));
		} catch (Exception e) {
			return "-";
		}
	}

	public static float ParsetStringToFloat(String str, float defaultValue) {
		try {
			return Float.parseFloat(str.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String ParseDoubleToString(double value, String format) {
		return new DecimalFormat(format).format(value);
	}

	public static String ParseFloatToString(float value, String format) {
		return new DecimalFormat(format).format(value);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	public static int dip2px(Context context, float dpValue) {

		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}


}
