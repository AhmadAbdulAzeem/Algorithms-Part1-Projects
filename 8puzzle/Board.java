import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public final class Board {
    // Private inner Pair class
    private class Pair {
        private final int row;
        private final int col;

        public Pair(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    private final int[][] board;
    private final int N;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    // Constructor.  You may assume that the constructor receives an n-by-n array containing the n2 integers between 0 and n2 − 1, where 0 represents the blank square. You may also assume that 2 ≤ n < 128.
    public Board(int[][] tiles) {
        this.N = tiles[0].length;
        this.board = new int[this.N][this.N];
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                this.board[row][col] = tiles[row][col];
            }
        }
    }

    private int[][] getTiles() {
        int[][] copy = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            System.arraycopy(this.board[i], 0, copy[i], 0, this.N);
        }
        return copy;
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.N + "\n");
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                sb.append(this.board[row][col] + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return this.N;
    }

    // helper function for calculating hamming distance
    // get the grid value based on its location as if the board is in the right position
    private int calculateTileValue(int row, int col) {
        return (row * this.N) + col + 1;
    }

    // Method to return a row col Pair of goal tile value
    private Pair getRowColPair(int goalTileValue) {
        int gRow = 0, gCol = 0;
        boolean found = false;
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (calculateTileValue(row, col) == goalTileValue) {
                    gRow = row;
                    gCol = col;
                    found = true;
                    break;
                }
            }
            if (found)
                break;
        }
        return new Pair(gRow, gCol);
    }

    // number of board out of place
    // The Hamming distance between a board and the goal board is the number of board in the wrong position.
    public int hamming() {
        int numberOfWrongTiles = 0;
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.board[row][col] == 0)
                    continue;
                if (this.board[row][col] != calculateTileValue(row, col))
                    numberOfWrongTiles++;
            }
        }

        return numberOfWrongTiles;
    }

    // sum of Manhattan distances between board and goal
    // The Manhattan distance between a board and the goal board is the sum of the Manhattan distances (sum of the vertical and horizontal distance) from the board to their goal positions.
    // Manhattan distance = |r - r_goal| + |c - c_goal|
    public int manhattan() {
        int manhattanDistance = 0;
        List<Pair> wrongPairs = new ArrayList<>();
        List<Pair> goalPairs = new ArrayList<>();
        Pair goalPair, wrongPair;

        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.board[row][col] == 0)
                    continue;
                if (this.board[row][col] != calculateTileValue(row, col)) {
                    wrongPairs.add(new Pair(row, col));
                    goalPairs.add(getRowColPair(this.board[row][col]));
                }
            }
        }

        for (int i = 0; i < goalPairs.size(); i++) {
            goalPair = goalPairs.get(i);
            wrongPair = wrongPairs.get(i);

            manhattanDistance += Math.abs(wrongPair.getRow() - goalPair.getRow()) + Math.abs(
                    wrongPair.getCol() - goalPair.getCol());
        }

        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.board[row][col] == 0)
                    continue;
                if (this.board[row][col] != calculateTileValue(row, col))
                    return false;
            }
        }
        return true;
    }

    private boolean areTilesInSamePosition(Board otherBoard) {
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.board[row][col] != otherBoard.getTiles()[row][col])
                    return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board board = (Board) y;
        //  Two boards are equal if they have the same size and their corresponding tiles are in the same positions.
        return this.N == board.dimension() && this.areTilesInSamePosition(board);
    }

    // all neighboring boards
    // The neighbors() method returns an iterable containing the neighbors of the board. Depending on the location of the blank square, a board can have 2, 3, or 4 neighbors.
    // Iterable<Board> This means that the Iterable will specifically contain objects of type Board. This is a way to specify the type of objects that the Iterable will contain.
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>();
        int blankRow = 0, blankCol = 0;
        boolean found = false;

        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.board[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                    found = true;
                    break;
                }
            }
            if (found)
                break;
        }

        // Possible moves: up, down, left, right
        int[][] directions = {
                { -1, 0 }, // up
                { 1, 0 },  // down
                { 0, -1 }, // left
                { 0, 1 }   // right
        };

        for (int[] direction : directions) {
            int newRow = blankRow + direction[0];
            int newCol = blankCol + direction[1];

            if (newRow >= 0 && newRow < this.N && newCol >= 0 && newCol < this.N) {
                int[][] newTiles = this.getTiles();
                int temp = newTiles[blankRow][blankCol];
                newTiles[blankRow][blankCol] = newTiles[newRow][newCol];
                newTiles[newRow][newCol] = temp;
                neighbors.add(new Board(newTiles));
            }
        }
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of board
    public Board twin() {
        int[][] tiles = this.getTiles();
        int row1 = 0, col1 = 0, row2 = 0, col2 = 0;
        boolean foundFirst = false;
        boolean foundSecond = false;
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (tiles[row][col] != 0) {
                    if (!foundFirst) {
                        row1 = row;
                        col1 = col;
                        foundFirst = true;
                    }
                    else {
                        row2 = row;
                        col2 = col;
                        foundSecond = true;
                        break;
                    }
                }
            }
            if (foundSecond) break;
        }
        int temp = tiles[row1][col1];
        tiles[row1][col1] = tiles[row2][col2];
        tiles[row2][col2] = temp;
        return new Board(tiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read in the board specified in the filename
        In in = new In("test.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board board = new Board(tiles);

        // read in the board specified in the filename
        In in2 = new In("puzzle3x3-10.txt");
        int n2 = in2.readInt();
        int[][] tiles2 = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles2[i][j] = in2.readInt();
            }
        }

        Board board2 = new Board(tiles2);

        Board board3 = board.twin();
        StdOut.println(
                "**********************************Board1**********************************");
        StdOut.println(board.toString());

        StdOut.println(
                "**********************************Neighbors**********************************");
        List<Board> neighbors = (List<Board>) board.neighbors();
        for (Board neighbor : neighbors) {
            StdOut.println(neighbor.toString());
        }

        StdOut.println("**********************************Twin**********************************");
        StdOut.println(board3.toString());

        StdOut.println(
                "**********************************Board2**********************************");
        StdOut.println(board2.toString());

        StdOut.println(
                "**********************************Neighbors2**********************************");
        List<Board> neighbors2 = (List<Board>) board2.neighbors();
        for (Board neighbor : neighbors2) {
            StdOut.println(neighbor.toString());
        }

        int hammingDistance = board.hamming();
        StdOut.println(hammingDistance);
        assert (hammingDistance == 5);

        int manhattanDistance = board.manhattan();
        StdOut.println(manhattanDistance);
        assert (manhattanDistance == 10);

        assert (!board.isGoal());

        assert (board.equals(board));
        assert (!board.equals(5));
        assert (!board.equals(board2));
    }

}
