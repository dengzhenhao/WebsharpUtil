package com.websharputil.assets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AssetsUtil {
	
	/** 
	 * 安装assets中的apk
	 * @param context
	 * @param fileName
	 * @param assetPath
	 */
	public static void handlerInstallAssetsApk(Context context,
			String fileName, String assetPath) {

		File file = new File(fileName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			try {
				file.createNewFile();
				InputStream assetsDB = context.getAssets().open(assetPath);
				OutputStream dbOut;
				dbOut = new FileOutputStream(fileName);

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
		Uri uri = Uri.fromFile(new File(fileName));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 复制Assets文件夹中的文件到sd卡
	 * 
	 * @param targetFile
	 * @param assetsFile
	 * @param context
	 */
	public static void CopyAssetsFile(String targetFile, String assetsFile,
			Context context) {
		File file = new File(targetFile);
		if (file.exists()) {
			return;
		}
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		try {
			file.createNewFile();
			InputStream assetsDB = context.getAssets().open(assetsFile);// strln是assets文件夹下的文件名
			OutputStream dbOut;
			dbOut = new FileOutputStream(targetFile);

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
