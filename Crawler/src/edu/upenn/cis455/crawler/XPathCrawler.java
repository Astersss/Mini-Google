package edu.upenn.cis455.crawler;

import java.io.IOException;
import java.net.UnknownHostException;

import edu.upenn.cis455.storage.DBWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * This class is created from starting my crawler.
 * 
 */


public class XPathCrawler {
	
	public String urlString;
	public static String root;
	public int maxSize;
	public int maxNum = 1000;
	public static int visited = 0;
	public static FrontierQueueRuntime frontierQueueRuntime = null;

	public DBWrapper dbWrapper;
	public String seedUrl = "";
	
	
	public final String URLSPOUT = "URLSPOUT";
	public final String CRAWLERBOLT = "CRAWLERBOLT";
	public final String DOWNLOADBOLT = "DOWNLOADBOLT";
	public final String EXTRACTBOLT = "EXTRACTBOLT";
	public final String RECORDBOLT = "RECORDBOLT";
	
	
	
	/**
	 * @param url
	 * @param root
	 * @param maxSize
	 */
	public XPathCrawler(String url, String root, String maxSize){
		this.urlString = url;
		XPathCrawler.root = root;
		this.maxSize = Integer.parseInt(maxSize);
		frontierQueueRuntime = new FrontierQueueRuntime(this.maxSize);
		
		//frontierQueueRuntime.offer("https://www.apple.com/");
		frontierQueueRuntime.offer("https://www.w3schools.com/");
		//frontierQueueRuntime.offer("https://www.usnews.com/news/the-run/articles/2017-05-08/after-hillary-clinton-a-female-president-may-not-be-democrats-2020-priority");
		//frontierQueueRuntime.offer("https://www.gop.com/");
		
		this.dbWrapper = DBWrapper.getInstance(root);
	}
	
	
	/**
	 * @param url
	 * @param root
	 * @param maxSize
	 * @param maxNum
	 */
	public XPathCrawler(String url, String root, String maxSize, String maxNum){
		this(url, root, maxSize);
		this.maxNum = Integer.parseInt(maxNum);;
	}
	
	/*
	 * 
	 * Crawl method is responsible for creating topology and cluster objects.
	 * In the meantime, it creates spout and bolts and starts executing the job.
	 * 
	 */
	public void crawl() throws InterruptedException{
		Config config = new Config();
        TopologyBuilder builder = new TopologyBuilder();
        
        
        URLSpout urlSpout = new URLSpout();
        CrawlBolt crawlBolt = new CrawlBolt();
        DownloadBolt downloadBolt = new DownloadBolt();
        ExtractedBolt extractedBolt = new ExtractedBolt();
        RecordBolt recordBolt = new RecordBolt();
        
        builder.setSpout(URLSPOUT, urlSpout, 2);
        builder.setBolt(CRAWLERBOLT, crawlBolt, 8).fieldsGrouping(URLSPOUT, new Fields("URL"));
        builder.setBolt(DOWNLOADBOLT, downloadBolt, 16).shuffleGrouping(CRAWLERBOLT);
        builder.setBolt(EXTRACTBOLT, extractedBolt, 24).shuffleGrouping(DOWNLOADBOLT);
        builder.setBolt(RECORDBOLT, recordBolt, 48).shuffleGrouping(EXTRACTBOLT);
        
        LocalCluster cluster = new LocalCluster();
        Topology topo = builder.createTopology();

        ObjectMapper mapper = new ObjectMapper();
		try {
			String str = mapper.writeValueAsString(topo);
			
			System.out.println("The StormLite topology is:\n" + str);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		cluster.submitTopology("Crawler", config, builder.createTopology());
		
		
		Thread.sleep(900000000);
		
		cluster.killTopology("Crawler");
		cluster.shutdown();
		
		
	}
	
	
    public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException{
    	
    	if (args.length != 3 && args.length != 4){
    		System.out.println("      NAME: KEJIN FAN\nSEAS LOGIN: fankejin");
    	} else if (args.length == 3){
			
    		XPathCrawler xCrawler = new XPathCrawler(args[0], args[1], args[2]);
    		xCrawler.crawl();
    		xCrawler.dbWrapper.close();
    		
		} else {
			
			XPathCrawler xCrawler = new XPathCrawler(args[0], args[1], args[2], args[3]);
    		xCrawler.crawl();
			xCrawler.dbWrapper.close();
		}
		
    }
	
}
