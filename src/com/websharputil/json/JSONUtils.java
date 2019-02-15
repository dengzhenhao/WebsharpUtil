package com.websharputil.json;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.websharputil.common.LogUtil;

public class JSONUtils {

	public static final String TAG = "JSONUtils";

	public static final String EMPTY_JSON = "{}";
	public static final String EMPTY_JSON_ARRAY = "[]";
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss SSS";
	public static final double SINCE_VERSION_10 = 1.0d;
	public static final double SINCE_VERSION_11 = 1.1d;
	public static final double SINCE_VERSION_12 = 1.2d;
	public static final double UNTIL_VERSION_10 = SINCE_VERSION_10;
	public static final double UNTIL_VERSION_11 = SINCE_VERSION_11;
	public static final double UNTIL_VERSION_12 = SINCE_VERSION_12;

	/**
	 * <p>
	 * <code>JSONUtils</code> instances should NOT be constructed in standard
	 * programming. Instead, the class should be used as
	 * <code>JSONUtils.fromJson("foo");</code>.
	 * </p>
	 * <p>
	 * This constructor is public to permit tools that require a JavaBean
	 * instance to operate.
	 * </p>
	 */
	public JSONUtils() {
		super();
	}

	public static String toJson(Object target, Type targetType,
			boolean isSerializeNulls, Double version, String datePattern,
			boolean excludesFieldsWithoutExpose) {
		if (target == null)
			return EMPTY_JSON;
		GsonBuilder builder = new GsonBuilder();
		if (isSerializeNulls)
			builder.serializeNulls();
		if (version != null)
			builder.setVersion(version.doubleValue());
		if (TextUtils.isEmpty(datePattern))
			datePattern = DEFAULT_DATE_PATTERN;
		builder.setDateFormat(datePattern);
		if (excludesFieldsWithoutExpose)
			builder.excludeFieldsWithoutExposeAnnotation();
		return toJson2(target, targetType, builder);
	}

	public static String toJson(Object target) {
		return toJson(target, null, false, null, null, true);
	}

	public static String toJson(Object target, String datePattern) {
		return toJson(target, null, false, null, datePattern, true);
	}

	public static String toJson(Object target, Double version) {
		return toJson(target, null, false, version, null, true);
	}

	public static String toJson(Object target,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, null, null,
				excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Double version,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, null, false, version, null,
				excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Type targetType) {
		return toJson(target, targetType, false, null, null, true);
	}

	public static String toJson(Object target, Type targetType, Double version) {
		return toJson(target, targetType, false, version, null, true);
	}

	public static String toJson(Object target, Type targetType,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, null, null,
				excludesFieldsWithoutExpose);
	}

	public static String toJson(Object target, Type targetType, Double version,
			boolean excludesFieldsWithoutExpose) {
		return toJson(target, targetType, false, version, null,
				excludesFieldsWithoutExpose);
	}

	public static <T> T fromJson(String json, TypeToken<T> token,
			String datePattern) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (TextUtils.isEmpty(datePattern)) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, token.getType());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static <T> T fromJson(String json, TypeToken<T> token) {
		return fromJson(json, token, null);
	}

	public static <T> T fromJson(String json, Class<T> clazz, String datePattern) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		if (TextUtils.isEmpty(datePattern)) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		Gson gson = builder.create();
		try {
			return gson.fromJson(json, clazz);
		} catch (Exception ex) {
			LogUtil.e(ex, "%s ", json, clazz.getName());
			return null;
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(json, clazz, null);
	}

	public static <T> T fromJson(JsonElement jsonElement, Class<T> clazz) {
		if (null == jsonElement) {
			return null;
		}
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		try {
			return gson.fromJson(jsonElement, clazz);
		} catch (Exception ex) {
			LogUtil.e(ex, "%s%s", jsonElement.toString(),
					clazz.getName());
			return null;
		}
	}

	public static String toJson2(Object target, Type targetType,
			GsonBuilder builder) {
		if (target == null)
			return EMPTY_JSON;
		Gson gson = null;
		if (builder == null) {
			gson = new Gson();
		} else {
			gson = builder.create();
		}
		String result = EMPTY_JSON;
		try {
			if (targetType == null) {
				result = gson.toJson(target);
			} else {
				result = gson.toJson(target, targetType);
			}
		} catch (Exception ex) {
			LogUtil.e(ex, "%s", target
					.getClass().getName());
			if (target instanceof Collection<?>
					|| target instanceof Iterator<?>
					|| target instanceof Enumeration<?>
					|| target.getClass().isArray()) {
				result = EMPTY_JSON_ARRAY;
			}
		}
		return result;
	}
}