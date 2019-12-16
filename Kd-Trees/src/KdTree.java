import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class KdTree {
    private Node root;

    private int size;
    private Queue<Point2D> rangeQueue;
    private Point2D neighbor;

    private static class Node {
        private Point2D p;
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
        }
    }

    public KdTree() {
        size = 0;
    }                              // construct an empty set of points

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     * @param p Point2D ready to be inserted
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calling insert() with a null key");
        if (!contains(p)) {
            root = insert(root, p, true);
            size++;
        }
    }

    private Node insert (Node node, Point2D p, boolean splitHorizontal) {
        if (p == null) throw new IllegalArgumentException("calling insert() with a null key");
        if (node == null) return new Node(p);
        int cmp;
        if (splitHorizontal) cmp = Point2D.X_ORDER.compare(p, node.p);
        else cmp = Point2D.Y_ORDER.compare(p, node.p);

        if (cmp < 0) node.lb  = insert(node.lb,  p, !splitHorizontal);
        else node.rt  = insert(node.rt,  p, !splitHorizontal);
        return node;
    }

    public boolean contains(Point2D p) {            // does the set contain point p?
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(p) != null;
    }

    /**
     * check whether Point2D p in the KdTree. True: return p, False: return null
     * @param p
     * @return
     */
    private Point2D get(Point2D p) {
        return get(root, p, true);
    }

    private Point2D get(Node node, Point2D p, boolean splitHorizontal) {
        if (p == null) throw new IllegalArgumentException("calling get() with a null key");
        if (node == null) return null;
        if (p.compareTo(node.p) == 0) return p;
        int cmp;
        if (splitHorizontal) cmp = Point2D.X_ORDER.compare(p, node.p);
        else cmp = Point2D.Y_ORDER.compare(p, node.p);

        if (cmp < 0) return get(node.lb, p, !splitHorizontal);
        else         return get(node.rt, p, !splitHorizontal);
    }

    public void draw() {                      // draw all points to standard draw
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        RectHV r = new RectHV(0, 0, 1, 1);
        r.draw();
        drawPoints(root, r, true);
    }

    private void drawPoints(Node node, RectHV nodeRect, boolean splitHorizontal) {
        while (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            node.p.draw();
            if (splitHorizontal) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), nodeRect.ymin(), node.p.x(), nodeRect.ymax());
            } 
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(nodeRect.xmin(), node.p.y(), nodeRect.xmax(), node.p.y());
            }
            drawPoints(node.lb, getRect(node.p, nodeRect, !splitHorizontal, true), !splitHorizontal);
            drawPoints(node.rt, getRect(node.p, nodeRect, !splitHorizontal, false), !splitHorizontal);
        }
    }

    /**
     * all points that are inside the rectangle (or on the boundary)
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("calling range() with null argument");
        rangeQueue = new Queue<>();
        if (root == null) return rangeQueue;
        RectHV r = new RectHV(0, 0, 1, 1);
        rangeFind(root, r, rect, false);
        return rangeQueue;
    }

    /**
     * querying node and its subtrees' node whether falling into the {@code rect}
     * @param node current Node
     * @param nodeRect rectangle corresponding to {@code node}
     * @param rect query rectangle
     * @param splitHorizontal line corresponding to {@code node} is vertical or not?
     */
    private void rangeFind(Node node, RectHV nodeRect, RectHV rect, boolean splitHorizontal) {
        if (rect.intersects(nodeRect)) {
            if (rect.contains(node.p)) rangeQueue.enqueue(node.p);
            if (node.lb != null) rangeFind(node.lb, getRect(node.p, nodeRect, !splitHorizontal, true), rect, !splitHorizontal);
            if (node.rt != null) rangeFind(node.rt, getRect(node.p, nodeRect, !splitHorizontal, false), rect, !splitHorizontal);
        }
    }

    /**
     * get the node corresponding rectangle devided by point p
     * @param p devided point
     * @param nodeRect father node's corresponding rectangle (being devided)
     * @param splitHorizontal p line is vertical
     * @param isLB wishing part is left/bottom or not
     * @return
     */
    private RectHV getRect(Point2D p, RectHV nodeRect, boolean splitHorizontal, boolean isLB) {
        if (splitHorizontal && isLB) return new RectHV(nodeRect.xmin(), nodeRect.ymin(), p.x(), nodeRect.ymax());
        else if (splitHorizontal)    return new RectHV(p.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
        else if (isLB)            return new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), p.y());
        else                      return new RectHV(nodeRect.xmin(), p.y(), nodeRect.xmax(), nodeRect.ymax());
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calling nearest() with null argument");
        if (root == null) return null;
        else neighbor = root.p;
        RectHV r = new RectHV(0, 0, 1, 1);
        nearestFind(p, root, r, false);
        return neighbor;
    }

    /**
     * querying node and its subtrees' node is the potential neighbor node
     * @param p querying point
     * @param node
     * @param nodeRect
     * @param splitHorizontal
     */
    private void nearestFind(Point2D p, Node node, RectHV nodeRect, boolean splitHorizontal) {
        if (!nodeRect.contains(p) && nodeRect.distanceSquaredTo(p) > p.distanceSquaredTo(neighbor)) return;
        if (p.distanceSquaredTo(node.p) < p.distanceSquaredTo(neighbor)) neighbor = node.p;
        int cmp;
        if (!splitHorizontal) cmp = Point2D.X_ORDER.compare(p, node.p);
        else cmp = Point2D.Y_ORDER.compare(p, node.p);
        if (cmp >= 0){
            if (node.rt != null) nearestFind(p, node.rt, getRect(node.p, nodeRect, !splitHorizontal, false), !splitHorizontal);
            if (node.lb != null) nearestFind(p, node.lb, getRect(node.p, nodeRect, !splitHorizontal, true), !splitHorizontal);
        } else {
            if (node.lb != null) nearestFind(p, node.lb, getRect(node.p, nodeRect, !splitHorizontal, true), !splitHorizontal);
            if (node.rt != null) nearestFind(p, node.rt, getRect(node.p, nodeRect, !splitHorizontal, false), !splitHorizontal);
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        Iterable<Point2D> queue;
        queue = tree.range(new RectHV(0.38, 0.6, 0.62, 0.72));
    }
}