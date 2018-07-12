package com.lyagricultural.yuanjian.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tongguan.yuanjian.family.Utils.LogUtil;
import com.tongguan.yuanjian.family.Utils.MurmurHash;
import com.tongguan.yuanjian.family.Utils.constant.ReturnCode;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class ToolUtils
{

	// 隐藏输入法
	public static void hideKeyBoard(Activity act)
	{
		InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
		View focusView = act.getCurrentFocus();
		if(focusView != null)
		{
			imm.hideSoftInputFromWindow(focusView.getWindowToken(),0);
		}
	}

	public static float getDensity(Activity activity)
	{
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		return metric.density;
	}

	public static int findIdByResName(Context context, String uiType, String idName)
	{
		try
		{
			Class<?> localClass = Class.forName(context.getPackageName() + ".R$" + uiType);
			Field localField = localClass.getField(idName);
			int i = Integer.parseInt(localField.get(localField.getName()).toString());
			return i;
		} catch (Exception localException)
		{}
		return 0;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getNowTime()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sDateFormat.format(new java.util.Date());
	}

	public static String getPhoneName()
	{
		return android.os.Build.MODEL;
	}

	// 获取版本号
	public static String getVersionName(Context context)
	{
		String version = "";
		try
		{
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
			version = packInfo.versionName;
		} catch (Exception e)
		{
			version = "1.0.0.0";
		}
		return version;
	}

	public static String getSDPath(Context context)
	{
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if(sdCardExist)
		{
			sdDir = Environment.getExternalStorageDirectory();// 获取T根目录
		}
		else
		{ // T卡不存在时获取路径
			sdDir = context.getFilesDir().getAbsoluteFile();
		}
		return sdDir.toString();
	}

	private static String getFileName(String pathandname)
	{
		int start = pathandname.lastIndexOf("/");
		if(start != -1)
		{
			return pathandname.substring(start + 1);
		}
		else
		{
			return null;
		}
	}

	/**
	 * 获取视频的缩略图 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
	 * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
	 * 
	 * @param videoPath 视频的路径
	 * @param width 指定输出视频缩略图的宽度
	 * @param height 指定输出视频缩略图的高度度
	 * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
	 *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
	 * @return 指定大小的视频缩略图
	 */
	public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind)
	{
		Bitmap bitmap = null;
		// 获取视频的缩略图
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath,kind);
		bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	// 获取某个文件的位置
	public static int getfilePostion(ArrayList<String> flist, String fileName)
	{
		int pos = 0;
		if(flist == null || flist.size() <= 0)
		{
			return 0;
		}
		for(int i = 0; i < flist.size(); i++)
		{
			if(getFileName(flist.get(i)).endsWith(fileName))
			{
				pos = i;
				break;
			}
		}
		return pos;
	}

	// 删除文件,保留文件夹
	public static void deleteFile(File file)
	{
		if(file.isFile())
		{
			file.delete();
			return;
		}
	}

	// 判断是否有网络连接
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取可用的网络信息
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if(networkinfo != null && networkinfo.isAvailable())
		{
			// 网络可用
			return true;
		}
		// 网络不可用
		return false;
	}

	// 判断WIFI网络是否可用
	public static boolean isWifiConnected(Context context)
	{
		if(context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWiFiNetworkInfo != null)
			{
				return mWiFiNetworkInfo.isConnected();
			}
		}
		return false;
	}

	public static boolean checkIsIp(String IPAddr)
	{// 判断是否是一个合法IP
		boolean b = false;
		while(IPAddr.startsWith(" "))
		{
			IPAddr = IPAddr.substring(1,IPAddr.length()).trim();
		}
		while(IPAddr.endsWith(" "))
		{
			IPAddr = IPAddr.substring(0,IPAddr.length() - 1).trim();
		}
		if(IPAddr.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"))
		{
			String s[] = IPAddr.split("\\.");
			if(Integer.parseInt(s[0]) < 255)
				if(Integer.parseInt(s[1]) < 255)
					if(Integer.parseInt(s[2]) < 255)
						if(Integer.parseInt(s[3]) < 255)
							b = true;
		}
		return b;
	}

	// true表示含有字母
	public static boolean checkIsHostName(String addr)
	{
		return Pattern.compile("(?i)[a-z]").matcher(addr).find();
	}

	// 获取错误信息
	public static void getErrorInfo(Activity activity, int result)
	{
		String errorInfo = "";

		switch (result)
		{
			case ReturnCode.LOGIN_FAIL:
				errorInfo = "账号或密码错误";
				break;
			case ReturnCode.TCS_LOAD:
				errorInfo = "TCS负载";
				break;
			case ReturnCode.USER_EXPIRE:
				errorInfo = "用户已经过期";
				break;
			case ReturnCode.CAMERA_OFFLINE:
				errorInfo = "摄像头不在线";
				break;
			case ReturnCode.ACCOUNT_DISABLE:
				errorInfo = "账号已停用";
				break;
			case ReturnCode.USER_NOT_LOGIN:
				errorInfo = "用户还未登录";
				break;
			case ReturnCode.IPC_IDENTIFY_USER_EXISTS:
				errorInfo = "用户已注册";
				break;
			case ReturnCode.SAME_USENAME:
				errorInfo = "同一帐号不能多点登录";
				break;
			case ReturnCode.LOGIN_OTHER_PLACE:
				errorInfo = "帐号在其他地方登录";
				break;
			case ReturnCode.LOGIN_USER_NUMBER_LIMIT: // -219
				errorInfo = "超过登录用户数限制";
				break;
			case ReturnCode.VOD_SERVER_OFFLINE:
				errorInfo = "点播服务器不在线";
				break;
			case ReturnCode.ALARM_SERVER_OFFLINE:
				errorInfo = "报警服务器不在线";
				break;
			case ReturnCode.VS_SERVER_OFFLINE:
				errorInfo = "VS服务器不在线";
				break;
			case ReturnCode.DEVICE_CLOSED: // 设备已关闭
				//errorInfo = "设备已关闭";
				break;
			case ReturnCode.UNKNOW_ERROR:
				//errorInfo ="未知错误";
				break;
			default:
				break;
		}
		Toast.makeText(activity,errorInfo,Toast.LENGTH_SHORT).show();
	}

	// 获取状态栏的高度
	public static int getStatusBarHeight(Activity activity)
	{
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		return frame.top;
	}

	// 随机获取3位数的整数(100-999)
	public static int getRandomInterger()
	{
		Random r = new Random();
		return r.nextInt(899) + 100;
	}

	// 获取图片名称(时间+随机数)
	@SuppressLint("SimpleDateFormat")
	public static String getImageName()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
		String date = sDateFormat.format(new java.util.Date());
		if(date.length() == 15)
		{
			// 毫秒数为1位
			String temp = date.substring(0,date.length() - 1) + "00" + date.substring(date.length() - 1,date.length());
			date = temp;
		}
		else if(date.length() == 16)
		{
			// 毫秒数为2位
			String temp = date.substring(0,date.length() - 2) + "0" + date.substring(date.length() - 2,date.length());
			date = temp;
		}
		return date + getRandomInterger();// 总共20位
	}

	public static String getAndroidId(Context context)
	{
		String androidId = Settings.System.getString(context.getContentResolver(),"android_id");
		return androidId == null ? "" : androidId;
	}

	public static String getImei(Context context)
	{
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		return imei == null ? "" : imei;
	}
	

	public static String getUniqueString(Context context)
	{
		String imei = ToolUtils.getImei(context);
		LogUtil.i("aaaaaaaaaa imei: " + imei);
		if(!TextUtils.isDigitsOnly(imei))
		{
			return imei;
		}
		String androidId = Settings.System.getString(context.getContentResolver(), Secure.ANDROID_ID);
		LogUtil.i("aaaaaaaaaa androidid : " + androidId);
		if(!TextUtils.isDigitsOnly(androidId))
		{
			return androidId;
		}

		String serialNum = android.os.Build.SERIAL;
		LogUtil.i("aaaaaaaaaa SerialNumber: " + serialNum);
		if(!TextUtils.isDigitsOnly(serialNum))
		{
			return serialNum;
		}

		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		String wifiMac = info.getMacAddress();
		LogUtil.i("aaaaaaaaaa wifi mac: " + wifiMac);
		if(!TextUtils.isDigitsOnly(wifiMac))
		{
			return wifiMac;
		}
		
		return "unknowdevice";

	}

	public static long getLongImei(String imei)
	{
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		if(pattern.matcher(imei).matches())
		{
			return Long.parseLong(imei);
		}
		else
		{
			return MurmurHash.hash64(imei);
		}

	}

	public static String getTerminalId(Context context)
	{
		String imei = getImei(context);
		return imei;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath 图像的路径
	 * @param width 指定输出图像的宽度
	 * @param height 指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height)
	{
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath,options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if(beWidth < beHeight)
		{
			be = beWidth;
		}
		else
		{
			be = beHeight;
		}
		if(be <= 0)
		{
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath,options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap,width,height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	private static int getUid(Activity activity)
	{
		try
		{
			PackageManager pm = activity.getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo(activity.getPackageName(),PackageManager.GET_ACTIVITIES);
			return ai.uid;
		} catch (Exception e)
		{
			return 0;
		}
	}

	public static long getTotalBytes(Activity activity)
	{
		return TrafficStats.getUidRxBytes(getUid(activity)) + TrafficStats.getUidTxBytes(getUid(activity));
	}

	// 转换文件大小
	public static String FormetFileSize(long fileSize)
	{
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		String wrongSize = "0B";
		if(fileSize == 0)
		{
			return wrongSize;
		}
		if(fileSize < 1024)
		{
			fileSizeString = df.format((double) fileSize) + "B";
		}
		else if(fileSize < (1024 * 1024))
		{
			fileSizeString = df.format((double) fileSize / 1024) + "KB";
		}
		else if(fileSize < (1024 * 1024 * 1024))
		{
			fileSizeString = df.format((double) fileSize / (1024 * 1024)) + "MB";
		}
		else
		{
			fileSizeString = df.format((double) fileSize / (1024 * 1024 * 1024)) + "GB";
		}
		return fileSizeString;
	}

	public static String formatTime(long miliSecends)
	{
		int secends = (int) (miliSecends / 1000);
		int hour = secends / 60 / 60;
		int minute = (secends / 60) % 60;
		int secend = secends % 60;
		return getTimeFormatString(hour) + ":" + getTimeFormatString(minute) + ":" + getTimeFormatString(secend);
	}

	public static String formatTime(int secendTime)
	{
		int hour = secendTime / 60 / 60;
		int minute = (secendTime / 60) % 60;
		int secend = secendTime % 60;
		return getTimeFormatString(hour) + ":" + getTimeFormatString(minute) + ":" + getTimeFormatString(secend);
	}

	public static String getTimeFormatString(int time)
	{
		if(time == 0)
		{
			return "00";
		}
		else if(time < 10)
		{
			return "0" + time;
		}
		else
		{
			return time + "";
		}
	}

	public static void mkDirs(Activity activity, String folder)
	{
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED))
		{
			File dir = new File(getSDPath(activity) + folder);
			if(!dir.exists())
			{
				dir.mkdirs();
			}
		}
	}

	/**
	 * 栈顶Activity名称
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getTopActivity(Context context)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);

		if(runningTaskInfos.size() > 0)
		{
			String shortName = (runningTaskInfos.get(0).topActivity).getShortClassName();

			int _lastIndex = shortName.lastIndexOf(".");

			if(_lastIndex != -1)
			{
				shortName = TextUtils.substring(shortName,_lastIndex + 1,shortName.length());
			}

			return shortName;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 拼接imei,imsi,Android_id
	 * 
	 * @param context
	 * @return
	 */
	public static long getUniqueId(Context context)
	{
		final TelephonyManager tlm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String deviceId = tlm.getDeviceId();
		// String simSerialNum = tlm.getSimSerialNumber();
		String androidId = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);

		/*
		 * WifiManager wm =
		 * (WifiManager)context.getSystemService(Context.WIFI_SERVICE); String
		 * mac = wm.getConnectionInfo().getMacAddress();
		 * 
		 * StringBuilder mLongId = new StringBuilder();
		 * 
		 * mLongId.append(deviceId == null ? "" : deviceId) .append(simSerialNum
		 * == null ? "" : simSerialNum) .append(androidId == null ? "" :
		 * androidId) .append(mac == null ? "" : mac.replaceAll(":",""));
		 */

		try
		{
			return Long.parseLong(deviceId);
		} catch (Exception e)
		{
			long _id = parseMd5L16ToLong(androidId);
			return Math.abs(_id);
		}
	}

	public static long parseMd5L16ToLong(String md5L16)
	{
		md5L16 = md5L16.toLowerCase(Locale.getDefault());
		byte[] bA = md5L16.getBytes();
		long re = 0L;
		for(int i = 0; i < bA.length; i++)
		{
			// 加下一位的字符时，先将前面字符计算的结果左移4位
			re <<= 4;
			// 0-9数组
			byte b = (byte) (bA[i] - 48);
			// A-F字母
			if(b > 9)
			{
				b = (byte) (b - 39);
			}
			// 非16进制的字符
			if(b > 15 || b < 0)
			{
				throw new NumberFormatException("For input string '" + md5L16);
			}
			re += b;
		}
		return re;
	}

	public static int DPToPX(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static void setPopupWindowTouchModal(PopupWindow popupWindow, boolean touchModal)
	{
		if(null == popupWindow)
		{
			return;
		}
		Method method;
		try
		{

			method = PopupWindow.class.getDeclaredMethod("setTouchModal",boolean.class);
			method.setAccessible(true);
			method.invoke(popupWindow,touchModal);

		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 取得指定图片的宽和高
	 * 
	 * @param path 文件路径
	 * @return int[] int[0]-width int[1]-height
	 */
	public static int[] getImagePixels(String path)
	{
		File _file = new File(path);

		if(!_file.exists())
		{
			throw new IllegalArgumentException("The specified path file does not exist");
		}

		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path,options);

		options.inJustDecodeBounds = false;

		int _w = options.outWidth == -1 ? 0 : options.outWidth;
		int _h = options.outHeight == -1 ? 0 : options.outHeight;

		return new int[]{_w, _h};
	}

	/**
	 * 是否是2的幂 power of two
	 * 
	 * @param arg 正整数
	 * @return
	 */
	public static boolean isPowerOfTwo(int arg)
	{
		return (arg & -arg) == arg;
	}
}
