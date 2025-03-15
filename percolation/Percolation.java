import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] sites;
    private WeightedQuickUnionUF WQUF;
    private int border;
    private int number_of_open_sites;
    private int virtualBottom, virtualTop;

    private int convert_to_1d_index(int row, int col){
        return row * border + col;
    }

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n is less tan or equal zero");
        border = n;
        number_of_open_sites = 0;
        int Nsquare = n * n;
        sites = new boolean[Nsquare];
        for (int i = 0; i < Nsquare; i++) {
            // all sites initially blocked
            sites[i] = false;
        }
        virtualBottom = Nsquare + 1;
        virtualTop = Nsquare;
        // The extra 2 elements are for virtual top an bottom sites
        WQUF = new WeightedQuickUnionUF(Nsquare + 2);
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if((row < 1 || row > border) || (col < 1 || col > border))
            throw new IllegalArgumentException("row or column is out of border");

        int index = convert_to_1d_index(row - 1, col - 1);

        if(isOpen(row, col) == true)
            return;

        // opens the site
        sites[index] = true;
        number_of_open_sites++;

        // if the site is in top row connects it to virtual top site
        if(row == 1)
            WQUF.union(index, virtualTop);

        // if the site is in bottom row connects it to virtual bottom site
        if(row == border)
            WQUF.union(index, virtualBottom);

        // if the left site is open connect to it
        if( ((col - 1) <= border) && ((col - 1) >= 1) ){
            if(isOpen(row, col - 1) == true){
                int left_site_index = convert_to_1d_index(row - 1, col - 2);
                WQUF.union(index, left_site_index);
            }
        }

        // if the right site is open connect to it
        if( ((col + 1) <= border) && ((col + 1) > 1) ){
            if(isOpen(row, col + 1) == true){
                int right_site_index = convert_to_1d_index(row - 1, col);
                WQUF.union(index, right_site_index);
            }
        }

        // if the upper site is open connect to it
        if( ((row - 1) <= border) && ((row - 1) >= 1) ){
            if(isOpen(row - 1, col) == true){
                int upper_site_index = convert_to_1d_index(row - 2, col - 1);
                WQUF.union(index, upper_site_index);
            }
        }

        // if the down site is open connect to it
        if( ((row + 1) <= border) && ((row + 1) > 1) ){
            if(isOpen(row + 1, col) == true){
                int down_site_index = convert_to_1d_index(row, col - 1);
                WQUF.union(index, down_site_index);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if((row < 1 || row > border) || (col < 1 || col > border))
            throw new IllegalArgumentException("row or column is out of border");
        int index = convert_to_1d_index(row - 1, col - 1);
        return sites[index] == true;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if((row < 1 || row > border) || (col < 1 || col > border))
            throw new IllegalArgumentException("row or column is out of border");
        int index = convert_to_1d_index(row - 1, col - 1);
        return WQUF.connected(index, virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return number_of_open_sites;
    }

    // does the system percolate?
    public boolean percolates(){
        return WQUF.connected(virtualTop, virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(4);
        percolation.open(1, 4);
        percolation.open(2, 4);
        percolation.open(3, 4);
        percolation.open(4, 4);

        System.out.println(percolation.percolates());
    }
}
