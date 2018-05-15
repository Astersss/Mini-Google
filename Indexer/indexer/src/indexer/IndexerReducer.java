package indexer;

import java.io.IOException;

import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class IndexerReducer extends Reducer<Text, Text, Text, Text>{
	Text reducerText = new Text();
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Iterator<Text> iter = values.iterator();
		
		
		while(iter.hasNext()) {
			
			reducerText.set(iter.next());
			context.write(key, reducerText);
		}
	}
}
