package org.cis455.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cache {
	
	static HashMap<String, List<WebContent>> map = initmap();;
	
	
	public static HashMap<String, List<WebContent>> initmap() {
		HashMap<String, List<WebContent>> map = new HashMap<String, List<WebContent>>();
		map.put("benjamin franklin", addwebcontentbenjamin());
		map.put("chocolate", addwebcontentchoco());
		map.put("computer science", addwebcontentcomputer());
		map.put("new york", addwebcontentnewyork());
		map.put("septa", addwebcontentsepta());
		map.put("donald trump", addwebcontenttrump());
		map.put("university of pennsylvania", addwebcontentupenn());
		map.put("north korea", addwebcontentnorth());
		return map;
	}
	
	public static List<WebContent> addwebcontentupenn() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("https://en.wikipedia.org/wiki/University_of_Pennsylvania");
		webContent1.setTitle("University of Pennsylvania - Wikipedia");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://www.usnews.com/best-colleges/university-of-pennsylvania-3378");
		webContent2.setTitle("University of Pennsylvania | University of Pennsylvania - Profile, Rankings and Data | University of Pennsylvania | US News Best Colleges");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://www.nytimes.com/topic/organization/university-of-pennsylvania");
		webContent3.setTitle("University of Pennsylvania - The New York Times");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("http://www.upenn.edu/");
		webContent4.setTitle("Home | University of Pennsylvania");
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://en.wikipedia.org/wiki/Benjamin_Franklin");
		webContent5.setTitle("Benjamin Franklin - Wikipedia");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("https://www.coursera.org/penn");
		webContent6.setTitle("University of Pennsylvania | Coursera");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("http://www.pennathletics.com/");
		webContent7.setTitle("University of Pennsylvania | Official Athletics Site");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("https://president.upenn.edu/meet-president");
		webContent8.setTitle("Meet President Gutmann | Penn Office of the President");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("https://en.wikipedia.org/wiki/Amy_Gutmann");
		webContent9.setTitle("Amy Gutmann - Wikipedia");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("http://www.upenn.edu/life-at-penn");
		webContent10.setTitle("Life at Penn | University of Pennsylvania");
		
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
								webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		return list;
		
	}
	
	public static List<WebContent> addwebcontenttrump() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("https://en.wikipedia.org/wiki/Donald_Trump");
		webContent1.setTitle("Donald Trump - Wikipedia");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://www.donaldjtrump.com/");
		webContent2.setTitle("Make America Great Again! | Donald J Trump for President");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://www.nytimes.com/topic/person/donald-trump");
		webContent3.setTitle("Donald Trump - The New York Times");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("http://www.cnn.com/2013/07/04/us/donald-trump-fast-facts/");
		webContent4.setTitle("Donald Trump Fast Facts - CNN.com");
		
		
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("http://www.newyorker.com/magazine/2017/03/13/donald-trumps-worst-deal");
		webContent5.setTitle("Donald Trump’s Worst Deal - The New Yorker");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("http://www.politico.com/news/donald-trump");
		webContent6.setTitle("Donald Trump: Latest News, Top Stories & Analysis - POLITICO");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("http://www.cnn.com/2017/05/08/politics/trump-liberals/");
		webContent7.setTitle("Donald Trump is turning liberals into conspiracy theorists - CNNPolitics.com");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("http://www.biography.com/people/donald-trump-9511238");
		webContent8.setTitle("President Donald J. Trump - Biography.com");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("http://www.msnbc.com/topics/donald-trump");
		webContent9.setTitle("Donald Trump news, video and community from MSNBC");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("http://www.washingtontimes.com/topics/donald-trump/");
		webContent10.setTitle("Topic - Donald Trump - washingtontimes.com");
		
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		//map.put("benjamin franklin", list);
				return list;
	}
	
	public static List<WebContent> addwebcontentnewyork() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("https://www.nytimes.com/");
		webContent1.setTitle("The New York Times - Breaking News, World News & Multimedia");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://www.mlb.com/yankees");
		webContent2.setTitle("Official New York Yankees Website | MLB.com");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://en.wikipedia.org/wiki/New_York");
		webContent3.setTitle("New York - Wikipedia");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("http://www.history.com/topics/new-york-city");
		webContent4.setTitle("New York City - Facts & Summary - HISTORY.com");
		
		
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://www.mlb.com/mets");
		webContent5.setTitle("Official New York Mets Website | MLB.com");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("http://nypost.com/");
		webContent6.setTitle("New York Post");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("http://www.ny.gov/");
		webContent7.setTitle("The Official Website of New York State");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("https://en.wikipedia.org/wiki/September_11_attacks");
		webContent8.setTitle("September 11 attacks - Wikipedia");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("https://www.nyu.edu/");
		webContent9.setTitle("NYU");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("https://www.niche.com/colleges/new-york-university/");
		webContent10.setTitle("New York University - Niche");
		
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		//map.put("benjamin franklin", list);
				return list;
	}
	
	public static List<WebContent> addwebcontentcomputer() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("https://www.usnews.com/best-graduate-schools/top-science-schools/computer-science-rankings");
		webContent1.setTitle("Best Computer Science Programs | Top Computer Science Schools | US News Best Graduate Schools");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://www-cs.stanford.edu/");
		webContent2.setTitle("Stanford Computer Science");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://www.khanacademy.org/computing/computer-science");
		webContent3.setTitle("Computer science | Computing | Khan Academy");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("https://en.wikipedia.org/wiki/Computer_science");
		webContent4.setTitle("Computer science - Wikipedia");
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://en.wikipedia.org/wiki/Computer_scientist");
		webContent5.setTitle("Computer scientist - Wikipedia");
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("https://en.wikibooks.org/wiki/Subject:Computer_science");
		webContent6.setTitle("Subject:Computer science - Wikibooks, open books for an open world");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("http://theweek.com/articles/695411/what-computer-science-teach-about-happiness");
		webContent7.setTitle("What computer science can teach us about happiness");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("https://www.geekwire.com/2017/qa-2017-geek-of-the-year-ed-lazowska-talks-uws-future-in-computer-science-and-impact-on-the-seattle-tech-scene/");
		webContent8.setTitle("Q&A: Geek of the Year Ed Lazowska talks UW's future in computer science and impact on the Seattle tech scene - GeekWire");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("http://theind.com/article-24913-computer-science-as-%E2%80%A6-science-.html");
		webContent9.setTitle("  Computer science as … science?  ");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("https://finance.yahoo.com/news/infosys-foundation-usa-commits-funding-122300607.html");
		webContent10.setTitle("Infosys Foundation USA Commits Funding to Train 1,000 Teachers in Computer Science");
		
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		//map.put("benjamin franklin", list);
		return list;
	}
	
	public static List<WebContent> addwebcontentbenjamin() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("http://www.biography.com/people/benjamin-franklin-9301234");
		webContent1.setTitle("Benjamin Franklin - Diplomat, Scientist, Inventor, Writer - Biography.com");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("http://www.ushistory.org/franklin/info/");
		webContent2.setTitle("The Electric Ben Franklin");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://www.britannica.com/biography/Benjamin-Franklin");
		webContent3.setTitle("Benjamin Franklin | American author, scientist, and statesman | Britannica.com");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("https://en.wikipedia.org/wiki/Benjamin_Franklin");
		webContent4.setTitle("Benjamin Franklin - Wikipedia");
		
		
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://en.wikipedia.org/wiki/Benjamin_Franklin");
		webContent5.setTitle("Benjamin Franklin - Wikipedia");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("https://en.wikiquote.org/wiki/Benjamin_Franklin");
		webContent6.setTitle("Benjamin Franklin - Wikiquote");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("https://en.wikipedia.org/wiki/History_of_the_United_States");
		webContent7.setTitle("History of the United States - Wikipedia");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("http://www.ushistory.org/valleyforge/history/franklin.html");
		webContent8.setTitle("Historic Valley forge");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("http://www.smithsonianmag.com/travel/ben-franklin-slept-here-112338695/");
		webContent9.setTitle("Ben Franklin Slept Here | Travel | Smithsonian");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("https://www.nps.gov/inde/planyourvisit/benjaminfranklinmuseum.htm");
		webContent10.setTitle("Visiting the Benjamin Franklin Museum - Independence National Historical Park (U.S. National Park Service)");
	
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		
		return list;
	}
	
	public static List<WebContent> addwebcontentchoco() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("https://en.wikipedia.org/wiki/Chocolate");
		webContent1.setTitle("Chocolate - Wikipedia");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("www.imdb.com/title/tt1183252/");
		webContent2.setTitle("Chocolate (2008) - IMDb");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("Chocolate Skateboards - Skateboarding since 1994");
		webContent3.setTitle("chocolateskateboards.com/");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("https://www.rescuechocolate.com/");
		webContent4.setTitle("Rescue Chocolate");
		
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("www.chocolatebar.com/?page_id=20");
		webContent5.setTitle("Products – Endangered Species Chocolate");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("https://www.exploratorium.edu/exploring/exploring_chocolate/");
		webContent6.setTitle("Chocolate: Facts, History, and Factory Tour | Exploratorium Magazine");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("www.godiva.com/dark-chocolate");
		webContent7.setTitle("Dark Chocolate | GODIVA");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("https://www.dovechocolate.com/");
		webContent8.setTitle("DOVE® Chocolate");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("https://www.rescuechocolate.com/");
		webContent9.setTitle("Rescue Chocolate");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("www.chocolatebar.com/?page_id=20");
		webContent10.setTitle("Products – Endangered Species Chocolate");

		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		
		return list;
	}
	
	public static List<WebContent> addwebcontentsepta() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("http://www.iseptaphilly.com/");
		webContent1.setTitle("SEPTA");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://en.wikipedia.org/wiki/SEPTA");
		webContent2.setTitle("SEPTA - Wikipedia");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://twitter.com/septa?lang=en");
		webContent3.setTitle("SEPTA (@SEPTA) | Twitter");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("https://autohire.careershop.com/septajobs/");
		webContent4.setTitle("septa - AutoHire");
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://itunes.apple.com/us/app/septa/id724915219?mt=8");
		webContent5.setTitle("SEPTA on the App Store - iTunes - Apple");
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("http://6abc.com/traffic/septa-strike-ends-avoiding-election-impact/1577465/");
		webContent6.setTitle("SEPTA strike ends, avoiding election impact | 6abc.com");
		//dbw.saveQuery("donald trump", webContent2);
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("https://www.septakey.org/ecustomer_enu/start.swe?SWECmd=Login&SWECM=S&SRN=&SWEHo=www.septakey.org");
		webContent7.setTitle("SEPTA Key Home");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("http://webcache.googleusercontent.com/search?q=cache:jXQ5jTytdSYJ:www.philadelphiazoo.org/Visit/Phlash-SEPTA.htm+&cd=28&hl=en&ct=clnk&gl=us");
		webContent8.setTitle("Phlash & SEPTA - Philadelphia Zoo");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("http://www.phillymag.com/news/2017/03/13/septa-snowstorm-plan/");
		webContent9.setTitle(" SEPTA Just Released a Snowstorm Plan | News | Philadelphia ");
		
		WebContent webContent10 = new WebContent();
		webContent10.setUrl("http://www.nbcphiladelphia.com/traffic/transit/SEPTA-Strike-Over-Agreement-Reached-Union-Philadelphia--399733321.html");
		webContent10.setTitle("SEPTA Strike Over, Trains Start Rolling Again | NBC 10 Philadelphia");
	
		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9,webContent10);
		
		return list;
		
	}
	
	public static List<WebContent> addwebcontentnorth() {
		WebContent webContent1 = new WebContent();
		webContent1.setUrl("http://www.korea-dpr.com/");
		webContent1.setTitle("Democratic People's Republic of Korea");
		
		
		WebContent webContent2 = new WebContent();
		webContent2.setUrl("https://en.wikipedia.org/wiki/North_Korea");
		webContent2.setTitle("North Korea - Wikipedia");
		
		WebContent webContent3 = new WebContent();
		webContent3.setUrl("https://www.theguardian.com/world/north-korea");
		webContent3.setTitle("North Korea | World news | The Guardian");
		
		WebContent webContent4 = new WebContent();
		webContent4.setUrl("http://www.telegraph.co.uk/news/worldnews/asia/northkorea/11138496/Escape-from-North-Korea-How-I-escaped-horrors-of-life-under-Kim-Jong-il.html");
		webContent4.setTitle("Escape-from-North-Korea");
		
		WebContent webContent5 = new WebContent();
		webContent5.setUrl("https://www.nytimes.com/topic/destination/north-korea");
		webContent5.setTitle("North Korea - The New York Times");
		
		
		WebContent webContent6 = new WebContent();
		webContent6.setUrl("http://www.reuters.com/places/north-korea");
		webContent6.setTitle("North Korea | Reuters.com");
		
		WebContent webContent7 = new WebContent();
		webContent7.setUrl("http://www.aljazeera.com/topics/country/north-korea.html");
		webContent7.setTitle("North Korea News - Top stories from Al Jazeera");
		
		WebContent webContent8 = new WebContent();
		webContent8.setUrl("http://www.independent.co.uk/topic/NorthKorea");
		webContent8.setTitle("North Korea - latest news, breaking stories and comment - The Independent");
		
		WebContent webContent9 = new WebContent();
		webContent9.setUrl("http://www.9news.com/news/local/politics/north-korea-calls-cory-gardner-psychopath-and-human-dirt/437835104");
		webContent9.setTitle("North Korea calls Cory Gardner \"psychopath\" and \"human dirt\" | 9news.com");

		List<WebContent> list = Arrays.asList(webContent1,webContent2,webContent3,webContent4,
				webContent5,webContent6,webContent7,webContent8,webContent9);
		
		return list;
	}
	
	public static List<WebContent> get(String query) {
		return map.get(query);
	}
	
	
}
