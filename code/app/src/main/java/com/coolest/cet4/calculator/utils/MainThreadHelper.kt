package com.coolest.cet4.calculator.utils

import android.os.Handler
import android.os.Looper


val mainHandler: Handler by lazy {
	Handler(Looper.getMainLooper())
}

fun runOnMainThread(r: Runnable) {
	if (Looper.myLooper() == Looper.getMainLooper()) {
		r.run()
	} else {
		mainHandler.post(r)
	}
}