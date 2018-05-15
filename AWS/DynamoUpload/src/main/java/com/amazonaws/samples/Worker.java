package com.amazonaws.samples;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Created by marcusma on 5/9/17.
 */
public class Worker extends Thread{

	private BlockingQueue<HashMap<String, HashMap<String, String>>> workerBlockingQueue;

	public Worker(BlockingQueue<HashMap<String, HashMap<String, String>>> tempQueue){
		this.workerBlockingQueue = tempQueue;
	}

	public void run(){
		while (true){
			HashMap<String, HashMap<String, String>> temp = workerBlockingQueue.poll();
			if (temp == null) {
				
				continue;
			}
			//System.out.println("In worker, the tmp has size " + temp.size());
			for(String keyToSubmit : temp.keySet()){
				if (keyToSubmit.equals("")) continue;
				ParseAndUpload.uploadToIndexer(temp.get(keyToSubmit), keyToSubmit);
				System.out.println("key: " + keyToSubmit);
				break;
			}
			ParseAndUpload.addCount();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//System.out.println("========worker ends=========");
	}
}
