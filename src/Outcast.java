import java.util.HashMap;

public class Outcast {

    private static final String H = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/1wordnet/wordnet/hypernyms.txt";
    private static final String S = "/run/media/bert/280AC22E0AF59495/coursera/algorithms/2/assignments/1wordnet/wordnet/synsets.txt";

    private WordNet wn;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new java.lang.NullPointerException();
        }

        wn = wordnet;
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

    // see test client below
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        System.out.println("test1");

        String[][] words = {
                { "horse", "zebra", "cat", "bear", "table" },
                { "water", "soda", "bed", "orange_juice", "milk",
                        "apple_juice", "tea", "coffee" },
                { "apple", "pear", "peach", "banana", "lime", "lemon",
                        "blueberry", "strawberry", "mango", "watermelon",
                        "potato" } };

        String[] outcasts = { "table", "bed", "potato" };

        WordNet wn = getWordNetFull();
        Outcast oc = new Outcast(wn);
        for (int i = 0; i < outcasts.length; i++) {
            String actual = oc.outcast(words[i]);
            String expected = outcasts[i];
            assert expected.equals(actual);
        }
    }

    private static WordNet getWordNetFull() {
        return new WordNet(S, H);
    }
}