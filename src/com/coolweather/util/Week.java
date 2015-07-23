package com.coolweather.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Week {
	 public static List<String> getWeekList(Date dt) {
	        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(dt);
	        List<String> list=new ArrayList<String>();
	        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	        if (w < 0)
	            w = 0;
	        for (int i = 0; i < 5; i++) {
				list.add(weekDays[w++%7]);
			}
	        return list;
	    }
}
