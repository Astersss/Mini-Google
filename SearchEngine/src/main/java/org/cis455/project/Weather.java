package org.cis455.project;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.*;

public class Weather {
	
	public static InputStream in;
	public static BufferedReader br;
	public static JSONObject obj;
	public static String F;
	public static String C;
	public static String weather;
	public static String UV;
	public static String langitude;
	public static String longitude;
	public static String elevation;
	
	public static ArrayList<String> getWeather(String city, String state) {
		ArrayList<String> arrayList = new ArrayList<String>();
		String urlString = "http://api.wunderground.com/api/4212fdedc86ec0b5/conditions/q/" + state + "/" + city + ".json";	
		HttpURLConnection connection = null;
		
		try{
			URL httpURL = new URL(urlString);
			
			connection = (HttpURLConnection)httpURL.openConnection();
			
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));	
			
			StringBuffer sb = new StringBuffer();
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			
			obj = new JSONObject(sb.toString());
			F = "" + obj.getJSONObject("current_observation").get("temp_f");
			C = "" + obj.getJSONObject("current_observation").get("temp_c");
			weather = (String) obj.getJSONObject("current_observation").get("weather");
			UV = (String) obj.getJSONObject("current_observation").get("UV");
			langitude = (String) obj.getJSONObject("current_observation").getJSONObject("observation_location").get("latitude");
			longitude = (String) obj.getJSONObject("current_observation").getJSONObject("observation_location").get("longitude");
			elevation = (String) obj.getJSONObject("current_observation").getJSONObject("observation_location").get("elevation");
			
			arrayList.add(F); arrayList.add(C); arrayList.add(weather);
			arrayList.add(UV); arrayList.add(langitude); arrayList.add(longitude);
			arrayList.add(elevation);
		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return arrayList;
	}
	
	public static void main(String[] args){
		ArrayList<String> arrayList = getWeather("Pittsburg", "PA");
		for (String string : arrayList){
			System.out.println(string);
		}
	}
}
