import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] trialsResults;
    private int trialCount;
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n or trials is less tan or equal zero");

        trialCount = trials;
        trialsResults = new double[trials];

        for(int trial = 0; trial < trials; trial++){
            Percolation percolation = new Percolation(n);
            while(!percolation.percolates()){
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                percolation.open(row, col);
            }
            int openSites = percolation.numberOfOpenSites();
            double result = (double) openSites / (n * n);
            trialsResults[trial] = result;
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(trialsResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(trialsResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - ((1.96 * stddev()) / Math.sqrt(trialCount));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + ((1.96 * stddev()) / Math.sqrt(trialCount));
    }

    // test client (see below)
    public static void main(String[] args){
        int gridSize = 10;
        int trialCount = 10;
        if (args.length >= 2) {
            gridSize = Integer.parseInt(args[0]);
            trialCount = Integer.parseInt(args[1]);
        }
        PercolationStats ps = new PercolationStats(gridSize, trialCount);

        String confidence = ps.confidenceLo() + ", " + ps.confidenceHi();
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }

}
