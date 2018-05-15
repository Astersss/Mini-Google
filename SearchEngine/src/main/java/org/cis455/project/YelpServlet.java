package org.cis455.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class YelpServlet extends HttpServlet{
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		System.out.println("In yelp post");
	}
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String servletUrl = req.getServletPath();
		System.out.println("servlet url " + servletUrl);
		PrintWriter out = resp.getWriter();
		String htmlHeader = "<head>"
							+"<link href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css\" rel=\"stylesheet\">"
							+"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">"
							+"<link rel=\"stylesheet\" href=\"css/send_email.css\">"
							+"<link href=\"https://select2.github.io/dist/css/select2.min.css\" rel=\"stylesheet\">"
							+"</head>";
		out.println("<!DOCTYPE html>");
		out.println(htmlHeader);
		out.println("<body>");
		if(servletUrl.equals("/yelp")) {
//			String createQuery = "<form action=	'yelpsearch' method= \"get\"><br>" 
//					   + "City<input type = \"text\" name = \"city\"><br>" 
//			           + "Food<input type = \"text\" name = \"food\"><br>"
//					   + "<input type =  \"submit\" value = \"search\"></form><br>";
//			out.println(createQuery);
			
			String createQuery = "<br><br><div class=\"panel panel-default container\" style=\"margin:0 auto; width: 40%\">"
						+"<form action=	'yelpsearch' method= \"get\">"
						+ "<div class=\"form-group\">"
						+ "<br><label for=\"formGroupExampleInput\">City</label>"
						+ "<input type=\"text\" class=\"form-control\" id=\"formGroupExampleInput\" name = \"city\" placeholder=\"city name\">"
						+ "</div>"
						+ "<div class=\"form-group\">"
						+ "<label for=\"formGroupExampleInput2\">Food</label>"
						+ "<input type=\"text\" class=\"form-control\" id=\"formGroupExampleInput2\" name = \"food\" placeholder=\"sushi\">"
						+ "</div>"
						+ "<div class=\"form-group\">"
						+ "<input type=\"submit\" class=\"btn btn-success\" value = \"search\">"
						+ "<br><a href=\"index.jsp\">Back to Home</a>"
						+ "</div></form></div>";
			out.println(createQuery);
			
		} else if(servletUrl.equals("/yelpsearch")) {
			String item = req.getParameter("food");
			String city = req.getParameter("city");
			ArrayList<HashMap<String, String>> ret = Search.search(item, city);
			
			out.println("<br>Most Recommended Restaurants For You: ");
			out.println("<ul>");
			for(int i = 0; i < 5; i++) {
				String name = ret.get(i).get("name");
				String phone = ret.get(i).get("display_phone");
				out.println("<li>" + name +":" +phone + "</li>");
			}
			out.println("</ul>");
			out.println("<a href=\"index.jsp\">Back to Home</a>");
		}
		
		out.println("</body></html");
	
	}
}
