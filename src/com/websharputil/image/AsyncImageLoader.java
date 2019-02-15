package com.websharputil.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.websharputil.common.AppData;
import com.websharputil.common.LogUtil;
import com.websharputil.common.Util;
import com.websharputil.file.FileUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {

	private HashMap<String, SoftReference<Bitmap>> imageCache;

	public void clear() {
		if (imageCache != null) {
			imageCache.clear();
			System.gc();
		}
	}

	public AsyncImageLoader() {
		FileUtil.InitDefaultDirs();
		imageCache = new HashMap<String, SoftReference<Bitmap>>();
	}

	public Bitmap loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		String imageName = FileUtil.GetFileNameFromUrl(imageUrl);
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(imageUrl);
			Bitmap bm = softReference.get();
			if (bm != null) {
				return bm;
			}
		}

		File f = new File(AppData.SDCARD_IMAGE_DIR + imageName);
		Bitmap bm = null;

		try {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inPreferredConfig = Bitmap.Config.RGB_565;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			opt.inSampleSize = (int) ((f.length() / 1024) > 50 ? ((f.length() / 1024) / 50)
					: 1);
			bm = BitmapFactory.decodeFile(AppData.SDCARD_IMAGE_DIR + imageName,
					opt);
			// bm = Drawable.createFromPath(ConfigUtil.SDCARD_IMAGE_DIR
			// + imageName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (f.exists() && bm != null) {
			imageCache.put(imageUrl, new SoftReference<Bitmap>(bm));
			return bm;
		}

		final Handler handler = new Handler() {
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Bitmap) message.obj, imageUrl);
			}
		};
		new Thread() {
			@Override
			public void run() {
				File file = new File(AppData.SDCARD_IMAGE_DIR
						+ FileUtil.GetFileNameFromUrl(imageUrl));
				Bitmap bm = loadImageFromUrl(imageUrl, file);
				imageCache.put(imageUrl, new SoftReference<Bitmap>(bm));
				Message message = handler.obtainMessage(0, bm);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}

	public static Bitmap loadImageFromUrl(String url, File file) {
		LogUtil.d("filepath:%s" ,file.getAbsolutePath());
		URL m;
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			m = new URL(url);
			inStream = (InputStream) m.getContent();
			if (!file.exists()) {
				file.createNewFile();
			}
			fs = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int byteread = 0;

			while ((byteread = inStream.read(buffer)) != -1) {
				fs.write(buffer, 0, byteread);
			}

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fs != null)
					fs.flush();
				if (inStream != null)
					inStream.close();
				if (fs != null)
					fs.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inSampleSize = (int) ((file.length() / 1024) > 50 ? ((file.length() / 1024) / 50)
				: 1);
		Bitmap d = BitmapFactory.decodeFile(file.getAbsolutePath(), opt);
		// Bitmap d = Drawable.createFromPath(file.getAbsolutePath());
		return d;
	}

	public interface ImageCallback {
		public void imageLoaded(Bitmap imageDrawable, String imageUrl);
	}

}
