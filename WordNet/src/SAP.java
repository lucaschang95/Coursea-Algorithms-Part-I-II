import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class SAP {

    private final Digraph digraph;
    private final int V;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException("SAP constructor argument is null");
        digraph = new Digraph(G);
        V = digraph.V();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int[] info = ancInfo(v, w);
        return info[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int[] info = ancInfo(v, w);
        return info[0];
    }

    private int[] ancInfo(int v, int w) {
        if ((Integer) v == null || (Integer) w == null) throw new IllegalArgumentException("ancInfo argument is null");
        boolean hasAncestorBefore = false;
        int anc = -1;
        int dist = -1;
        int tempDist;
        BreadthFirstDirectedPaths BFDPv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths BFDPw = new BreadthFirstDirectedPaths(digraph, w);
        for (int i = 0; i < V; i++) {
            if (BFDPv.hasPathTo(i) && BFDPw.hasPathTo(i)) {
                tempDist = BFDPv.distTo(i) + BFDPw.distTo(i);
                if (hasAncestorBefore && tempDist < dist) {
                    dist = tempDist;
                    anc = i;
                }
                if (!hasAncestorBefore) {
                    hasAncestorBefore = true;
                    dist = tempDist;
                    anc = i;
                }
            }
        }
        int[] info = new int[2];
        info[0] = anc;
        info[1] = dist;
        return info;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int[] info = ancInfo(v, w);
        return info[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int[] info = ancInfo(v, w);
        return info[0];
    }

    private int[] ancInfo(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) throw new IllegalArgumentException("ancInfo argument is null");
        Iterator<Integer> vIter = v.iterator();
        while (vIter.hasNext()) {
            Integer a = vIter.next();
            if (a == null) throw new IllegalArgumentException("ancInfo argument is null");
            if (a < 0 || a >= V) throw new IllegalArgumentException("ancInfo argument is out of bound");
        }
        Iterator<Integer> wIter = w.iterator();
        while (wIter.hasNext()) {
            Integer a = wIter.next();
            if (a == null) throw new IllegalArgumentException("ancInfo argument is null");
            if (a < 0 || a >= V) throw new IllegalArgumentException("ancInfo argument is out of bound");
        }
        boolean hasAncestorBefore = false;
        int anc = -1;
        int dist = -1;
        int tempDist;
        BreadthFirstDirectedPaths BFDPv = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths BFDPw = new BreadthFirstDirectedPaths(digraph, w);
        for (int i = 0; i < V; i++) {
            if (BFDPv.hasPathTo(i) && BFDPw.hasPathTo(i)) {
                tempDist = BFDPv.distTo(i) + BFDPw.distTo(i);
                if (hasAncestorBefore && tempDist < dist) {
                    dist = tempDist;
                    anc = i;
                }
                if (!hasAncestorBefore) {
                    hasAncestorBefore = true;
                    dist = tempDist;
                    anc = i;
                }
            }
        }
        int[] info = new int[2];
        info[0] = anc;
        info[1] = dist;
        return info;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}