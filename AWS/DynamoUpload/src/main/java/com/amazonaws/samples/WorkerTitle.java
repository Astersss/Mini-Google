package com.amazonaws.samples;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class WorkerTitle extends Thread{
	private BlockingQueue<ArrayList<String>> workerBlockingQueue;
	
	public WorkerTitle(BlockingQueue<ArrayList<String>> tempQueue){
		this.workerBlockingQueue = tempQueue;
	}

	public void run(){
		System.out.println("worker starts");
		while (true){
			ArrayList<String> temp = workerBlockingQueue.poll();
			if (temp == null) {
				System.out.println("it is null");
				continue;
				
			}
			//System.out.println("In worker, the tmp has size " + temp.size());
	
			ParseAndUpload.uploadToHash_Title(temp.get(1), temp.get(0)+"___");
			
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
