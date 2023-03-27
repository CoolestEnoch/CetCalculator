package com.coolest.cet4.calculator.utils

import android.content.Context
import com.coolest.cet4.calculator.utils.CoolUtils
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.content.res.Configuration
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object CoolUtils {
	@JvmStatic
	fun getEncourageTextByScore(score: Double): String {
		val text = StringBuffer("不错哟~~")
		if (score.toInt() <= 0) {
			text.delete(0, text.length)
			text.append("你输入的数据有错误哟~爬\uD83D\uDC74\uD83D\uDC49")
		}
		if (score > 248.5 * 0.6) {
			text.delete(0, text.length)
			text.append("过啦~")
		}
		if (score > 248.5 * 0.6 && score.toInt() <425 /*&& !ScrollingActivity.writing_t.getText().toString().equals("")
				&& !ScrollingActivity.translate_t.getText().toString().equals("")*/) {
			text.delete(0, text.length)
			text.append("哎呀，没过，得加油嘞~")
		}
		if (score.toInt() >= 425) {
			text.delete(0, text.length)
			text.append("这波不稳过么~")
		}
		if (score.toInt() >= 500) {
			text.delete(0, text.length)
			text.append("秀啊~")
		}
		if (score.toInt() >= 580) {
			text.delete(0, text.length)
			text.append("\uD83D\uDC2E\uD83C\uDF7A")
		}
		return text.toString()
	}

	@JvmStatic
	fun hideSoftInput(context: Context, view: View?) {
		val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		if (view != null && imm != null) {
			imm.hideSoftInputFromWindow(view.windowToken, 0)
			// imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY); // 或者第二个参数传InputMethodManager.HIDE_IMPLICIT_ONLY
		}
	}

	//平板判断算法，来自Google IO
	fun isPad(context: Context): Boolean {
		return ((context.resources.configuration.screenLayout
				and Configuration.SCREENLAYOUT_SIZE_MASK)
				>= Configuration.SCREENLAYOUT_SIZE_LARGE)
	}

	fun isPad() = adbExec("getprop ro.build.characteristics").contains("tablet")

	fun adbExec(cmd: String?): String {
		var reader: BufferedReader? = null
		var content = ""
		try {
			//("ps -P|grep bg")执行失败，PC端adb shell ps -P|grep bg执行成功
			//Process process = Runtime.getRuntime().exec("ps -P|grep tv");
			//-P 显示程序调度状态，通常是bg或fg，获取失败返回un和er
			// Process process = Runtime.getRuntime().exec("ps -P");
			//打印进程信息，不过滤任何条件
			val process = Runtime.getRuntime().exec(cmd)
			reader = BufferedReader(InputStreamReader(process.inputStream))
			val output = StringBuffer()
			var read: Int
			val buffer = CharArray(4096)
			while (reader.read(buffer).also { read = it } > 0) {
				output.append(buffer, 0, read)
			}
			reader.close()
			content = output.toString()
		} catch (e: Exception) {
			e.printStackTrace()
		}
		return content
	}

	fun getAppVersionName(context: Context): String? {
		var versionName = ""
		try {
			// ---get the package info---
			val pm = context.packageManager
			val pi = pm.getPackageInfo(context.packageName, 0)
			versionName = pi.versionName
			//			versioncode = pi.versionCode;
			if (versionName == null || versionName.length <= 0) {
				return ""
			}
		} catch (e: Exception) {
			Log.e("VersionInfo", "Exception", e)
		}
		return versionName
	}
}