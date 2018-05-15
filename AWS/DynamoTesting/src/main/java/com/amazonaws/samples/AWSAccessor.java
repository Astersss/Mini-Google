package com.amazonaws.samples;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.iterable.S3Objects;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import org.apache.commons.codec.digest.DigestUtils;




public class AWSAccessor {
	static BasicAWSCredentials credentials = new BasicAWSCredentials("KEY", "KEY");
	
	
	@SuppressWarnings("deprecation")
	static AmazonS3 s3 = new AmazonS3Client(credentials);	
	@SuppressWarnings("deprecation")
	static AmazonS3 s3_del = new AmazonS3Client(credentials);
	
	//static Logger log = Logger.getLogger(AWSAccessor.class);
	
	static void uploadFileToS3(Webpage webpage){
		
        
		try{
			ByteArrayInputStream bstream = new ByteArrayInputStream(webpage.content);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(webpage.contentLength);
			meta.setContentType(webpage.contentType);
			meta.setLastModified(webpage.lastModifiedDate);

			meta.setContentLanguage(webpage.url);
			
			String hash = DigestUtils.sha256Hex(webpage.url);
			
			s3.putObject(new PutObjectRequest("mazhiyucrawl", hash, bstream, meta));

			
		} catch (AmazonServiceException ase) {
			System.out.println("AmazonServiceException");
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException");
        }
		
	}
	
static void uploadFileToS3(Webpage webpage, String hash){
		
		try{
			ByteArrayInputStream bstream = new ByteArrayInputStream(webpage.content);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(webpage.contentLength);
			meta.setContentType(webpage.contentType);
			meta.setLastModified(webpage.lastModifiedDate);
			meta.setContentLanguage(webpage.url);

			s3.putObject(new PutObjectRequest("403forbidden", hash, bstream, meta));

			
		} catch (AmazonServiceException ase) {
			System.out.println("AmazonServiceException");
        } catch (AmazonClientException ace) {
        	System.out.println("AmazonClientException");
        }
		
	}
	
	
	
	static Webpage downloadFileFromS3(String url) throws IOException{
        
		try {
			String key = url;
			
			S3Object s3Object = null;
			try {
				s3Object = s3_del.getObject(new GetObjectRequest("404notfound", key));
			} catch (AmazonS3Exception e) {
				System.out.println("AmazonS3Exception");
				System.out.println(key);
				return null;
			}
			
			InputStream in = s3Object.getObjectContent();
			
			ObjectMetadata meta = s3Object.getObjectMetadata();
			
			InputStreamReader streamReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while( (line = bufferedReader.readLine()) != null ) {
				sb.append(line + "\n");
			}
			
			
			long contentLength = meta.getContentLength();
			String contentType = meta.getContentType();
			String language = meta.getContentLanguage();
			Date date = meta.getLastModified();
			
			try {
				Webpage webpage = new Webpage(date, url, contentType, sb.toString());
				webpage.contentLength = contentLength;
				webpage.language = language;
				return webpage;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
            return null;
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException");
        }
		return null;
		
	}
	
	
	static Webpage testDownloadFileFromS3(String url) throws IOException{
		
		try {
			String key = url;
			S3Object s3Object = s3.getObject(new GetObjectRequest("finalcrawl", key));
			InputStream in = s3Object.getObjectContent();
			
			ObjectMetadata meta = s3Object.getObjectMetadata();
			
			InputStreamReader streamReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(streamReader);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while( (line = bufferedReader.readLine()) != null ) {
				sb.append(line);
			}
			
			
			long contentLength = meta.getContentLength();
			String contentType = meta.getContentType();
			Date date = meta.getLastModified();
			
			try {
				Webpage webpage = new Webpage(date, url, contentType, sb.toString());
				webpage.contentLength = contentLength;
				webpage.url = meta.getContentLanguage();

				return webpage;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (AmazonServiceException ase) {
			System.out.println("AmazonServiceException");
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException");
        } 
		return null;
		
	}
	
	
	public static void s3iterator(){
		
		setRegion();
        int i = 0;
        int j = 0;
        for (S3ObjectSummary summary : S3Objects.withPrefix(s3_del, "404notfound", "") ) {
        	
        	
        	String key = summary.getKey();
        	//System.out.println("the key is " + key);
        	if (i % 200 == 0){
        		System.out.println(i + "th key is " + key);
        		System.out.println(j + " keys are empty");
        		Date date2 = new Date();
        		System.out.println(date2.toGMTString());
        		j = 0;
        	}

        	i++;
        	try {
				Webpage webpage = downloadFileFromS3(key);
				if (webpage == null) {
					j++;
					continue;
				}
				
				String hash = DigestUtils.sha256Hex(webpage.url);

				uploadFileToS3(webpage, hash);
				DeleteObject(key);
				
			} catch (IOException e) {
				e.printStackTrace();
			};
        }
	}
	
	public static void getObjectCounts(){
		int i = 0;
		for (S3ObjectSummary summary : S3Objects.withPrefix(s3, "jingxuanset", "") ) {
			if (i % 1000 == 0) System.out.println(i);
	        i++;
		}
		System.out.println("Final Count is " + i);
	}
	
	public static void getObjectCounts(String bucketName){
		int i = 0;
		for (S3ObjectSummary summary : S3Objects.withPrefix(s3, bucketName, "") ) {
			if (i % 1000 == 0) System.out.println(i);
	        i++;
		}
		System.out.println("Final Count is " + i);
		System.out.println((new Date()).toGMTString());
	}
	
	public static void setRegion(){
		Region usWest2 = Region.getRegion(Regions.US_EAST_2);
        s3_del.setRegion(usWest2);
	}
	
	public static void DeleteObject(String key){
		try {
			s3_del.deleteObject("404notfound", key);
        } catch (AmazonServiceException ase) {
            System.out.println("AmazonServiceException");
        } catch (AmazonClientException ace) {
            System.out.println("AmazonClientException");
        }
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException{
		
		//s3iterator();
		
        getObjectCounts("jingxuanset");
        
	}
}
