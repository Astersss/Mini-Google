package org.cis455.project;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/*
 * Servlet for show main interface interface
 */
public class IndexServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp){
//		System.out.println();
//		try {
////		String htmlPath = "../../webcontent/homePage.html";
////		System.out.println("htmlPath:"+htmlPath);
//		File f = new File("/Users/yimengxu/Documents/workspace/cis555_SearchEngine/WebContent/homePage.html");
////		File f = new File(htmlPath);
//		StringBuilder sb = new StringBuilder();
//		Scanner sc = new Scanner(f);
//		while (sc.hasNext()){
//			sb.append(sc.nextLine() + "\n");
//		}
//		sc.close();
//		resp.setStatus(HttpServletResponse.SC_OK);
//		resp.setContentType("text/html");
//       
//		PrintWriter pw = resp.getWriter();
//        pw.println(sb.toString());
//        pw.flush();
//        
//		}catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}
