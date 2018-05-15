package edu.upenn.cis455.mapreduce.worker;

import edu.upenn.cis.stormlite.Config;

import java.io.*;
import java.net.Socket;

/**
 * Created by marcusma on 4/11/17.
 */
public class HeartBeat implements Runnable{

	private Socket clientSocket;
	private String hostName;
	private int portNumber;
	private Config statusConfig;
	public boolean threadRun = true;

	HeartBeat(String hName, int pNum, Config sConfig){
		this.hostName = hName;
		this.portNumber = pNum;
		this.statusConfig = sConfig;
	}

	@Override
	public void run(){

		while(threadRun){

			try{

				clientSocket = new Socket("127.0.0.1", 8080);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

				String content = "GET /master/workerstatus?port=" + statusConfig.get("statusport").toString() + "&status=" + statusConfig.get("statusstatus").toString() + "&keysread=" + statusConfig.get("statuskeysread").toString() + "&keyswritten=" + statusConfig.get("statuskeyswritten").toString() + "&results=" + statusConfig.get("statusresults").toString() + "&job=" + statusConfig.get("statusjob") + " HTTP/1.0\n\n";

				bw.write(content);
				bw.flush();

				BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String temp;
				StringBuffer sb = new StringBuffer();
				while((temp = br.readLine()) != null){
					sb.append(temp);
				}

//				System.out.println("HeartBeat hits... with content: " + content + " and reply: " + sb.toString());
				System.out.println("Heartbeats sends...");

				Thread.sleep(10000);
			}
			catch(IOException e){
				e.printStackTrace();
			}
			catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
