package com.zinkowin.tanyaung.factory;

import java.text.DecimalFormat;

public class FormatToMmk {
	
	private static final DecimalFormat formator = new DecimalFormat("#,##0");
	
	public static boolean isEmpty(String str) {
		return null == str || isEmpty(str);
	}

	public static String formatToMmk(int data) {
		return String.format("%s MMK", formator.format(data));
	}
}
