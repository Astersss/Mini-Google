package edu.upenn.cis455.crawler;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import edu.upenn.cis455.crawler.XPathCrawler;

public class URLSpout implements IRichSpout
{
	static Logger log = Logger.getLogger(URLSpout.class);
	FrontierQueueRuntime frontierQueueRuntime;
	public SpoutOutputCollector collector;
	public String id = UUID.randomUUID().toString();
	
	public URLSpout(){
		log.info("Starting Spout");
		this.frontierQueueRuntime = XPathCrawler.frontierQueueRuntime;
	}
	
	@Override
	public String getExecutorId() {
		return id;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("URL"));
	}

	@Override
	public void open(Map<String, String> config, TopologyContext topo,
			SpoutOutputCollector collector) {

		this.collector = collector;
	}

	@Override
	public void close() {
		
	}

	@Override
	public void nextTuple() {
		
		try {
			if (!frontierQueueRuntime.isEmpty()){
				String urlString = frontierQueueRuntime.poll();

				if (!RobotCache.isValid(urlString)) return;
				
				if (!RobotCache.checkDelay(urlString)){
					System.out.println(urlString + ": Pushed back to queue" );
					frontierQueueRuntime.offer(urlString);
					//Thread.sleep(500);
					//this.collector.emit(new Values<Object>(urlString));
				} else {
					
					this.collector.emit(new Values<Object>(urlString));
				}
			} 
		} catch (Exception e) {
			
		}
		
		Thread.yield();
	}
	

	@Override
	public void setRouter(IStreamRouter router) {
		this.collector.setRouter(router);
	}

}