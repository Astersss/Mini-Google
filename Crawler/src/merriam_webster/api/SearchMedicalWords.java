package merriam_webster.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class SearchMedicalWords {
	
	public static InputStream in;
	public static BufferedReader br;
	
	public static void parseXML(String xml){

		try {
			File inputFile = new File("response.xml");
	         DocumentBuilderFactory dbFactory 
	            = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
			
			System.out.println("Root element :" 
		            + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("student");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String Search(String word) {
		String urlString = "http://www.dictionaryapi.com/api/references/medical/v2/xml/" + word + "?key=152b0759-9ed9-48f1-9791-62297aa92049";	
		HttpURLConnection connection = null;
		
		try{
			URL httpURL = new URL(urlString);
			
			connection = (HttpURLConnection)httpURL.openConnection();
			
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			
			in = connection.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));	
			
			StringBuffer sb = new StringBuffer();
			String line = "";
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			
			File file = new File("response.xml");
			PrintWriter pWriter = new PrintWriter(file);
			pWriter.println(sb.toString());
			pWriter.flush();
			pWriter.close();
			
			//System.out.println(sb.toString());
			parseXML(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		Search(args[0]);
	}
}
