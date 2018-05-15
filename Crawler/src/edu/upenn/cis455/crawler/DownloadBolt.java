package edu.upenn.cis455.crawler;

import java.util.Map;
import java.util.UUID;
import org.apache.log4j.Logger;

import edu.upenn.cis455.storage.Webpage;

public class DownloadBolt implements IRichBolt{
	
	static Logger log = Logger.getLogger(DownloadBolt.class);
	
	Fields fields = new Fields("URL", "WEBPAGE");
	
	String executorId = UUID.randomUUID().toString();
	
	private OutputCollector collector;
	
	
	public DownloadBolt(){
		log.info("start DownloadBolt");
	}
	
	@Override
	public String getExecutorId() {
		return executorId;
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
		
		Webpage webpage = (Webpage)input.getObjectByField("WEBPAGE");
		String urlString = input.getStringByField("URL");
		String isDownloaded = input.getStringByField("ISDOWNLOADED");

		if (webpage == null) {
			return;
		}
		
		if (isDownloaded.equals("FALSE")){
			AWSAccessor.uploadFileToS3(webpage);
		}
		
		collector.emit(new Values<Object>(urlString, webpage));
		
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
