import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
	private final int size;
	private final WeightedQuickUnionUF grid;
	private boolean[] open;
	private int count;

	public Percolation(int n) {
		if (n < 1) {
			throw new java.lang.IllegalArgumentException();
		}
		grid = new WeightedQuickUnionUF(n*n+2);
		size = n;
		open = new boolean[n*n+2];
		open[0] = true;
		count = 0;
		for (int i = 1; i <= n*n+1; i++) {
			open[i] = false;
		}
	}
	
	private void validate(int row, int col) {
		if (row < 1 || row > size || col < 1 || col > size) {
			throw new java.lang.IllegalArgumentException();
		}
	}
	private int xyTo1D(int row, int col) { 
		return (size*(row-1)+col);
	}

	public void open(int row, int col) {
		validate(row, col);
		if (!open[xyTo1D(row, col)]) {
			open[xyTo1D(row, col)] = true;
			count++;
			if (row > 1) {
				if (open[size*(row-2)+col]) grid.union(xyTo1D(row, col), xyTo1D(row-1, col));
			} else {
				grid.union(0, col);
			}
			if (row < size) {
				if (open[size*(row)+col]) grid.union(xyTo1D(row, col), xyTo1D(row+1, col));
			} else {
				grid.union(xyTo1D(size, col), size*size+1);
			}
			if (col > 1) {
				if (open[size*(row-1)+col-1]) grid.union(xyTo1D(row, col), xyTo1D(row, col-1));
			} 
			if (col < size) {
				if (open[size*(row-1)+col+1]) grid.union(xyTo1D(row, col), xyTo1D(row, col+1));
			}
		}
	}

	public boolean isOpen(int row, int col) {
		validate(row, col);
		return open[xyTo1D(row, col)];
	}
	
	public boolean isFull(int row, int col) {
		validate(row, col);
		return grid.connected(0, xyTo1D(row, col));
	}
	
	public int numberOfOpenSites() {
		return count;
		
	}
	
	public boolean percolates() {
		return grid.connected(0, size*size+1);
	}
}
