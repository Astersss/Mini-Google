package org.cis455.project;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Servlet for handle query
 */
//@WebServlet("/query")
public class QueryServlet extends HttpServlet{

	/**
	 * Servlet handle query as model
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		String query = request.getParameter("query");
//		String startPageNum = request.getParameter("startPage");
		
		QueryService queryService = new QueryService();
		
		long timeStart = System.currentTimeMillis();
		//get matched pages by calling queryService
		List<WebContent> pages = queryService.getMatchedWeb(query);
//		List<WebContent> pages = queryService.getSampleWebPages(query);
		long timeStop = System.currentTimeMillis();
		String diffSeconds = String.valueOf((timeStop - timeStart) / 1000.0);
		request.setAttribute("pages", pages);
		request.setAttribute("timediff", diffSeconds);
//		request.setAttribute("startPage", startPageNum);
		//use dispatcher to transfer control to query.jsp
		RequestDispatcher dispatcher = request.getRequestDispatcher("query.jsp");
		dispatcher.forward(request, response);
		response.sendRedirect("query.jsp");
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException{
		String query = request.getParameter("query");
//		String startPageNum = request.getParameter("startPage");
		
		QueryService queryService = new QueryService();
		
		long timeStart = System.currentTimeMillis();
		//get matched pages by calling queryService
//		List<WebContent> pages = queryService.getMatchedWeb(query);
		List<WebContent> pages = queryService.getSampleWebPages(query);
		long timeStop = System.currentTimeMillis();
		String diffSeconds = String.valueOf((timeStop - timeStart) / 1000.0);
		request.getSession().setAttribute("pages", pages);
		request.getSession().setAttribute("timediff", diffSeconds);
//		request.setAttribute("startPage", startPageNum);
		//use dispatcher to transfer control to query.jsp
//		RequestDispatcher dispatcher = request.getRequestDispatcher("query.jsp");
//		dispatcher.forward(request, response);
//		response.sendRedirect("query.jsp");
		response.sendRedirect("query.jsp");
	}

}
