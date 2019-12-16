import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.Iterator;

public class WordNet {

    private ST<Integer, String> synsetST;
    private Digraph digraph;
    private SET<String> synsetSet;
    private SET<String> nounSet;
    private int V;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        buildSynsetST(synsets);
        buildDigragph(hypernyms);
    }

    /**
     * build SymbolTable of synsets, convert between synsets{SET<String>} and Integer
     *
     * @param synsets
     */
    private void buildSynsetST(String synsets) {
        In in1 = new In(synsets);
        synsetST = new ST<>();
        nounSet = new SET<>();

        while (in1.hasNextLine()) {
            String line = in1.readLine();
            String[] fields = line.split(",");
            synsetST.put(Integer.parseInt(fields[0]), fields[1]);
            String[] currentNounSets = fields[1].split(" ");
            for (String a : currentNounSets) {
                if (!nounSet.contains(a)) nounSet.add(a);
            }
        }
        V = synsetST.size();
    }

    private void buildDigragph(String hypernyms) {
        In in2 = new In(hypernyms);
        digraph = new Digraph(V);
        while (in2.hasNextLine()) {
            String line = in2.readLine();
            String[] fields = line.split(",");
            for (int i = 1; i < fields.length; i++) {
                digraph.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }
        }
        DirectedCycle checkCycle = new DirectedCycle(digraph);
        if (checkCycle.hasCycle()) throw new IllegalArgumentException("input hypernyms has cycles");
        int rootNum = 0;
        for (int i = 0; i < V; i++) {
            if (digraph.outdegree(i) == 0) rootNum++;
        }
        if (rootNum > 1) throw new IllegalArgumentException("input hypernyms has mutiple roots");
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounSet;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("isNoun argument is null");
        return nounSet.contains(word);
    }

    /**
     * distance between nounA and nounB
     *
     * @param nounA
     * @param nounB
     * @return
     */
    public int distance(String nounA, String nounB) {
        SAP sapModel = new SAP(digraph);
        Bag<Integer> aBag = containedIterable(nounA);
        Bag<Integer> bBag = containedIterable(nounB);
        return sapModel.length(aBag, bBag);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        SAP sapModel = new SAP(digraph);
        Bag<Integer> aBag = containedIterable(nounA);
        Bag<Integer> bBag = containedIterable(nounB);
        int ancId = sapModel.ancestor(aBag, bBag);
        return synsetST.get(ancId);
    }

    /**
     * return Iterable<Interger> each containing input String a
     *
     * @return
     */
    private Bag<Integer> containedIterable(String noun) {
        if (noun == null) throw new IllegalArgumentException("containedIterable argument is null");
        if (!isNoun(noun)) throw new IllegalArgumentException("containedIterable argument is not a wordnet noun");
        Bag<Integer> bag = new Bag<>();
        for (int i = 0; i < V; i++) {
            String currentSynset = synsetST.get(i);
            String[] nounsArray = currentSynset.split(" ");
            for (String string : nounsArray) {
                if (string.equals(noun)) {
                    bag.add(i);
                    break;
                }
            }
        }
        return bag;
    }

    // do unit testing of this classf1
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");
        Bag<Integer> bag1 = net.containedIterable("gamma_globulin");
        Iterator<Integer> bag1Iterator = bag1.iterator();
        while (bag1Iterator.hasNext()) {
            int i = bag1Iterator.next();
            System.out.println(i);
            System.out.println(net.synsetST.get(i));
        }
        //System.out.println(net.distance("till", "allelomorph"));
    }
}