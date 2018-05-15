package edu.upenn.cis455.crawler;

import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;
import edu.upenn.cis455.storage.Webpage;

public class CrawlBolt implements IRichBolt{
	
	static Logger log = Logger.getLogger(CrawlBolt.class);
	String executorId = UUID.randomUUID().toString();
	public FrontierQueueRuntime frontierQueueRuntime;
	
	Fields fields = new Fields("URL", "WEBPAGE", "ISDOWNLOADED");
	String id = UUID.randomUUID().toString();
	
	private OutputCollector collector;

	
	public CrawlBolt(){
		log.info("Starting CrawlBolt");
    	this.frontierQueueRuntime = XPathCrawler.frontierQueueRuntime;
	}
	
	@Override
	public String getExecutorId() {
		return id;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(fields);
	}

	@Override
	public void cleanup() {
		
	}

	@Override
	public void execute(Tuple input) {
		String url = input.getStringByField("URL");
		
		try {
			
			if (url.startsWith("https")){
				MyHttpsClient client = new MyHttpsClient(url);
				client.sendRequest(false);
				
				if (frontierQueueRuntime.canDownload(client)){
					Webpage webpage = client.getWebpage();
					collector.emit(new Values<Object>(url, webpage, "FALSE"));
				} else {
					
				}
				
			} else if (url.startsWith("http")){
				MyHttpClient client = new MyHttpClient(url);
				client.sendRequest(false);
				
				if (frontierQueueRuntime.canDownload(client)){
					Webpage webpage = client.getWebpage();
					collector.emit(new Values<Object>(url, webpage, "FALSE"));
				} else {
					
				}
			}
		} catch (Exception e) {
			
		}
		

	}	
	
	
	@Override
	public void prepare(Map<String, String> stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void setRouter(IStreamRouter router) {
		this.collector.setRouter(router);
	}

	@Override
	public Fields getSchema() {
		return fields;
	}

}
