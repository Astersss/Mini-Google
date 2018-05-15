package title;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TitleMapper extends Mapper<LongWritable, Text, Text, Text> {
	Text valueText = new Text();
	Text wordText = new Text();

	@Override
	protected void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {
		if (value != null) {
			String[] strArr = value.toString().split(" ", 2);
			if (strArr.length == 2) {
				String filename = strArr[0];
				String file = strArr[1];
				boolean isContinue = true;

				if (file.length() != 0 && file != null && !file.isEmpty()) {
					org.jsoup.nodes.Document d = null;
					try {
						d = Jsoup.parse(file);
					} catch (Exception e) {
						isContinue = false;
					}
					if (isContinue) {
						String title = d.title();
						if (title != null) {
							wordText.set(filename);
							valueText.set(title);
							context.write(wordText, valueText);
						}
					}
				}
			}
		}
	}
}
