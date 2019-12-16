import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private Node oCurrNode;
    private Node tCurrNode;
    private Stack<Board> solution;

    private class Node implements Comparable<Node>{

        private Board board;
        private int mPriority;
        private Node previous;
        private int moves;

        public Node(Board inboard, Node previous) {
            this.board = inboard;
            this.previous = previous;
            if (previous == null) moves = 0;
            else moves = previous.moves + 1;
            mPriority = board.manhattan() + moves;
        }

        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.mPriority, o.mPriority);
        }
    }

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException("Constructor argument Board is null!");

        oCurrNode = new Node(initial, null);
        tCurrNode = new Node(initial.twin(), null);
        MinPQ<Node> originPQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        originPQ.insert(oCurrNode);
        twinPQ.insert(tCurrNode);

        while (true) {
            oCurrNode = originPQ.delMin();
            if (oCurrNode.board.isGoal()) break;
            neighborsIntoPQ(oCurrNode, originPQ);

            tCurrNode = twinPQ.delMin();
            if (tCurrNode.board.isGoal()) break;
            neighborsIntoPQ(tCurrNode, twinPQ);
        }
    }

    private void neighborsIntoPQ(Node node, MinPQ<Node> pq) {
        Iterable<Board> neighbors = node.board.neighbors();
        for (Board a : neighbors) {
            if (node.previous == null || !a.equals(node.previous.board)) {
                pq.insert(new Node(a, node));
            }
        }
    }

    public boolean isSolvable() {
        return oCurrNode.board.isGoal();
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     * @return
     */
    public int moves() {
        if (oCurrNode.board.isGoal()) return oCurrNode.moves;
        else return -1;
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     * @return
     */
    public Iterable<Board> solution() {
        if (oCurrNode.board.isGoal()) {
            solution = new Stack<Board>();
            Node node = oCurrNode;
            while (node != null) {
                solution.push(node.board);
                node = node.previous;
            }
            return solution;
        }
        else return null;
    }



    /**
     * solve a slider puzzle (given below)
     * @param args
     */
    public static void main(String[] args) {
    }
}
