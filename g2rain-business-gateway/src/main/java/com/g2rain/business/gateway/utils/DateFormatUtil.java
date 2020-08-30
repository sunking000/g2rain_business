package com.g2rain.business.gateway.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static String format(Date date) {
		if (date == null)
			return null;
		return dateFormat.format(date);
	}

	public static String formatToDate(Date date) {
		if (date == null)
			return null;
		String dateString = dateFormat.format(date);
		return dateString.substring(0, dateString.indexOf(" "));
	}

	public static String format(long date) {
		if (date == 0L)
			return null;
		return dateFormat.format(new Date(date));
	}
}

