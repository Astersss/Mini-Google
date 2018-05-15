package merriam_webster.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SearchCollegiateWords {
	public static InputStream in;
	public static BufferedReader br;
	
	public static String Search(String word) {
		String urlString = "http://www.dictionaryapi.com/api/v1/references/collegiate/xml/" + word + "?key=1d78e8b9-be8e-4c76-a2df-11b3a57005b0";	
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
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Search(args[0]);
	}
}
