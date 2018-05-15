package edu.upenn.cis455.mapreduce.master;

import edu.upenn.cis455.mapreduce.worker.Master;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by marcusma on 4/5/17.
 */
public class SubmitWork extends HttpServlet{

	//static Logger log = Logger.getLogger(MasterServlet.class);

	@Override
	public void doPost(HttpServletRequest hReq, HttpServletResponse hRes) throws IOException{

		System.out.println("SubmitWork submit the form");
		Master master = new Master(null);
		//log.info("SubmitWork submit the form");

	}

}
