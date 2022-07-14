package com.coolest.cet4.calculator.utils.java;

import java.util.ArrayList;
import java.util.List;

public class ScoreSheetUtil_java {
	List<Double> correctNumListLisRead = new ArrayList<Double>();
	List<Double> correctNumListTraWri = new ArrayList<Double>();

	public ScoreSheetUtil_java() {

		correctNumListLisRead.add(0.0);
		correctNumListLisRead.add(105.0);
		correctNumListLisRead.add(105.0);
		correctNumListLisRead.add(108.5);
		correctNumListLisRead.add(112.0);
		correctNumListLisRead.add(115.5);
		correctNumListLisRead.add(119.0);
		correctNumListLisRead.add(119.0);
		correctNumListLisRead.add(122.5);
		correctNumListLisRead.add(126.0);
		correctNumListLisRead.add(126.0);
		correctNumListLisRead.add(129.5);
		correctNumListLisRead.add(133.0);
		correctNumListLisRead.add(136.5);
		correctNumListLisRead.add(140.0);
		correctNumListLisRead.add(143.5);
		correctNumListLisRead.add(147.0);
		correctNumListLisRead.add(150.5);
		correctNumListLisRead.add(154.0);
		correctNumListLisRead.add(154.0);
		correctNumListLisRead.add(157.5);
		correctNumListLisRead.add(161.0);
		correctNumListLisRead.add(164.5);
		correctNumListLisRead.add(168.0);
		correctNumListLisRead.add(171.5);
		correctNumListLisRead.add(175.0);
		correctNumListLisRead.add(178.5);
		correctNumListLisRead.add(185.5);
		correctNumListLisRead.add(192.5);
		correctNumListLisRead.add(199.5);
		correctNumListLisRead.add(206.5);
		correctNumListLisRead.add(213.5);
		correctNumListLisRead.add(220.5);
		correctNumListLisRead.add(227.5);
		correctNumListLisRead.add(238.0);
		correctNumListLisRead.add(248.5);



		correctNumListTraWri.add(43.5);
		correctNumListTraWri.add(46.5);
		correctNumListTraWri.add(49.5);
		correctNumListTraWri.add(52.5);
		correctNumListTraWri.add(55.5);
		correctNumListTraWri.add(58.5);
		correctNumListTraWri.add(63.0);
		correctNumListTraWri.add(67.5);
		correctNumListTraWri.add(72.0);
		correctNumListTraWri.add(76.5);
		correctNumListTraWri.add(81.0);
		correctNumListTraWri.add(85.5);
		correctNumListTraWri.add(90.0);
		correctNumListTraWri.add(94.5);
		correctNumListTraWri.add(100.5);
		correctNumListTraWri.add(106.5);
	}

	public double getLisReadStandardScoreByCorrectNum(int corNum){
//		double standardScore = 0;
//		standardScore = correctNumListLisRead.get(corNum) * 10 * 0.35;
		return correctNumListLisRead.get(corNum);
	}

	public double getTraWriStandardScoreByCorrectNum(int corNum){
		return correctNumListTraWri.get(corNum);
	}

}
