/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final Point[] points;
    private int numberOfSegments = 0;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null) {
            // throw an IllegalArgumentException if the argument to the constructor is null
            throw new IllegalArgumentException("The input points array is null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException(
                        "The input points array contains a null element");
            }
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException(
                            "The input points array contains a repeated point");
                }
            }
        }

        this.points = points.clone();
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        ArrayList<LineSegment> lineSegments = new ArrayList<>();
        int numberOfPoints = points.length;

        for (int p = 0; p < numberOfPoints; p++) {
            for (int q = p + 1; q < numberOfPoints; q++) {
                for (int r = q + 1; r < numberOfPoints; r++) {
                    for (int s = r + 1; s < numberOfPoints; s++) {
                        // To check whether the 4 points p, q, r, and s are collinear, check
                        // whether the three slopes between p and q, between p and r, and between p and s are all equal.
                        double slopePQ = points[p].slopeTo(points[q]);
                        double slopePR = points[p].slopeTo(points[r]);
                        double slopePS = points[p].slopeTo(points[s]);

                        if (slopePQ == slopePR && slopePR == slopePS) {
                            numberOfSegments++;
                            Point[] collinearPoints = {
                                    points[p], points[q], points[r], points[s]
                            };
                            Arrays.sort(collinearPoints);
                            lineSegments.add(
                                    new LineSegment(collinearPoints[0], collinearPoints[3]));
                        }
                    }
                }
            }
        }

        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
