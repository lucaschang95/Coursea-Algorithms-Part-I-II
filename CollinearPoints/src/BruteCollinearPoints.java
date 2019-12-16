import java.util.ArrayList;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {

    private int num;
    private final ArrayList<LineSegment> list;
    private final int sz;
    private Point[] points;

    public BruteCollinearPoints (Point[] inPoints) {
        if (inPoints == null) throw new IllegalArgumentException();
        sz = inPoints.length;
        num = 0;
        list = new ArrayList<>();
        points = new Point[sz];

        for (int i = 0; i < sz; i++) {
            if (inPoints[i] == null) { throw new IllegalArgumentException(); }
        }

        for (int i = 0; i < sz-1;i++) {
            for (int j = i+1; j < sz; j++) {
                if (inPoints[i].compareTo(inPoints[j]) == 0) throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < inPoints.length; i++) {
            points[i] = inPoints[i];
        }

        Merge.sort(points);

        for (int a = 0; a < sz; a++) {
            for (int b = a + 1; b < sz; b++) {
                for (int c = b + 1; c < sz; c++) {
                    if (points[a].slopeTo(points[b]) == points[a].slopeTo(points[c])) {
                        for (int d = c + 1; d < sz; d++) {
                            if (points[a].slopeTo(points[b]) == points[a].slopeTo(points[d])) {
                                list.add(new LineSegment(points[a], points[d]));
                                num++;
                            }
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return num;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegmentArray = new LineSegment[num];
        for (int i = 0; i < num; i++) {
            lineSegmentArray[i] = list.get(i);
        }
        return lineSegmentArray;
    }

    public static void main(String[] args) {

    }
}