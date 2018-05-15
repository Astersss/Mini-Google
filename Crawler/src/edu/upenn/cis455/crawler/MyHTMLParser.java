package edu.upenn.cis455.crawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * parse the HTML webpage and get all hrefs
 */
public class MyHTMLParser {			// only parse the text/html
	
	private static Document document;
	private static Elements links;
	private static ArrayList<String> linksArr;

	/**
	 * @param urlString
	 * parse url and save links in queue
	 */
	public static ArrayList<String> parseForHtml(String urlString){
		if (urlString == null) return null;
		
		try {
			document = Jsoup.connect(urlString).get();
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		} catch (Exception e1){
			return null;
		}
		
		links = document.select("a[href]");
		linksArr = new ArrayList<>();
		
		for (Element element : links){
			linksArr.add(element.attr("abs:href"));
		}
		//System.out.println(linksArr.size());
		
		return linksArr;
	}
	
	public static void main(String[] args){
		//String url = "http://crawltest.cis.upenn.edu/";
		
		//for (String tmp : MyHTMLParser.parseForHtml(url)){
			//System.out.println(tmp);
		//}
	}
	
}
