package edu.upenn.cis455.mapreduce.master;

import edu.upenn.cis455.mapreduce.Context;

import java.util.HashMap;

/**
 * Created by marcusma on 4/9/17.
 */
public class ConfigContext extends HashMap implements Context{

	public ConfigContext(){
		super();
	}

	@Override
	public void write(String key, String value){
		super.put(key, value);
	}
}
