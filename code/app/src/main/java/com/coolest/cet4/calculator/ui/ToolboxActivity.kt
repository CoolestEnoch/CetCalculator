package com.coolest.cet4.calculator.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import com.coolest.cet4.calculator.R
import com.coolest.cet4.calculator.databinding.ActivityToolboxBinding
import com.coolest.cet4.calculator.utils.ViewUtils_Kotlin
import com.coolest.cet4.calculator.utils.bullshitEntry
import com.google.android.material.snackbar.Snackbar

class ToolboxActivity : AppCompatActivity() {
	private lateinit var binding: ActivityToolboxBinding


	//获取剪贴板管理器：
	lateinit var clipBoardManager: ClipboardManager
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityToolboxBinding.inflate(layoutInflater)
		setContentView(binding.root)

		supportActionBar?.let {
			it.setHomeButtonEnabled(true)
			it.setDisplayHomeAsUpEnabled(true)
		}
		this.title = "大学生实用工具箱"

		ViewUtils_Kotlin.getBigCardFromBottom(this, "这只是一个初期版本", ViewUtils_Kotlin.createBigButton(
			this, R.drawable.ic_baseline_grass_24, R.color.light_blue,
			listOf("初代版本", "这只是一个初期版本，可能会有bug"), null, R.color.white
		) {}).show()


		//相关系统服务
		clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

		binding.bullshitBtn.apply {
			setOnClickListener {
				val mainLl = LinearLayout(this@ToolboxActivity).apply {
					layoutParams = LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
					orientation = LinearLayout.VERTICAL
				}
				val infoCheckLl = LinearLayout(this@ToolboxActivity).apply {
					layoutParams = LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT
					)
					orientation = LinearLayout.HORIZONTAL
				}
				val topicText = EditText(this@ToolboxActivity).apply {
					layoutParams =
						LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f)
					hint = "文章主题"
					isSingleLine = true
				}
				val numText = EditText(this@ToolboxActivity).apply {
					layoutParams =
						LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
					hint = "长度"
					inputType = InputType.TYPE_CLASS_NUMBER
					isSingleLine = true
				}
				val outputText = EditText(this@ToolboxActivity).apply {
					layoutParams = ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT
					)
				}
				val confirmBtn = ViewUtils_Kotlin.createBigButton(
					this@ToolboxActivity, null, R.color.teal_200,
					listOf("生成"), null, R.color.black_text
				) {
					try {
						val topic = topicText.text.toString()
						val length = numText.text.toString().toInt()
						outputText.setText(genBullShitText(topic, length))
					} catch (e: Exception) {
						Snackbar.make(window.decorView, "输入有误", Snackbar.LENGTH_LONG).show()
					}
				}
				val copyBtn = ViewUtils_Kotlin.createBigButton(
					this@ToolboxActivity, null, R.color.teal_200,
					listOf("复制全文"), null, R.color.black_text
				) {
					copyText(outputText.text.toString())
				}
				infoCheckLl.apply {
					addView(topicText)
					addView(numText)
				}
				mainLl.apply {
					addView(infoCheckLl)
					addView(confirmBtn)
					addView(copyBtn)
					addView(outputText)
				}
				binding.toolboxActivityView.addView(mainLl)

				binding.toolboxActivityView.removeView(this)
			}
		}
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			android.R.id.home -> finish()
		}
		return super.onOptionsItemSelected(item)
	}

	fun genBullShitText(topic: String, length: Int): String = bullshitEntry(topic, length)

	private fun copyText(text: String) {
		// 创建普通字符型ClipData
		val mClipData = ClipData.newPlainText("Label", text)
		// 将ClipData内容放到系统剪贴板里。
		clipBoardManager.setPrimaryClip(mClipData)
		Snackbar.make(window.decorView, "已复制到剪贴板!", Snackbar.LENGTH_LONG).show()
	}
}