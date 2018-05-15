package com.amazonaws.samples;

import java.io.IOException;
import java.util.Date;

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
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/*
THIS CLASS IS CREATED BY KEJIN FAN TO READ A TXT FILE AND UPLOAD
HASH TO URL INFO TO DYNAMO DB.
*/


public class HashUrlConverter {
	
	static BasicAWSCredentials credentials = new BasicAWSCredentials("KEY", "KEY");
	
	
	@SuppressWarnings("deprecation")
	static AmazonS3 s3 = new AmazonS3Client(credentials);
	static String bucketName = "finalcrawl";
	
	static final String hash_url = "hash_url";
	static final String key_hash_url = "HASH";
	static final String attr_hash_url = "URL";
	
	
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
	
	static DynamoDB dynamoDB = new DynamoDB(client);
	static Table table_hash_url = dynamoDB.getTable(hash_url);
	
	
	@SuppressWarnings("unused")
	public static void uploadToHash_Url(String url_value, String hash_key) {
		
		Item item = new Item()
				.with(key_hash_url, hash_key)
			    .with(attr_hash_url, url_value);
			
		PutItemOutcome outcome = table_hash_url.putItem(item);
	}
	
	public static void main(String[] args){
		
		
		
		int i = 0;
		
        for (S3ObjectSummary summary : S3Objects.withPrefix(s3, bucketName, "") ) {
        	i++;
        	
        	String key = summary.getKey();
        	
        	if (i % 100 == 0){
        		System.out.println(i + "th key is " + key);
        		//Date date2 = new Date();
        		//System.out.println(date2.toGMTString());
        	}

        	try {
				Webpage webpage = AWSAccessor.testDownloadFileFromS3(key);
				uploadToHash_Url(webpage.url, key + "___");
				if (i % 100 == 0){
					System.out.println(webpage.url);
				}
			} catch (IOException e1) {
				
				e1.printStackTrace();
			} catch (Exception e) {
				System.out.println("exception");
			}
        	
        }
	}
}
