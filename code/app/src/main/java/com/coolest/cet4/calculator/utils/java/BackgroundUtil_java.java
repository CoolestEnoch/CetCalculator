package com.coolest.cet4.calculator.utils.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class BackgroundUtil_java {

	Map<String, String> coolMap;
	List<String> coolList;
	Set coolKeySet;

	public BackgroundUtil_java() {
		coolMap = new TreeMap<>();
		coolMap.put("Fluid", "file:///android_asset/fluid_v2.html");
		coolMap.put("Hurricane", "file:///android_asset/hurricane.html");
		coolMap.put("Windmills", "file:///android_asset/windmills.html");
		coolMap.put("Colorful", "file:///android_asset/colorful.html");
		coolMap.put("Waves", "file:///android_asset/waves.html");
		coolMap.put("Bubbles", "file:///android_asset/bubbles.html");
		coolMap.put("Ripples", "file:///android_asset/ripples.html");
		coolMap.put("Sparks", "file:///android_asset/spark.html");
		coolMap.put("Char Flow", "file:///android_asset/Char_flow.html");
		coolMap.put("Hill and a Clock", "file:///android_asset/Hill_and_Clock.html");
		coolMap.put("A blue silk", "file:///android_asset/ribbon.html");
		coolMap.put("Triangle", "file:///android_asset/triangle_wave.html");
		coolMap.put("Harbor", "file:///android_asset/harbor.html");

	}

	private void updateData() {
		coolKeySet = coolMap.keySet();
		coolList = new ArrayList<>();
		for (Object o : coolKeySet)
			coolList.add((String) o);
	}

	public String getEle(int i) {
		updateData();
		return coolMap.get(coolList.get(i));
	}

	public int getEleIdByName(String name) {
		updateData();
		int i = 0;
		for (String s : coolList) {
			if (name.equals(s)) return i;
			i++;
		}
		return -1;
	}

	public int getLength() {
		updateData();
		return coolMap.size();
	}

	public String[] getNameArr() {
		updateData();
		int i = 0;
		String[] strArr = new String[coolList.size()];
		for (String s : coolList) {
			strArr[i] = s;
			i++;
		}
		return strArr;
	}

	String getNameById(int id) {
		String[] items = this.getNameArr();
		return items[id];
	}

	public boolean withinPerformanceBlackList(int id) {
		List<String> list = new ArrayList<>();
		list.add("Windmills");
		list.add("Waves");
		list.add("Hill and a Clock");

		return list.contains(getNameById(id));
	}

}
