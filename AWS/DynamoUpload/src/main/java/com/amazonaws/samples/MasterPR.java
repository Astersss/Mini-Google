package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class MasterPR extends Thread {
	private BlockingQueue<ArrayList<String>> masterBlockingQueue;

	//private File masterFile;
	private BufferedReader br;

	public MasterPR(BlockingQueue<ArrayList<String>> tempQueue, File tempFile){

		this.masterBlockingQueue = tempQueue;
		
		

		try{
			this.br = new BufferedReader(new FileReader(tempFile));
			System.out.println("br done");
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		int j = 0;
		try{
			System.out.println("====== master starts ======");
			String temp;
			
			while((temp = br.readLine()) != null && temp.length() != 0){
				String[] valueSet = temp.split(",");
				if (valueSet.length != 2) continue;
				
				ArrayList<String> arrayList = new ArrayList<String>();
				try {
					arrayList.add(valueSet[0].substring(1)); arrayList.add(valueSet[1].substring(0, 6));
				} catch (Exception e) {
					continue;
				}
				
				this.masterBlockingQueue.offer(arrayList);
				System.out.println(j++);
				
			}
			
			
			System.out.println("====== master ENDs ======");
			
		}
		catch(IOException e){
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
