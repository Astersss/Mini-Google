package edu.upenn.cis.stormlite.spout;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import edu.upenn.cis.stormlite.Config;
import org.apache.log4j.Logger;

import edu.upenn.cis.stormlite.OutputFieldsDeclarer;
import edu.upenn.cis.stormlite.TopologyContext;
import edu.upenn.cis.stormlite.routers.StreamRouter;
import edu.upenn.cis.stormlite.spout.IRichSpout;
import edu.upenn.cis.stormlite.spout.SpoutOutputCollector;
import edu.upenn.cis.stormlite.tuple.Fields;
import edu.upenn.cis.stormlite.tuple.Values;

/**
 * Simple word spout, largely derived from
 * https://github.com/apache/storm/tree/master/examples/storm-mongodb-examples
 * but customized to use a file called words.txt.
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
public abstract class FileSpout implements IRichSpout {
	static Logger log = Logger.getLogger(FileSpout.class);

    /**
     * To make it easier to debug: we have a unique ID for each
     * instance of the WordSpout, aka each "executor"
     */
    String executorId = UUID.randomUUID().toString();

    /**
	 * The collector is the destination for tuples; you "emit" tuples there
	 */
	SpoutOutputCollector collector;
	
	/**
	 * This is a simple file reader
	 */
	File filename;
//	ArrayList<File> fileNames;
    BufferedReader reader;
	Random r = new Random();
	String combinedDir;
	
	int inx = 0;
	boolean sentEof = false;
	
    public FileSpout() {
//		System.out.println("before we get filename: " + filename);
//    	filename = getFilename();
//		System.out.println("after we get filename: " + filename);
//		fileNames = new ArrayList<>();
	}
    
    public abstract String getFilename();

	protected Config statusConfig;

    /**
     * Initializes the instance of the spout (note that there can be multiple
     * objects instantiated)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

    	statusConfig = (Config)conf;
        this.collector = collector;

//        String localDir = statusConfig.get("localdir");
//		String inputDir = statusConfig.get("inputdir");
//
//		if(localDir.endsWith("/")){
//			combinedDir = localDir + inputDir;
//		}
//		else{
//			combinedDir = localDir + "/" + inputDir;
//		}

		filename = new File(statusConfig.get("titlefile"));

//		File tempFile = new File(combinedDir);
//		File[] fileList = tempFile.listFiles();
//
//		for(File temp : fileList){
//			if(temp.isFile()){
//				fileNames.add(temp);
//			}
//		}

		System.out.println("Start spout for " + filename + "...");
//		log.debug(getExecutorId() + " opening file reader");

//        try {

        	
        	// If we have a worker index, read appropriate file among xyz.txt.0, xyz.txt.1, etc.
//        	if (conf.containsKey("workerIndex"))
//        		reader = new BufferedReader(new FileReader(filename + "." + conf.get("workerIndex")));
//        	else
//				reader = new BufferedReader(new FileReader(filename));
//		}
//		catch (FileNotFoundException e) {
//			log.error("Cannot find the file: " + combinedDir);
//			e.printStackTrace();
//		}
    }

    /**
     * Shut down the spout
     */
    @Override
    public void close() {
    	if (reader != null)
	    	try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    /**
     * The real work happens here, in incremental fashion.  We process and output
     * the next item(s).  They get fed to the collector, which routes them
     * to targets
     */
    @Override
    public synchronized void nextTuple() {

    	if(!sentEof){
			//for(File temp : fileNames){

				try{
					reader = new BufferedReader(new FileReader(filename.getCanonicalFile().toString()));

					if(reader != null){
						try{
							String line;
							while((line = reader.readLine()) != null){
								log.debug(getExecutorId() + " read from file " + filename.getCanonicalPath() + ": " + line);
//								String[] words = line.split("[ \\t\\,.]");

//								for(String word : words){

									log.debug(getExecutorId() + " emitting " + line);
									this.collector.emit(new Values<Object>(String.valueOf(inx++), line));

//								}
//							else if(!sentEof){
//								log.info(getExecutorId() + " finished file " + getFilename() + " and emitting EOS");
//								this.collector.emitEndOfStream();
//								sentEof = true;
//							}
							}
							System.out.println("Finish reading " + filename.getCanonicalPath().toString());
						}
						catch(IOException e){
							e.printStackTrace();
						}
					}
				}
				catch(IOException e){
					e.printStackTrace();
				}

			this.collector.emitEndOfStream();
			sentEof = true;
			Thread.yield();

			}
//			System.out.println("Finish reading all the files in the folder: " + combinedDir);

		//}
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("key", "value"));
    }


	@Override
	public String getExecutorId() {
		
		return executorId;
	}


	@Override
	public void setRouter(StreamRouter router) {
		this.collector.setRouter(router);
	}

}
