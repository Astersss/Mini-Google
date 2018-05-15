package linkgraph;

import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class LinkgraphMapper extends Mapper<LongWritable, Text, Text, Text> {
	Text text = new Text();
	Text wordText = new Text();

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		if (value != null && value.toString().length() != 0) {
			String[] strArr = value.toString().split(" ", 2);

			if (strArr.length == 2) {
				String filename = strArr[0];
				String file = strArr[1];

				boolean isContinue = true;

				// String filePath = ((FileSplit)
				// context.getInputSplit()).getPath().getName();
				if (file != null && !file.isEmpty() && file.length() != 0) {
					org.jsoup.nodes.Document d = null;
					try {

						d = Jsoup.parse(file);
					} catch (Exception e) {
						isContinue = false;
					}
					if (isContinue && d != null) {
						Elements es = d.select("[href]");
						int size = es.size();
						for (int i = 0; i < size; i++) {
							String link = es.get(i).attr("href").toString();

							if (link.startsWith("http") || link.startsWith("https")) {
								String hash = DigestUtils.sha256Hex(link);
								wordText.set(filename);
								text.set(hash);
								context.write(wordText, text);

							}
						}
					}
				}
			}
		}
	}
}
