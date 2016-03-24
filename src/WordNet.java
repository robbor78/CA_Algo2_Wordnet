import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

/*Corner cases.  
 * All methods and the constructor should throw a java.lang.NullPointerException 
 * if any argument is null. 
 * The constructor should throw a java.lang.IllegalArgumentException if the 
 * input does not correspond to a rooted DAG. 
 * The distance() and sap() methods should throw a 
 * java.lang.IllegalArgumentException unless both of the noun arguments 
 * are WordNet nouns.

Performance requirements.  
Your data type should use space linear in the input size 
(size of synsets and hypernyms files). 
The constructor should take time linearithmic (or better) in the input size. 
The method isNoun() should run in time logarithmic (or better) in the number of nouns. 
The methods distance() and sap() should run in time linear in the size of the WordNet digraph. 
For the analysis, assume that the number of nouns per synset is bounded by a constant. */
public class WordNet {

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (isNullOrEmpty(synsets) || isNullOrEmpty(hypernyms)) {
			throw new java.lang.NullPointerException();
		}

		// The constructor should take time linearithmic (or better) in the
		// input size.
		
		buildNounData(synsets);
buildGraph(hypernyms);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return mapNoun2ID.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		// The method isNoun() should run in time logarithmic (or better) in the
		// number of nouns.

		return mapNoun2ID.containsKey(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		/*
		 * The distance() method should throw a
		 * java.lang.IllegalArgumentException unless both of the noun arguments
		 * are WordNet nouns.
		 */

		// The methods distance() and sap() should run in time linear in the
		// size of the WordNet digraph.

		return -1;
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		/*
		 * The sap() method should throw a java.lang.IllegalArgumentException
		 * unless both of the noun arguments are WordNet nouns.
		 */

		// The methods distance() and sap() should run in time linear in the
		// size of the WordNet digraph.

		return null;
	}

	private boolean isNullOrEmpty(String s) {
		return s == null || s == "";
	}
	
	private void buildNounData(String filename) {
		//example:
		//36,AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire
		mapID2Noun = new HashMap<Integer, HashSet<String>>();
		mapNoun2ID = new HashMap<String, HashSet<Integer>>();
		
		In in = new In(filename);
		while (in.hasNextLine()) {
			String line = in.readLine();
			String[] parts = line.split(",");
			int id = Integer.parseInt(parts[0]);
			String[] nouns = parts[1].split("\\s+");
			
			Arrays.stream(nouns).forEach(x-> {
				if (!mapID2Noun.containsKey(id)){
			mapID2Noun.put(id, new HashSet<String>());
				}
			mapID2Noun.get(id).add(x);
			
			if (!mapNoun2ID.containsKey(x)) {
				mapNoun2ID.put(x, new HashSet<Integer>());
			}
			mapNoun2ID.get(x).add(id);
			});
		}
	}
	
	private void buildGraph(String hypernyms) {
		//example:
		//164,21012,56099
		
		g = new Digraph(mapID2Noun.size());
		
		In hypernymsIn = new In(hypernyms);
		while (hypernymsIn.hasNextLine()) {
			String hyperLine = hypernymsIn.readLine();
			int[] hyperParts = Arrays.stream(hyperLine.split(",")).mapToInt(Integer::parseInt).toArray();
			
			int f = hyperParts[0];
			Arrays.stream(hyperParts).skip(1).forEach(x->g.addEdge(f, x));
		}
	}
	
	private Digraph g;
	private HashMap<Integer,HashSet<String>> mapID2Noun;
	private HashMap<String, HashSet<Integer>> mapNoun2ID;

	// do unit testing of this class
	public static void main(String[] args) {

	}
}