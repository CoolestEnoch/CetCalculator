package com.coolest.cet4.calculator

import android.app.Application
import android.graphics.Bitmap

class MyApplication: Application() {

	companion object{
		@JvmStatic
		var dailyBingPaper:Bitmap? = null
	}

	override fun onCreate() {
		super.onCreate()
	}
}