package com.coolest.cet4.calculator.utils

import android.content.Context
import android.util.Log
import android.widget.LinearLayout
import android.widget.ScrollView
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.coolest.cet4.calculator.MyApplication
import com.coolest.cet4.calculator.R
import java.lang.StringBuilder
import java.util.*

object ReleaseNotesUtil {
	var noteList: MutableList<Version?> = ArrayList()
	var alert: AlertDialog? = null
	var builder: AlertDialog.Builder? = null
	const val tab_and_new_line = "\n\t\t\t"
	fun showAlertDialog(title: String?, str: String?, context: Context?) {
		builder = AlertDialog.Builder(context!!)
		alert = builder!!
			.setTitle(title)
			.setMessage(str)
			.setPositiveButton("确定") { dialog, which -> }.create() //创建AlertDialog对象
		alert!!.show() //显示对话框
		/*alert = builder!!
			.setTitle(title)
			.setMessage(str)
			.setNegativeButton("取消") { dialog, which ->
				Toast.makeText(
					context,
					"你点击了取消按钮~",
					Toast.LENGTH_SHORT
				).show()
			}
			.setPositiveButton("确定", object : DialogInterface.OnClickListener {
				fun onClck(dialog: DialogInterface?, which: Int) {
					Toast.makeText(context, "你点击了确定按钮~", Toast.LENGTH_SHORT).show()
				}
			})
			.setNeutralButton("中立") { dialog, which ->
				Toast.makeText(
					context,
					"你点击了中立按钮~",
					Toast.LENGTH_SHORT
				).show()
			}
			.create() //创建AlertDialog对象
		alert!!.show() //显示对话框*/
	}

	fun setReleaseNotes() {
		noteList.apply {
			add(Version(1.0, "第一个版本"));
			add(Version(1.1, "[优化]算分算法"));
			add(Version(1.2, "[修复]非法输入可能导致程序卡死的bug" + tab_and_new_line + "[修复]平板横屏打开可能导致闪退的bug"));
			add(Version(1.3, "[调整]将一键清空按钮移动至显著区域"));
			add(Version(1.4, "[修复]新一键清空按钮按下后出现的延迟问题"));
			add(Version(1.5, "[调整]将估分结果移至显著区域"));
			add(Version(1.6, "[增加]估分结果动画"));
			add(Version(1.7, "[优化]重构估分结果动画逻辑"));
			add(Version(1.8, "[增加]一个主题"));
			add(Version(1.9, "[增加]主题管理器"));
			add(Version(1.10, "[增加]了三个主题"));
			add(Version(1.11, "[优化]重构主题管理器底层"));
			add(Version(1.12, "[增加]好多个主题"));
			add(Version(2.0, "[增加]主题性能白名单"));
			add(Version(2.1, "[优化]重构主题性能白名单底层"));
			add(Version(2.2, "[修复]平板横屏打开可能无法显示内容的bug" + tab_and_new_line + "[新增]更新日志功能"));
			add(Version(2.3, "[修复]无限小数分数的处理"));
			add(Version(2.4, "[新增]适配小米平板5 & Mix Fold平行视界" + tab_and_new_line + "[新增]适配MIUI小白条沉浸"));
			add(Version(2.5, "[优化]分数显示逻辑" + tab_and_new_line + "[修复]偶现无法显示分数的bug"));
			add(Version(2.6, "[新增]在线更新"));
			add(Version(2.61, "[新增]支持六级算分啦!"));
			add(Version(2.62, "[修复]六级算分算法错误" + tab_and_new_line + "[优化]算分性能优化"));
			add(Version(2.621, "[修复]深色模式显示异常" + tab_and_new_line + "[优化]底层完整重构, 弃用java并迁移至kotlin"));
			add(Version(2.622, "[修复]无法算分的异常" + tab_and_new_line + "[优化]透明通知栏适配"));
			add(Version(2.631, "[新增]大学生实用工具箱"));
			add(Version(2.7, "[适配]Material You 动态配色算法\n(需更新手机系统至Android 12或更高版本)"));
//			add(Version(2.333, "[画饼]增加历史成绩储存功能\n"));
		}
		Collections.reverse(noteList)
	}

	val releaseNotes: String
		get() {
			if (noteList.isEmpty()) setReleaseNotes()
			val sb = StringBuilder()
			for (v in noteList) {
				sb.append(v)
			}
			return sb.toString()
		}

	@JvmStatic
	fun showReleaseNotes(context: Context) {
//		showAlertDialog("更新日志" + "   " + "\n当前版本" + CoolUtils.getAppVersionName(context), getReleaseNotes(), context);
		releaseNotes
		val linearLayout = LinearLayout(context)
		val params = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		)
		linearLayout.layoutParams = params
		linearLayout.orientation = LinearLayout.VERTICAL
		Log.e("for", "before")
		for (version in noteList) {
			val list = LinkedList<String>()
			list.add(version!!.version.toString())
			val detail = version.notes.split(tab_and_new_line.toRegex()).toTypedArray()
			for (str in detail) {
				list.add(str)
			}
			linearLayout.addView(
				ViewUtils_Kotlin.createBigButton(
					context,
					R.drawable.ic_baseline_grass_24,
					R.color.item_card_bg,
					list, null, R.color.white
				) {}
			)
			Log.e("for", "run in")
		}
		Log.e("for", "after")
		val scrollView = ScrollView(context)
		val scrollParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		)
		scrollView.layoutParams = scrollParams
		scrollView.addView(linearLayout)
		ViewUtils_Kotlin.getBigCardFromBottom(
			context,
			"更新日志\t当前版本${
				context.packageManager.getPackageInfo(
					context.packageName,
					0
				).versionName
			}",
			scrollView
		).show()
	}

	@JvmStatic
	fun showLastReleaseNotes(context: Context?) {
//		showAlertDialog("更新日志" + "   " + "\n当前版本" + CoolUtils.getAppVersionName(context), getReleaseNotes(), context);
		releaseNotes
		val linearLayout = LinearLayout(context)
		val params = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		)
		linearLayout.layoutParams = params
		linearLayout.orientation = LinearLayout.VERTICAL
		val version = noteList[0]
		val list = LinkedList<String>()
		list.add(version!!.version.toString())
		val detail = version.notes.split(tab_and_new_line.toRegex()).toTypedArray()
		for (str in detail) {
			list.add(str)
		}
		linearLayout.addView(
			ViewUtils_Kotlin.createBigButton(
				context!!,
				R.drawable.ic_baseline_grass_24,
				R.color.item_card_bg,
				list,
				MyApplication.dailyBingPaper,
				R.color.white
			){}
		)
		Log.e("for", "run in")
		val scrollView = ScrollView(context)
		val scrollParams: ViewGroup.LayoutParams = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		)
		scrollView.layoutParams = scrollParams
		scrollView.addView(linearLayout)
		ViewUtils_Kotlin.getBigCardFromBottom(context, "这个版本更新了啥", scrollView).show()
	}

	class Version(var version: Double, var notes: String) {
		override fun toString(): String {
			return "$version $notes\n"
		}
	}
}