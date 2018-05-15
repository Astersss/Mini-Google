package edu.upenn.cis455.mapreduce.master;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Calendar;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

public class MasterServlet extends HttpServlet {

	//static Logger log = Logger.getLogger(MasterServlet.class);

	static private HashMap<String, HashMap<String, Object>> masterInfoMap = new HashMap<>(); // key: the string of each worker's ip and port number;

  	static final long serialVersionUID = 455555001;

  	static public HashMap<String, HashMap<String, Object>> getMap(){
  		return masterInfoMap;
	}

  	public void doGet(HttpServletRequest request, HttpServletResponse response)
       throws java.io.IOException{

  		String ipAddr = request.getRemoteAddr();
		String portNum = request.getParameter("port");
		String status = request.getParameter("status");
		String job = request.getParameter("job");
		String keysRead = request.getParameter("keysread");
		String keysWritten = request.getParameter("keyswritten");
		String results = request.getParameter("results");
		String tempKey = ipAddr+portNum;
		Long currentTime = Calendar.getInstance().getTimeInMillis();

		HashMap<String, Object> tempMap = new HashMap<>();

		if(ipAddr != null && portNum != null && status != null && job != null && keysRead != null && keysWritten != null && results != null && tempKey != null && currentTime != null){

			tempMap.put("ip", ipAddr);
			tempMap.put("port", portNum);
			tempMap.put("status", status);
			tempMap.put("job", job);
			tempMap.put("keysread", keysRead);
			tempMap.put("keyswritten", keysWritten);
			tempMap.put("results", results);
			tempMap.put("currenttime", currentTime);
			masterInfoMap.put(tempKey, tempMap);

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>Master</title></head>");
			out.println("<body>Info well kept in master</body></html>");

		}
		else{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html><head><title>Master</title></head>");
			out.println("<body>Missing info in GET method</body></html>");
		}

		System.out.println("MasterServlet works fine");
		//log.debug("MasterServlet works fine");
  	}
}
  
