package edu.upenn.cis455.mapreduce.worker;

import static spark.Spark.setPort;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.upenn.cis.stormlite.Config;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.upenn.cis.stormlite.DistributedCluster;
import edu.upenn.cis.stormlite.TopologyContext;
import edu.upenn.cis.stormlite.distributed.WorkerHelper;
import edu.upenn.cis.stormlite.distributed.WorkerJob;
import edu.upenn.cis.stormlite.routers.StreamRouter;
import edu.upenn.cis.stormlite.tuple.Tuple;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Simple listener for worker creation 
 * 
 * @author zives
 *
 */
public class WorkerServer {
	static Logger log = Logger.getLogger(WorkerServer.class);

    static DistributedCluster cluster = new DistributedCluster();
    
    List<TopologyContext> contexts = new ArrayList<>();

	static Config statusConfig;

	int myPort;
	
	static List<String> topologies = new ArrayList<>();

	public static HeartBeat heartBeat;
	
	public WorkerServer(int arg){ // 0: master ip:port; 1: local directory; 2: local port number;

		System.out.println("Worker with port: " + arg + " has created...");

		myPort = arg;

		log.info("Creating server listener at socket " + myPort);
	
		setPort(myPort);

		System.out.println("port set success...");

    	final ObjectMapper om = new ObjectMapper();

        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        statusConfig = new Config();
//        statusConfig.put("statusport", Integer.toString(myPort));
//        statusConfig.put("statusstatus", "IDLE");
//        statusConfig.put("statusjob", "NULL");
//        statusConfig.put("statuskeysread", "0");
//        statusConfig.put("statuskeyswritten", "0");
//        statusConfig.put("statusresults", "NULL");
//
//        statusConfig.put("localdir", args[1]);
//
//		URLInfo tempUrl = new URLInfo(args[0]);
//
//		if(tempUrl != null && tempUrl.getHostName() != null){
//			heartBeat = new HeartBeat(tempUrl.getHostName(), tempUrl.getPortNo(), statusConfig);
//			new Thread(heartBeat).start();
//		}
//		else{
//			throw new RuntimeException("Error on specifying master's address...");
//		}


		Spark.post(new Route("/definejob"){

			@Override
			public Object handle(Request arg0, Response arg1) {
	        	
	        	WorkerJob workerJob;
				try {
					workerJob = om.readValue(arg0.body(), WorkerJob.class);

					for(String tempKey : workerJob.getConfig().keySet()){
						statusConfig.put(tempKey, workerJob.getConfig().get(tempKey));
					}

//					statusConfig.put("statusjob", workerJob.getConfig().get("job"));
//					statusConfig.put("statusstatus", "WAITING");

		        	try {
//						log.info("Processing job definition request " + statusConfig.get("job") + " on machine " + statusConfig.get("workerIndex"));

						contexts.add(cluster.submitTopology(statusConfig.get("job"), statusConfig, workerJob.getTopology()));
						
						synchronized (topologies) {
							topologies.add(workerJob.getConfig().get("job"));
						}
					}
					catch (ClassNotFoundException e) {
						e.printStackTrace();
						System.out.println("MapReduce job cannot be found");
					}
		            return "Job launched";
				}
				catch (IOException e) {
					e.printStackTrace();
					
					// Internal server error
					arg1.status(500);
					return e.getMessage();
				} 
	        	
			}
        	
        });
        
        Spark.post(new Route("/runjob") {

			@Override
			public Object handle(Request arg0, Response arg1) {
        		log.info("Starting job!");
				cluster.startTopology();
				
				return "Started";
			}
        });
        
        Spark.post(new Route("/pushdata/:stream") {

			@Override
			public Object handle(Request arg0, Response arg1) {
				try {
					String stream = arg0.params(":stream");
					Tuple tuple = om.readValue(arg0.body(), Tuple.class);
					
					log.debug("Worker received: " + tuple + " for " + stream);
					
					// Find the destination stream and route to it
					StreamRouter router = cluster.getStreamRouter(stream);
					
					if (contexts.isEmpty())
						log.error("No topology context -- were we initialized??");
					
			    	if (!tuple.isEndOfStream())
			    		contexts.get(contexts.size() - 1).incSendOutputs(router.getKey(tuple.getValues()));
					
					if (tuple.isEndOfStream())
						router.executeEndOfStreamLocally(contexts.get(contexts.size() - 1));
					else
						router.executeLocally(tuple, contexts.get(contexts.size() - 1));
					
					return "OK";
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
					arg1.status(500);
					return e.getMessage();
				}
				
			}
        	
        });

		System.out.println("Finish creating one worker...");

	}

	// not used in real situation;
	public static void createWorker(Map<String, String> config) {
		if (!config.containsKey("workerlist"))
			throw new RuntimeException("Worker spout doesn't have list of worker IP addresses/ports");

		if (!config.containsKey("workerIndex"))
			throw new RuntimeException("Worker spout doesn't know its worker ID");

		else{

			String[] addresses = WorkerHelper.getWorkers(config);
			String myAddress = addresses[Integer.valueOf(config.get("workerIndex"))];

			log.debug("Initializing worker " + myAddress);

			URL url;
			try {
				url = new URL(myAddress);
				//new WorkerServer(url.getPort());
			}
			catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void shutdown(){
		synchronized(topologies) {
			for (String topo: topologies)
				cluster.killTopology(topo);
		}
		
    	cluster.shutdown();
		heartBeat.threadRun = false;
		System.exit(0);
	}

//	public static void main(String[] args){
//
//		if(args.length != 3){
//			throw new RuntimeException("Wrong number of arguments");
//		}
//		else{
//			new WorkerServer(args);
//		}
//
//		System.out.println("Please press [Enter] to stop the program...");
//		try{
//			(new BufferedReader(new InputStreamReader(System.in))).readLine();
//			WorkerServer.shutdown();
//		}
//		catch(IOException e){
//			System.err.println("Error occuring while reading standard input...");
//			e.printStackTrace();
//		}
//
//	}
}
