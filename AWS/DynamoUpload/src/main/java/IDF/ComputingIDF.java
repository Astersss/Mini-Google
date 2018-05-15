package IDF;
import java.io.*;
import java.util.HashMap;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by marcusma on 5/9/17.
 */
public class ComputingIDF{

	

	static BasicAWSCredentials credentials = new BasicAWSCredentials("KEY", "KEY");
	static Region usWest2 = Region.getRegion(Regions.US_EAST_1);


	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSCredentialsProvider() {

		public void refresh() {

		}

		public AWSCredentials getCredentials() {
			return credentials;
		}
	}).build();

	//static final String hash_url = "hash_url";
	static final String hash_url = "hash_url_backup";
	static final String key_hash_url = "HASH";
	static final String attr_hash_url = "URL";

	//static final String indexer = "indexer";
	static final String indexer = "indexer_backup";
	static final String key_indexer = "WORD";
	static final String attr_indexer = "MAP";

	//static final String hash_pagerank = "hash_pagerank";
	static final String hash_pagerank = "hash_pagerank_backup";
	static final String key_hash_pagerank = "HASH";
	static final String attr_hash_pagerank = "PAGERANK";

	//static final String word_idf = "word_idf";
	static final String word_idf = "word_idf";
	static final String key_word_idf = "WORD";
	static final String attr_word_idf  = "IDF";

	//static final String hash_title = "hash_title";
	static final String hash_title = "hash_title_backup";
	static final String key_hash_title = "HASH";
	static final String attr_hash_title = "TITLE";



	static DynamoDB dynamoDB = new DynamoDB(client);

	static Table table_indexer = dynamoDB.getTable(indexer);
	static Table table_hash_url = dynamoDB.getTable(hash_url);
	static Table table_hash_pagerank = dynamoDB.getTable(hash_pagerank);
	static Table table_word_idf = dynamoDB.getTable(word_idf);
	static Table table_hash_title = dynamoDB.getTable(hash_title);

	@SuppressWarnings("deprecation")
	static AmazonS3 s3 = new AmazonS3Client(credentials);

	@SuppressWarnings("unused")
	public static void uploadToWord_IDF(String idf, String word_key) {

		Item item = new Item()
				.with(key_word_idf, word_key)
				.with(attr_word_idf, idf);

		PutItemOutcome outcome = table_word_idf.putItem(item);
	}
	
	public static void main(String[] args){

		File file = new File(args[0]); // input index table;

		HashMap<String, String> idfMap = new HashMap<String, String>();

		try{

			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp;
			int count = 0;
			String currentWord;
			String pastWord = null;

			while((temp = br.readLine()) != null){
				
				String[] firstSet = temp.split("\\s+", 3);

				if(firstSet.length != 3){
					continue;
				}
				else{
					currentWord = firstSet[0];
					//System.out.println("current word is " + currentWord);

					if(pastWord == null || currentWord.equals(pastWord)){
						count++;
						pastWord = currentWord;
					}
					else{
						idfMap.put(currentWord, Double.toString(Math.log(5099/count)));
						System.out.println("current word is " + currentWord);
						pastWord = currentWord;
						count = 1;
					}
				}
			}
			
			System.out.println("idf map size is " + idfMap.size());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(String key : idfMap.keySet()){
				System.out.println("key: " + key);
				System.out.println("value: " + idfMap.get(key));
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				uploadToWord_IDF(idfMap.get(key), key);
			}
			System.out.println("idf done!");

		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
