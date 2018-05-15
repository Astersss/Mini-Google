package linkgraph;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.MultithreadedMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;



public class LinkgraphDriver {
	public static void main(String[] args) throws Exception {
		String input = args[0];
		String output = args[1];
		
		Job job = null;

		try { 

			job = new Job();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		job.setJarByClass(LinkgraphDriver.class);
		job.setJobName("indexer");
		//job.setInputFormatClass(MyFileInputFormat.class);
		
		
		MultithreadedMapper.setMapperClass(job, LinkgraphMapper.class);
		MultithreadedMapper.setNumberOfThreads(job, 10);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(MultithreadedMapper.class);
		job.setReducerClass(LinkgraphReducer.class);
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		job.waitForCompletion(true);
		

	}
}
