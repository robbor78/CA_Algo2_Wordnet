import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {

    private static String PATH = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/1wordnet/wordnet/";
    private static final String H = PATH + "hypernyms.txt";
    private static final String S = PATH + "synsets.txt";

    private SAP sap;
    private HashMap<Integer, String> mapID2Noun;
    private HashMap<String, HashSet<Integer>> mapNoun2ID;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (isNullOrEmpty(synsets) || isNullOrEmpty(hypernyms)) {
            throw new java.lang.NullPointerException();
        }

        buildNounData(synsets);
        buildGraph(hypernyms);
    }

    // public WordNet(WordNet wordnet) {
    // if (wordnet == null) {
    // throw new java.lang.NullPointerException();
    // }
    //
    // sap = new SAP(wordnet.sap);
    // mapID2Noun = new HashMap<Integer, String>(wordnet.mapID2Noun);
    // mapNoun2ID = new HashMap<String, HashSet<Integer>>(wordnet.mapNoun2ID);
    // }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return mapNoun2ID.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (isNullOrEmpty(word)) {
            throw new java.lang.NullPointerException();
        }
        return mapNoun2ID.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        Function3<Iterable<Integer>, Iterable<Integer>, Integer> f = (v, w) -> sap
                .length(v, w);
        return searchWordNet(nounA, nounB, f);
    }

    public String sap(String nounA, String nounB) {
        Function3<Iterable<Integer>, Iterable<Integer>, Integer> f = (v, w) -> sap
                .ancestor(v, w);
        int ancestor = searchWordNet(nounA, nounB, f);
        return mapID2Noun.get(ancestor); // .iterator().next();
    }

    private <T> T searchWordNet(String nounA, String nounB,
            Function3<Iterable<Integer>, Iterable<Integer>, T> f) {
        if (isNullOrEmpty(nounA) || isNullOrEmpty(nounB)) {
            throw new java.lang.NullPointerException();
        }

        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        Iterable<Integer> ai = mapNoun2ID.get(nounA);
        Iterable<Integer> bi = mapNoun2ID.get(nounB);
        return f.apply(ai, bi);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    private void buildNounData(String filename) {
        // example:
        // 36,AND_circuit AND_gate,a circuit in a computer that fires only when
        // all of its inputs fire
        mapID2Noun = new HashMap<Integer, String>();
        mapNoun2ID = new HashMap<String, HashSet<Integer>>();

        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);

            if (!mapID2Noun.containsKey(id)) {
                mapID2Noun.put(id, parts[1]);
            }

            String[] nouns = parts[1].split("\\s+");
            Arrays.stream(nouns).forEach(noun -> {

                if (!mapNoun2ID.containsKey(noun)) {
                    mapNoun2ID.put(noun, new HashSet<Integer>());
                }
                mapNoun2ID.get(noun).add(id);
            });
        }
    }

    private void buildGraph(String hypernyms) {
        // example:
        // 164,21012,56099

        Digraph g = new Digraph(mapID2Noun.size());

        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String hyperLine = hypernymsIn.readLine();
            int[] hyperParts = Arrays.stream(hyperLine.split(","))
                    .mapToInt(Integer::parseInt).toArray();

            int f = hyperParts[0];
            Arrays.stream(hyperParts).skip(1).forEach(x -> g.addEdge(f, x));
        }

        checkCycles(g);
        checkRoot(g);

        sap = new SAP(g);
    }

    private void checkCycles(Digraph g) {
        if (new DirectedCycle(g).hasCycle()) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    private void checkRoot(Digraph g) {
        int roots = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0) {
                roots++;
            }
            if (roots > 1) {
                throw new java.lang.IllegalArgumentException();
            }
        }
    }

    @FunctionalInterface
    private interface Function3<A, B, R> {
        // R is like Return, but doesn't have to be last in the list nor named
        // R.
        R apply(A a, B b);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        testLoadWordNet();
        test1();
        test2();
        testDistance();
        testCycle();
    }

    private static void testCycle() {
        System.out.println("testCycle");
        String[][] files = {
                { "synsets3.txt", "hypernyms3InvalidTwoRoots.txt" },
                { "synsets3.txt", "hypernyms3InvalidCycle.txt" },
                { "synsets6.txt", "hypernyms6InvalidTwoRoots.txt" },
                { "synsets6.txt", "hypernyms6InvalidCycle.txt" },
                { "synsets6.txt", "hypernyms6InvalidCycle+Path.txt" } };

        for (int i = 0; i < files.length; i++) {
            try {
                @SuppressWarnings("unused")
                WordNet wn = new WordNet(PATH + files[i][0], PATH + files[i][1]);
                assert false;
            } catch (IllegalArgumentException e) {
                //System.out.println("cycle detected");
                assert true;
            }
        }

    }

    private static void testDistance() {
        System.out.println("testDistance");

        WordNet wn = getWordNetFull();

        int[] distances = { 23, 33, 27, 29 };
        String[][] words = { { "white_marlin", "mileage" },
                { "Black_Plague", "black_marlin" },
                { "American_water_spaniel", "histology" },
                { "Brown_Swiss", "barrel_roll" } };

        for (int i = 0; i < distances.length; i++) {
            int actualDistance = wn.distance(words[i][0], words[i][1]);
            int expectedDistance = distances[i];
            assert expectedDistance == actualDistance;
        }

    }

    private static void test1() {
        System.out.println("test1");

        WordNet wn = getWordNetFull();
        String sap = wn.sap("worm", "bird");

        assert sap.equals("animal animate_being beast brute creature fauna");
    }

    private static void test2() {
        System.out.println("test2");

        WordNet wn = getWordNetFull();
        String sap = wn.sap("individual", "edible_fruit");

        assert sap.contains("physical_entity");
    }

    private static void testLoadWordNet() {
        System.out.println("testLoadWordNet");

        WordNet wn = getWordNetFull();
        assert !wn.isNoun("sdfd43a");
        assert wn.isNoun("Asclepias");
    }

    private static WordNet getWordNetFull() {
        return new WordNet(S, H);
    }

}