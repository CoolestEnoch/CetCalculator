package com.coolest.cet4.calculator.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.coolest.cet4.calculator.MyApplication.Companion.dailyBingPaper
import com.coolest.cet4.calculator.R
import com.coolest.cet4.calculator.databinding.ActivityScrollingBinding
import com.coolest.cet4.calculator.utils.BackgroundUtil
import com.coolest.cet4.calculator.utils.CoolUtils
import com.coolest.cet4.calculator.utils.ReleaseNotesUtil.showLastReleaseNotes
import com.coolest.cet4.calculator.utils.ReleaseNotesUtil.showReleaseNotes
import com.coolest.cet4.calculator.utils.ScoreSheetUtil
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin.createBigButton
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin.getBigCardFromBottom
import com.coolest.cet4.calculator.utils.checkUpdate
import com.coolest.toolbox.utils.getTodayWallpaperURL
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import java.io.DataInputStream
import java.net.URL
import java.util.*
import kotlin.concurrent.thread

class ScrollingActivity : AppCompatActivity() {
	private lateinit var binding: ActivityScrollingBinding

	//界面变量
	var mainActivity: Activity = this
	var mainView: View? = null
	var toolBarLayout: CollapsingToolbarLayout? = null

	//界面EditText变量
//	var listen_A_t: EditText? = null
//	var listen_B_t: EditText? = null
//	var listen_C_t: EditText? = null
//	var findViewById<EditText>(R.id.reading_section_A): EditText? = null
//	var findViewById<EditText>(R.id.reading_section_B): EditText? = null
//	var findViewById<EditText>(R.id.reading_section_C): EditText? = null
//	var fab_cal: FloatingActionButton? = null
//	var fab_cls: FloatingActionButton? = null
//	var findViewById<EditText>(R.id.writing_inp): EditText? = null
//	var findViewById<EditText>(R.id.translate_inp): EditText? = null
	var author_b_item: MenuItem? = null
//	var title_web_view: WebView? = null
//	var title_web_view_settings: WebSettings? = null

	//哼,哼,啊啊啊啊啊
	lateinit var mediaPlayer: MediaPlayer

	//工作变量
	var score = 0.0
	var clear_clicked = false
	var changingTitle = false
	var titleStrQue: Queue<String> = LinkedList()
	var editTexts: MutableList<EditText?> = ArrayList()
	lateinit var bg_header_URL: String
	var autoRefreshThread: Thread = Thread(AutoRefresh())

	//流体背景
//	var bgu: BackgroundUtil? = null

	//工具类创建
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityScrollingBinding.inflate(
			layoutInflater
		)
		setContentView(binding!!.root)
		val toolbar = binding!!.toolbar
		setSupportActionBar(toolbar)
		toolBarLayout = binding!!.toolbarLayout
		toolBarLayout!!.title = title

		//设置每日图片
		thread {
			try {
				val wallpaperUrl = URL(getTodayWallpaperURL())
				val dis = DataInputStream(wallpaperUrl.openStream())
				val dailyWallpaper = BitmapFactory.decodeStream(dis)
				dailyBingPaper = dailyWallpaper
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}


		//检查更新
		try {
			val currentVersion = packageManager.getPackageInfo(packageName, 0).versionName
			checkUpdate(
				currentVersion,
				findViewById(R.id.mainRefreshLayout),
				"CoolestEnoch",
				"CetCalculator"
			)
			//判断是否经历了版本更新, 如果更新了就弹出更新日志
			val lastVersion =
				getSharedPreferences("config", MODE_PRIVATE).getString("last_version", "1.0")
			if (lastVersion != currentVersion) {
				getSharedPreferences("config", MODE_PRIVATE).edit()
					.putString("last_version", currentVersion).commit()
				showLastReleaseNotes(mainActivity)
			}
		} catch (e: PackageManager.NameNotFoundException) {
			e.printStackTrace()
		}

		//初始化fab_cal
/*		fab_cal = binding!!.calcB
		fab_cls = binding!!.clsB*/

		//工具类创建
//		scoreSheetUtil = ScoreSheetUtil()

		//界面变量声明
/*		listen_A_t = findViewById(R.id.listen_section_A)
		listen_B_t = findViewById(R.id.listen_section_B)
		listen_C_t = findViewById(R.id.listen_section_C)
		findViewById<EditText>(R.id.reading_section_A) = findViewById(R.id.reading_section_A)
		findViewById<EditText>(R.id.reading_section_B) = findViewById(R.id.reading_section_B)
		findViewById<EditText>(R.id.reading_section_C) = findViewById(R.id.reading_section_C)
		findViewById<EditText>(R.id.writing_inp) = findViewById(R.id.writing_inp)
		findViewById<EditText>(R.id.translate_inp) = findViewById(R.id.translate_inp)*/
		mainView = window.decorView
		titleStrQue = LinkedList()
		editTexts = ArrayList()

		//哼哼哼啊啊啊啊啊啊啊啊啊
		mediaPlayer = MediaPlayer.create(mainActivity, R.raw.checksum)
		editTexts.add(findViewById<EditText>(R.id.listen_section_A))
		editTexts.add(findViewById<EditText>(R.id.listen_section_B))
		editTexts.add(findViewById<EditText>(R.id.listen_section_C))
		editTexts.add(findViewById<EditText>(R.id.reading_section_A))
		editTexts.add(findViewById<EditText>(R.id.reading_section_B))
		editTexts.add(findViewById<EditText>(R.id.reading_section_C))
		editTexts.add(findViewById<EditText>(R.id.writing_inp))
		editTexts.add(findViewById<EditText>(R.id.translate_inp))
		/**
		 * MIUI小白条沉浸
		 * from https://dev.mi.com/console/doc/detail?pId=2229
		 */
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) //设置沉浸式状态栏，在MIUI系统中，状态栏背景透明。原生系统中，状态栏背景半透明。
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION) //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。
		/**
		 * MIUI平行世界适配
		 * from https://dev.mi.com/console/doc/detail?pId=2448
		 */
		val config = this.applicationContext.resources.configuration.toString()
		val isInMagicWindow = config.contains("miui-magic-windows")
		if (isInMagicWindow) {
			Snackbar.make(mainView!!, "当前运行在平行视界下", Snackbar.LENGTH_LONG).show()
		}

		

		//流体背景
//		bgu = BackgroundUtil()
		try {
//			bg_header_URL = "file:///android_asset/hurricane.html";
			var random = (Math.random() * BackgroundUtil.length * 2).toInt() / 2
			while (BackgroundUtil!!.withinPerformanceBlackList(random)) {
				random = (Math.random() * BackgroundUtil!!.length * 2).toInt() / 2
			}
			bg_header_URL = BackgroundUtil.getEle(random)
//			title_web_view = findViewById<WebView>(R.id.bg_webview)
//			title_web_view_settings = findViewById<WebView>(R.id.bg_webview).settings
//			title_web_view_settings!!.javaScriptEnabled = true
			findViewById<WebView>(R.id.bg_webview).settings.javaScriptEnabled = true
			//apk内
//			title_web_view.loadUrl("file:///android_asset/index.html");
			findViewById<WebView>(R.id.bg_webview).loadUrl(bg_header_URL)
			//内部存储
			//title_web_view.loadUrl("content://com.android.htmlfileprovider/sdcard/index.html");
			//真互联网
			//title_web_view.loadUrl("http://wap.baidu.com");
			if ((bg_header_URL == "file:///android_asset/index.html")) {
				autoRefreshThread.interrupt()
				autoRefreshThread = Thread(AutoRefresh())
				autoRefreshThread.start()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}


		binding!!.calcB.setOnClickListener {
			CoolUtils.hideSoftInput(mainActivity, window.decorView)
			score = calculateScore(
				getSharedPreferences(
					"config",
					MODE_PRIVATE
				).getString("cet_level", "")
			)
			if (score >= 0) {
				//整数分数的处理
				var score_s = if ((score.toString().contains(".0"))) score.toString()
					.substring(0, score.toString().indexOf('.')) else score.toString()
				//无限小数分数的处理
				if (score_s.contains(".") && score_s.substring(score_s.indexOf(".") + 1).length > 2) {
					score_s = score_s.substring(0, score_s.indexOf(".") + 1)
				}
				Snackbar.make(
					it,
					score_s + "分," + CoolUtils.getEncourageTextByScore(score),
					Snackbar.LENGTH_LONG
				)
					.setAction("确定"
					) { Toast.makeText(mainActivity, "喵", Toast.LENGTH_SHORT).show() }.show()
				if (score > 0) {
					changeTitle(score_s + "分," + CoolUtils.getEncourageTextByScore(score), 100)
					//旋转滑稽
					//showAnimate(author_b_item);
				}
			} else if (score == -1.0) {
				Snackbar.make((mainView)!!, "请检查输入，是不是有一栏只输入了一个小数点", Snackbar.LENGTH_LONG)
					.setAction("清除输入错误", object : View.OnClickListener {
						override fun onClick(v: View) {
							for (e: EditText? in editTexts) {
								if (e!!.text.toString().startsWith(".")) e.setText("")
								Snackbar.make((mainView)!!, "清除完成", Snackbar.LENGTH_LONG)
									.setAction("null", null).show()
							}
						}
					}).show()
			} else if (score == -2.0) {
				Snackbar.make(window.decorView, "你没有选学段!", Snackbar.LENGTH_LONG)
					.setAction("这就去选", object : View.OnClickListener {
						override fun onClick(v: View) {
							select_cet_level(mainActivity)
						}
					}).show()
			}
		}

		binding!!.calcB.setOnLongClickListener {
//				弹出Dialog选择框
//				final String[] items = {"Fluid", "Hurricane", "Windmills", "Colorful", "Waves"};
			AlertDialog.Builder(mainActivity).apply {
				setTitle("Choose your background")
				setItems(BackgroundUtil.nameArr
						) { _, which -> // which 下标从0开始
					//					var selected: Int
					if (which == BackgroundUtil.getEleIndexByName("Fluid")) {
						if (findViewById<WebView>(R.id.bg_webview).url != BackgroundUtil.getEle(which)) {
							autoRefreshThread.interrupt()
							autoRefreshThread = Thread(AutoRefresh())
							bg_header_URL = BackgroundUtil.getEle(which)
							findViewById<WebView>(R.id.bg_webview).clearCache(true)
							findViewById<WebView>(R.id.bg_webview).loadUrl(bg_header_URL)
							autoRefreshThread.start()
						}
					} else {
						if (findViewById<WebView>(R.id.bg_webview).url != BackgroundUtil.getEle(which)) {
							autoRefreshThread.interrupt()
							bg_header_URL = BackgroundUtil.getEle(which)
							findViewById<WebView>(R.id.bg_webview).clearCache(true)
							findViewById<WebView>(R.id.bg_webview).loadUrl(bg_header_URL)
						}
					}
					if (which == BackgroundUtil.getEleIndexByName("Waves") && !CoolUtils.isPad()) {
						Snackbar.make(mainView!!, "这个背景在手机上可能无法正常显示", Snackbar.LENGTH_LONG)
							.setAction("换掉", object : View.OnClickListener {
								override fun onClick(v: View) {
									var random = (Math.random() * BackgroundUtil.length * 2).toInt() / 2
									while (BackgroundUtil.withinPerformanceBlackList(random)) {
										random = (Math.random() * BackgroundUtil.length * 2).toInt() / 2
									}
									bg_header_URL = BackgroundUtil.getEle(random)
									findViewById<WebView>(R.id.bg_webview).loadUrl(bg_header_URL)
								}
							}).show()
					} else if (BackgroundUtil.withinPerformanceBlackList(which)) {
						Snackbar.make(mainView!!, "手机性能如果不够可能会造成亿点卡顿", Snackbar.LENGTH_LONG)
							.setAction("换掉", object : View.OnClickListener {
								override fun onClick(v: View) {
									var random = (Math.random() * BackgroundUtil.length * 2).toInt() / 2
									while (BackgroundUtil.withinPerformanceBlackList(random)) {
										random = (Math.random() * BackgroundUtil.length * 2).toInt() / 2
									}
									bg_header_URL = BackgroundUtil.getEle(random)
									findViewById<WebView>(R.id.bg_webview).loadUrl(bg_header_URL)
								}
							}).show()
					} else {
						Snackbar.make(mainView!!, "Done", Snackbar.LENGTH_LONG).show()
					}
				}
				show()
			}
			false
		}
		binding!!.clsB.setOnClickListener {
			cleaAll()
			changeTitle("清空完成", 100)
			Snackbar.make(it, "完成", Snackbar.LENGTH_LONG)
				.setAction("确定"
				) { Toast.makeText(mainActivity, "已经全部清空啦喵", Toast.LENGTH_SHORT).show() }.show()
			//旋转滑稽
//				hideAnimate(author_b_item);
		}
		try {
			for (e: EditText? in editTexts) {
				e!!.addTextChangedListener(object : TextWatcher {
					override fun beforeTextChanged(
						s: CharSequence,
						start: Int,
						count: Int,
						after: Int
					) {
					}

					override fun onTextChanged(
						s: CharSequence,
						start: Int,
						before: Int,
						count: Int
					) {
						// 输入的内容变化的监听
						if (!clear_clicked && !toolBarLayout!!.title.toString()
								.contains("级算分器")
						) changeTitle(
							getSharedPreferences(
								"config",
								MODE_PRIVATE
							).getString("cet_level", "") + "算分器", 100
						)
						//							toolBarLayout.setTitle(getTitle());
						//哼，哼，啊啊啊啊啊啊啊
						if ((e.text.toString().contains("114514")
									|| e.text.toString().contains("1919810")
									|| toolBarLayout!!.title.toString().contains("114514")
									|| toolBarLayout!!.title.toString().contains("1919810"))
						) {
							Toast.makeText(mainActivity, "在粪坑学习的屑", Toast.LENGTH_SHORT).show()
							toilet()
						} else if (mediaPlayer.isPlaying) {
							mediaPlayer.stop()
							author_b_item!!.setIcon(R.mipmap.sunglass_huaji)
						}
					}

					override fun afterTextChanged(s: Editable) {}
				})
			}
		} catch (e: Exception) {
		}

//		listen_A_t.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mainView = v;
//			}
//		});
	}

	override fun onResume() {
		super.onResume()
		toolBarLayout!!.title =
			getSharedPreferences("config", MODE_PRIVATE).getString("cet_level", "") + "算分器"
		//问你是几级
		val cet_level = getSharedPreferences("config", MODE_PRIVATE).getString("cet_level", "")
		if ((cet_level == "")) {
			val ll = findViewById<LinearLayout>(R.id.mainLinerLayout)
			ll.visibility = View.GONE
			val mcv = findViewById<MaterialCardView>(R.id.cet_select_btn)
			mcv.setOnClickListener(object : View.OnClickListener {
				override fun onClick(v: View) {
					select_cet_level(mainActivity)
				}
			})
			select_cet_level(this)
		} else {
			val mcv = findViewById<MaterialCardView>(R.id.cet_select_btn)
			mcv.visibility = View.GONE
			findViewById<View>(R.id.mainLinerLayout).visibility = View.VISIBLE
			toolBarLayout!!.title = cet_level + "算分器"
			if ((cet_level == "六级")) {
				(findViewById<View>(R.id.listen_section_A_tip) as TextView).text = "1-8题对几个"
				(findViewById<View>(R.id.listen_section_A) as EditText).hint = "输入0-8的整数"
				(findViewById<View>(R.id.listen_section_B_tip) as TextView).text = "9-15题对几个"
				(findViewById<View>(R.id.listen_section_B) as EditText).hint = "输入0-7的整数"
			}
		}
	}

	private fun select_cet_level(context: Context) {
		val cet_level_select_ll = LinearLayout(context)
		cet_level_select_ll.layoutParams = LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT
		)
		cet_level_select_ll.orientation = LinearLayout.VERTICAL
		val md = getBigCardFromBottom(context, "你是四级还是六级?", cet_level_select_ll)
		val list_cet: MutableList<String?> = LinkedList()
		list_cet.add("四级")
		val sp = getSharedPreferences("config", MODE_PRIVATE).edit()
		val cet4 = createBigButton(
			context,
			null,
			R.color.light_blue,
			list_cet,
			dailyBingPaper,
			R.color.white
		) {
			sp.putString("cet_level", "四级").commit()
			md.cancel()
			onResume()
			null
		}
		list_cet.clear()
		list_cet.add("六级")
		val cet6 = createBigButton(
			context,
			null,
			R.color.light_blue,
			list_cet,
			dailyBingPaper,
			R.color.white
		) {
			sp.putString("cet_level", "六级").commit()
			md.cancel()
			onResume()
			null
		}
		cet_level_select_ll.addView(cet4)
		cet_level_select_ll.addView(cet6)
		md.show()
	}

	@SuppressLint("ResourceType")
	override fun onCreateOptionsMenu(menu: Menu): Boolean {
		// Inflate the menu; this adds items to the action bar if it is present.
		menuInflater.inflate(R.menu.menu_scrolling, menu)
		//关于作者
		author_b_item = menu.findItem(R.id.author_button)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		when(item.itemId){
			R.id.author_button -> {
				/*Snackbar.make(mainView, "作者: Coolest Enoch", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Toast.makeText(this, "作者: Coolest Enoch", Toast.LENGTH_SHORT).show();
				Uri uri = Uri.parse("https://github.com/coolestenoch");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.setData(uri);
				startActivity(intent);*/
				startActivity(Intent(this, AboutActivity::class.java))}
			R.id.release_notes -> {
				showReleaseNotes(mainActivity)
			}
			R.id.gotoToolbox -> {
				startActivity(Intent(this, ToolboxActivity::class.java))
			}
		}
		return super.onOptionsItemSelected(item)
	}


	/**
	 * 摇晃滑稽相关动画代码
	 * 相关文件:
	 * res/anim下两个xml
	 * res/layout/huaji_roll.xml
	 *
	 *
	 * 调用方法:
	 * showAnimate(item按钮)
	 * hideAnimate(item按钮)
	 *
	 * @param item
	 */
	private fun showAnimate(item: MenuItem) {
		hideAnimate(item)
		//这里使用一个ImageView设置成MenuItem的ActionView，这样我们就可以使用这个ImageView显示旋转动画了
		val qrView = layoutInflater.inflate(R.layout.huaji_roll, null) as ImageView
		qrView.setImageResource(R.mipmap.sunglass_huaji)
		item.actionView = qrView
		//显示动画
		val animation = AnimationUtils.loadAnimation(this, R.anim.shake)
		animation.repeatMode = Animation.RESTART
		animation.repeatCount = Animation.INFINITE
		qrView.startAnimation(animation)
	}

	private fun hideAnimate(menuItem: MenuItem?) {
		if (menuItem != null) {
			val view = menuItem.actionView
			if (view != null) {
				view.clearAnimation()
				menuItem.actionView = null
			}
		}
	}

	fun toilet() {
		runOnUiThread(object : Runnable {
			override fun run() {
				// 更新UI的操作
				toolBarLayout!!.title = "哼,哼,啊啊啊啊啊"
			}
		})
		mediaPlayer.stop()
		mediaPlayer = MediaPlayer.create(mainActivity, R.raw.checksum)
		mediaPlayer.start()
		author_b_item!!.setIcon(R.mipmap.ysxb)
	}

	internal inner class AutoRefresh : Runnable {
		override fun run() {
			try {
				while (true) {
					Thread.sleep(5000)
					runOnUiThread(object : Runnable {
						override fun run() {
							// 更新UI的操作
							findViewById<WebView>(R.id.bg_webview).loadUrl((findViewById<WebView>(R.id.bg_webview).url)!!)
							findViewById<WebView>(R.id.bg_webview).reload()
						}
					})
				}
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
		}
	}

	fun cleaAll() {
		Thread(ClearAllThread()).start()
	}

	internal inner class ClearAllThread : Runnable {
		override fun run() {
			CoolUtils.hideSoftInput(mainActivity, window.decorView)
			for (e: EditText? in editTexts) {
				clear_clicked = true
				runOnUiThread(object : Runnable {
					override fun run() {
						// 更新UI的操作
						e!!.setText("")
					}
				})
			}
			try {
				Thread.sleep(3000)
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
			changeTitle(title.toString(), 100)
			//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					// 更新UI的操作
//					toolBarLayout.setTitle(getTitle());
//				}
//			});
			clear_clicked = false
		}
	}

	fun changeTitle(inp: String, sleepTime: Int) {
		titleStrQue.offer(inp)
		if (!changingTitle) {
			Log.e("Thread", "Start")
			Thread(changeTitleClass(sleepTime)).start()
		}
	}

	internal inner class changeTitleClass(var sleepTime: Int) : Runnable {
		override fun run() {
			changingTitle = true
			while (!titleStrQue.isEmpty()) {
				changeTitleBase(toolBarLayout!!.title.toString(), sleepTime, true)
				changeTitleBase(titleStrQue.poll(), 100, false)
			}
			changingTitle = false
		}
	}

	fun changeTitleBase(inp: String, sleepTime: Int, reverse: Boolean) {
		val str = StringBuffer()
		var i: Int
		try {
			if (!reverse) {
				i = 0
				while (i < inp.length) {
					str.append(inp[i])
					runOnUiThread(object : Runnable {
						override fun run() {
							// 更新UI的操作
							toolBarLayout!!.title = str.toString()
						}
					})
					Thread.sleep(sleepTime.toLong())
					i++
				}
			}
			if (reverse) {
				str.append(inp)
				i = inp.length
				while (i > 0) {
					str.deleteCharAt(i - 1)
					runOnUiThread(object : Runnable {
						override fun run() {
							// 更新UI的操作
							toolBarLayout!!.title = str.toString()
						}
					})
					Thread.sleep(sleepTime.toLong())
					i--
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}

	fun calculateScore(cet_level: String?): Double {
		var corrNum = 0
		var scored = 0.0
		if (cet_level != "") {
			try {
/*				for (e: EditText? in editTexts) {
					if ((e!!.text.toString().contains("114514")
								|| e.text.toString().contains("1919810")
								|| toolBarLayout!!.title.toString().contains("114514")
								|| toolBarLayout!!.title.toString().contains("1919810"))
					) {
						toilet()
						Toast.makeText(mainActivity, "这么臭的分数就没必要算了吧", Toast.LENGTH_SHORT).show()
						return (-1).toDouble()
					}
				}*/
				//听力
				if (findViewById<EditText>(R.id.listen_section_A)!!.text.toString() != "") {
					if ((cet_level == "四级")) {
						if (findViewById<EditText>(R.id.listen_section_A)!!.text.toString()
								.toDouble()
								.toInt() <= 7
						) corrNum += findViewById<EditText>(R.id.listen_section_A)!!.text.toString()
							.toDouble()
							.toInt() else corrNum += 7
					} else if ((cet_level == "六级")) {
						if (findViewById<EditText>(R.id.listen_section_A)!!.text.toString()
								.toDouble()
								.toInt() <= 8
						) corrNum += findViewById<EditText>(R.id.listen_section_A)!!.text.toString()
							.toDouble()
							.toInt() else corrNum += 8
					}
				}
				if (findViewById<EditText>(R.id.listen_section_B)!!.text.toString() != "") {
					if ((cet_level == "四级")) {
						if (findViewById<EditText>(R.id.listen_section_B)!!.text.toString()
								.toDouble()
								.toInt() <= 8
						) corrNum += findViewById<EditText>(R.id.listen_section_B)!!.text.toString()
							.toDouble()
							.toInt() else corrNum += 8
					} else if ((cet_level == "六级")) {
						if (findViewById<EditText>(R.id.listen_section_B)!!.text.toString()
								.toDouble()
								.toInt() <= 7
						) corrNum += findViewById<EditText>(R.id.listen_section_B)!!.text.toString()
							.toDouble()
							.toInt() else corrNum += 7
					}
				}
				if (findViewById<EditText>(R.id.listen_section_C)!!.text.toString() != "") {
					if (findViewById<EditText>(R.id.listen_section_C)!!.text.toString().toDouble()
							.toInt() <= 10
					) corrNum += 2 * findViewById<EditText>(R.id.listen_section_C)!!.text.toString()
						.toDouble()
						.toInt() else corrNum += 20
				}
				if (corrNum < 0) corrNum = 0
				if (corrNum > 35) corrNum = 35
				scored += ScoreSheetUtil.getLisReadStandardScoreByCorrectNum(corrNum)
				corrNum = 0

				//阅读
				if (findViewById<EditText>(R.id.reading_section_A)!!.text.toString() != "") {
					if (findViewById<EditText>(R.id.reading_section_A)!!.text.toString()
							.toDouble() <= 10
					) corrNum += Math.round(
						findViewById<EditText>(R.id.reading_section_A)!!.text.toString()
							.toDouble() / 2.0
					).toInt() else corrNum += 5
				}
				if (findViewById<EditText>(R.id.reading_section_B)!!.text.toString() != "") {
					if (findViewById<EditText>(R.id.reading_section_B)!!.text.toString().toDouble()
							.toInt() <= 10
					) corrNum += findViewById<EditText>(R.id.reading_section_B)!!.text.toString()
						.toDouble().toInt() else corrNum += 10
				}
				if (findViewById<EditText>(R.id.reading_section_C)!!.text.toString() != "") {
					val tmp =
						findViewById<EditText>(R.id.reading_section_C)!!.text.toString().toDouble()
							.toInt()
					if (tmp <= 20) corrNum = corrNum + tmp * 2 else corrNum += 20
					//			Toast.makeText(mainActivity, String.valueOf(tmp), Toast.LENGTH_SHORT).show();
				}
				if (corrNum < 0) corrNum = 0
				if (corrNum > 35) corrNum = 35
				scored += ScoreSheetUtil!!.getLisReadStandardScoreByCorrectNum(corrNum)

				//写作和翻译
				if (findViewById<EditText>(R.id.writing_inp)!!.text.toString() != "") {
					scored += ScoreSheetUtil!!.getTraWriStandardScoreByCorrectNum(
						findViewById<EditText>(R.id.writing_inp)!!.text.toString().toInt()
					)
				}
				if (findViewById<EditText>(R.id.translate_inp)!!.text.toString() != "") {
					scored += ScoreSheetUtil!!.getTraWriStandardScoreByCorrectNum(
						findViewById<EditText>(R.id.translate_inp)!!.text.toString().toInt()
					)
				}
			} catch (e: Exception) {
				e.printStackTrace()
				scored = -1.0
			}
		} else {
			scored = -2.0
		}
		return scored
	}

	//旧算法，来自知乎，计算结果和张莉莉上课PPT讲的不一样
	fun calculateScore_old(): Int {
		var score_i = 0.0
		//听力
		if (findViewById<EditText>(R.id.listen_section_A)!!.text.toString() != "") {
			score_i += 49.7 * (findViewById<EditText>(R.id.listen_section_A)!!.text.toString()
				.toInt()) / 7.0
		}
		if (findViewById<EditText>(R.id.listen_section_B)!!.text.toString() != "") {
			score_i += 56.8 * (findViewById<EditText>(R.id.listen_section_B)!!.text.toString()
				.toInt()) / 7.0
		}
		if (findViewById<EditText>(R.id.listen_section_C)!!.text.toString() != "") {
			score_i += 142.0 * (findViewById<EditText>(R.id.listen_section_C)!!.text.toString()
				.toInt()) / 7.0
		}

		//阅读
		if (findViewById<EditText>(R.id.reading_section_A)!!.text.toString() != "") {
			score_i += 35.5 * (findViewById<EditText>(R.id.reading_section_A)!!.text.toString()
				.toInt()) / 10.0
		}
		if (findViewById<EditText>(R.id.reading_section_B)!!.text.toString() != "") {
			score_i += 71.0 * (findViewById<EditText>(R.id.reading_section_B)!!.text.toString()
				.toInt()) / 10.0
		}
		if (findViewById<EditText>(R.id.reading_section_C)!!.text.toString() != "") {
			score_i += 142.0 * (findViewById<EditText>(R.id.reading_section_C)!!.text.toString()
				.toInt()) / 10.0
		}

		//写作和翻译
		if (findViewById<EditText>(R.id.writing_inp)!!.text.toString() != "") {
			score_i += 106.5 * (findViewById<EditText>(R.id.writing_inp)!!.text.toString()
				.toInt()) / 15.0
		}
		if (findViewById<EditText>(R.id.translate_inp)!!.text.toString() != "") {
			score_i += 106.5 * (findViewById<EditText>(R.id.translate_inp)!!.text.toString()
				.toInt()) / 15.0
		}
		return score_i.toInt()

		/*
		String score = "0";
		BigDecimal scoreb = new BigDecimal(Double.valueOf(0.0).toString());
		BigDecimal div = new BigDecimal(Double.valueOf(0.0).toString());
		BigDecimal tmp = null;
		if (!listen_A_t.getText().toString().equals("")) {
			div = new BigDecimal(Double.valueOf(7.0).toString());
			tmp = new BigDecimal(Double.parseDouble(listen_A_t.getText().toString()));
			tmp = tmp.divide(div);
			scoreb.add(tmp);
		}

		score = scoreb.toString();
		return Integer.parseInt(String.valueOf(Math.round(Float.parseFloat(score))));*/
	}
}