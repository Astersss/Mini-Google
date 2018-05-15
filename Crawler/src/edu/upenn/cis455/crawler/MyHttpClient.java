package edu.upenn.cis455.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.HashMap;

import org.apache.log4j.Logger;


import edu.upenn.cis455.crawler.info.URLInfo;
import edu.upenn.cis455.storage.Webpage;

public class MyHttpClient {
	
	private final String TEXTHTML = "text/html";
	private final String TEXTXML = "text/xml";
	private final String APPXML = "application/xml";
	private final String XML = "+xml";
	
	public String url;
	public String hostName;
	public int portNumber;
	public long contentLength;
	public String contentType = "text/html";
	public long lastModified;
	public String language;
	public InputStream in;
	public BufferedReader br;
	static Logger log = Logger.getLogger(MyHttpsClient.class);
	
	public MyHttpClient(String url) {
		this.url = url;
		URLInfo urlInfo = new URLInfo(url);
		this.hostName = urlInfo.getHostName();
		this.portNumber = urlInfo.getPortNo();

	}
	
	public boolean sendRequest(boolean isGET){
		HttpURLConnection connection = null;
		
		if (isGET){
			System.out.println(url + ": Downloading");
		} else {
			System.out.println(url + ": Sending HEAD request");
		}
		
		try{
			URL httpURL = new URL(url);
			if(httpURL.getHost() == null) return false;
			
			connection = (HttpURLConnection)httpURL.openConnection();
			connection.setRequestProperty("User-Agent", "cis455crawler");
			connection.setRequestProperty("Connection", "close");
			if(!isGET) {
				connection.setRequestMethod("HEAD");
			} else {
				connection.setRequestMethod("GET");
			}
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			
			
			//System.out.println("The response code for " + url + " is " + connection.getResponseCode());
			contentLength = connection.getContentLength();
			contentType = connection.getContentType();
			lastModified = connection.getLastModified();

			//System.out.println("MyHttpClient: " + lastModified + " is the last modified time.");
			
			if(isGET) {
				in = connection.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));	
			}
			return true;
			
		} catch(MalformedURLException e){
			//e.printStackTrace();
			return false;
		} catch(IOException e){
			//e.printStackTrace();
			return false;
		}
	}
	
	@SuppressWarnings("unused")
	public Webpage getWebpage() throws IOException{
		sendRequest(true);
		
		HashMap<String, String> getHeaders = new HashMap<>();
		StringBuffer sb = new StringBuffer();
		
		String line = "";
		while((line = br.readLine()) != null){
			sb.append(line);
		}
		
		Webpage webpage = new Webpage(lastModified, url, contentType, sb.toString());
		return webpage;
	}
	
	public boolean isValidType(){
		return  contentType.equalsIgnoreCase(TEXTHTML) ||
				contentType.equalsIgnoreCase(TEXTXML) ||
				contentType.equalsIgnoreCase(APPXML) ||
				contentType.endsWith(XML) || 
				contentType.contains(TEXTHTML) ||
				contentType.contains(TEXTXML) ||
				contentType.contains(APPXML) ||
				contentType.contains(XML);
	}
	
	public boolean isValidLength(int maxSize){
		return maxSize * 1024 * 1024 > contentLength;
	}
	
	public static void main(String[] args) throws IOException{
		String url = "http://crawltest.cis.upenn.edu/cnn/";
		MyHttpClient client = new MyHttpClient(url);
		//client.sendRequest(true);
		
		Webpage webpage = client.getWebpage();
		System.out.println(webpage.contentLength);
		System.out.println(webpage.contentType);
		System.out.println(webpage.language);
		System.out.println(webpage.getContent());
		System.out.println(webpage.SHA256CheckSum);
	}
	
}
