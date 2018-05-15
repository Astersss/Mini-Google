package yelp.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import yelp.api.JSONParse;
import org.json.simple.JSONObject;

public class FileRead {
	
	// read in search history of a specific user.
	public static HashMap<String, ArrayList<JSONObject>> readSearchHistory(String username) throws FileNotFoundException {
		HashMap<String, ArrayList<JSONObject>> res = new HashMap<>();
			try {
			      File file = new File("userinfo/" + username + ".txt");
			      
			      // if this user does not have a history yet, create a new txt for it.
			      if (file.createNewFile()){
			      
			      }else{
			    	  
			      }
			      
			      BufferedReader br1 = new BufferedReader(new FileReader(file));
			      String key, value;
			      int len;
			      
			      // read in user's search history
			      while((key = br1.readLine()) != null && (value = br1.readLine()) != null){
			    	  ArrayList<JSONObject> jsonArray = new ArrayList<>();
			    	  len = Integer.parseInt(value);
			    	  while (len > 0){
			    		  value = br1.readLine();
				    	  jsonArray.add(JSONParse.getJSON(value));
			    		  len--;
			    	  }
			    	  res.put(key, jsonArray);
			      }

				  br1.close();
			      
		    	} catch (IOException e) {
			      e.printStackTrace();
			}
			
		return res;
	}
	
	
	// read in confidential info. this should be done once user happens this applet
	public static HashMap<String, String> readConfidential(String fileName) {
		BufferedReader br;
		HashMap<String, String> confidential = new HashMap<>();
		try {
			File file = new File(fileName);
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while ((line = br.readLine()) != null){
				String[] info = line.split(" ");
				confidential.put(info[0], info[1]);
				
			}
			br.close();
		} catch (Exception e) {	
			System.out.println("cannot read confidential");
		}
		return confidential;
	}

}
