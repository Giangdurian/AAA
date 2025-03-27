import java.util.HashMap;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final HashMap<Integer, String> synSets;
    private final HashMap<String, Bag<Integer>> nounMap;
    private final Digraph digraph;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        int size = 0;
        nounMap = new HashMap<>();
        synSets = new HashMap<>();
        if (synsets == null) {
            throw new IllegalArgumentException("Synsets file is null");
        }
        In synsetsIn = new In(synsets);
        while (synsetsIn.hasNextLine()) {
            ++size;//
            String line = synsetsIn.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            synSets.put(id, fields[1]);
            String[] nouns = fields[1].split(" ");
            for (String noun : nouns) {
                if (nounMap.containsKey(noun)) {
                    nounMap.get(noun).add(id);
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(id);
                    nounMap.put(noun, bag);
                }
            }
        }
        if (hypernyms == null) {
            throw new IllegalArgumentException("Hypernyms file is null");
        }
        In hypernymsIn = new In(hypernyms);
        digraph = new Digraph(size);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                digraph.addEdge(v, w);
            }
        }
        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle()) {
            throw new IllegalArgumentException("Digraph has a cycle");
        }
        int rootCount = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) {
                ++rootCount;
            }
        }
        if (rootCount != 1) {
            throw new IllegalArgumentException("Digraph has more than one root");
        }
        sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Word is null");
        }
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Noun is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Noun is not in WordNet");
        }
        int res[] = sap.sap(nounMap.get(nounA), nounMap.get(nounB));
        return res[0];
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("Noun is null");
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Noun is not in WordNet");
        }
        int res[] = sap.sap(nounMap.get(nounA), nounMap.get(nounB));
        return synSets.get(res[1]);

    }


    public static void main(String[] args) {

    }
}