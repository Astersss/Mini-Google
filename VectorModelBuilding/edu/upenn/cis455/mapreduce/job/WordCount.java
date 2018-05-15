package edu.upenn.cis455.mapreduce.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.upenn.cis455.mapreduce.Context;
import edu.upenn.cis455.mapreduce.Job;
import edu.upenn.cis455.mapreduce.master.ConfigContext;

public class WordCount implements Job {

	public void map(String key, String value, Context context){
    // (1, word), (2, word)
		if(((HashMap<Object, Object>)context).containsKey(value)){
			int tempNum = (Integer)(((HashMap<Object, Object>)context).get(value));
			((HashMap<Object, Object>)context).put(value, tempNum+1);
		}
		else{
			((HashMap<Object, Object>)context).put(value, 1);
		}
  	}
  
  	public void reduce(String key, Iterator<String> values, Context context){
    // Your reduce function for WordCount goes here
		if(((ConfigContext)context).containsKey(key)){
			while(values.hasNext()){
				((List<String>)(((ConfigContext)context).get(key))).add(values.next());
			}
		}
		else{
			ArrayList<String> tempArrayList = new ArrayList<>();
			while(values.hasNext()){
				tempArrayList.add(values.next());
			}
			((ConfigContext)context).put(key, tempArrayList);
		}

  	}
  
}
