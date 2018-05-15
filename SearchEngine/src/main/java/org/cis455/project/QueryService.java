package org.cis455.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



//import edu.stanford.nlp.simple.*;

public class QueryService {
	static List<String> bigram = Arrays.asList("chocolate", "septa", "new york", "donald trump", "university of pennsylvania", "benjamin franklin", "north korea", "computer science");
	static int ret = 20;
//	static List<String> stopwords = Arrays.asList("a", "able", "about",
//			"across", "after", "all", "almost", "also", "am", "among", "an",
//			"and", "any", "are", "as", "at", "be", "because", "been", "but",
//			"by", "can", "cannot", "could", "dear", "did", "do", "does",
//			"either", "else", "ever", "every", "for", "from", "get", "got",
//			"had", "has", "have", "he", "her", "hers", "him", "his", "how",
//			"however", "i", "if", "in", "into", "is", "it", "its", "just",
//			"least", "let", "like", "likely", "may", "me", "might", "most",
//			"must", "my", "neither", "no", "nor", "not", "of", "off", "often",
//			"on", "only", "or", "other", "our", "own", "rather", "said", "say",
//			"says", "she", "should", "since", "so", "some", "than", "that",
//			"the", "their", "them", "then", "there", "these", "they", "this",
//			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were",
//			"what", "when", "where", "which", "while", "who", "whom", "why",
//			"will", "with", "would", "yet", "you", "your");
	
	Map<String,Double> idfMap = new HashMap<String,Double>();
	boolean incache = false;
	static int N = 276000;
	
	public List<WebContent> getSampleWebPages(String query){
		query = query.toLowerCase().trim();
		List<WebContent> pageList = new ArrayList<WebContent>();
		//test page
		for(int i = 0; i < 50; i++){
			WebContent web = new WebContent();
			web.setUrl("https://www.google.com");
			web.setTitle("Google");
			web.setSummary("Search the world's information, "
					+ "including webpages, images, videos and more. "
					+ "Google has many special features to help you find exactly what you're looking..");
			pageList.add(web);
		}
		return pageList;
	}

	public List<WebContent> getMatchedWeb(String query){
		query = query.toLowerCase().trim();
		List<WebContent> pageList = new ArrayList<WebContent>();
		//long time1 = System.currentTimeMillis();
		List<String> wordList = parseQuery(query);
		//long time2 = System.currentTimeMillis();
		
		//System.out.println("parse query takes: " + (time2 - time1));
		
		//long time3 = System.currentTimeMillis();
		Map<String, Double> queryWeight = getQueryVector(wordList);// word : count*idf
		//long time4 = System.currentTimeMillis();
		//System.out.println("get query weight takes: " + (time4 - time3));
		
		//get url list and map of each corresponding tfij
		List<String> urlHashList = new ArrayList<String>(); 
		
//		Map<String, String> titleMap = new HashMap<String,String>();
		
		Map<String, Double> contentScore = new HashMap<String, Double>(); //hash : score
		Map<String, Double> titleScore = new HashMap<String, Double>();  
		Map<Double, String> finalScoreTreeMap = new TreeMap<Double,String>();
//		Map<String, Double> pageRankScore = new HashMap<String, Double>();
		HashMap<String, Double> finalScore = new HashMap<String, Double>();
		System.out.println("start content score..");
		
		for(String word : wordList){
			//System.out.println(word);
//			double idf = idfMap.get(word);
		
			Map<String,String> indexerMap = DynamoAccessor.downloadFromIndexer(word); //word :< urlhash : tfij>
			if(indexerMap == null){
				continue;
			}
			//calculate idf
			int n = indexerMap.keySet().size();
			double idf = Math.log(N/n);
			idfMap.put(word, idf);
			
			
			//System.out.println("**********Get Inverted index of word :"+word);
			int i = 0;
			for(String hash : indexerMap.keySet()){
				
				if((i++) > 100){
					break;
				}

				double tf = Double.parseDouble(indexerMap.get(hash));
				
//				queryWeight.put(word, queryWeight.get(word) * idf);

				double addedWeight = tf*idf * queryWeight.get(word) * idf; //tfij *idf* Wiq
				
				if(contentScore.containsKey(hash)){
					contentScore.put(hash, contentScore.get(hash) + addedWeight);
				}else{
					contentScore.put(hash, addedWeight);
				}
				
			}
		}
		//long time5 = System.currentTimeMillis();
		//System.out.println("get content score takes: " + (time5 - time4));
		System.out.println("finish content score");
		
		if(contentScore.isEmpty()){
			System.out.println("going to return page list");
			return pageList;
		}
		
		urlHashList = new ArrayList<String>(contentScore.keySet());
//		System.out.println("finish content score, goint to calculate title score...");
//		titleScore = getTitleScore(urlHashList,wordList,queryWeight);
//		System.out.println("finish get title score, going to calculate pagerank score...");
		
		
//		for(String urlhash : urlHashList){
//			//System.out.println("hah");
//			//System.out.println((new Date()).toGMTString());
//			String prScore = DynamoAccessor.downloadFromHash_PageRank(urlhash);
//			//System.out.println((new Date()).toGMTString());
//			if(prScore != null){
//				pageRankScore.put(urlhash, Double.parseDouble(prScore));
//			}
//		}
		System.out.println("calculate total score...");

		for(String urlhash : urlHashList){
			
			double content_score = contentScore.get(urlhash);
			double title_score = 0;
			double pagerank_score = 0;
			
			String prScore = DynamoAccessor.downloadFromHash_PageRank(urlhash);
			//System.out.println((new Date()).toGMTString());
			if(prScore != null){
				pagerank_score = Double.parseDouble(prScore);
			}
		
			String title = DynamoAccessor.downloadFromHash_Title(urlhash);
			if(title != null){
				for(String word : wordList){
					if(title.contains(word)){
						title_score += queryWeight.get(word);
					}
				}
			}
			
//			double total_score = content_score + title_score + pagerank_score;
//			double total_score = content_score + title_score;
			double total_score = content_score*0.2 + title_score*0.8;

//			double total_score = title_score;
			finalScore.put(urlhash, total_score);
//			finalScoreTreeMap.put(total_score, urlhash);
		}
		
//		System.out.println("*********final score: "+finalScore);
		
		HashMap<String,Double> sortedMap = sortByValues(finalScore);
//		System.out.println(sortedMap);
		
		System.out.println("sort hash list finish");
		
		int i = 0;
		for(String hash : sortedMap.keySet()){
			if (i > ret) break;
			
			System.out.println("NO "+i +" score:"+finalScore.get(hash));
			
			String url = DynamoAccessor.downloadFromHash_Url(hash);
			if(url == null) {
				System.out.println("*************url of hash "+hash+" not found");
				continue;
			}
			String title = DynamoAccessor.downloadFromHash_Title(hash);
			if(title == null) {
				System.out.println("***********title of hash "+hash+"not found");
				title = "";
			}
			i++;
			
			WebContent web = new WebContent();
			web.setTitle(title);
			web.setUrl(url);
			web.setSummary("here is summary");
			pageList.add(web);
//			System.out.println("add hash to list:"+hash);
		}
		
		
		
//		if (incache) {
//			Collections.shuffle(pageList);
//		}
		
		return pageList;
	}
	
	private static HashMap sortByValues(HashMap map) { 
	       List list = new LinkedList(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	                  .compareTo(((Map.Entry) (o1)).getValue());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	  }
	
	
	public Map<String,Double> getTitleScore(List<String> urlHashList,
													List<String> wordList,
													Map<String, Double> queryWeight){
		Map<String,Double> titleScore = new HashMap<String,Double>();
		double maxScore = 0;
		for(String urlhash : urlHashList){
			//calculate title score for each urlhash
			String title = DynamoAccessor.downloadFromHash_Title(urlhash);
			if(title == null){
				continue;
			}
			//calculate score for each title 
			List<String> titleWords = parseQuery(title);
			double score = 0;

			
			for(String word : wordList){
				if(titleWords.contains(word)){
					score += queryWeight.get(word) * idfMap.get(word);
				}
			}
				
			//System.out.println("put url "+urlhash +" title score:"+score);
			titleScore.put(urlhash, score);
//			if(maxScore < score){
//				maxScore = score;
//			}
		}
//		//normalize
//		for(String urlhash : titleScore.keySet()){
//			titleScore.put(urlhash, titleScore.get(urlhash)/maxScore);
//			//System.out.println("final score for url "+urlhash +" title score:"+titleScore.get(urlhash));
//		}
		return titleScore;
	}
	
	
	public Map<String, Double> getQueryVector(List<String> wordList){
		Map<String, Double> queryVector = new HashMap<String,Double>(); // word : count*idf
		//calculate the query vector
		for(String word : wordList){
			if(queryVector.containsKey(word)){
				queryVector.put(word,queryVector.get(word)+1.0);
			}else{
				queryVector.put(word, 1.0);
			}
		}
		
		for(String word : queryVector.keySet()){
			double count = queryVector.get(word);
			queryVector.put(word,count);
			//System.out.println("query word:"+word+ " count:"+count+" idf:"+idf+" query weight:"+queryVector.get(word));		
		}
		return queryVector;
	}
	
	
	public boolean isBigram(String query) {
		List<String> tmp = parseQuery(query);
		if(tmp.size() == 2) {
			String ret = tmp.get(0).trim() + " " + tmp.get(1).trim();
			if(this.bigram.contains(ret)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isSingleWord(String query) {
		List<String> tmp = parseQuery(query);
		if(tmp.size() == 1) {
			
				return true;
			
		}
		return false;
	}
	
	public static List<String> parseQuery(String query){

		List<String> parsedWords = new ArrayList<String>();
		// change query to lower case
		query = query.toLowerCase();
		// apply stemmer/lemma to sentence 
//		Sentence sent = new Sentence(query);
//		List<String> wordList = sent.lemmas();
		StringTokenizer st = new StringTokenizer(query," ");
		
		while(st.hasMoreTokens()){
			String word = st.nextToken();
			// check if the word is non alphabetic
			String regex = "[^A-Za-z0-9]+"; //exclude character
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(word);
			if(matcher.find()){//token contains special character
				//System.out.println("QueryService exclude special character "+word);
			}else{
				parsedWords.add(word);
			}
		}
		return parsedWords;
	}
	
	public String getStemmedQuery(List<String> list) {
		StringBuilder sb = new StringBuilder();
		for(String str: list) {
			sb.append(str).append(" ");
		}
		return sb.toString().trim();
	}
	

}
