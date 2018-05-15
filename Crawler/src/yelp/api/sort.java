package yelp.api;

import java.util.*;

import org.json.simple.JSONObject;

public class sort {
	
	// sort the arraylist of json objects according to the field specified by condition
	public static PriorityQueue<JSONObject> Sort(ArrayList<JSONObject> value, final String condition) {
		PriorityQueue<JSONObject> pq = new PriorityQueue<>(10, new Comparator<JSONObject>() {
			@Override
			public int compare(JSONObject j1, JSONObject j2) {
				if (condition.equals("Rating")) {

					double r1 = Double.parseDouble(j1.get("rating").toString());
					double r2 = Double.parseDouble(j2.get("rating").toString());

					return r2 > r1 ? 1 : -1;
				}

				else {
					int i1 = Integer.parseInt(j1.get("review_count").toString());
					int i2 = Integer.parseInt(j2.get("review_count").toString());

					return i2 > i1 ? 1 : -1;
				}

			}
		});
		for (int i = 0; i < value.size(); i++) {
			pq.offer(value.get(i));
		}

		return pq;
	}

}
