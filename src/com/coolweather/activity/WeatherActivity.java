package com.coolweather.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.util.Week;
import com.coolweather.util.HttpCallbackListener;
import com.coolweather.util.HttpUtils;
import com.coolweather.util.Utility;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	
	private TextView cityNameText;
	
	private TextView publishText;
	
	private TextView weatherDespText;
	
	private TextView tempText;
	
	private TextView curTempText;
		
	private TextView weekText;
	
	private TextView dateText;
	
	private ImageButton switchCity;
	
	private ImageButton refreshWeather;
	
	private TextView week2;

	private TextView week3;
	
	private TextView week4;
	
	private TextView week5;
	
	private TextView week6;
	
	private TextView weather2;

	private TextView weather3;
	
	private TextView weather4;
	
	private TextView weather5;
	
	private TextView weather6;
	
	private TextView temp2;

	private TextView temp3;
	
	private TextView temp4;
	
	private TextView temp5;
	
	private TextView temp6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText=(TextView) findViewById(R.id.city_name);
		publishText=(TextView) findViewById(R.id.publishTime);
		weatherDespText=(TextView) findViewById(R.id.weather_desp);
		tempText=(TextView) findViewById(R.id.temp);
		curTempText=(TextView) findViewById(R.id.current_temp);
		weekText=(TextView) findViewById(R.id.week);
		dateText=(TextView) findViewById(R.id.date);
		switchCity=(ImageButton) findViewById(R.id.switch_city);
		refreshWeather=(ImageButton) findViewById(R.id.refresh_weather);
		week2=(TextView) findViewById(R.id.week2);
		week3=(TextView) findViewById(R.id.week3);
		week4=(TextView) findViewById(R.id.week4);
		week5=(TextView) findViewById(R.id.week5);
		week6=(TextView) findViewById(R.id.week6);
		weather2=(TextView) findViewById(R.id.weather2);
		weather3=(TextView) findViewById(R.id.weather3);
		weather4=(TextView) findViewById(R.id.weather4);
		weather5=(TextView) findViewById(R.id.weather5);
		weather6=(TextView) findViewById(R.id.weather6);
		temp2=(TextView) findViewById(R.id.temp2);
		temp3=(TextView) findViewById(R.id.temp3);
		temp4=(TextView) findViewById(R.id.temp4);
		temp5=(TextView) findViewById(R.id.temp5);
		temp6=(TextView) findViewById(R.id.temp6);
		String countyCode = getIntent().getStringExtra("county_code");
		if (!TextUtils.isEmpty(countyCode)) {
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		} else {
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	private void showWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		tempText.setText(prefs.getString("temp1", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText(prefs.getString("publishTime", "")+"更新");
		curTempText.setText(prefs.getString("cur_temp", ""));
		weekText.setText(prefs.getString("week", ""));
		dateText.setText(prefs.getString("date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		List<String> list=Week.getWeekList(new Date());
		week2.setText(list.get(0));
		week3.setText(list.get(1));
		week4.setText(list.get(2));
		week5.setText(list.get(3));
		week6.setText(list.get(4));
		weather2.setText(prefs.getString("weather2", ""));
		weather3.setText(prefs.getString("weather3", ""));
		weather4.setText(prefs.getString("weather4", ""));
		weather5.setText(prefs.getString("weather5", ""));
		weather6.setText(prefs.getString("weather6", ""));
		temp2.setText(prefs.getString("temp2", ""));
		temp3.setText(prefs.getString("temp3", ""));
		temp4.setText(prefs.getString("temp4", ""));
		temp5.setText(prefs.getString("temp5", ""));
		temp6.setText(prefs.getString("temp6", ""));
	}
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	private void queryWeatherInfo(String weatherCode){
		String address="http://weatherapi.market.xiaomi.com/wtr-v2/weather?cityId="+weatherCode+".html";
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
		editor.putString("weather_code", weatherCode);
		editor.commit();
		queryFromServer(address, "weatherCode");
	}
	private void queryFromServer(final String address, final String type) {
		// TODO Auto-generated method stub
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if ("countyCode".equals(type)) {
					if (!TextUtils.isEmpty(response)) {
						String[] array = response.split("\\|");
						if (array!=null&&array.length==2) {
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.switch_city:
			SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(this).edit();
			editor.putBoolean(ChooseAreaActivity.CITY_SELECTED, false);
			editor.commit();
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中...");
			SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}

}
