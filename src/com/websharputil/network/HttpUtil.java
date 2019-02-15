package com.websharputil.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import org.json.JSONObject;

import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;

public class HttpUtil {

	public static final int CONNECT_TIME_OUT = 10000;
	public static final int READ_TIME_OUT = 10000;

	/**
	 * 取得url返回�?
	 * 
	 * @param url
	 * @param args
	 *            url参数，以键�?对加�?
	 * @param requestProperty
	 *            头信息，以键值对加入
	 * @return
	 */
	public static String getURLContent(String url,
			HashMap<String, String> args,
			HashMap<String, String> requestProperty) {
		HttpURLConnection l_connection = null;

		BufferedReader l_reader = null;
		StringBuffer sTotalString = new StringBuffer();
		try {
			String params = "";
			if (args != null) {
				params = "?";
				Iterator<String> keys = args.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					params += "&" + key + "=" + args.get(key).trim();
				}
				keys = null;
			}
			params = params.replaceFirst("&", "");
			LogUtil.e("url:%s", url + params);
			URL l_url = new URL(params.equals("?") ? url : url + params);
			l_connection = (HttpURLConnection) l_url.openConnection();
			l_connection.setConnectTimeout(CONNECT_TIME_OUT);// 连接超时
			l_connection.setReadTimeout(READ_TIME_OUT);// 读取内容超时
			l_connection.setDoOutput(true);
			l_connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
			l_connection.setUseCaches(false);// Post 请求不能使用缓存
			if (requestProperty != null) {
				Iterator<String> keys = requestProperty.keySet().iterator();
				while (keys.hasNext()) {
					String key = keys.next();
					l_connection.setRequestProperty(key,
							requestProperty.get(key).trim());
				}
			}
			l_connection.connect();
			InputStream l_urlStream = l_connection.getInputStream(); // 得到返回�?
			l_reader = new BufferedReader(new InputStreamReader(l_urlStream,
					"utf-8"));
			String sCurrentLine = "";
			while ((sCurrentLine = l_reader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
			}

			
		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();
			return AppData.NETWORK_ERROR;
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			return AppData.NETWORK_ERROR;
		} catch (IOException ex) {
			ex.printStackTrace();
			return AppData.NETWORK_ERROR;
		} catch (Exception ex) {
			ex.printStackTrace();
			return AppData.NETWORK_ERROR;
		} finally {
			l_connection.disconnect(); // 断开连接
			System.gc();
		}

		return sTotalString.toString();
	}

	/**
	 * 根据给定的url地址访问网络，得到响应内�?这里为GET方式访问)
	 * 
	 * @param url
	 *            指定的url地址
	 * @return web服务器响应的内容，为<code>String</code>类型，当访问失败时，返回为null
	 */
	public static String getWebContent(String url) {
		LogUtil.e("%s",url);
		// 创建�?��http请求对象
		HttpGet request = new HttpGet(url);
		// 创建HttpParams以用来设置HTTP参数
		HttpParams params = new BasicHttpParams();
		// 设置连接超时或响应超�?
		HttpConnectionParams.setConnectionTimeout(params, 5000);
		HttpConnectionParams.setSoTimeout(params, 5000);
		// 创建�?��网络访问处理对象
		HttpClient httpClient = new DefaultHttpClient(params);
		try {
			// 执行请求参数�?
			HttpResponse response = httpClient.execute(request);
			// 判断是否请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 获得响应信息
				String content = EntityUtils.toString(response.getEntity());
				return content;
			} else {
				// 网连接失败，使用Toast显示提示信息
				// Toast.makeText(context, "网络访问失败，请�?��您机器的联网设备!",
				// Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 释放网络连接资源
			httpClient.getConnectionManager().shutdown();
		}
		return null;
	}

	/**
	 * 解析出url请求的路径，包括页面
	 * 
	 * @param strURL
	 *            url地址
	 * @return url路径
	 */
	public static String UrlPage(String strURL) {
		String strPage = null;
		String[] arrSplit = null;

		strURL = strURL.trim().toLowerCase();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 0) {
			if (arrSplit.length > 1) {
				if (arrSplit[0] != null) {
					strPage = arrSplit[0];
				}
			}
		}

		return strPage;
	}

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * 
	 * @param strURL
	 *            url地址
	 * @return url请求参数部分
	 */
	private static String GetParametStrFromUrl(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;

		strURL = strURL.trim().toLowerCase();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}

		return strAllParam;
	}

	/**
	 * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * 
	 * @param URL
	 *            url地址
	 * @return url请求参数部分
	 */
	public static Map<String, String> URLRequest(String URL) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = GetParametStrFromUrl(URL);
		if (strUrlParam == null) {
			return mapRequest;
		}
		// 每个键值为一组 www.2cto.com
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");

			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

	public static int GetRespStatus(String url) {
		int status = -1;
		try {

			HttpHead head = new HttpHead(url);
			HttpClient client = new DefaultHttpClient();
			HttpResponse resp = client.execute(head);
			status = resp.getStatusLine().getStatusCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}

}
