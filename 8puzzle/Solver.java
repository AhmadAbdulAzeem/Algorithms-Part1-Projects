import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.List;

public final class Solver {
    private SearchNode lastNode;
    private boolean isSolvable;
    private MinPQ<SearchNode> searchNodes;

    // find a solution to the initial board (using the A* algorithm)
    //  First, insert the initial search node (the initial board, 0 moves, and a null previous search node) into a priority queue.
    // Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all neighboring search nodes
    //  Repeat this procedure until the search node dequeued corresponds to the goal board.
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Arguments cannot be null");
        lastNode = null;
        searchNodes = new MinPQ<>();
        searchNodes.insert(new SearchNode(initial, 0, null));

        while (true) {
            SearchNode minNode = searchNodes.delMin();
            if (minNode.getBoard().isGoal()) {
                this.isSolvable = true;
                this.lastNode = minNode;
                break;
            }

            if (minNode.getBoard().hamming() == 2 && minNode.getBoard().twin().isGoal()) {
                isSolvable = false;
                break;
            }

            for (Board neighbor : minNode.getBoard().neighbors()) {
                if (minNode.getPrevious() == null || !neighbor.equals(
                        minNode.getPrevious().getBoard())) {
                    searchNodes.insert(new SearchNode(neighbor, minNode.getMoves() + 1, minNode));
                }
            }
        }
    }

    private final class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPrevious() {
            return previous;
        }

        public int compareTo(SearchNode other) {
            return Integer.compare((this.getBoard().manhattan() + this.getMoves()),
                                   (other.getBoard().manhattan() + other.getMoves()));
        }
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return isSolvable;
    }


    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) {
            return -1;
        }
        return lastNode.getMoves();
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        List<Board> boards = new ArrayList<>();
        SearchNode node = this.lastNode;
        while (node != null) {
            boards.add(0, node.getBoard());
            node = node.getPrevious();
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        In in = new In("puzzle3x3-unsolvable.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        Board board_unsolvable = new Board(tiles);

        In in2 = new In("puzzle3x3-00.txt");
        int n2 = in2.readInt();
        int[][] tiles2 = new int[n][n];
        for (int i = 0; i < n2; i++) {
            for (int j = 0; j < n2; j++) {
                tiles2[i][j] = in2.readInt();
            }
        }

        Board board_solvable = new Board(tiles2);

        assert (new Solver(board_unsolvable).isSolvable() == false);
        assert (new Solver(board_solvable).isSolvable() == true);

        Solver solver = new Solver(board_solvable);
        System.out.println(solver.moves());
        for (Board board : solver.solution()) {
            System.out.println(board.toString());
        }


        In in3 = new In("puzzle3x3-12.txt");
        int n3 = in3.readInt();
        int[][] tiles3 = new int[n3][n3];
        for (int i = 0; i < n3; i++) {
            for (int j = 0; j < n3; j++) {
                tiles3[i][j] = in3.readInt();
            }
        }

        Board board_solvable_2 = new Board(tiles3);

        Solver solver2 = new Solver(board_solvable_2);
        System.out.println(solver2.moves());
        for (Board board : solver2.solution()) {
            System.out.println(board.toString());
        }
    }

}
