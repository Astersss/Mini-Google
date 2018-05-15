package com.amazonaws.samples;
import java.io.*;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import com.amazonaws.services.simpleemail.model.ListReceiptRuleSetsRequest;


/**
 * Created by marcusma on 5/9/17.
 */
public class Master extends Thread{

	private BlockingQueue<HashMap<String, HashMap<String, String>>> masterBlockingQueue;

	//private File masterFile;
	private BufferedReader br;

	public Master(BlockingQueue<HashMap<String, HashMap<String, String>>> tempQueue, File tempFile){

		this.masterBlockingQueue = tempQueue;

		File masterFile = tempFile;

		try{
			this.br = new BufferedReader(new FileReader(tempFile));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}

	}

	public void run(){

		try{
			System.out.println("====== master starts ======");
			String previousKey = "";
			String currentKey = "";
			HashMap<String, String> tempHashMap = new HashMap<String, String>();

			String temp;
			
			while((temp = br.readLine()) != null && temp.length() != 0){
				
				
				String[] valueSet = temp.split("\\s+");

				if(valueSet.length != 3){
					continue;
				}
				else{
					currentKey = valueSet[0];
					//System.out.println("current key is " + currentKey);

					if(currentKey.equals(previousKey) && !previousKey.equals("")){
						tempHashMap.put(valueSet[1] + "___", valueSet[2]);
						
					} 
					else {
						
						if (previousKey.equals("")){
							previousKey = currentKey;
							tempHashMap.put(valueSet[1] + "___", valueSet[2]);
							continue;
						}

						HashMap<String, HashMap<String, String>> mapToSubmit= new HashMap<String, HashMap<String, String>>();
						
						mapToSubmit.put(previousKey, tempHashMap);
						//System.out.println("key is " + previousKey);
						//System.out.println("size is " + tempHashMap.size());
						//}
						
						//System.out.println("previous key is " + previousKey);
						//System.out.println("current key is " + currentKey);
						//System.out.println("size of temp hash map is " + tempHashMap.size());
						
						this.masterBlockingQueue.offer(mapToSubmit);
						previousKey = currentKey;

						tempHashMap = new HashMap<String, String>();
						tempHashMap.put(valueSet[1] + "___", valueSet[2]);
					}

				}
			}
			
			HashMap<String, HashMap<String, String>> mapToSubmit= new HashMap<String, HashMap<String, String>>();
			
			mapToSubmit.put(previousKey, tempHashMap);
			this.masterBlockingQueue.offer(mapToSubmit);
			
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
