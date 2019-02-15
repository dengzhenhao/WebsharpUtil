package com.websharputil.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WeekUtil {
	// 获取当前时间所在年的周数
		public static int getWeekOfYear(Date date) {
			Calendar c = new GregorianCalendar();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.setMinimalDaysInFirstWeek(7);
			c.setTime(date);

			return c.get(Calendar.WEEK_OF_YEAR);
		}

		// 获取当前时间所在年的最大周数
		public static int getMaxWeekNumOfYear(int year) {
			Calendar c = new GregorianCalendar();
			c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);

			return getWeekOfYear(c.getTime());
		}

		// 获取某年的第几周的开始日期
		public static Date getFirstDayOfWeek(int year, int week) {
			Calendar c = new GregorianCalendar();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, Calendar.JANUARY);
			c.set(Calendar.DATE, 1);

			Calendar cal = (GregorianCalendar) c.clone();
			cal.add(Calendar.DATE, week * 7);

			return getFirstDayOfWeek(cal.getTime());
		}

		// 获取某年的第几周的结束日期
		public static Date getLastDayOfWeek(int year, int week) {
			Calendar c = new GregorianCalendar();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, Calendar.JANUARY);
			c.set(Calendar.DATE, 1);

			Calendar cal = (GregorianCalendar) c.clone();
			cal.add(Calendar.DATE, week * 7);

			return getLastDayOfWeek(cal.getTime());
		}

		// 获取当前时间所在周的开始日期
		public static Date getFirstDayOfWeek(Date date) {
			Calendar c = new GregorianCalendar();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.setTime(date);
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
			return c.getTime();
		}

		// 获取当前时间所在周的结束日期
		public static Date getLastDayOfWeek(Date date) {
			Calendar c = new GregorianCalendar();
			c.setFirstDayOfWeek(Calendar.MONDAY);
			c.setTime(date);
			c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
			return c.getTime();
		}

		public static void main(String[] args) {
			int year = 2015;
			int week = 27;

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date today = new Date();
			Calendar c = new GregorianCalendar();
			c.setTime(today);

			System.out.println("当前时间:current date = " + sdf.format(today));
			System.out.println("当前第几周:getWeekOfYear = " + getWeekOfYear(today));
			System.out.println("年度总周数:getMaxWeekNumOfYear = " + getMaxWeekNumOfYear(year));
			System.out.println("本周第一天:getFirstDayOfWeek = " + sdf.format(getFirstDayOfWeek(year, week)));
			System.out.println("本周最后一天:getLastDayOfWeek = " + sdf.format(getLastDayOfWeek(year, week)));
			System.out.println("本周第一天:getFirstDayOfWeek = " + sdf.format(getFirstDayOfWeek(today)));
			System.out.println("本周最后一天:getLastDayOfWeek = " + sdf.format(getLastDayOfWeek(today)));
		}
}
