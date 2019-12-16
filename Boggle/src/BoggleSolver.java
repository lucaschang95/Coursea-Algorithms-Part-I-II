import edu.princeton.cs.algs4.SET;

public class BoggleSolver {

    private static final int R = 26;

    private boolean[] marked;
    private char[] board;
    private int rows;  // number of rows
    private int cols;  // number of columns
    private Cube[] adj;
    private Node root;

    private static class Node {
        private boolean isWord;
        private Node[] next = new Node[R];
    }

    private static class Cube {
        private int n = 0;
        private int[] neighbor = new int[8];
    }
    /**
     * Initializes the data structure using the given array of strings as the dictionary.
     * You can assume each word in the dictionary contains only the uppercase letters A through Z.
     * @param dictionary input stream of dictonary
     */
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException("BoggleSolver argument is null");
        for (int i = 0; i < dictionary.length; i++) {
            addToTrie(dictionary[i]);
        }
    }

    private void addToTrie(String word) {
        if (word == null) throw new IllegalArgumentException("addToTrie argument is null");
        root = add(root, word, 0);
    }

    private Node add(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (d == word.length()) x.isWord = true;
        else {
            char c = word.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], word, d + 1);
        }
        return x;
    }

    private boolean contains(String word) {
        if (word == null) throw new IllegalArgumentException("contains argument is null");
        Node x = get(root, word, 0);
        if (x == null) return false;
        return x.isWord;
    }

    private Node get(Node x, String word, int d) {
        if (x == null) return null;
        if (d == word.length()) return x;
        char c = word.charAt(d);
        return get(x.next[c - 'A'], word, d + 1);
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an Iterable.
     * @param board
     * @return
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) throw new IllegalArgumentException("getAllValidWords argument is null");

        rows = board.rows();
        cols = board.cols();
        marked = new boolean[rows * cols];
        this.board = new char[rows * cols];
        precomputeAdj();
        //form the board char[]
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = i * cols + j;
                char c = board.getLetter(i, j);
                this.board[idx] = c;
            }
        }

        SET<String> allValidWords = DFS();
        return allValidWords;
    }

    private SET<String> DFS() {
        SET<String> words = new SET<String>();
        for(int i = 0; i < rows * cols; i++) {
            DFS(i, new StringBuilder(), words, root);
        }
        return words;
    }

    private void DFS(int idx, StringBuilder pre, SET<String> words, Node n) {
        char c = board[idx];
        Node next = n.next[c - 'A'];
        if (c == 'Q' && next != null) next = next.next['U' - 'A'];
        if (next == null) return;

        if (c == 'Q') pre.append("QU");
        else pre.append(c);
        String str = pre.toString();
        if (pre.length() > 2 && next.isWord) words.add(str);

        marked[idx] = true;
        for(int k = 0; k < adj[idx].n; k++) {
            int nextIdx = adj[idx].neighbor[k];
            if (!marked[nextIdx])  DFS(nextIdx, new StringBuilder(pre), words, next);
        }
        marked[idx] = false;
    }


    private void precomputeAdj() {
        adj = new Cube[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = i * cols + j;
                adj[idx] = new Cube();
                if (i > 0 && j > 0)               adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j - 1;
                if (i > 0)                        adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j;
                if (i > 0 && j < cols - 1)        adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j + 1;
                if (j > 0)                        adj[idx].neighbor[adj[idx].n++] = i * cols + j - 1;
                if (j < cols - 1)                 adj[idx].neighbor[adj[idx].n++] = i * cols + j + 1;
                if (i < rows - 1 && j > 0)        adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j - 1;
                if (i < rows - 1)                 adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j;
                if (i < rows - 1 && j < cols - 1) adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j + 1;
            }
        }
    }


    public int scoreOf(String word) {
        if (word == null) throw new IllegalArgumentException("scoreOf argument is null");
        if (!contains(word)) return 0;
        if (word.length() <  3) return 0;
        if (word.length() <= 4) return 1;
        if (word.length() == 5) return 2;
        if (word.length() == 6) return 3;
        if (word.length() == 7) return 5;
        return 11;
    }

}
