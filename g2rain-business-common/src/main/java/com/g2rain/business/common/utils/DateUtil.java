package com.g2rain.business.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static String getDateSecondLongStringFrom2010(Date date) {
		long secondLong2010 = 1262278861;
		long secondLongDate = date.getTime() / 1000;
		
		return secondLongDate - secondLong2010 + "";
	}

	public static String getDateSecondLongStringFrom2010() {
		return getDateSecondLongStringFrom2010(new Date());
	}

	public static List<String> getBetweenDates(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date start = null;
		Date end = null;
		try {
			start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
			return null;
		}

		List<String> result = new ArrayList<String>();
		Calendar temp = Calendar.getInstance();
		temp.setTime(start);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		while (temp.before(tempEnd)) {
			result.add(sdf.format(temp.getTime()));
			temp.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static void main(String[] args) {
		System.out.println(getDateSecondLongStringFrom2010());
	}
}
