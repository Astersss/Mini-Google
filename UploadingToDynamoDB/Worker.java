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
		HashMap<String, HashMap<String, String>> temp = workerBlockingQueue.poll();

		for(String keyToSubmit : temp.keySet()){
			ParseAndUpload.uploadToIndexer(temp.get(keyToSubmit), keyToSubmit);
			System.out.println("key: " + keyToSubmit);
			break;
		}
	}
}
