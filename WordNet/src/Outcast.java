public class Outcast {

    private final WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int size = nouns.length;
        for (String a: nouns) {
            if (!wordnet.isNoun(a)) throw new IllegalArgumentException("outcast argument is not a WordNet noun");
        }
        int maxDistSum = 0;
        int maxIndex = 0;
        for (int i = 0; i < size; i++) {
            int distSum = 0;
            for (int j = 0; j < size; j++) {
                distSum += wordnet.distance(nouns[i], nouns[j]);
            }
            if (distSum > maxDistSum) {
                maxDistSum = distSum;
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }

    // see test client below
    public static void main(String[] args) {
        String[] a = {"probability", "statistics", "mathematics", "physics"};
        Outcast cast4 = new Outcast(new WordNet("synsets.txt", "hypernyms.txt"));
        String ans = cast4.outcast(a);
    }