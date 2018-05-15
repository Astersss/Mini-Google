package org.cis455.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

//import com.sun.javafx.collections.MappingChange.Map;

public class SpellChecker {
	
	public static String dictionaryfile;
    public static ArrayList <String> wordlist = new ArrayList<String>();
    public HashMap <String, Integer> distanceMap = new HashMap<String, Integer>();
    
    public SpellChecker(String filename){
    	this.dictionaryfile = filename;
    	try {
        	loadWordList(dictionaryfile);
        } catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
    }
    
    @SuppressWarnings("resource")
	public static void loadWordList(String filename) throws IOException{
        String line;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        while((line = br.readLine()) != null){
            if(line.length()>2 && line.length()<26)
                wordlist.add(line);
        }
    }
    
    public String suggestWord(String inputWord){
        /**
         * Prints a list of words that can replace the city name in a 
         * sorted list, along with their edit distances (difference in name)
         */
        int i;
        for(String s : wordlist){
            i = computeDistance(s, inputWord);
            if(i<3){
                // Adjust i. The lesser, the more precise number of options
            	distanceMap.put(s, i);
            }
        }

        // Sorting to get the best cities in the top
        // Don't play with this part
        List<Entry<String, Integer>> entries = new ArrayList<Entry<String, Integer>>(distanceMap.entrySet());
        Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
                return e1.getValue().compareTo(e2.getValue());
            }
        });
        
        return entries.get(0).getKey();
     }

	
	 private static int computeDistance(String s1, String s2) {
	      /**
	       * Returns the edit distance needed to convert string s1 to s2
	       * If returns 0, the strings are same
	       * If returns 1, that means either a character is added, removed or replaced
	       */
	    s1 = s1.toLowerCase();
	    s2 = s2.toLowerCase();
	    int[] costs = new int[s2.length() + 1];
	    for (int i = 0; i <= s1.length(); i++) {
	      int lastValue = i;
	      for (int j = 0; j <= s2.length(); j++) {
	        if (i == 0)
	          costs[j] = j;
	        else {
	          if (j > 0) {
	            int newValue = costs[j - 1];
	            if (s1.charAt(i - 1) != s2.charAt(j - 1))
	              newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
	            costs[j - 1] = lastValue;
	            lastValue = newValue;
	          }
	        }
	      }
	      if (i > 0)
	        costs[s2.length()] = lastValue;
	    }
	    return costs[s2.length()];
	}
	 
	public static void main(String[] args){
		SpellChecker checker = 
				new SpellChecker("/Users/yimengxu/Documents/workspace/cis555_SearchEngine/store/wordList.txt");
		String test = "commuter";
		System.out.println(checker.suggestWord(test));
		
	}
    
}
