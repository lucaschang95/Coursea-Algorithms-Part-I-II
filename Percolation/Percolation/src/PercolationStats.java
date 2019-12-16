import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

	private final int trials;
	private final double[] rate;

	public PercolationStats(int n, int trials) {
		if (n <= 0|| trials <= 0) {
			throw new java.lang.IllegalArgumentException();
		}
		this.trials = trials;
		rate = new double[trials];
		for (int i = 0; i <= trials-1; i++) {
			Percolation percolationModel = new Percolation(n);
			while (!percolationModel.percolates()) {
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1;
				percolationModel.open(row, col);
			}
			rate[i] = (double)percolationModel.numberOfOpenSites()/(double)(n*n);
		}
	}
	
	public double mean() {
		return StdStats.mean(rate);
	}
	
	public double stddev() {
		return StdStats.stddev(rate);
	}
	
	public double confidenceLo() {
		return (mean() - 1.96*stddev()/Math.sqrt(trials));
	}

	public double confidenceHi() {
		return (mean() + 1.96*stddev()/Math.sqrt(trials));
	}
	
	public static void main(String[] args) {
        
		int n, trials;
        n = StdIn.readInt();
        trials = StdIn.readInt();
        PercolationStats client = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + client.mean());
        StdOut.println("stddev                  = " + client.stddev());
        StdOut.println("95% confidence interval = " + "[" + client.confidenceLo() + ", " + client.confidenceHi() + "]");


	}
}

