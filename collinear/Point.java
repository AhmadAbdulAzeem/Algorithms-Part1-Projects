/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {

        double slope;
        // Treat the slope of a horizontal line segment as positive zero
        if ((this.y == that.y) && (this.compareTo(that) != 0)) {
            slope = 0.0;
        }
        // Treat the slope of a vertical line segment as positive infinity
        else if ((this.x == that.x) && (this.compareTo(that) != 0)) {
            slope = Double.POSITIVE_INFINITY;
        }
        // Treat the slope of a degenerate line segment (between a point and itself) as negative infinity
        else if (this.compareTo(that) == 0) {
            slope = Double.NEGATIVE_INFINITY;
        }
        // Slope
        else {
            slope = (double) (that.y - this.y) / (that.x - this.x);
        }

        return slope;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        if ((this.y < that.y) || ((this.y == that.y) && (this.x < that.x)))
            return -1;
        else if ((this.x == that.x) && (this.y == that.y))
            return 0;
        else
            return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        class SlopeOrder implements Comparator<Point> {
            public int compare(Point p1, Point p2) {
                double slope1 = slopeTo(p1);
                double slope2 = slopeTo(p2);
                return Double.compare(slope1, slope2);
            }
        }
        Comparator<Point> SLOPE_ORDER = new SlopeOrder();
        return SLOPE_ORDER;
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        Point p1 = new Point(1, 1);
        Point p2 = new Point(1, 1);

        int compare_reslt;
        double slope;
        double slope_order;

        compare_reslt = p1.compareTo(p2);
        slope = p1.slopeTo(p2);
        assert (compare_reslt == 0);
        assert (slope == Double.NEGATIVE_INFINITY);     // slope between a point and itself

        Point p3 = new Point(3, 5);
        Point p4 = new Point(4, 6);
        compare_reslt = p3.compareTo(p4);
        slope = p3.slopeTo(p4);
        assert (compare_reslt == -1);
        assert (slope > 0);                             // +ve slope

        Point p5 = new Point(3, 6);
        Point p6 = new Point(5, 6);
        compare_reslt = p5.compareTo(p6);
        slope = p5.slopeTo(p6);
        assert (compare_reslt == -1);
        assert (slope == 0.0);                          // slope of a horizontal line segment

        Point p7 = new Point(3, 7);
        Point p8 = new Point(5, 6);
        compare_reslt = p7.compareTo(p8);
        slope = p7.slopeTo(p8);
        assert (compare_reslt == 1);
        assert (slope < 0);                             // -ve slope

        Point p9 = new Point(8, 2);
        Point p10 = new Point(8, 6);
        slope = p9.slopeTo(p10);
        assert (slope == Double.POSITIVE_INFINITY);     // slope of a vertical line segment

        // test slopeOrder()
        slope_order = p1.slopeOrder().compare(p7, p8);
        assert (slope_order > 0);

        slope_order = p1.slopeOrder().compare(p8, p7);
        assert (slope_order < 0);

        System.out.println("All tests passed!");
    }
}
