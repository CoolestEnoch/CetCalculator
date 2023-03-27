package com.coolest.cet4.calculator.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.coolest.cet4.calculator.MyApplication
import com.coolest.cet4.calculator.R
import com.coolest.cet4.calculator.databinding.ActivityAboutBinding
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin.createBigButton
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin.getBigCardFromBottom
import com.coolest.cet4.calculator.utils.checkUpdate
import com.coolest.cet4.calculator.utils.runOnMainThread
import com.coolest.cet4.calculator.utils.showAllRelease
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.glide.transformations.BlurTransformation
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread

class AboutActivity : AppCompatActivity() {
	private lateinit var binding: ActivityAboutBinding

	private val githubName = "CoolestEnoch"
	private val githubRepo = "CetCalculator"

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityAboutBinding.inflate(layoutInflater)
		setContentView(binding.root)

		//显示返回按钮
		supportActionBar?.let {
			it.setHomeButtonEnabled(true)
			it.setDisplayHomeAsUpEnabled(true)
		}

		binding.cardAuthor.setOnClickListener {
			Snackbar.make(window.decorView, "作者: Coolest Enoch", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
			try {
				Thread.sleep(100)
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
			Toast.makeText(this, "作者: Coolest Enoch", Toast.LENGTH_SHORT).show()
			startActivity(Intent().apply {
				action = "android.intent.action.VIEW"
				data = Uri.parse("https://github.com/coolestenoch")
			})
		}

		//作者列表
		val map1 = mapOf("coolestenoch" to "Author")
		processDeveloperInfo(map1, binding.authorListView, null, binding.authorLayout, this)

		//贡献者列表
//		val list2 = mapOf("IceBear733" to "Idea provider")
		val map2 = null
		processDeveloperInfo(
			map2,
			binding.contributorListView,
			binding.contributorListTip,
			binding.contributorLayout,
			this
		)

		//下拉刷新检查更新
		binding.swipeRefresh.apply {
			setColorSchemeColors(
				resources.getColor(R.color.green),
				resources.getColor(R.color.blue),
				resources.getColor(R.color.yellow),
				resources.getColor(R.color.purple),
				resources.getColor(R.color.orange),
				resources.getColor(R.color.red),
				resources.getColor(R.color.cyan)
			)
			setOnRefreshListener {
				checkUpdate(
					packageManager.getPackageInfo(packageName, 0).versionName,
					binding.swipeRefresh,
					githubName,
					githubRepo
				)
			}
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu_about, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> finish()
			R.id.action_update -> {
				checkUpdate(
					packageManager.getPackageInfo(packageName, 0).versionName,
					binding.swipeRefresh,
					githubName,
					githubRepo
				)
			}
			R.id.release_history -> {
				showAllRelease(
					binding.swipeRefresh, "所有版本", githubName, githubRepo
				)
			}
			R.id.cet_level_select -> {
				val cet_level_select_ll = LinearLayout(this).apply {
					layoutParams =
						LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT
						)
					orientation = LinearLayout.VERTICAL
				}
				val md = getBigCardFromBottom(this, "你是四级还是六级?", cet_level_select_ll)
				val list_cet: MutableList<String?> = LinkedList()
				list_cet.add("四级")
				val sp = getSharedPreferences("config", MODE_PRIVATE).edit()
				val cet4 =
					createBigButton(
						this,
						null,
						R.color.item_card_bg,
						list_cet,
						MyApplication.dailyBingPaper,
						R.color.white
					) {
						sp.putString("cet_level", "四级").commit()
						md.cancel()
						null
					}

				list_cet.clear()
				list_cet.add("六级")
				val cet6 =
					createBigButton(
						this,
						null,
						R.color.item_card_bg,
						list_cet,
						MyApplication.dailyBingPaper,
						R.color.white
					) {
						sp.putString("cet_level", "六级").commit()
						md.cancel()
						null
					}

				cet_level_select_ll.addView(cet4)
				cet_level_select_ll.addView(cet6)
				md.show()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	fun processDeveloperInfo(
		githubUserNameMap: Map<String, String>?,
		developersView: ViewGroup,
		tipView: TextView?,
		cardViewMainLayout: LinearLayout,
		context: Context
	) {
		thread {
			try {
				val tempViewList = LinkedList<MaterialCardView?>()
				if (githubUserNameMap != null) {
					for ((username, description) in githubUserNameMap) {
						try {
							//调用GitHub API获取json
							var githubResponseJson: String? = ""
							var avatar_url: String? = ""
							var nickName: String? = ""
							var bio: String? = ""
							val client = OkHttpClient()
							val request = Request.Builder()
								.url("https://api.github.com/users/$username")
								.build()
							val response = client.newCall(request).execute()
							githubResponseJson = response.body?.string()
							Log.e("github", "$githubResponseJson")

							//解析json获取图片地址
							val responseJson = JSONObject(githubResponseJson)
							avatar_url = "${responseJson.get("avatar_url")}"
							nickName = "${responseJson.get("login")}"
							bio = "${responseJson.get("bio")}"

							//添加卡片
							val textList = mutableListOf(nickName, bio)
							if (!githubUserNameMap[username].equals("")) {
								textList.add("")
								textList.add(description)
							}
							runOnUiThread {
								tempViewList.add(
									ViewUtils_Kotlin.createBigButton(
										context,
										avatar_url,
										R.color.normal_card_bg,
										textList,
										MyApplication.dailyBingPaper,
										R.color.white
									) {
										startActivity(Intent().apply {
											action = "android.intent.action.VIEW"
											data = Uri.parse("https://github.com/$username")
										})
									}
								)
							}
							runOnUiThread {
								developersView.removeAllViews()
								for (view in tempViewList) {
									developersView.addView(view)
								}
							}
						} catch (e: Exception) {
							if (e.toString()
									.contains("Unable to resolve host \"api.github.com\"")
							) {
								Snackbar.make(
									window.decorView,
									"无法连接到GitHub服务器, 请检查网络",
									Snackbar.LENGTH_LONG
								).show()
							} else {
								e.printStackTrace()
							}
						}
					}
				} else {
					runOnMainThread {
						tipView?.text = "暂无"
						runOnMainThread {
							try {
								Glide.with(context)
									.asBitmap()
									.load(MyApplication.dailyBingPaper)
									.transform(BlurTransformation(200))
									.into(object : SimpleTarget<Bitmap?>() {
										override fun onResourceReady(
											resource: Bitmap,
											transition: Transition<in Bitmap?>?
										) {
											val drawable: Drawable = BitmapDrawable(resource)
											cardViewMainLayout.background = drawable
										}
									})
								val lp = cardViewMainLayout.layoutParams
								lp.height = ViewUtils_Kotlin.dp2px(context, 111)
								cardViewMainLayout.layoutParams = lp
							} catch (e: Exception) {
								e.printStackTrace()
							}
						}
					}
				}
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}


}