package com.wuwind.corelibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;

public class MediaUtil {

	/**
	 * 拍照
	 * 
	 * @param activity
	 * @param requestCode
	 * @return 存储路径
	 */
	public static String getImageFromCamera(Activity activity, int requestCode) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			String outFilePath = null;
			try {
				outFilePath = FilePathUtil.getMainPath();
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.show(activity, "创建文件夹失败");
				return null;
			}
			File dir = new File(outFilePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String capturePath = outFilePath + "/" + System.currentTimeMillis()
					+ ".jpg";
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(capturePath)));
			intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			activity.startActivityForResult(intent, requestCode);
			return capturePath;
		} else {
			Toast.makeText(activity, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
			return null;
		}
	}

	// 本地相册
	public static void getImageFromAlbum(Activity activity, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		activity.startActivityForResult(intent, requestCode);
	}

	// 查看图片
	public static void showPicture(Activity activity, String path) {
		File file = new File(path);
		// 下方是是通过Intent调用系统的图片查看器的关键代码
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "image/*");
		activity.startActivity(intent);
	}

	// 声音播放
	public static void playAudio(Activity activity, String path) {
		File file = new File(path);
		// 下方是是通过Intent调用系统的图片查看器的关键代码
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "audio/*");
		activity.startActivity(intent);
	}

	public static final void callPhone(Activity activity, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phoneNumber));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(intent);
	}
}
