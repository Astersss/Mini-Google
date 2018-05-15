package edu.upenn.cis455.crawler;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

public class RecordBolt implements IRichBolt{
	static Logger log = Logger.getLogger(RecordBolt.class);
	
	String executorId = UUID.randomUUID().toString();
	Fields schema = new Fields();
	private OutputCollector collector;
	private FrontierQueueRuntime frontierQueueRuntime;
	
	public RecordBolt() {
    	log.debug("Starting RecordBolt");
    	this.frontierQueueRuntime = XPathCrawler.frontierQueueRuntime;
    }
	
	@Override
	public String getExecutorId() {
		return executorId;
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(schema);
	}
	@Override
	public void cleanup() {
		
	}
	@Override
	public void execute(Tuple input) {
		//System.out.println("start record bolt");
		@SuppressWarnings("unchecked")
		ArrayList<String> links = (ArrayList<String>) input.getObjectByField("LINKS");
		
		for (String link : links){

			//if (RobotCache.isValid(link)){
			if (!frontierQueueRuntime.isVisitedURL(link)){
				frontierQueueRuntime.offer(link);
			}
			//}
			//}
		}
		System.out.println("after this webpage, frontier queue size is " + frontierQueueRuntime.size());
		//System.out.println("end record bolt");
		
	}
	@Override
	public void prepare(Map<String, String> stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	@Override
	public void setRouter(IStreamRouter router) {
		this.collector.setRouter(router);
	}
	@Override
	public Fields getSchema() {
		return schema;
	}
}
