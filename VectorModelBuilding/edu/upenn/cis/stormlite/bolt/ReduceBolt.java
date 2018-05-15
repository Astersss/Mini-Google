package edu.upenn.cis.stormlite.bolt;

import java.io.*;
import java.util.*;

import edu.upenn.cis.stormlite.Config;
import edu.upenn.cis.stormlite.tuple.Values;
import edu.upenn.cis455.mapreduce.master.ConfigContext;
import org.apache.log4j.Logger;

import edu.upenn.cis.stormlite.OutputFieldsDeclarer;
import edu.upenn.cis.stormlite.TopologyContext;
import edu.upenn.cis.stormlite.routers.StreamRouter;
import edu.upenn.cis.stormlite.tuple.Fields;
import edu.upenn.cis.stormlite.tuple.Tuple;
import edu.upenn.cis455.mapreduce.Job;

/**
 * A simple adapter that takes a MapReduce "Job" and calls the "reduce"
 * on a per-tuple basis
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

public class ReduceBolt implements IRichBolt {
	static Logger log = Logger.getLogger(ReduceBolt.class);

	
	Job reduceJob;

    /**
     * To make it easier to debug: we have a unique ID for each
     * instance of the WordCounter, aka each "executor"
     */
    String executorId = UUID.randomUUID().toString();
    
	Fields schema = new Fields("key", "value");
	
	boolean sentEof = false;

	File outputDir;
	
	/**
	 * Buffer for state, by key
	 */
	Config statusConfig = new Config();

	ConfigContext configContext = new ConfigContext();

	/**
     * This is where we send our output stream
     */
    private OutputCollector collector;
    
    private TopologyContext context;
    
    int neededVotesToComplete;
    
    public ReduceBolt() {
    }
    
    /**
     * Initialization, just saves the output stream destination
     */
    @Override
    public void prepare(Map<String,String> stormConf, 
    		TopologyContext context, OutputCollector collector) {

        this.collector = collector;
        this.context = context;
		this.statusConfig = (Config)stormConf;

//		this.statusConfig.put("statuskeyswritten", "0");

//        if (!stormConf.containsKey("reduceclass"))
//        	throw new RuntimeException("Mapper class is not specified as a config option");
//        else {
//        	String mapperClass = stormConf.get("reduceclass");
//
//        	try {
//				reduceJob = (Job)Class.forName(mapperClass).newInstance();
//			}
//			catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
//				log.debug("Unable to instantiate the class " + mapperClass);
//				e.printStackTrace();
//				throw new RuntimeException("Unable to instantiate the class " + mapperClass);
//			}
//        }

		if (!stormConf.containsKey("outputdir"))
        	throw new RuntimeException("outputdir is not specified as a config option");
        else {
			outputDir = new File(stormConf.get("outputdir"));
        }

        if (!stormConf.containsKey("mapexecutors")) {
        	throw new RuntimeException("Reducer class doesn't know how many map bolt executors");
        }

        int intMapExecutors = Integer.parseInt(stormConf.get("mapexecutors"));
        int intReduceExecutors = Integer.parseInt(stormConf.get("reduceexecutors"));
        int intWorker = Integer.parseInt(stormConf.get("numofworkers"));

        neededVotesToComplete = (intWorker-1)*intMapExecutors*intReduceExecutors+intMapExecutors;
		System.out.println("Initializing neededVotesToComplete for reduce to: " + neededVotesToComplete);
	}

    /**
     * Process a tuple received from the stream, buffering by key
     * until we hit end of stream
     */
    @Override
    public synchronized void execute(Tuple input){

//    	statusConfig.put("statusstatus", "REDUCING");

    	if (sentEof){
			if(input != null)
	        	throw new RuntimeException(getExecutorId() + "gets more data tuples than expected...");
    		// Already done!
		}
		else if (input.isEndOfStream()) {

			neededVotesToComplete--;
			System.out.println(getExecutorId() + " decrease to: " + neededVotesToComplete + "sentEof: " + sentEof);

			if(neededVotesToComplete == 0){
				sentEof = true;

//				for(String tempKey : (Set<String>)configContext.keySet()){
//					int tempSum = 0;
//					for(String temp : (List<String>)configContext.get(tempKey)){
//						tempSum += Integer.parseInt(temp);
//					}
//					collector.emit(new Values<Object>(tempKey, Integer.toString(tempSum)));
//				}
				File[] fileList = outputDir.listFiles();
				StringBuffer sb = new StringBuffer();
				BufferedReader br;
				BufferedWriter bw;

				for(File temp : fileList){
					if(temp.isFile() && temp.getName().endsWith("txt")){
						try{
							br = new BufferedReader(new FileReader(temp));
							String temptemp;

							while((temptemp = br.readLine()) != null && temptemp.length() != 0){
								System.out.println("line from file: " + temp + "," + temptemp);
								sb.append(temptemp);
								sb.append("\n");
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
				}

				try{
					bw = new BufferedWriter(new FileWriter(new File(outputDir+"/output1.txt")));
					bw.write(sb.toString());
					bw.flush();
					bw.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}

//				collector.emitEndOfStream();
				System.out.println("Reduce finishes...");
//				System.exit(1);
			}

		}
    	else{
    		
//    		String key = input.getStringByField("key");
//	        String value = input.getStringByField("value");
//			System.out.println(getExecutorId() + " received " + key + " / " + value + " from map bolt... with number: " + neededVotesToComplete);
//
//			int tempWritten = Integer.parseInt(statusConfig.get("statuskeyswritten"));
//			tempWritten++;
//			statusConfig.put("statuskeyswritten", Integer.toString(tempWritten));
//
//			ArrayList<String> tempArrayList = new ArrayList<>();
//			tempArrayList.add(value);
//
//			reduceJob.reduce(key, tempArrayList.iterator(), configContext);
//
//	        context.incReduceOutputs(key);
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
