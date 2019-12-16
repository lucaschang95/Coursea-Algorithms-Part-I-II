import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Iterator;

public class Board {

    private final int sz;
    private final int[] blocks1D;

    public Board(int[][] blocks) {

        if (blocks == null) throw new NullPointerException("Null blocks");
        
        sz = blocks.length;
        blocks1D = new int[sz*sz];
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                blocks1D[i*sz + j] = blocks[i][j];
            }
        }

    }

    private Board(int[] blocks) {
        if (blocks == null) throw new NullPointerException("Null blocks");
        sz = (int) Math.sqrt(blocks.length);
        this.blocks1D = blocks;
    }

    public int dimension() {
        return sz;
    }

    public int hamming() {
        int h = 0;
        for (int i = 0; i < blocks1D.length; i++) {
            if (blocks1D[i] != 0 && blocks1D[i] != i + 1) {
                h++;
            }
        }
        return h;
    }

    public int manhattan() {
        int m = 0;
        
        for (int i = 0; i < blocks1D.length; i++) {
            if (blocks1D[i] != 0 && blocks1D[i] != i + 1) {
                m += Math.abs((blocks1D[i] - 1) / sz - i / sz) + Math.abs((blocks1D[i] - 1) % sz - i % sz);
            }
        }
        return m;
    }

    public boolean isGoal() {
        return this.hamming() == 0;
    }

    public Board twin() {
        int[] twinBlocks;
        
        if (blocks1D[0] != 0 && blocks1D[1] != 0)
            twinBlocks = exch(0, 1);
        else 
            twinBlocks = exch(sz * sz - 2, sz * sz - 1);
        return new Board(twinBlocks);
    }

    private int[] exch(int p, int q) {
        int[] a = Arrays.copyOf(blocks1D, blocks1D.length);
        int swap = a[p];
        a[p] = a[q];
        a[q] = swap;
        return a;
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        
        if (that.dimension() != this.dimension()) return false;
        return Arrays.equals(this.blocks1D, that.blocks1D);
    }

    public Iterable<Board> neighbors() {

        Stack<Board> neighbors = new Stack<Board>();
        int[] xDiff = {-1, 1, 0, 0};
        int[] yDiff = {0, 0, -1, 1};
        int[] swappedBlocks;
        int idxOfBlank; // position of the blank square
        int idxOfNB;    // position of a neighbor

        for (idxOfBlank = 0; idxOfBlank < blocks1D.length; ++idxOfBlank)
            if (blocks1D[idxOfBlank] == 0) break;
        for (int i = 0; i < 4; ++i)
        {
            int rowOfNB = idxOfBlank / sz + xDiff[i];
            int colOfNB = idxOfBlank % sz + yDiff[i];

            if (rowOfNB >= 0 && rowOfNB < sz && colOfNB >= 0 && colOfNB < sz)
            {
                idxOfNB = rowOfNB * sz + colOfNB;
                swappedBlocks = exch(idxOfBlank, idxOfNB);
                neighbors.push(new Board(swappedBlocks));
            }
        }
        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(sz + "\n");
        for (int i = 0; i < sz; i++) {
            for (int j = 0; j < sz; j++) {
                s.append(String.format("%2d ", blocks1D[i*sz + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
