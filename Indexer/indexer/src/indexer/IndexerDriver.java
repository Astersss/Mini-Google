package indexer;

import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.map.MultithreadedMapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;




public class IndexerDriver {


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
		
		job.setJarByClass(IndexerDriver.class);
		job.setJobName("indexer");
		
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		MultithreadedMapper.setMapperClass(job, IndexerMapper.class);
		MultithreadedMapper.setNumberOfThreads(job, 10);

		job.setMapperClass(MultithreadedMapper.class);
		job.setReducerClass(IndexerReducer.class);
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		job.waitForCompletion(true);
		

	}

}
