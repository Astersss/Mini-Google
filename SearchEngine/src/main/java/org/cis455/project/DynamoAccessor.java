package org.cis455.project;

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
//import edu.stanford.nlp.simple.*;

public class DynamoAccessor {
	static BasicAWSCredentials credentials = new BasicAWSCredentials("KEY", "KEY");
	static Region usWest2 = Region.getRegion(Regions.US_EAST_1);
	static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSCredentialsProvider() {
		
		public void refresh() {
			// TODO Auto-generated method stub
			
		}
		
		public AWSCredentials getCredentials() {
			// TODO Auto-generated method stub
			return credentials;
		}
	}).build();
	
	static final String hash_url = "hash_url";
	static final String key_hash_url = "HASH";
	static final String attr_hash_url = "URL";
	
	static final String indexer = "indexer";
	static final String key_indexer = "WORD";
	static final String attr_indexer = "MAP";
	
	static final String hash_pagerank = "hash_pagerank";
	static final String key_hash_pagerank = "HASH";
	static final String attr_hash_pagerank = "PAGERANK";
	
	static final String word_idf = "word_idf";
	static final String key_word_idf = "WORD";
	static final String attr_word_idf  = "IDF";
	
	static final String hash_title = "hash_title";
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
	public static void uploadToIndexer(HashMap<String, String> map, String key) {
		
		Item item = new Item()
				.with(key_indexer, key)
			    .with(attr_indexer, map);
			
		PutItemOutcome outcome = table_indexer.putItem(item);
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, String> downloadFromIndexer(String query) {
		
		Item item = table_indexer.getItem(key_indexer, query);	
		if (item == null) return null;
		return (HashMap<String, String>)item.asMap().get(attr_indexer);
	}
	
	
	@SuppressWarnings("unused")
	public static void uploadToHash_Url(String url_value, String hash_key) {
		
		Item item = new Item()
				.with(key_hash_url, hash_key)
			    .with(attr_hash_url, url_value);
			
		PutItemOutcome outcome = table_hash_url.putItem(item);
	}
	
	
	public static String downloadFromHash_Url(String hash_key) {
		
		Item item = table_hash_url.getItem(key_hash_url, hash_key);	
		if (item == null) return null;
		return item.getString(attr_hash_url);
	}
	
	@SuppressWarnings("unused")
	public static void uploadToHash_PageRank(String pr, String hash_key) {
	
		Item item = new Item()
				.with(key_hash_pagerank, hash_key)
			    .with(attr_hash_pagerank, pr);
			
		PutItemOutcome outcome = table_hash_pagerank.putItem(item);
	}
	
	
	public static String downloadFromHash_PageRank(String hash_key) {

		Item item = table_hash_pagerank.getItem(key_hash_pagerank, hash_key);	
		if (item == null) return null;
		return item.getString(attr_hash_pagerank);
	}
	
	@SuppressWarnings("unused")
	public static void uploadToHash_Title(String title, String hash_key) {

		Item item = new Item()
				.with(key_hash_title, hash_key)
			    .with(attr_hash_title, title);
			
		PutItemOutcome outcome = table_hash_title.putItem(item);
	}
	
	
	public static String downloadFromHash_Title(String hash_key) {
		Item item = table_hash_title.getItem(key_hash_title, hash_key);	
		if (item == null) return null;
		return item.getString(attr_hash_title);
	}
	
	@SuppressWarnings("unused")
	public static void uploadToWord_IDF(String idf, String word_key) {
	
		Item item = new Item()
				.with(key_word_idf, word_key)
			    .with(attr_word_idf, idf);
			
		PutItemOutcome outcome = table_word_idf.putItem(item);
	}
	
	
	public static String downloadFromWord_IDF(String word_key) {
	
		Item item = table_word_idf.getItem(key_word_idf, word_key);	
		if (item == null) return null;
		return item.getString(attr_word_idf);
	}
	
	
	public static void main(String[] args) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("a", "1"); map.put("b", "2"); map.put("c", "3");
		uploadToIndexer(map, "1234");
		
		
		
		HashMap<String, String> map1 = downloadFromIndexer("1234");
		
		if (map1 == null) {
			System.out.println("hsaldkfj");
			return;
		}
		
		for (String key : map1.keySet()){
			System.out.println(key);
			System.out.println(map1.get(key));
		}
		
		System.out.println(DynamoAccessor.downloadFromHash_Url("09be5b1afa1df63333a7e26ef8e41bc3385d59e3b75a6fd13dc7ec28abf4e6bc"));
		
	}
}
