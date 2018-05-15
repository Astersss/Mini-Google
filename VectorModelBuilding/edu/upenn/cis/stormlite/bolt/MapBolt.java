package edu.upenn.cis.stormlite.bolt;

import java.io.*;
import java.util.*;

import edu.upenn.cis.stormlite.Config;
import edu.upenn.cis.stormlite.tuple.Values;
import edu.upenn.cis455.mapreduce.master.ConfigContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import edu.upenn.cis.stormlite.OutputFieldsDeclarer;
import edu.upenn.cis.stormlite.TopologyContext;
import edu.upenn.cis.stormlite.distributed.WorkerHelper;
import edu.upenn.cis.stormlite.routers.StreamRouter;
import edu.upenn.cis.stormlite.tuple.Fields;
import edu.upenn.cis.stormlite.tuple.Tuple;
import edu.upenn.cis455.mapreduce.Job;

/**
 * A simple adapter that takes a MapReduce "Job" and calls the "map"
 * on a per-tuple basis.
 * 
 * 
 */
/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class MapBolt implements IRichBolt {
	static Logger log = Logger.getLogger(MapBolt.class);

//	Job mapJob;
	private String wordListFile;
	private String idfListFile;
	private ArrayList<String> wordList;
	private HashMap<String, Double> idfMap;
	private File outputFile;

    /**
     * To make it easier to debug: we have a unique ID for each
     * instance of the WordCounter, aka each "executor"
     */
    String executorId = UUID.randomUUID().toString();
    
	Fields schema = new Fields("key", "value");

	ConfigContext configContext = new ConfigContext();
	
	/**
	 * This tracks how many "end of stream" messages we've seen
	 */
	int neededVotesToComplete;

	/**
     * This is where we send our output stream
     */
    private OutputCollector collector;
    
    private TopologyContext context;

    private Config statusConfig;
    
    public MapBolt() {
    }
    
	/**
     * Initialization, just saves the output stream destination
     */
    @Override
    public void prepare(Map<String,String> stormConf, 
    		TopologyContext context, OutputCollector collector) {

    	this.collector = collector;
        this.context = context;

//        this.statusConfig = (Config)stormConf;
//        this.statusConfig.put("statuskeysread", "0");

        this.wordList = new ArrayList<>();
        this.idfMap = new HashMap<>();
        
//        if (!stormConf.containsKey("mapclass"))
//        	throw new RuntimeException("Mapper class is not specified as a config option");
//        else {
//        	String mapperClass = stormConf.get("mapclass");
//
//        	try {
//				mapJob = (Job)Class.forName(mapperClass).newInstance();
//			}
//			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//				log.error("Unable to instantiate the class " + mapperClass);
//				e.printStackTrace();
//				throw new RuntimeException("Unable to instantiate the class " + mapperClass);
//			}
//        }

		if (!stormConf.containsKey("wordlistfile"))
        	throw new RuntimeException("wordlist file is not specified as a config option");
        else {

			wordListFile = stormConf.get("wordlistfile");

			System.out.println("wordlistfile: " + wordListFile);

        	File tempFile = new File(wordListFile);
			try{
				BufferedReader br = new BufferedReader(new FileReader(tempFile));
				String line;

				while((line = br.readLine()) != null){
					wordList.add(line.trim());
				}
				br.close();

			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}

		if (!stormConf.containsKey("idflistfile"))
			throw new RuntimeException("idflist file is not specified as a config option");
		else {
			idfListFile = stormConf.get("idflistfile");

			File tempFile = new File(idfListFile);

			try{
				BufferedReader br = new BufferedReader(new FileReader(tempFile));
				String line;

				while((line = br.readLine()) != null){

					String[] tempString = line.split("\\s+");
					idfMap.put(tempString[0].trim(), Double.parseDouble(tempString[1]));
				}

				br.close();
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}

		if (!stormConf.containsKey("outputdir"))
			throw new RuntimeException("output file is not specified as a config option");
		else {
			outputFile = new File(stormConf.get("outputdir") + "/output_file." + executorId + ".txt");
			System.out.println("Original output file: " + outputFile);
			try{
				outputFile.createNewFile();
				System.out.println("New output file: " + outputFile);
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
        
        if (!stormConf.containsKey("spoutexecutors")) {
        	throw new RuntimeException("Mapper class doesn't know how many input spout executors");
        }

        int intSpoutExecutors = Integer.parseInt(stormConf.get("spoutexecutors"));
		int intMapExecutors = Integer.parseInt(stormConf.get("mapexecutors"));
		int intWorker = Integer.parseInt(stormConf.get("numofworkers"));

		neededVotesToComplete = (intWorker-1)*intSpoutExecutors*intMapExecutors+intSpoutExecutors;
		System.out.println("Initializing neededVotesToComplete for map to: " + neededVotesToComplete);

    }

    /**
     * Process a tuple received from the stream, incrementing our
     * counter and outputting a result
     */
    @Override
    public synchronized void execute(Tuple input) {

    	if (!input.isEndOfStream()) {

	        String key = input.getStringByField("key");
	        String value = input.getStringByField("value");
			System.out.println(getExecutorId() + " received " + key + " / " + value + "from spout...");
//			statusConfig.put("statusstatus", "MAPPING");

//			int preKeysRead = Integer.parseInt(statusConfig.get("statuskeysread"));
//			preKeysRead++;
//			statusConfig.put("statuskeysread", Integer.toString(preKeysRead));
	        
	        if (neededVotesToComplete == 0){
				throw new RuntimeException("We received data after we thought the stream had ended!");
			}

//			mapJob.map(key, value, configContext);

			String url = value.split("\\s+", 2)[0];
			String title = value.split("\\s+", 2)[1];
			StringBuffer sb = new StringBuffer();
			sb.append(url);
			sb.append(" ");
			for(String tempWord : wordList){
				int tf = StringUtils.countMatches(title, tempWord);
				double idf = idfMap.get(tempWord);
				Double tempProd = tf*idf;
				sb.append(tempProd.toString() + " ");
			}

			try{
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile, true));
				sb.append("\n");
				bw.write(sb.toString());
				bw.flush();
				bw.close();
				System.out.println("Finish writing a line in to file " + outputFile);
			}
			catch(IOException e){
				e.printStackTrace();
			}

			context.incMapOutputs(key);

    	}
    	else if(input.isEndOfStream()) {
    		neededVotesToComplete--;
    		if(neededVotesToComplete != 0){
				System.out.println("new needed value for Mapbolt: " + neededVotesToComplete);
			}
			else{
//				for(String tempKey : (Set<String>)configContext.keySet()){
//					collector.emit(new Values<Object>(tempKey, configContext.get(tempKey).toString()));
//				}
//				collector.emit(new Values<Object>("nonsence"));
				System.out.println("Mapbolt finishes...");
				collector.emitEndOfStream();
			}
    	}

    }

    /**
     * Shutdown, just frees memory
     */
    @Override
    public void cleanup() {
    }

    /**
     * Lets the downstream operators know our schema
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(schema);
    }

    /**
     * Used for debug purposes, shows our exeuctor/operator's unique ID
     */
	@Override
	public String getExecutorId() {
		return executorId;
	}

	/**
	 * Called during topology setup, sets the router to the next
	 * bolt
	 */
	@Override
	public void setRouter(StreamRouter router) {
		this.collector.setRouter(router);
	}

	/**
	 * The fields (schema) of our output stream
	 */
	@Override
	public Fields getSchema() {
		return schema;
	}
}
