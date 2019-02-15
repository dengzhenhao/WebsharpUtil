package com.websharputil.file;

import java.io.BufferedReader;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.websharputil.common.ConvertUtil;
import com.websharputil.common.Util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

public class FileUtil {

	public static void FileCopy(File s, File t) {

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileNameFromUrl(String url) {
		File f = new File("");

		if (url.equals(""))
			return "";
		if (url.indexOf("/") == -1)
			return url;
		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * 得到文件的编码类�?
	 * 
	 * @param fileName
	 * @return
	 */
	public static String GetCharset(String fileName) {
		java.io.File f = new java.io.File(fileName);
		java.io.BufferedInputStream bin;
		try {
			bin = new java.io.BufferedInputStream(
					new java.io.FileInputStream(f));
			int p = (bin.read() << 8) + bin.read();
			String code = null;
			switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "Unicode big endian";
				break;
			default:
				code = "ANSI";
			}
			return code;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * 方法名: GetFileSize 描述: 得到文件大小,M为单位,保留2位小数
	 * 
	 * @param totalSize
	 * @return String 抛出异常
	 */
	public static String GetFileSizeForMB(int totalSize, String format) {
		return ConvertUtil.ParseDoubleToString(totalSize * 1.0 / (1024 * 1024),
				format);
	}

	/**
	 * 
	 * 方法名: GetFileSizeForKB 描述: 得到文件大小,以 KB为单位,保留2位小数
	 * 
	 * @param totalSize
	 * @return String 抛出异常
	 */
	public static String GetFileSizeForKB(int totalSize, String format) {
		return ConvertUtil
				.ParseDoubleToString(totalSize * 1.0 / (1024), format);
	}

	public static String GetFileNameFromUrl(String url) {
		if (url.equals(""))
			return "";
		if (url.indexOf("/") == -1)
			return url;
		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * 
	 * 方法名: IsFileExists 描述: 文件是否存在
	 * 
	 * @param filePath
	 * @return boolean 抛出异常
	 */
	public static boolean IsFileExists(String filePath) {
		try {
			File f = new File(filePath);
			return f.exists();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * 方法名: CreateDirs 描述: 初始化目录 抛出异常
	 */
	public static void InitDefaultDirs(String... arr) {
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				FileUtil.CreateDir(arr[i]);
			}
		}
	}

	// 获取对应目录下的文件列表
	public static List<String> GetFiles(String path) {
		List<String> lst_return = new ArrayList<String>();
		String filePath;
		String fileName;
		String extName;
		File file = new File(path);

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				GetFiles(f.getAbsolutePath());
			}
		} else {
			filePath = file.getAbsolutePath();
			lst_return.add(filePath);
		}
		return lst_return;
	}

	public static boolean CreateDir(String path) {
		boolean ret = false;
		try {
			File file = new File(path);
			if (!file.exists())
				file.mkdirs();
			if (!file.isDirectory()) {
				ret = file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 创建文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean CreateFile(String path) {
		boolean ret = false;
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			if (!file.isFile()) {
				ret = file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void DeleteDirectory(File directory) {
		File[] childFiles = directory.listFiles();

		for (int i = 0; i < childFiles.length; i++) {
			if (childFiles[i].isFile()) {
				if (!childFiles[i].getName().contains("mixplayer")) {
					childFiles[i].delete();
				}
			}
		}
	}

	public static boolean DownloadUrlFile(String fileUrl, String fileName,
			String localPath) {
		try {
			File f = new File(localPath + fileName);
			if (f.exists()) {
				f.delete();
			}
			int bytesum = 0;
			int byteread = 0;
			URL url = new URL(fileUrl);
			f = new File(localPath, fileName);
			InputStream inStream = (InputStream) url.getContent();
			FileOutputStream fs = new FileOutputStream(f);
			byte[] buffer = new byte[1024];
			int length;
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.flush();
			inStream.close();
			fs.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void WriteFile(String fileName, String value) {
		try {
			File f = new File(fileName);
			if (!f.exists()) {
				f.createNewFile();
			}

			FileWriter fw = new FileWriter(f, true);// 创建FileWriter对象，用来写入字符流
			BufferedWriter bw = new BufferedWriter(fw, 8192); // 将缓冲对文件的输
			bw.write(value); // 写入文件
			bw.flush(); // 刷新该流的缓
			bw.close();
			fw.close();
			f = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开文件时调用,打开第三方的应用
	 */
	public static void OpenFilesByApp(String path, boolean isUrl,
			Context context) {
		Uri uri = null;
		if (isUrl) {
			uri = Uri.parse(path);
		} else {
			uri = Uri.fromFile(new File(path));
		}

		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);

		String type = getMIMEType(path);
		intent.setDataAndType(uri, type);
		if (!type.equals("*/*")) {
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				context.startActivity(ShowOpenAppListDialog(path));
			}
		} else {
			context.startActivity(ShowOpenAppListDialog(path));
		}
	}

	public static Intent ShowOpenAppListDialog(String param) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		Uri uri = Uri.fromFile(new File(param));
		intent.setDataAndType(uri, "*/*");
		return intent;
	}

	/**
	 * --获取文件类型 --
	 */
	public static String getMIMEType(String filePath) {
		String type = "*/*";
		String fName = filePath;

		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}

		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "") {
			return type;
		}

		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0])) {
				type = MIME_MapTable[i][1];
			}
		}
		return type;
	}

	/**
	 * -- MIME 列表 --
	 */
	public static final String[][] MIME_MapTable = {
			// --{后缀名， MIME类型} --
			{ ".3gp", "video/3gpp" }, { ".3gpp", "video/3gpp" },
			{ ".aac", "audio/x-mpeg" }, { ".amr", "audio/x-mpeg" },
			{ ".apk", "application/vnd.android.package-archive" },
			{ ".avi", "video/x-msvideo" },
			{ ".aab", "application/x-authoware-bin" },
			{ ".aam", "application/x-authoware-map" },
			{ ".aas", "application/x-authoware-seg" },
			{ ".ai", "application/postscript" }, { ".aif", "audio/x-aiff" },
			{ ".aifc", "audio/x-aiff" }, { ".aiff", "audio/x-aiff" },
			{ ".als", "audio/x-alpha5" }, { ".amc", "application/x-mpeg" },
			{ ".ani", "application/octet-stream" }, { ".asc", "text/plain" },
			{ ".asd", "application/astound" }, { ".asf", "video/x-ms-asf" },
			{ ".asn", "application/astound" },
			{ ".asp", "application/x-asap" }, { ".asx", " video/x-ms-asf" },
			{ ".au", "audio/basic" }, { ".avb", "application/octet-stream" },
			{ ".awb", "audio/amr-wb" }, { ".bcpio", "application/x-bcpio" },
			{ ".bld", "application/bld" }, { ".bld2", "application/bld2" },
			{ ".bpk", "application/octet-stream" },
			{ ".bz2", "application/x-bzip2" },
			{ ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" },
			{ ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" },
			{ ".cal", "image/x-cals" }, { ".ccn", "application/x-cnc" },
			{ ".cco", "application/x-cocoa" },
			{ ".cdf", "application/x-netcdf" },
			{ ".cgi", "magnus-internal/cgi" },
			{ ".chat", "application/x-chat" },
			{ ".clp", "application/x-msclip" },
			{ ".cmx", "application/x-cmx" },
			{ ".co", "application/x-cult3d-object" },
			{ ".cod", "image/cis-cod" }, { ".cpio", "application/x-cpio" },
			{ ".cpt", "application/mac-compactpro" },
			{ ".crd", "application/x-mscardfile" },
			{ ".csh", "application/x-csh" }, { ".csm", "chemical/x-csml" },
			{ ".csml", "chemical/x-csml" }, { ".css", "text/css" },
			{ ".cur", "application/octet-stream" },
			{ ".doc", "application/msword" }, { ".dcm", "x-lml/x-evm" },
			{ ".dcr", "application/x-director" }, { ".dcx", "image/x-dcx" },
			{ ".dhtml", "text/html" }, { ".dir", "application/x-director" },
			{ ".dll", "application/octet-stream" },
			{ ".dmg", "application/octet-stream" },
			{ ".dms", "application/octet-stream" },
			{ ".dot", "application/x-dot" }, { ".dvi", "application/x-dvi" },
			{ ".dwf", "drawing/x-dwf" }, { ".dwg", "application/x-autocad" },
			{ ".dxf", "application/x-autocad" },
			{ ".dxr", "application/x-director" },
			{ ".ebk", "application/x-expandedbook" },
			{ ".emb", "chemical/x-embl-dl-nucleotide" },
			{ ".embl", "chemical/x-embl-dl-nucleotide" },
			{ ".eps", "application/postscript" },
			{ ".epub", "application/epub+zip" }, { ".eri", "image/x-eri" },
			{ ".es", "audio/echospeech" }, { ".esl", "audio/echospeech" },
			{ ".etc", "application/x-earthtime" }, { ".etx", "text/x-setext" },
			{ ".evm", "x-lml/x-evm" }, { ".evy", "application/x-envoy" },
			{ ".exe", "application/octet-stream" },
			{ ".fh4", "image/x-freehand" }, { ".fh5", "image/x-freehand" },
			{ ".fhc", "image/x-freehand" }, { ".fif", "image/fif" },
			{ ".fm", "application/x-maker" }, { ".fpx", "image/x-fpx" },
			{ ".fvi", "video/isivideo" }, { ".flv", "video/x-msvideo" },
			{ ".gau", "chemical/x-gaussian-input" },
			{ ".gca", "application/x-gca-compressed" },
			{ ".gdb", "x-lml/x-gdb" }, { ".gif", "image/gif" },
			{ ".gps", "application/x-gps" }, { ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" }, { ".gif", "image/gif" },
			{ ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" },
			{ ".h", "text/plain" }, { ".hdf", "application/x-hdf" },
			{ ".hdm", "text/x-hdml" }, { ".hdml", "text/x-hdml" },
			{ ".htm", "text/html" }, { ".html", "text/html" },
			{ ".hlp", "application/winhlp" },
			{ ".hqx", "application/mac-binhex40" }, { ".hts", "text/html" },
			{ ".ice", "x-conference/x-cooltalk" },
			{ ".ico", "application/octet-stream" }, { ".ief", "image/ief" },
			{ ".ifm", "image/gif" }, { ".ifs", "image/ifs" },
			{ ".imy", "audio/melody" },
			{ ".ins", "application/x-net-install" },
			{ ".ips", "application/x-ipscript" },
			{ ".ipx", "application/x-ipix" }, { ".it", "audio/x-mod" },
			{ ".itz", "audio/x-mod" }, { ".ivr", "i-world/i-vrml" },
			{ ".j2k", "image/j2k" },
			{ ".jad", "text/vnd.sun.j2me.app-descriptor" },
			{ ".jam", "application/x-jam" },
			{ ".jnlp", "application/x-java-jnlp-file" },
			{ ".jpe", "image/jpeg" }, { ".jpz", "image/jpeg" },
			{ ".jwc", "application/jwc" },
			{ ".jar", "application/java-archive" }, { ".java", "text/plain" },
			{ ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" },
			{ ".js", "application/x-javascript" },
			{ ".kjx", "application/x-kjx" }, { ".lak", "x-lml/x-lak" },
			{ ".latex", "application/x-latex" },
			{ ".lcc", "application/fastman" },
			{ ".lcl", "application/x-digitalloca" },
			{ ".lcr", "application/x-digitalloca" },
			{ ".lgh", "application/lgh" },
			{ ".lha", "application/octet-stream" }, { ".lml", "x-lml/x-lml" },
			{ ".lmlpack", "x-lml/x-lmlpack" }, { ".log", "text/plain" },
			{ ".lsf", "video/x-ms-asf" }, { ".lsx", "video/x-ms-asf" },
			{ ".lzh", "application/x-lzh " },
			{ ".m13", "application/x-msmediaview" },
			{ ".m14", "application/x-msmediaview" }, { ".m15", "audio/x-mod" },
			{ ".m3u", "audio/x-mpegurl" }, { ".m3url", "audio/x-mpegurl" },
			{ ".ma1", "audio/ma1" }, { ".ma2", "audio/ma2" },
			{ ".ma3", "audio/ma3" }, { ".ma5", "audio/ma5" },
			{ ".man", "application/x-troff-man" },
			{ ".map", "magnus-internal/imagemap" },
			{ ".mbd", "application/mbedlet" },
			{ ".mct", "application/x-mascot" },
			{ ".mdb", "application/x-msaccess" }, { ".mdz", "audio/x-mod" },
			{ ".me", "application/x-troff-me" }, { ".mel", "text/x-vmel" },
			{ ".mi", "application/x-mif" }, { ".mid", "audio/midi" },
			{ ".midi", "audio/midi" }, { ".m4a", "audio/mp4a-latm" },
			{ ".m4b", "audio/mp4a-latm" }, { ".m4p", "audio/mp4a-latm" },
			{ ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" },
			{ ".mp3", "audio/x-mpeg" }, { ".mp4", "video/mp4" },
			{ ".mpc", "application/vnd.mpohun.certificate" },
			{ ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" },
			{ ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" },
			{ ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" },
			{ ".mif", "application/x-mif" }, { ".mil", "image/x-cals" },
			{ ".mio", "audio/x-mio" }, { ".mmf", "application/x-skt-lbs" },
			{ ".mng", "video/x-mng" }, { ".mny", "application/x-msmoney" },
			{ ".moc", "application/x-mocha" },
			{ ".mocha", "application/x-mocha" }, { ".mod", "audio/x-mod" },
			{ ".mof", "application/x-yumekara" },
			{ ".mol", "chemical/x-mdl-molfile" },
			{ ".mop", "chemical/x-mopac-input" },
			{ ".movie", "video/x-sgi-movie" },
			{ ".mpn", "application/vnd.mophun.application" },
			{ ".mpp", "application/vnd.ms-project" },
			{ ".mps", "application/x-mapserver" }, { ".mrl", "text/x-mrml" },
			{ ".mrm", "application/x-mrm" },
			{ ".ms", "application/x-troff-ms" },
			{ ".mts", "application/metastream" },
			{ ".mtx", "application/metastream" },
			{ ".mtz", "application/metastream" },
			{ ".mzv", "application/metastream" },
			{ ".nar", "application/zip" }, { ".nbmp", "image/nbmp" },
			{ ".nc", "application/x-netcdf" }, { ".ndb", "x-lml/x-ndb" },
			{ ".ndwn", "application/ndwn" }, { ".nif", "application/x-nif" },
			{ ".nmz", "application/x-scream" },
			{ ".nokia-op-logo", "image/vnd.nok-oplogo-color" },
			{ ".npx", "application/x-netfpx" }, { ".nsnd", "audio/nsnd" },
			{ ".nva", "application/x-neva1" }, { ".oda", "application/oda" },
			{ ".oom", "application/x-atlasMate-plugin" },
			{ ".ogg", "audio/ogg" }, { ".pac", "audio/x-pac" },
			{ ".pae", "audio/x-epac" }, { ".pan", "application/x-pan" },
			{ ".pbm", "image/x-portable-bitmap" }, { ".pcx", "image/x-pcx" },
			{ ".pda", "image/x-pda" }, { ".pdb", "chemical/x-pdb" },
			{ ".pdf", "application/pdf" },
			{ ".pfr", "application/font-tdpfr" },
			{ ".pgm", "image/x-portable-graymap" },
			{ ".pict", "image/x-pict" }, { ".pm", "application/x-perl" },
			{ ".pmd", "application/x-pmd" }, { ".png", "image/png" },
			{ ".pnm", "image/x-portable-anymap" }, { ".pnz", "image/png" },
			{ ".pot", "application/vnd.ms-powerpoint" },
			{ ".ppm", "image/x-portable-pixmap" },
			{ ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pqf", "application/x-cprplayer" },
			{ ".pqi", "application/cprplayer" },
			{ ".prc", "application/x-prc" },
			{ ".proxy", "application/x-ns-proxy-autoconfig" },
			{ ".prop", "text/plain" }, { ".ps", "application/postscript" },
			{ ".ptlk", "application/listenup" },
			{ ".pub", "application/x-mspublisher" },
			{ ".pvx", "video/x-pv-pvx" }, { ".qcp", "audio/vnd.qcelp" },
			{ ".qt", "video/quicktime" }, { ".qti", "image/x-quicktime" },
			{ ".qtif", "image/x-quicktime" },
			{ ".r3t", "text/vnd.rn-realtext3d" },
			{ ".ra", "audio/x-pn-realaudio" },
			{ ".ram", "audio/x-pn-realaudio" },
			{ ".ras", "image/x-cmu-raster" },
			{ ".rdf", "application/rdf+xml" },
			{ ".rf", "image/vnd.rn-realflash" }, { ".rgb", "image/x-rgb" },
			{ ".rlf", "application/x-richlink" },
			{ ".rm", "audio/x-pn-realaudio" }, { ".rmf", "audio/x-rmf" },
			{ ".rmm", "audio/x-pn-realaudio" },
			{ ".rnx", "application/vnd.rn-realplayer" },
			{ ".roff", "application/x-troff" },
			{ ".rp", "image/vnd.rn-realpix" },
			{ ".rpm", "audio/x-pn-realaudio-plugin" },
			{ ".rt", "text/vnd.rn-realtext" }, { ".rte", "x-lml/x-gps" },
			{ ".rtf", "application/rtf" },
			{ ".rtg", "application/metastream" }, { ".rtx", "text/richtext" },
			{ ".rv", "video/vnd.rn-realvideo" },
			{ ".rwc", "application/x-rogerwilco" },
			{ ".rar", "application/x-rar-compressed" },
			{ ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".s3m", "audio/x-mod" }, { ".s3z", "audio/x-mod" },
			{ ".sca", "application/x-supercard" },
			{ ".scd", "application/x-msschedule" },
			{ ".sdf", "application/e-score" },
			{ ".sea", "application/x-stuffit" }, { ".sgm", "text/x-sgml" },
			{ ".sgml", "text/x-sgml" }, { ".shar", "application/x-shar" },
			{ ".shtml", "magnus-internal/parsed-html" },
			{ ".shw", "application/presentations" }, { ".si6", "image/si6" },
			{ ".si7", "image/vnd.stiwap.sis" },
			{ ".si9", "image/vnd.lgtwap.sis" },
			{ ".sis", "application/vnd.symbian.install" },
			{ ".sit", "application/x-stuffit" },
			{ ".skd", "application/x-koan" }, { ".skm", "application/x-koan" },
			{ ".skp", "application/x-koan" }, { ".skt", "application/x-koan" },
			{ ".slc", "application/x-salsa" }, { ".smd", "audio/x-smd" },
			{ ".smi", "application/smil" }, { ".smil", "application/smil" },
			{ ".smp", "application/studiom" }, { ".smz", "audio/x-smd" },
			{ ".sh", "application/x-sh" }, { ".snd", "audio/basic" },
			{ ".spc", "text/x-speech" },
			{ ".spl", "application/futuresplash" },
			{ ".spr", "application/x-sprite" },
			{ ".sprite", "application/x-sprite" },
			{ ".sdp", "application/sdp" }, { ".spt", "application/x-spt" },
			{ ".src", "application/x-wais-source" },
			{ ".stk", "application/hyperstudio" }, { ".stm", "audio/x-mod" },
			{ ".sv4cpio", "application/x-sv4cpio" },
			{ ".sv4crc", "application/x-sv4crc" }, { ".svf", "image/vnd" },
			{ ".svg", "image/svg-xml" }, { ".svh", "image/svh" },
			{ ".svr", "x-world/x-svr" },
			{ ".swf", "application/x-shockwave-flash" },
			{ ".swfl", "application/x-shockwave-flash" },
			{ ".t", "application/x-troff" },
			{ ".tad", "application/octet-stream" },
			{ ".talk", "text/x-speech" }, { ".tar", "application/x-tar" },
			{ ".taz", "application/x-tar" },
			{ ".tbp", "application/x-timbuktu" },
			{ ".tbt", "application/x-timbuktu" },
			{ ".tcl", "application/x-tcl" }, { ".tex", "application/x-tex" },
			{ ".texi", "application/x-texinfo" },
			{ ".texinfo", "application/x-texinfo" },
			{ ".tgz", "application/x-tar" },
			{ ".thm", "application/vnd.eri.thm" }, { ".tif", "image/tiff" },
			{ ".tiff", "image/tiff" }, { ".tki", "application/x-tkined" },
			{ ".tkined", "application/x-tkined" },
			{ ".toc", "application/toc" }, { ".toy", "image/toy" },
			{ ".tr", "application/x-troff" }, { ".trk", "x-lml/x-gps" },
			{ ".trm", "application/x-msterminal" },
			{ ".tsi", "audio/tsplayer" }, { ".tsp", "application/dsptype" },
			{ ".tsv", "text/tab-separated-values" },
			{ ".ttf", "application/octet-stream" },
			{ ".ttz", "application/t-time" }, { ".txt", "text/plain" },
			{ ".ult", "audio/x-mod" }, { ".ustar", "application/x-ustar" },
			{ ".uu", "application/x-uuencode" },
			{ ".uue", "application/x-uuencode" },
			{ ".vcd", "application/x-cdlink" }, { ".vcf", "text/x-vcard" },
			{ ".vdo", "video/vdo" }, { ".vib", "audio/vib" },
			{ ".viv", "video/vivo" }, { ".vivo", "video/vivo" },
			{ ".vmd", "application/vocaltec-media-desc" },
			{ ".vmf", "application/vocaltec-media-file" },
			{ ".vmi", "application/x-dreamcast-vms-info" },
			{ ".vms", "application/x-dreamcast-vms" },
			{ ".vox", "audio/voxware" }, { ".vqe", "audio/x-twinvq-plugin" },
			{ ".vqf", "audio/x-twinvq" }, { ".vql", "audio/x-twinvq" },
			{ ".vre", "x-world/x-vream" }, { ".vrml", "x-world/x-vrml" },
			{ ".vrt", "x-world/x-vrt" }, { ".vrw", "x-world/x-vream" },
			{ ".vts", "workbook/formulaone" }, { ".wax", "audio/x-ms-wax" },
			{ ".wbmp", "image/vnd.wap.wbmp" },
			{ ".web", "application/vnd.xara" }, { ".wav", "audio/x-wav" },
			{ ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" },
			{ ".wi", "image/wavelet" },
			{ ".wis", "application/x-InstallShield" },
			{ ".wm", "video/x-ms-wm" }, { ".wmd", "application/x-ms-wmd" },
			{ ".wmf", "application/x-msmetafile" },
			{ ".wml", "text/vnd.wap.wml" },
			{ ".wmlc", "application/vnd.wap.wmlc" },
			{ ".wmls", "text/vnd.wap.wmlscript" },
			{ ".wmlsc", "application/vnd.wap.wmlscriptc" },
			{ ".wmlscript", "text/vnd.wap.wmlscript" },
			{ ".wmv", "video/x-ms-wmv" }, { ".wmx", "video/x-ms-wmx" },
			{ ".wmz", "application/x-ms-wmz" }, { ".wpng", "image/x-up-wpng" },
			{ ".wps", "application/vnd.ms-works" }, { ".wpt", "x-lml/x-gps" },
			{ ".wri", "application/x-mswrite" }, { ".wrl", "x-world/x-vrml" },
			{ ".wrz", "x-world/x-vrml" }, { ".ws", "text/vnd.wap.wmlscript" },
			{ ".wsc", "application/vnd.wap.wmlscriptc" },
			{ ".wv", "video/wavelet" }, { ".wvx", "video/x-ms-wvx" },
			{ ".wxl", "application/x-wxl" },
			{ ".x-gzip", "application/x-gzip" },
			{ ".xar", "application/vnd.xara" }, { ".xbm", "image/x-xbitmap" },
			{ ".xdm", "application/x-xdma" },
			{ ".xdma", "application/x-xdma" },
			{ ".xdw", "application/vnd.fujixerox.docuworks" },
			{ ".xht", "application/xhtml+xml" },
			{ ".xhtm", "application/xhtml+xml" },
			{ ".xhtml", "application/xhtml+xml" },
			{ ".xla", "application/vnd.ms-excel" },
			{ ".xlc", "application/vnd.ms-excel" },
			{ ".xll", "application/x-excel" },
			{ ".xlm", "application/vnd.ms-excel" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlt", "application/vnd.ms-excel" },
			{ ".xlw", "application/vnd.ms-excel" }, { ".xm", "audio/x-mod" },
			{ ".xml", "text/xml" }, { ".xmz", "audio/x-mod" },
			{ ".xpi", "application/x-xpinstall" },
			{ ".xpm", "image/x-xpixmap" }, { ".xsit", "text/xml" },
			{ ".xsl", "text/xml" }, { ".xul", "text/xul" },
			{ ".xwd", "image/x-xwindowdump" }, { ".xyz", "chemical/x-pdb" },
			{ ".yz1", "application/x-yz1" },
			{ ".z", "application/x-compress" },
			{ ".zac", "application/x-zaurus-zac" },
			{ ".zip", "application/zip" }, { "", "*/*" } };

	public static String getAbsoluteImagePathFromUri(Activity act, Uri imageUri) {
		String path = "";
		if (Integer.valueOf(android.os.Build.VERSION.SDK) < 19) {
			path = getAbsoluteImagePathForSDK19(act, imageUri);
		} else {
			path = getImageAbsolutePathForSDK20(act, imageUri);
		}
		return path;
	}

	/**
	 * sdk19以下用这个方法取得相册选的照片的路径
	 * 
	 * @param act
	 * @param imageUri
	 * @return
	 */
	public static String getAbsoluteImagePathForSDK19(Activity act, Uri imageUri) {
		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };

		Cursor cursor = act.managedQuery(imageUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/**
	 * sdk20以上用这个方法取得相册选的照片的路径
	 * 
	 * @param act
	 * @param imageUri
	 * @return
	 */
	@TargetApi(19)
	public static String getImageAbsolutePathForSDK20(Activity context,
			Uri imageUri) {
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
				&& DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	public static boolean Move(File srcFile, String destPath) {
		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));

		return success;
	}

	public static boolean Move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}

	public static void Copy(File oldfile, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			// File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread;
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("error  ");
			e.printStackTrace();
		}
	}
}
