package com.coolweather.util;

import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.db.CoolWeatherDB;
import com.coolweather.model.City;
import com.coolweather.model.County;
import com.coolweather.model.Province;

public class Utility {
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB,String response){
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces=response.split(",");
			if (allProvinces!=null&&allProvinces.length>0) {
				for (String p : allProvinces) {
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	public  static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities!=null&&allCities.length>0) {
				for (String c : allCities) {
					String[] array=c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	public static boolean handleCountiesResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties=response.split(",");
			if (allCounties!=null&&allCounties.length>0) {
				for (String c : allCounties) {
					String[] array=c.split("\\|");
					County county = new County();
					county.setCountCode(array[0]);
					county.setCountName(array[1]);
					county.setCityId(cityId);
					coolWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	public static void handleWeatherResponse(Context context,String response){
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("forecast");
			String cityName = weatherInfo.getString("city");
			String temp1 = weatherInfo.getString("temp1");
			String weatherDesp = weatherInfo.getString("weather1");
			String date_y = weatherInfo.getString("date_y");
			String week = weatherInfo.getString("week");
			JSONObject aqi = jsonObject.getJSONObject("aqi");
			String pubTime=aqi.getString("pub_time");
			pubTime=pubTime.substring(pubTime.indexOf(" ")+1,pubTime.length());
			JSONObject realTime = jsonObject.getJSONObject("realtime");
			String curTemp = realTime.getString("temp");
			curTemp=curTemp+"℃";
			String weather2=weatherInfo.getString("weather2");
			String weather3=weatherInfo.getString("weather3");
			String weather4=weatherInfo.getString("weather4");
			String weather5=weatherInfo.getString("weather5");
			String weather6=weatherInfo.getString("weather6");
			String temp2=weatherInfo.getString("temp2");
			String temp3=weatherInfo.getString("temp3");
			String temp4=weatherInfo.getString("temp4");
			String temp5=weatherInfo.getString("temp5");
			String temp6=weatherInfo.getString("temp6");
			saveWeatherInfo(context,cityName,temp1,weatherDesp,week,date_y,pubTime,curTemp,weather2,weather3,weather4,weather5,weather6,temp2,temp3,temp4,temp5,temp6);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void saveWeatherInfo(Context context, String cityName,
			 String temp1, String weatherDesp,String week,
			String date,String publishTime,String curTemp,
			String weather2,String weather3,String weather4,String weather5,String weather6,
			String temp2,String temp3,String temp4,String temp5,String temp6) {
		// TODO Auto-generated method stub
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("temp1", temp1);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("week", week);
		editor.putString("date", date);
		editor.putString("publishTime", publishTime);
		editor.putString("cur_temp", curTemp);
		editor.putString("temp2", temp2);
		editor.putString("temp3", temp3);
		editor.putString("temp4", temp4);
		editor.putString("temp5", temp5);
		editor.putString("temp6", temp6);
		editor.putString("weather2", weather2);
		editor.putString("weather3", weather3);
		editor.putString("weather4", weather4);
		editor.putString("weather5", weather5);
		editor.putString("weather6", weather6);
		editor.commit();
	}
}
