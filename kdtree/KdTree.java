/* *****************************************************************************
 *  Name: Ahmed AbdulAzeem
 *  Date: 4-03-2025
 *  Description: Class represents a set of points in the unit square.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;

    private class Node {
        private double keyX, keyY;
        private Node left, right;
        private int count;

        public Node(Point2D p, int count) {
            keyX = p.x();
            keyY = p.y();
            this.count = count;
            left = null;
            right = null;
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        root = null;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    // Search and insert. The algorithms for search and insert are similar to those for BSTs,
    // but at the root we use the x-coordinate (if the point to be inserted has a smaller x-coordinate than the point at the root, go left; otherwise go right);
    // then at the next level, we use the y-coordinate (if the point to be inserted has a smaller y-coordinate than the point in the node, go left; otherwise go right);
    // then at the next level the x-coordinate, and so forth.
    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) {
            throw new IllegalArgumentException("Point is null");
        }
        root = insert(root, p, true);
    }

    private Node insert(Node x, Point2D p, boolean isVertical) {
        int cmp;
        if (x == null) return new Node(p, 1);
        if (isVertical)
            cmp = Double.compare(p.x(), x.keyX);
        else
            cmp = Double.compare(p.y(), x.keyY);
        if (cmp < 0)
            x.left = insert(x.left, p, !isVertical);
        else
            x.right = insert(x.right, p, !isVertical);
        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Point is null");
        Node x = root;
        boolean isVertical = true;
        int cmp;
        while (x != null) {
            if (p.equals(new Point2D(x.keyX, x.keyY))) return true;
            if (isVertical) {
                cmp = Double.compare(p.x(), x.keyX);
                isVertical = !isVertical;
            }
            else {
                cmp = Double.compare(p.y(), x.keyY);
                isVertical = !isVertical;
            }
            if (cmp < 0)
                x = x.left;
            else
                x = x.right;
        }
        return false;
    }

    // Draw. A 2d-tree divides the unit square in a simple way: all the points to the left of the root go in the left subtree; all those to the right go in the right subtree; and so forth,
    // recursively. Your draw() method should draw all of the points to standard draw in black and the subdivisions in red (for vertical splits) and blue (for horizontal splits).
    // This method need not be efficient—it is primarily for debugging.
    public void draw()                         // draw all points to standard draw
    {
        draw(root, new RectHV(0, 0, 1, 1), true);
    }

    private void draw(Node x, RectHV rectHV, boolean isVertical) {
        if (x == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        StdDraw.point(x.keyX, x.keyY);

        StdDraw.setPenRadius(0.001);
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.keyX, rectHV.ymin(), x.keyX, rectHV.ymax());
            draw(x.left, new RectHV(rectHV.xmin(), rectHV.ymin(), x.keyX, rectHV.ymax()),
                 !isVertical);
            draw(x.right, new RectHV(x.keyX, rectHV.ymin(), rectHV.xmax(), rectHV.ymax()),
                 !isVertical);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rectHV.xmin(), x.keyY, rectHV.xmax(), x.keyY);
            draw(x.left, new RectHV(rectHV.xmin(), rectHV.ymin(), rectHV.xmax(), x.keyY),
                 !isVertical);
            draw(x.right, new RectHV(rectHV.xmin(), x.keyY, rectHV.xmax(), rectHV.ymax()),
                 !isVertical);
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Rectangle is null");
        SET<Point2D> rangeSet = new SET<Point2D>();
        range(root, rect, rangeSet, true);
        return rangeSet;
    }

    private void range(Node x, RectHV rect, SET<Point2D> rangeSet, boolean isVertical) {
        if (x == null) return;
        if (rect.contains(new Point2D(x.keyX, x.keyY)))
            rangeSet.add(new Point2D(x.keyX, x.keyY));
        if (isVertical) {
            if (rect.xmax() < x.keyX)
                range(x.left, rect, rangeSet, !isVertical);
            else if (rect.xmin() > x.keyX)
                range(x.right, rect, rangeSet, !isVertical);
            else {
                range(x.left, rect, rangeSet, !isVertical);
                range(x.right, rect, rangeSet, !isVertical);
            }
        }
        else {
            if (rect.ymax() < x.keyY)
                range(x.left, rect, rangeSet, !isVertical);
            else if (rect.ymin() > x.keyY)
                range(x.right, rect, rangeSet, !isVertical);
            else {
                range(x.left, rect, rangeSet, !isVertical);
                range(x.right, rect, rangeSet, !isVertical);
            }
        }
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Point is null");
        if (isEmpty()) return null;
        return nearest(root, p, true);
    }

    // Nearest-neighbor search. To find a closest point to a given query point, start at the root and recursively search in both subtrees using the following pruning rule:
    // if the closest point discovered so far is closer than the distance between the query point and the rectangle corresponding to a node,
    // there is no need to explore that node (or its subtrees). That is, search a node only if it might contain a point that is closer than the best one found so far.
    // The effectiveness of the pruning rule depends on quickly finding a nearby point. To do this, organize the recursive method so that when there are two possible subtrees to go down,
    // you always choose the subtree that is on the same side of the splitting line as the query point as the first subtree to explore—the closest point found while exploring the first subtree may enable pruning of the second subtree.
    private Point2D nearest(Node x, Point2D p, boolean isVertical) {
        if (x == null) return null;
        Point2D nearestPoint = new Point2D(x.keyX, x.keyY);
        double nearestDistance = p.distanceSquaredTo(nearestPoint);

        Node first, second;
        if ((isVertical && p.x() < x.keyX) || (!isVertical && p.y() < x.keyY)) {
            first = x.left;
            second = x.right;
        }
        else {
            first = x.right;
            second = x.left;
        }

        Point2D candidate = nearest(first, p, !isVertical);
        if (candidate != null && p.distanceSquaredTo(candidate) < nearestDistance) {
            nearestPoint = candidate;
            nearestDistance = p.distanceSquaredTo(candidate);
        }

        RectHV rect;
        if (isVertical) {
            rect = new RectHV(x.keyX, 0, x.keyX, 1);
        }
        else {
            rect = new RectHV(0, x.keyY, 1, x.keyY);
        }

        if (second != null && rect.distanceSquaredTo(p) < nearestDistance) {
            candidate = nearest(second, p, !isVertical);
            if (candidate != null && p.distanceSquaredTo(candidate) < nearestDistance) {
                nearestPoint = candidate;
            }
        }

        return nearestPoint;
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        // unit testing of the methods (optional)
        KdTree pointSet = new KdTree();
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);
        pointSet.insert(p1);
        pointSet.insert(p2);
        pointSet.insert(p3);
        pointSet.insert(p4);
        pointSet.insert(p5);

        System.out.println("Size: " + pointSet.size());
        System.out.println("Contains p1: " + pointSet.contains(p1));
        System.out.println("Contains p2: " + pointSet.contains(p2));
        System.out.println("Contains p3: " + pointSet.contains(p3));
        System.out.println("Contains p3: " + pointSet.contains(new Point2D(0.7, 0.6)));

        RectHV rect = new RectHV(0.1, 0.1, 0.6, 0.6);
        SET<Point2D> rangeSet = new SET<Point2D>();
        rangeSet = (SET<Point2D>) pointSet.range(rect);
        System.out.println("Range: ");
        for (Point2D p : rangeSet) {
            System.out.println(p);
        }

        Point2D nearestPoint = pointSet.nearest(new Point2D(0.8, 0.8));
        System.out.println("Nearest point: " + nearestPoint);

        pointSet.draw();
    }
}
