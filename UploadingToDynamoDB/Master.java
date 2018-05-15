import java.io.*;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

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
			String previousKey = "";
			String currentKey = "";
			HashMap<String, String> tempHashMap = new HashMap<>();

			String temp;
			while((temp = br.readLine()) != null && temp.length() != 0){
				String[] valueSet = temp.split("\\s+");

				if(valueSet.length != 3){
					continue;
				}
				else{
					currentKey = valueSet[0];

					if(currentKey.equals(previousKey)){
						tempHashMap.put(valueSet[1], valueSet[2]);
					}
					else{

						HashMap<String, HashMap<String, String>> mapToSubmit= new HashMap<>();
						mapToSubmit.put(previousKey, tempHashMap);
						this.masterBlockingQueue.offer(mapToSubmit);
						previousKey = currentKey;

						tempHashMap = new HashMap<>();
					}

				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
