import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final Point[] points;
    private int numberOfSegments = 0;

    public FastCollinearPoints(Point[] points)    // finds all line segments containing 4 points
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
            Point[] sortedPoints = points.clone();
            Arrays.sort(sortedPoints);
            Arrays.sort(sortedPoints, points[p].slopeOrder());

            int q = 1;
            while (q < numberOfPoints) {
                ArrayList<Point> collinearPoints = new ArrayList<>();
                double slopePQ = sortedPoints[0].slopeTo(sortedPoints[q]);
                collinearPoints.add(sortedPoints[0]);
                collinearPoints.add(sortedPoints[q]);

                int r = q + 1;
                while (r < numberOfPoints) {
                    double slopePR = sortedPoints[0].slopeTo(sortedPoints[r]);
                    if (slopePQ == slopePR) {
                        collinearPoints.add(sortedPoints[r]);
                    }
                    else {
                        break;
                    }
                    r++;
                }

                if (collinearPoints.size() >= 4) {
                    Point[] collinearPointsArray = collinearPoints.toArray(new Point[0]);
                    Arrays.sort(collinearPointsArray);
                    if (sortedPoints[0].compareTo(collinearPointsArray[0]) == 0) {
                        lineSegments.add(new LineSegment(collinearPointsArray[0],
                                                         collinearPointsArray[
                                                                 collinearPointsArray.length - 1]));
                        numberOfSegments++;
                    }
                }

                q = r;
            }
        }

        return lineSegments.toArray(new LineSegment[0]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
