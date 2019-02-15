package com.websharputil.assets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.websharputil.common.AppData;
import com.websharputil.common.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @author dengzh
 * 
 */
public class FontUtil {
	public static Typeface tf = null;
	public static final String FONTPATH = "";

	public static void SET_TYPEFACE(Context context, String assetFontFileName,
			TextView... tvs) {
		try {
			for (TextView textView : tvs) {
				textView.setTypeface(GET_TYPEFACE(context, assetFontFileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void SET_TYPEFACE(Context context, String assetFontFileName,
			Button... tvs) {
		try {
			for (Button btn : tvs) {
				btn.setTypeface(GET_TYPEFACE(context, assetFontFileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Typeface GET_TYPEFACE(Context context,
			String assetFontFileName) {
		CopyFontToSDcard(context, assetFontFileName);
		if (tf == null) {
			try {
				tf = Typeface.createFromFile(AppData.SDCARD_FONT_File);
			} catch (Exception e) {
				e.printStackTrace();
				tf = Typeface.DEFAULT;
			}
		}
		return tf;
	}

	public static void CopyFontToSDcard(Context ctx, String assetFontFileName) {
		File ttf = new File(AppData.SDCARD_FONT_File);
		if (ttf.exists()) {
			return;
		}
		if (!ttf.getParentFile().exists()) {
			ttf.getParentFile().mkdirs();
		}

		try {
			ttf.createNewFile();
			InputStream assetsDB = ctx.getAssets().open(assetFontFileName);
			OutputStream dbOut;
			dbOut = new FileOutputStream(AppData.SDCARD_FONT_File);

			byte[] buffer = new byte[1024];
			int length;
			while ((length = assetsDB.read(buffer)) > 0) {
				dbOut.write(buffer, 0, length);
			}

			dbOut.flush();
			dbOut.close();
			assetsDB.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
