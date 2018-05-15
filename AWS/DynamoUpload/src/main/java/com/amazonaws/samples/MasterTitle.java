package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class MasterTitle extends Thread{
	private BlockingQueue<ArrayList<String>> masterBlockingQueue;

	//private File masterFile;
	private BufferedReader br;

	public MasterTitle(BlockingQueue<ArrayList<String>> tempQueue, File tempFile){

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
		try{
			System.out.println("====== master starts ======");
			String temp;
			
			
			while((temp = br.readLine()) != null && temp.length() != 0){
				
				String[] valueSet = temp.split("\\s+", 2);

				
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add(valueSet[0]); arrayList.add(valueSet[1]);
				this.masterBlockingQueue.offer(arrayList);
				//System.out.println(j++);
				
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
