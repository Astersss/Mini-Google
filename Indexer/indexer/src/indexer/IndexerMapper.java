package indexer;

import java.io.IOException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.stanford.nlp.simple.*;
import org.jsoup.Jsoup;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class IndexerMapper extends Mapper<LongWritable, Text, Text, Text> {
	

	private static Set<String> stopwords = new HashSet<String>();
	Text text = new Text();
	Text wordText = new Text();

	static {
		List<String> list = Arrays
				.asList(new String[] { "a", "able", "about", "across", "after", "all", "almost", "also",

						"aa", "aaa", "am", "ab", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by",
						"can", "cannot", "could", "dear", "did", "do", "does", "either", "else", "ever", "every", "for",
						"from", "get", "got", "had", "has", "have", "he", "her", "hers", "him", "his", "how", "however",
						"i", "if", "in", "into", "is", "it", "its", "just", "least", "let", "like", "likely", "may",
						"me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of", "off", "often", "on",
						"only", "or", "other", "our", "own", "rather", "said", "say", "says", "she", "should", "since",
						"so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this",
						"tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where",
						"which", "while", "who", "whom", "why", "will", "with", "would", "yet", "you", "your"});
		for (String w : list) {
			stopwords.add(w);
		}

	}

	@Override
	protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

		String[] strArr = value.toString().split(" ", 2);

		if (strArr.length == 2) {
			//System.out.println("logo: " + value.toString());

			String filename = strArr[0];
			String file = strArr[1];

			

			if (file != null && !file.isEmpty() && file.length() != 0) {
				Map<String, Integer> weights = new HashMap<String, Integer>();

				int maxOccur = parse(file, weights);
				if (maxOccur != -1) {
					for (String word : weights.keySet()) {
						double tf = 0.45 + 0.55 * weights.get(word) / maxOccur; // normalized
																				// tf
						String val = String.format("%.5f", tf);
						StringBuilder sb = new StringBuilder(filename);
						sb.append(" ").append(val);
						if(isWordValid(word)) {
						
							text.set(sb.toString());
							wordText.set(word);
							context.write(wordText, text);
						}
					}
					weights.clear();
				}

			}
		}

	}
	
	public boolean isWordValid(String word) {
		
		if(word.matches(".*\\d+.*")) return false;
		if(word.length() > 15) return false;
		if(word.contains(",") || word.contains(":") || word.contains(";") || word.contains("&")) return false;
		if(word.contains("/") || word.contains("-") || word.contains(".")) return false;
		return true;
	}

	public int parse(String file, Map<String, Integer> weights) {

		int NonEnglish = 3;
		Pattern pattern1 = Pattern.compile("^[a-zA-Z0-9]+[.@&-]*[a-zA-Z0-9]+");
		Pattern pattern2 = Pattern.compile("[a-zA-Z]+");

		org.jsoup.nodes.Document d = null;
		int maxOccur = 1;
		try {
			d = Jsoup.parse(file);

			d.select(":containsOwn(\u00a0").remove();
			Elements es = d.select("*");

			for (Element e : es) {
				String nodeName = e.nodeName();
				String text = e.ownText().trim();
				
				if (text != null && !text.isEmpty() && text.length() != 0) {

					Document tagContent = new Document(text);
					List<Sentence> sentences = tagContent.sentences();

					for (Sentence s : sentences) {
						
						long time1 = System.currentTimeMillis();
						List<String> words = s.lemmas();
						long time2 = System.currentTimeMillis();

						if (time2 - time1 > 25) {
							int size = words.size();
							int dirties = 0;
							Matcher m;
							for (String w : words) {

								m = pattern1.matcher(w);
								if (!m.matches()) {
									dirties++;
								}
							}
							if (size != 0 && dirties * 1.0 / size > 0.5) {
								NonEnglish--;

							}

							if (NonEnglish == 0) {
								weights.clear();
								return 1;
							}
						}

						Matcher m1, m2;
						for (String w : words) {

							w = w.trim();
							m1 = pattern1.matcher(w);
							m2 = pattern2.matcher(w);

							if (m1.matches()) {

								if (m2.find()) {

									if (!w.equalsIgnoreCase("-rsb-") && !w.equalsIgnoreCase("-lsb-")
											&& !w.equalsIgnoreCase("-lrb-") && !w.equalsIgnoreCase("-rrb-")
											&& !w.equalsIgnoreCase("-lcb-") && !w.equalsIgnoreCase("-rcb-")) {
										w = w.toLowerCase();

										if (!stopwords.contains(w)) {

											int weight = 1;
											if (nodeName.equalsIgnoreCase("title")) {

												weight = 1;
											}
											if (!weights.containsKey(w)) {

												weights.put(w, weight);
											} else {
												int occur = weights.get(w) + weight;
												weights.put(w, occur);
												maxOccur = Math.max(maxOccur, occur);
											}
										}
									}
								} else {

								}
							} else {

							}
						}
					}
				}
			}
		} catch (Exception e) {
			return -1;
		}
		return maxOccur;
	}

}
