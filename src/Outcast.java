import java.util.Arrays;
import java.util.HashMap;

public class Outcast {
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		if (wordnet == null) {
			throw new java.lang.NullPointerException();
		}

		wn = new WordNet(wordnet);
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		if (nouns == null || nouns.length == 0) {
			throw new java.lang.NullPointerException();
		}

		HashMap<Integer, HashMap<Integer, Integer>> ds = new HashMap<Integer, HashMap<Integer, Integer>>();

		int maxD = 0;
		String outcast = "";

		int length = nouns.length;
		for (int i = 0; i < length; i++) {
			
			String x = nouns[i];
			int d = 0;
			
			for (int j = 0; j < length; j++) {
				
				if (i == j) {
					continue;
				}

				int dxy = 0;
				
				if (ds.containsKey(i) && ds.get(i).containsKey(j)) {

					dxy = ds.get(i).get(j);

				} else if (ds.containsKey(j) && ds.get(j).containsKey(i)) {

					dxy = ds.get(j).get(i);

				} else {

					String y = nouns[j];
					dxy = wn.distance(x, y);
					if (!ds.containsKey(i)) {
						ds.put(i, new HashMap<Integer, Integer>());
					}
					ds.get(i).put(j, dxy);
				}
				
				d += dxy;

			}
			
			if (d > maxD) {
				maxD = d;
				outcast = x;
			}
		}

		return outcast;
	}

	private WordNet wn;

	// see test client below
	public static void main(String[] args) {
	}
}