package edu.upenn.cis455.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.upenn.cis455.crawler.info.URLInfo;
import edu.upenn.cis455.storage.DBWrapper;

public class RobotCache {
	private static DBWrapper db = DBWrapper.getInstance(XPathCrawler.root);
	static Logger log = Logger.getLogger(RobotCache.class);
	
	public static void addRobotsTxt(String url){
		URLInfo urlInfo = new URLInfo(url);
		String hostName = urlInfo.getHostName();
		if (hostName == null) return;
		if (!db.containsRobots(url)){
			db.addRobotMap(hostName, url);
		}
	}
	
	public static boolean isValid(String url){
		if (url == null) return false; 
		URL urlInfo = null;
		try {
			urlInfo = new URL(url);
		} catch (MalformedURLException e) {
			System.out.println("The MalformedURL is " + url);
			return false;
		}
		String hostName = urlInfo.getHost();
		if (hostName == null) {
			System.out.println("This url's hostName is null: " + url);
			return false;
		}
		
		return db.isValid(hostName, url);
	}
	
	public static boolean checkDelay(String url){
		URLInfo urlinfo = new URLInfo(url);
		String hostName = urlinfo.getHostName();
		
		if (db.containsRobots(hostName)){
			long delay = db.getDelay(hostName) * 1000;
			//System.out.println("delay is " + delay);
			long now = new Date().getTime();
			//System.out.println("now is " + now);
			long lastVisitTime = db.getRobotLastVisitTime(hostName);
			//System.out.println("last visit time is " + lastVisitTime);
			
			if ((lastVisitTime + delay) <= now) {
				db.setRobotLastVisitTime(hostName);
				return true;
			} else {
				System.out.println(url + ": Interval is too short");
				return false;
			}
		} else {
			addRobotsTxt(url);
			return true;
		}	
	}
	
	public static void updateHostLastAccess(String url){
		URLInfo urlInfo = new URLInfo(url);
		String hostName = urlInfo.getHostName();
		
		if (hostName == null){
			return;
		}
		
		if (!db.containsRobots(url)){
			addRobotsTxt(url);
		}
		db.setRobotLastVisitTime(hostName);
		
	}

}
