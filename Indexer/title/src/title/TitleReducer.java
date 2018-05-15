package title;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class TitleReducer extends Reducer<Text, Text, Text, Text> {
	protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Iterator<Text> iter = values.iterator();
		boolean isContinue = true;
		while(iter.hasNext() && isContinue) {
			String next = iter.next().toString();
			if(next != null && next.toString().length() != 0) {
				Text reducerText = new Text(next);
				context.write(key, reducerText);
				isContinue = false;
			}
		}
	}
}
