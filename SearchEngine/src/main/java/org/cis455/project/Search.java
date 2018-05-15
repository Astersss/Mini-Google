package org.cis455.project;

import java.util.ArrayList;
import java.util.HashMap;

import javax.print.DocFlavor.STRING;

import org.json.simple.JSONObject;





public class Search {
	
	private static ArrayList<HashMap<String, String>> value;
	
	public static ArrayList<HashMap<String, String>> search(String item, String location){

		ArrayList<String> reStrings = YelpAPI.search(item, location, 5);
		value = new ArrayList<HashMap<String, String>>();

		for (String string : reStrings) {
			JSONObject jsonObject = JSONParse.getJSON(string);
			
			HashMap<String, String> map = new HashMap<String, String>();

			map.put("name", (String)jsonObject.get("name"));
			map.put("rating", "" + jsonObject.get("rating"));
			map.put("review_count", "" + jsonObject.get("review_count"));
			map.put("display_phone",(String) jsonObject.get("display_phone"));
			map.put("id", (String)jsonObject.get("id"));
			//map.put("categories", (String)jsonObject.get("categories"));
			//map.put("location", (String)jsonObject.get("location"));
			value.add(map);
		}
		return value;
	}
	
}
