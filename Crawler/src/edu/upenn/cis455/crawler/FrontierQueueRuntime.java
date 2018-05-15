package edu.upenn.cis455.crawler;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import edu.upenn.cis455.storage.DBWrapper;
import edu.upenn.cis455.storage.VisitedURL;

public class FrontierQueueRuntime {
	public Queue<String> queue1 = null;
	public Queue<String> queue2 = null;
	public int maxSize = 400000;
	public volatile static int URLExecuted = 0;
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(FrontierQueueRuntime.class);
	private DBWrapper db = DBWrapper.getInstance(XPathCrawler.root);
	
	public FrontierQueueRuntime(){
		queue1 = new LinkedBlockingQueue<String>();
		queue2 = new LinkedBlockingQueue<String>();
	}
	
	public FrontierQueueRuntime(int size){
		this();
		maxSize = size;
	}
	
	public void getFrontierQueueFromDisk(){
		if (queue1.isEmpty()){
			db.pushIntoFrontierQueueDisk((LinkedBlockingQueue<String>)queue2);
			db.pollFromFrontierQueueDisk(1000, queue1);
		}
	}
	
	public void pushFrontierQueueIntoDisk(){
		db.pushIntoFrontierQueueDisk((LinkedBlockingQueue<String>)queue2);
	}
	
	public void offer(String url){
		queue2.offer(url);
		if (queue2.size() > 5000){
			pushFrontierQueueIntoDisk();
		}
	}
	
	public boolean isEmpty(){
		if (queue1.isEmpty()){
			if (db.isFrontierQueueDiskEmpty()){
				db.pushIntoFrontierQueueDisk(queue2);
			}
			db.pollFromFrontierQueueDisk(1000, queue1);
		}
		return queue1.isEmpty();
	}
	
	public String poll(){
		getFrontierQueueFromDisk();
		return queue1.poll();
	}
	
	public boolean canDownload(MyHttpClient client){
		String url = client.url;
		
		if (client.isValidLength(maxSize) && client.isValidType()){
			
			if (db.visitedURLContains(url)){
				long lastVisitTime = db.getLastVisited(url);
				long lastModified = client.lastModified;
				if (lastModified > lastVisitTime){
					db.updateLastVisited(url, lastModified);
					return true;
				} else {
					System.out.println(url + ": Not Modified");
					return false;
				}
			} else {
				VisitedURL v = new VisitedURL(url, client.lastModified);
				v.setLastVisited(client.lastModified);
				db.putVisitedURL(v);
				return true;
			}
		} else {
			return false;
		}
	}
	
	public boolean dbContains(MyHttpClient client){
		String url = client.url;

		long lastVisitTime = db.getLastVisited(url);
		long lastModified = client.lastModified;
	
		if (db.visitedURLContains(url) && lastModified < lastVisitTime){
			return true;
		} else 
			return false;
	}
	
	
	public boolean canDownload(MyHttpsClient client){
		String url = client.url;
		if (client.isValidLength(maxSize) && client.isValidType()){
			
			if (db.visitedURLContains(url)){
				long lastVisitTime = db.getLastVisited(url);
				long lastModified = client.lastModified;
				if (lastModified > lastVisitTime){
					db.updateLastVisited(url, lastModified);
					return true;
				} else {
					System.out.println(url + ": Not Modified");
					return false;
				}
			} else {
				VisitedURL v = new VisitedURL(url, client.lastModified);
				v.setLastVisited(client.lastModified);
				db.putVisitedURL(v);
				return true;
			}	
		} else {		
			return false;
		}
	}
	
	public boolean dbContains(MyHttpsClient client){
		String url = client.url;

		long lastVisitTime = db.getLastVisited(url);
		long lastModified = client.lastModified;
	
		if (db.visitedURLContains(url) && lastModified < lastVisitTime){
			return true;
		} else 
			return false;
	}
	
	public boolean isVisitedURL(String url){
		return db.visitedURLContains(url);
	}
	
	
	public int size(){
		return queue1.size();
	}
	
}
