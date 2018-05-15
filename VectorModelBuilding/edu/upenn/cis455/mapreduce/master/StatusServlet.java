package edu.upenn.cis455.mapreduce.master;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by marcusma on 4/4/17.
 */
public class StatusServlet extends HttpServlet{

	//static Logger log = Logger.getLogger(StatusServlet.class);

	@Override
	public void doGet(HttpServletRequest hReq, HttpServletResponse hRes) throws IOException{

		HashMap<String, HashMap<String, Object>> masterServletMap = MasterServlet.getMap();

		PrintWriter pw = hRes.getWriter();
		hRes.setContentType("text/html");
		pw.write("<html><head><title>Status</title></head>");
		int totalWorker = 0;

		if(masterServletMap != null && masterServletMap.size() != 0){

			for(String tempKey : masterServletMap.keySet()){

				if(Calendar.getInstance().getTimeInMillis() - (Long)masterServletMap.get(tempKey).get("currenttime") <= 30*1000){

					totalWorker++;
					pw.write("worker id:" + tempKey + ": </br>");

					pw.write("<p style=\"text-indent: 1em;\"> IP:Port: " + masterServletMap.get(tempKey).get("ip").toString() + ":" + masterServletMap.get(tempKey).get("port").toString() + "</br></p>");
					pw.write("<p style=\"text-indent: 1em;\"> Status: " + masterServletMap.get(tempKey).get("status").toString() + "</br></p>");
					pw.write("<p style=\"text-indent: 1em;\"> Job: " + masterServletMap.get(tempKey).get("job").toString() + "</br></p>");
					pw.write("<p style=\"text-indent: 1em;\"> Keys Read: " + masterServletMap.get(tempKey).get("keysread").toString() + "</br></p>");
					pw.write("<p style=\"text-indent: 1em;\"> Keys Written: " + masterServletMap.get(tempKey).get("keyswritten").toString() + "</br></p>");
					pw.write("<p style=\"text-indent: 1em;\"> Results: " + masterServletMap.get(tempKey).get("results").toString() + "</br></p>");

				}
			}
		}
		else{
			pw.write("No current status of the workers");
		}

		if(totalWorker == 0){
			pw.write("No current status of the workers");
		}
		else{
			pw.write("<form method=\"post\" action=\"submitwork\">\n" +
						//"<center>" +
						"<table border=\"0\" width=\"30%\" cellpadding=\"3\">" +
							"<thead>\n" +
								"<tr>\n" +
									"<th colspan=\"2\">New Job Specification</th>\n" +
								"</tr>\n" +
							"</thead>\n" +
							"<tbody>\n" +
								"<tr>\n" +
									"<td>Name of the Job</td>\n" +
									"<td><input type=\"text\" name=\"job\" value=\"\" /></td>\n" +
								"</tr>\n" +
								"<tr>\n" +
									"<td>Input Directory</td>\n" +
									"<td><input type=\"text\" name=\"inputdir\" value=\"\" /></td>\n" +
								"</tr>\n" +
								"<tr>\n" +
									"<td>Output Directory</td>\n" +
									"<td><input type=\"text\" name=\"outputdir\" value=\"\" /></td>\n" +
								"</tr>\n" +
								"<tr>\n" +
									"<td>Number of Map Thread on Each Worker</td>\n" +
									"<td><input type=\"text\" name=\"mapthread\" value=\"\" /></td>\n" +
								"</tr>\n" +
								"<tr>\n" +
									"<td>Number of Reduce Thread on Each Worker</td>\n" +
									"<td><input type=\"text\" name=\"reducethread\" value=\"\" /></td>\n" +
								"</tr>\n" +
								"<tr>\n" +
									"<td>\n" +
									"<input type=\"submit\" value=\"Submit\">\n" +
									"</td>\n" +
								"</tr>\n" +
							"</tbody>\n" +
						"</table>\n" +
						//"</center>\n" +
					"</form>");
		}
		System.out.println("StatusServlet works fine");
		//log.debug("StatusServlet works fine");
	}
}
