package linkgraph;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class LinkgraphReducer extends Reducer<Text, Text, Text, Text> {
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Iterator<Text> iter = values.iterator();
		int count = 0;
		String str = "";
		while(iter.hasNext() && count <= 10) {
			count++;
			str = str + " " + iter.next().toString();
			
		}
		Text reducerText = new Text(str);
		context.write(key, reducerText);
	}
}
