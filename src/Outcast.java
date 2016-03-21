public class Outcast {
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		if (wordnet == null) {
			throw new java.lang.NullPointerException();
		}
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		if (nouns == null || nouns.length == 0) {
			throw new java.lang.NullPointerException();
		}
		
		return null;
	}

	// see test client below
	public static void main(String[] args) {
	}
}