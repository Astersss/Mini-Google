package edu.upenn.cis455.crawler;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import edu.upenn.cis455.storage.Webpage;

public class ExtractedBolt implements IRichBolt{
	
	Fields fields = new Fields("URL", "WEBPAGE", "LINKS");
	String id = UUID.randomUUID().toString();
	
	private OutputCollector collector;
	
	public ExtractedBolt(){
		
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
		Webpage webpage = (Webpage) input.getObjectByField("WEBPAGE");

		ArrayList<String> allLinks = MyHTMLParser.parseForHtml(url);
		if (allLinks == null) return;
		ArrayList<String> links = new ArrayList<>();
		
		try {
			for (String urlstring : allLinks){
				if (urlstring == null || urlstring.equals("") || urlstring.length() > 80 || urlstring.contains("?")) continue;
				
				if (urlstring.contains("dreamwidth")) continue;
				if (urlstring.contains("#")) continue;
				if (urlstring.contains("twitter.com") || urlstring.contains("facebook.com") || urlstring.contains("google.com")) continue;
				if (urlstring.contains("jpg") || urlstring.contains("pdf") || urlstring.contains("php")) continue;
				
				//if (urlstring.contains("#cite") || urlstring.contains("#External_links")) continue;
				if (urlstring.contains("#sitelinks") || urlstring.contains("Special:") 
						|| urlstring.contains("#Bibliography") || urlstring.contains("#Reference")
						|| urlstring.contains("#Awards") || urlstring.contains("#Notes")
						|| urlstring.contains("#cite_note") || urlstring.contains("Template:")
						|| urlstring.contains("www.youtube.com")) continue;
				//if (RobotCache.isValid(urlstring)){
					links.add(urlstring.trim());
				//}
			}
		} catch (Exception e) {
			System.out.println("ConcurrentModificationException");
		}
		
		collector.emit(new Values<Object>(url, webpage, links));

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
