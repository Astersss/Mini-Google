package edu.upenn.cis455.mapreduce.worker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.upenn.cis.stormlite.Config;
import edu.upenn.cis.stormlite.Topology;
import edu.upenn.cis.stormlite.TopologyBuilder;
import edu.upenn.cis.stormlite.bolt.MapBolt;
import edu.upenn.cis.stormlite.bolt.ReduceBolt;
import edu.upenn.cis.stormlite.distributed.WorkerHelper;
import edu.upenn.cis.stormlite.distributed.WorkerJob;
import edu.upenn.cis.stormlite.spout.FileSpout;
import edu.upenn.cis.stormlite.tuple.Fields;
import edu.upenn.cis455.mapreduce.master.MasterServlet;
import org.apache.log4j.Logger;
import test.edu.upenn.cis.stormlite.PrintBolt;
import test.edu.upenn.cis.stormlite.mapreduce.WordFileSpout;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by marcusma on 4/6/17.
 */
public class Master extends HttpServlet{

	//private static Logger log = Logger.getLogger(Master.class);
	private static Config fullConfig;
	private static Topology topo;
	private static WorkerJob workerJob;
	private static ArrayList<WorkerServer> workerList;

//	private static HttpServletResponse httpServletResponse;

	private static final String WORD_SPOUT = "WORD_SPOUT";
	private static final String MAP_BOLT = "MAP_BOLT";
	private static final String REDUCE_BOLT = "REDUCE_BOLT";
//	private static final String PRINT_BOLT = "PRINT_BOLT";

	public Master(String[] args) throws IOException{ // word_list file path, idf file path, title file path, output directory path;

//		System.out.println("inside Master");
//		httpServletResponse = hRes;

//		String mapReduceClass = hReq.getParameter("job");
//		String inputDir = hReq.getParameter("inputdir");
//		String outputDir = hReq.getParameter("outputdir");
//		String mapThread = hReq.getParameter("mapthread");
//		String reduceThread = hReq.getParameter("reducethread");

		String wordListFile = args[0];
		String idfListFile = args[1];
		String titleFile = args[2];
		String mapThread = "5";
		String reduceThread = "1";

		fullConfig = new Config();
		fullConfig.put("job", "VectorModel");
//		fullConfig.put("inputdir", inputDir);
//		fullConfig.put("outputdir", outputDir);
//		fullConfig.put("mapclass", mapReduceClass);
//		fullConfig.put("reduceclass", mapReduceClass);

		fullConfig.put("spoutexecutors", "1");
		fullConfig.put("mapexecutors", mapThread);
		fullConfig.put("reduceexecutors", reduceThread);
		fullConfig.put("wordlistfile", wordListFile);
		fullConfig.put("idflistfile", idfListFile);
		fullConfig.put("titlefile", titleFile);
		fullConfig.put("outputdir", args[3]);

//		HashMap<String, HashMap<String, Object>> workerMap = MasterServlet.getMap();
		StringBuffer sb = new StringBuffer();
//
		int numOfWorker = 0;

		for(int i = 0; i < 1; i++){
//			if(Calendar.getInstance().getTimeInMillis() - (long)workerMap.get(tempKey).get("currenttime") <= 30 * 1000){
				sb.append(";" + "127.0.0.1" + ":1000" + Integer.toString(i+1));
				numOfWorker++;
//			}
		}
//
		fullConfig.put("workerlist", sb.toString().substring(1));
//		fullConfig.put("numofworkers", Integer.toString(numOfWorker));
		fullConfig.put("numofworkers", "1");
		workerList = new ArrayList<>();

//		for(String tempKey : fullConfig.keySet()){
//			System.out.println(tempKey + ": " + fullConfig.get(tempKey).toString());
//		}

		for(int i = 0; i<1; i++){
			WorkerServer tempWorker = new WorkerServer(10001+i);
			try{
				Thread.sleep(5000);
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
			workerList.add(tempWorker);
		}

		System.out.println("Initialize spouts and bolts...");

		FileSpout spout = new WordFileSpout();

		System.out.println("Finish initializing spouts...");

		MapBolt bolt = new MapBolt();
		ReduceBolt bolt2 = new ReduceBolt();
//		PrintBolt printer = new PrintBolt();

		System.out.println("Finish initializing bolts...");

		TopologyBuilder builder = new TopologyBuilder();

		// TODO check spout specification;
		builder.setSpout(WORD_SPOUT, spout, 1);

		builder.setBolt(MAP_BOLT, bolt, Integer.valueOf(mapThread)).fieldsGrouping(WORD_SPOUT, new Fields("value"));

		builder.setBolt(REDUCE_BOLT, bolt2, Integer.valueOf(reduceThread)).fieldsGrouping(MAP_BOLT, new Fields("key"));

		// TODO check specification;
//		builder.setBolt(PRINT_BOLT, printer, 1).firstGrouping(REDUCE_BOLT);

		topo = builder.createTopology();

		workerJob = new WorkerJob(topo, fullConfig);

		createAndStartWorker();
	}

	public static void createAndStartWorker() throws IOException{

		ObjectMapper mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

		try {

//			String[] workers = WorkerHelper.getWorkers(fullConfig);
//
//			int i = 0;
//
//			for (String dest: workers){
//
//				fullConfig.put("workerIndex", String.valueOf(i));
//				workerJob.getConfig().put("workerIndex", String.valueOf(i));
//
//				i++;
//
//				if(sendJob(dest, "POST", fullConfig, "definejob", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(workerJob)).getResponseCode() != HttpURLConnection.HTTP_OK) {
//					throw new RuntimeException("/definejob error");
//				}
//
//			}
//
//			for (String dest: workers) {
//				if (sendJob(dest, "POST", fullConfig, "runjob", "").getResponseCode() != HttpURLConnection.HTTP_OK) {
//					throw new RuntimeException("/runjob error");
//				}
//			}
			for(int i = 0; i < 1; i++){

				fullConfig.put("workerIndex", String.valueOf(i));
				workerJob.getConfig().put("workerIndex", String.valueOf(i));

				if(sendJob("http://127.0.0.1:1000"+Integer.toString(i+1), "POST", fullConfig, "definejob", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(workerJob)).getResponseCode() != HttpURLConnection.HTTP_OK){
					throw new RuntimeException("/definejob error...");
				}

				if(sendJob("http://127.0.0.1:1000"+Integer.toString(i+1), "POST", fullConfig, "runjob", "").getResponseCode() != HttpURLConnection.HTTP_OK){
					throw new RuntimeException("/runjob error...");
				}
			}
		}
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
	}

	static HttpURLConnection sendJob(String dest, String reqType, Config config, String job, String parameters) throws IOException{

		URL url = new URL(dest + "/" + job);

		//log.info("Sending request to " + url.toString());

		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(reqType);

		if (reqType.equals("POST")) {

			conn.setRequestProperty("Content-Type", "application/json");
			OutputStream os = conn.getOutputStream();
			byte[] toSend = parameters.getBytes();
			os.write(toSend);
			os.flush();
			conn.disconnect();

		}
		else{
			conn.getOutputStream();
		}
		return conn;
	}

	public static void main(String[] args){ // word_list file path, idf file path, title file path, output file path;
		try{
			Master master = new Master(args);
		}
		catch(IOException e){
			System.out.println("Master creation error...");
			e.printStackTrace();
		}
	}
}
