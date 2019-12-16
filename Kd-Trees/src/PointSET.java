import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import java.util.Iterator;

public class PointSET {

    private final SET<Point2D> pointSet;

    public PointSET() {                            // construct an empty set of points
        pointSet = new SET<>();
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return  pointSet.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calling insert() with null argument");
        if (!contains(p)) pointSet.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calling contains() with null argument");
        return pointSet.contains(p);
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        Iterator<Point2D> pointIterator = pointSet.iterator();
        Point2D current;
        while (pointIterator.hasNext()) {
            current = pointIterator.next();
            current.draw();
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("calling range() with null argument");
        Queue<Point2D> queue = new Queue<>();
        Iterator<Point2D> pointIterator = pointSet.iterator();
        Point2D current;
        while (pointIterator.hasNext()) {
            current = pointIterator.next();
            if (rect.contains(current)) {
                queue.enqueue(current);
            }
        }
        return queue;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calling nearest() with null argument");
        if (pointSet.isEmpty()) return null;
        double minSquaredD = 2.0;
        Point2D current;
        Point2D neighbor = null;
        Iterator<Point2D> pointIterator = pointSet.iterator();
        while (pointIterator.hasNext()) {
            current = pointIterator.next();
            if (current.distanceSquaredTo(p) <= minSquaredD) {
                minSquaredD = current.distanceSquaredTo(p);
                neighbor = current;
            }
        }
        return neighbor;
    }

    public static void main(String[] args) {
    }
}