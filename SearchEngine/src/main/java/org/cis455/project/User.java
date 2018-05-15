package org.cis455.project;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.simple.JSONObject;


public class User {
	static int id = 0;
	private int userId;
	private String username;
	@SuppressWarnings("unused")
	private String psd;
	private HashMap<String, ArrayList<JSONObject>> searchHistory;
	
	public User(String user, String psd) {
		username = user;
		this.psd = psd;
		this.userId = id++;
	
		try {
			this.searchHistory = FileRead.readSearchHistory(username);
			
		} catch (Exception e) {
			this.searchHistory = new HashMap<String, ArrayList<JSONObject>>();
		}
		
	}
	
	public void setUsername(String name) {
		this.username = name;
	}
	
	public void setPsd(String psd) {
		this.psd = psd;
	}
	
	public int getId() {
		return this.userId;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.getPassword();
	}
	
	
	// once user has searched sth, write the results to his search history txt file.
	public void writeSearchHistory(String key, ArrayList<JSONObject> value) {
		searchHistory.put(key, value);
		try {
			File file = new File("userinfo/" + username + ".txt");
			FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true); // true
																					// --->
																					// append
																					// data
			BufferedWriter bw = new BufferedWriter(fileWriter);
			
			bw.write(key);
			bw.newLine();
			bw.write(""+value.size());
			bw.newLine();
			for (JSONObject jsonObject : value){
				bw.write(jsonObject.toJSONString());
				bw.newLine();
			}
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	// return the user's search history as a hash map
	public HashMap<String, ArrayList<JSONObject>> getSearchHistory(){
		if (searchHistory == null || searchHistory.isEmpty()){
			return null;
		} else {
			return searchHistory;
		}
	}
	
	// get search history according to the key
	public ArrayList<JSONObject> getSearchHistory(String key){
		if (searchHistory.containsKey(key)){
			return searchHistory.get(key);
		} else return null;
	}
}
