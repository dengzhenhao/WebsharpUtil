package com.websharputil.image;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;

/**
 * 
 * @类名称：ImageUtil
 * @包名：com.websharp.mix.util
 * @描述： TODO
 * @创建人： dengzh
 * @创建时间:2014-12-11 下午12:00:30
 * @版本 V1.0
 * @Copyright (c) 2014 by 苏州威博世网络科技有限公司.
 */
public class ImageUtil {

	public static InputStream getRequest(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5000);
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			return is;
		}
		return null;
	}

	/**
	 * 下载图片并保存到本地,如果已经存在,那么读取并放入对应的集合中去
	 * 
	 * @param imageUrl
	 */
	public static int getImage(String imageUrl, String imageName, String pid,
			HashMap<String, Bitmap> imageCache) {
		LogUtil.d("%s", imageUrl);
		try {
			File f = new File(AppData.SDCARD_IMAGE_DIR + imageName);
			Bitmap bm = BitmapFactory.decodeFile(AppData.SDCARD_IMAGE_DIR
					+ imageName);
			if (f.exists() && bm != null) {
				imageCache.put(pid, bm);
				return 0;
			}
			int bytesum = 0;
			int byteread = 0;
			f = new File(AppData.SDCARD_IMAGE_DIR, imageName);

			if (!f.exists()) {
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				f.createNewFile();
			}

			Bitmap bmp = getRoundBitmapFromUrl(imageUrl, 15);

			Matrix m = new Matrix();
			m.setScale(0.5f, 0.5f);
			bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), m, false);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);

			InputStream inStream = new ByteArrayInputStream(baos.toByteArray());
			FileOutputStream fs = new FileOutputStream(f);

			byte[] buffer = new byte[1024];

			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.flush();
			inStream.close();
			fs.close();
			bm = BitmapFactory.decodeFile(AppData.SDCARD_IMAGE_DIR + imageName);
			if (bm != null) {
				imageCache.put(pid, bm);// 读取已存在图片作为drawable返回
			}
			bm = null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			for (int i = 0; i < e.getStackTrace().length; i++) {

				String str = e.getStackTrace()[i].getFileName() + ","
						+ e.getStackTrace()[i].getClassName() + ","
						+ e.getStackTrace()[i].getMethodName() + ","
						+ e.getStackTrace()[i].getLineNumber();

			}
			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	public static Drawable loadImageFromUrl(String url) {
		URL m;
		InputStream i = null;
		try {
			m = new URL(url);
			i = (InputStream) m.getContent();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Drawable d = Drawable.createFromStream(i, "src");
		return d;
	}

	public static Drawable getDrawableFromUrl(String url) throws Exception {
		return Drawable.createFromStream(getRequest(url), null);
	}

	public static Bitmap getBitmapFromUrl(String url) throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		return byteToBitmap(bytes);
	}

	public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		Bitmap bitmap = byteToBitmap(bytes);
		return toRoundCorner(bitmap, pixels);
	}

	public static Drawable getRoundDrawableFromUrl(String url, int pixels)
			throws Exception {
		byte[] bytes = getBytesFromUrl(url);
		BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
		return toRoundCorner(bitmapDrawable, pixels);
	}

	public static Bitmap byteToBitmap(byte[] byteArray) {
		if (byteArray.length != 0) {
			return BitmapFactory
					.decodeByteArray(byteArray, 0, byteArray.length);
		} else {
			return null;
		}
	}

	public static Drawable byteToDrawable(byte[] byteArray) {
		ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
		return Drawable.createFromStream(ins, null);
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}

	public static byte[] getBytesFromUrl(String url) throws Exception {
		return readInputStream(getRequest(url));
	}

	public static byte[] GetByteFromBitmap(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] GetByteFromFile(String path) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		Bitmap image = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空
		image.compress(CompressFormat.PNG, 100, output);// 把bitmap100%高质量压缩 到
		
		byte[] result = output.toByteArray();// 转换成功了
		
		try {
			image.recycle();
			image = null;
			output.close();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图�?
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/**
	 * 去色同时加圆�?
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 * 
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {

		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));

		return bitmapDrawable;
	}

	/**
	 * 从Assets中读取图�?
	 */
	public static Bitmap getImageFromAssetsFile(String fileName, Context context) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return image;

	}
	
	public static Bitmap getThumbImage(String path) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inSampleSize = 20;
		Bitmap image = BitmapFactory.decodeFile(path, newOpts);
		Bitmap bitmap_thumb = ThumbnailUtils.extractThumbnail(image, 200, 200);
		try {
			image.recycle();
			image = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap_thumb;
	}

	/**
	 * 压缩图片后返回byte[]数组
	 * 
	 * @param path
	 * @return
	 */
	public static  byte[] compressImage(String path) {

		int targetSize = 1280;
		int size = 165 * 1024;
		int quality = 90;
		float scale = 0.90f;

		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
		// newOpts.inPreferQualityOverSpeed= false;
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, newOpts);
		int imgWidth = newOpts.outWidth;
		int imgHeight = newOpts.outHeight;
		if ((imgWidth > imgHeight && imgWidth >= targetSize * 2)
				|| (imgHeight > imgWidth && imgHeight >= targetSize * 2)) {
			if (imgWidth > imgHeight) {
				newOpts.inSampleSize = 2;
			} else {
				newOpts.inSampleSize = 2;
			}
		}
		newOpts.inJustDecodeBounds = false;
		Bitmap image = BitmapFactory.decodeFile(path, newOpts);// 此时返回bm为空

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, quality, out);
		float zoom = (float) Math.sqrt(size / (float) out.toByteArray().length);
		Matrix matrix = new Matrix();
		matrix.setScale(zoom, zoom);
		Bitmap result = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
				image.getHeight(), matrix, true);
		out.reset();
		result.compress(Bitmap.CompressFormat.JPEG, quality, out);
		while (out.toByteArray().length > size) {
			matrix.setScale(scale, scale);
			result = Bitmap.createBitmap(result, 0, 0, result.getWidth(),
					result.getHeight(), matrix, true);
			out.reset();
			result.compress(Bitmap.CompressFormat.JPEG, quality, out);
		}
		byte[] arr = out.toByteArray();
		try {
			image.recycle();
			image = null;
			result.recycle();
			result = null;
			out.close();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
}