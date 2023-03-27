package com.coolest.cet4.calculator.utils

import java.util.*

object BackgroundUtil {
	private var coolMap = mapOf(
		"Fluid" to "file:///android_asset/fluid_v2.html",
		"Hurricane" to "file:///android_asset/hurricane.html",
		"Windmills" to "file:///android_asset/windmills.html",
		"Colorful" to "file:///android_asset/colorful.html",
		"Waves" to "file:///android_asset/waves.html",
		"Bubbles" to "file:///android_asset/bubbles.html",
		"Ripples" to "file:///android_asset/ripples.html",
		"Sparks" to "file:///android_asset/spark.html",
		"Char Flow" to "file:///android_asset/Char_flow.html",
		"Hill and a Clock" to "file:///android_asset/Hill_and_Clock.html",
		"A blue silk" to "file:///android_asset/ribbon.html",
		"Triangle" to "file:///android_asset/triangle_wave.html",
		"Harbor" to "file:///android_asset/harbor.html"
	)

	fun getEle(i: Int): String {
		var cnt = 0
		var rtn = ""
		for ((k, v) in coolMap) {
			if (cnt == i) {
				rtn = v
				break
			}
			cnt++
		}
		return rtn
	}

	fun getEleIndexByName(key: String): Int {
		var i = 0
		for ((k, v) in coolMap) {
			if (k == key) {
				return i
			}
			i++
		}
		return -1
	}

	val length = coolMap.size

	val nameArr = let {
		val list = LinkedList<String>()
		for ((k, v) in coolMap){
			list.add(k)
		}
		list.toTypedArray()
	}

	fun getNameById(id: Int): String {
		return nameArr[id]
	}

	fun withinPerformanceBlackList(id: Int): Boolean {
		val list: MutableList<String?> = ArrayList()
		list.add("Windmills")
		list.add("Waves")
		list.add("Hill and a Clock")
		return list.contains(getNameById(id))
	}
}