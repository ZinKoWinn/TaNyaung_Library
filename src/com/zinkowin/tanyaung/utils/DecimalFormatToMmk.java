package com.zinkowin.tanyaung.utils;

import java.text.DecimalFormat;

import javafx.util.StringConverter;

public class DecimalFormatToMmk extends StringConverter<Number>{
	
	private static final DecimalFormat format = new DecimalFormat("#,##0");

	@Override
	public String toString(Number num) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Number fromString(String str) {
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
