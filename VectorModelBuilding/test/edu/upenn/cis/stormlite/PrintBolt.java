package test.edu.upenn.cis.stormlite;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import edu.upenn.cis.stormlite.Config;
import org.apache.log4j.Logger;

import edu.upenn.cis.stormlite.OutputFieldsDeclarer;
import edu.upenn.cis.stormlite.TopologyContext;
import edu.upenn.cis.stormlite.bolt.IRichBolt;
import edu.upenn.cis.stormlite.bolt.OutputCollector;
import edu.upenn.cis.stormlite.routers.StreamRouter;
import edu.upenn.cis.stormlite.tuple.Fields;
import edu.upenn.cis.stormlite.tuple.Tuple;

/**
 * A trivial bolt that simply outputs its input stream to the
 * console
 * 
 * @author zives
 *
 */
public class PrintBolt implements IRichBolt {
	static Logger log = Logger.getLogger(PrintBolt.class);
	
	Fields myFields = new Fields();

    /**
     * To make it easier to debug: we have a unique ID for each
     * instance of the PrintBolt, aka each "executor"
     */
    String executorId = UUID.randomUUID().toString();

    Config statusConfig = new Config();

	@Override
	public void cleanup() {
		// Do nothing

	}

	@Override
	public void execute(Tuple input) {

		this.statusConfig.put("statusstatus", "IDLE");

		if(!input.isEndOfStream()){

			String key = input.getStringByField("key");
			String value = input.getStringByField("value");
			String tempValue = statusConfig.get("statusresults");
			StringBuffer sb;

			if(tempValue.equals("NULL")){
				sb = new StringBuffer();
			}
			else{
				sb = new StringBuffer(tempValue);
			}

			sb.append("(" + key + "," + value + ")");

			statusConfig.put("statusresults", sb.toString());
			System.out.println(getExecutorId() + ": " + input.toString());

			String localDir = statusConfig.get("localdir");
			String outputDir = statusConfig.get("outputdir");
			String combinedDir;

			if(localDir.endsWith("/")){
				combinedDir = localDir + outputDir;
			}
			else{
				combinedDir = localDir + "/" + outputDir;
			}

			if(combinedDir.endsWith("/")){
				combinedDir += "output.txt";
			}
			else{
				combinedDir += "/output.txt";
			}

			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(combinedDir));
				String[] tempStringArray = sb.toString().split("\\)");

				for(String tempTuple : tempStringArray){
					bw.write(tempTuple.replace("(", " ").replace(")", " ") + "\n");
				}
				bw.flush();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}


//		for(String temp : statusConfig.keySet()){
//			System.out.println(temp + ": " + statusConfig.get(temp));
//		}
	}

	@Override
	public void prepare(Map<String, String> stormConf, TopologyContext context, OutputCollector collector) {
		this.statusConfig = (Config)stormConf;
	}

	@Override
	public String getExecutorId() {
		return executorId;
	}

	@Override
	public void setRouter(StreamRouter router) {
		// Do nothing
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(myFields);
	}

	@Override
	public Fields getSchema() {
		return myFields;
	}

}
