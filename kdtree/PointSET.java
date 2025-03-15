/* *****************************************************************************
 *  Name: Ahmed AbdulAzeem
 *  Date: 1-03-2025
 *  Description: Class represents a set of points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> PointSet;

    public PointSET()                               // construct an empty set of points
    {
        PointSet = new SET<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return PointSet.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return PointSet.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        PointSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        return PointSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D p : PointSet) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) {
            throw new IllegalArgumentException("Rectangle is null");
        }
        SET<Point2D> rangeSet = new SET<Point2D>();
        for (Point2D p : PointSet) {
            if (rect.contains(p)) {
                rangeSet.add(p);
            }
        }
        return rangeSet;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException("Rectangle is null");
        }
        Point2D nearestPoint = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (Point2D point : PointSet) {
            double distance = p.distanceSquaredTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        // unit testing of the methods (optional)
        PointSET pointSet = new PointSET();
        Point2D p1 = new Point2D(0.1, 0.2);
        Point2D p2 = new Point2D(0.3, 0.4);
        Point2D p3 = new Point2D(0.5, 0.6);
        Point2D p4 = new Point2D(0.7, 0.8);
        Point2D p5 = new Point2D(0.9, 0.1);
        Point2D p6 = new Point2D(0.2, 0.3);
        Point2D p7 = new Point2D(0.4, 0.5);
        Point2D p8 = new Point2D(0.6, 0.7);
        Point2D p9 = new Point2D(0.8, 0.9);
        Point2D p10 = new Point2D(0.2, 0.2);
        pointSet.insert(p1);
        pointSet.insert(p2);
        pointSet.insert(p3);
        pointSet.insert(p4);
        pointSet.insert(p5);
        pointSet.insert(p6);
        pointSet.insert(p7);
        pointSet.insert(p8);
        pointSet.insert(p9);
        pointSet.insert(p10);

        // pointSet.draw();

        RectHV rect = new RectHV(0.1, 0.1, 0.5, 0.5);
        Iterable<Point2D> range = pointSet.range(rect);
        for (Point2D p : range) {
            System.out.println(p);
        }

        Point2D nearestPoint = pointSet.nearest(p8);
        System.out.println(nearestPoint);

    }
}
