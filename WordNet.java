import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {

    private Digraph G;
    private HashMap<String, Stack<Integer>> nouns;
    private HashMap<Integer, String> synsets;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }


        In csvReader = null;
        int vetexCount = 0;


        nouns = new HashMap<String, Stack<Integer>>();
        this.synsets = new HashMap<Integer, String>();
        csvReader = new In(synsets);
        String row;
        while ((row = csvReader.readLine()) != null) {
            vetexCount++;
            String[] data = row.split(",");
            int v = Integer.parseInt(data[0]);

            this.synsets.put(v, data[1]);

            for (String noun : data[1].split(" ")) {
                if (nouns.containsKey(noun)) {
                    nouns.get(noun).push(v);
                }
                else {
                    Stack<Integer> numbers = new Stack<Integer>();
                    numbers.push(v);
                    nouns.put(noun, numbers);
                }
            }
        }

        csvReader.close();


        G = new Digraph(vetexCount);
        csvReader = new In(hypernyms);
        boolean arealdyOneRoot = false;
        String row2;
        while ((row2 = csvReader.readLine()) != null) {
            String[] data = row2.split(",");
            int v = Integer.parseInt(data[0]);

            for (int i = 1; i < data.length; i++) {
                G.addEdge(v, Integer.parseInt(data[i]));
            }

            if (G.outdegree(v) == 0) {
                if (arealdyOneRoot) {
                    throw new IllegalArgumentException();
                }
                arealdyOneRoot = true;

            }
        }

        this.checkCircle();

        csvReader.close();


    }

    private void checkCircle() {
        boolean[] onStack = new boolean[G.V()];
        boolean[] marked = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                this.doDfsCheckCircle(v, marked, onStack);
            }
        }
    }

    private void doDfsCheckCircle(int w, boolean[] onStack, boolean[] marked) {
        marked[w] = true;
        onStack[w] = true;

        for (int v : G.adj(w)) {
            if (!marked[v]) {
                this.doDfsCheckCircle(v, onStack, marked);
            }
            else if (onStack[v]) {
                throw new IllegalArgumentException();
            }
        }

        onStack[w] = false;
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return this.nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        Stack<Integer> setA = this.nouns.get(nounA);
        Stack<Integer> setB = this.nouns.get(nounB);

        SAP sap = new SAP(G);

        return sap.length(setA, setB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }

        Stack<Integer> setA = this.nouns.get(nounA);
        Stack<Integer> setB = this.nouns.get(nounB);

        SAP sap = new SAP(G);

        return this.synsets.get(sap.ancestor(setA, setB));
    }

    // do unit testing of this class
    public static void main(String[] args) {

        String synsets = StdIn.readString();
        String hypernyms = StdIn.readString();

        WordNet wordNet = new WordNet(synsets, hypernyms);

        for (String noun : wordNet.nouns()) {
            StdOut.printf("nouns = %s\n", noun);
        }
    }
}
