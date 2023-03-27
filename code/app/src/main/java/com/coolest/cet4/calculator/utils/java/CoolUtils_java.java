package com.coolest.cet4.calculator.utils.java;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CoolUtils_java {
	private CoolUtils_java() {
	}

	public static String getEncourageTextByScore(double score){
		StringBuffer text = new StringBuffer("不错哟~~");
		if((int)score <= 0){
			text.delete(0, text.length());
			text.append("你输入的数据有错误哟~爬\uD83D\uDC74\uD83D\uDC49");
		}
		if(score > 248.5 * 0.6){
			text.delete(0, text.length());
			text.append("过啦~");
		}
		if(score > 248.5 * 0.6 && (int)score < 425
				/*&& !ScrollingActivity.writing_t.getText().toString().equals("")
				&& !ScrollingActivity.translate_t.getText().toString().equals("")*/){
			text.delete(0, text.length());
			text.append("哎呀，没过，得加油嘞~");
		}
		if((int)score >= 425){
			text.delete(0, text.length());
			text.append("这波不稳过么~");
		}
		if((int)score >= 500){
			text.delete(0, text.length());
			text.append("秀啊~");
		}
		if((int)score >= 580){
			text.delete(0, text.length());
			text.append("\uD83D\uDC2E\uD83C\uDF7A");
		}

		return text.toString();
	}

	public static void hideSoftInput(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view != null && imm != null){
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			// imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY); // 或者第二个参数传InputMethodManager.HIDE_IMPLICIT_ONLY
		}
	}

	//平板判断算法，来自Google IO
	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isPad() {
		return adbExec("getprop ro.build.characteristics").contains("tablet");
	}

	public static String adbExec(String cmd){
		BufferedReader reader = null;
		String content = "";
		try {
			//("ps -P|grep bg")执行失败，PC端adb shell ps -P|grep bg执行成功
			//Process process = Runtime.getRuntime().exec("ps -P|grep tv");
			//-P 显示程序调度状态，通常是bg或fg，获取失败返回un和er
			// Process process = Runtime.getRuntime().exec("ps -P");
			//打印进程信息，不过滤任何条件
			Process process = Runtime.getRuntime().exec(cmd);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuffer output = new StringBuffer();
			int read;
			char[] buffer = new char[4096];
			while ((read = reader.read(buffer)) > 0) {
				output.append(buffer, 0, read);
			}
			reader.close();
			content = output.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
//			versioncode = pi.versionCode;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}
}
