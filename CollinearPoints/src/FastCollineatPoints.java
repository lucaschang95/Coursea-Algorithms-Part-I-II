import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPoints {

    private int num;
    private int sz;
    private double[] slopesArray;

    private ArrayList<LineSegment> list;


    /**
     * 1. scan each input array: points[i]
     * 2. check if collinear with other points
     * 3. output the line segments to a specific arraylist
     *
     */
    public FastCollinearPoints(Point[] inPoints) {
        list = new ArrayList<>();
        num = 0;
        sz = inPoints.length;
        slopesArray = new double[sz];

        Point[] points = inPoints;
        Merge.sort(points);



        for (int i = 0; i < sz-3; i++) {
            for (int j = 0; j < sz; j++) {
                slopesArray[j] = points[i].slopeTo(points[j]);
            }
            double[] aux = Arrays.copyOf(slopesArray, sz);
            Arrays.sort(aux, i+1, sz);
            for (int j = i+1; j < sz-2; j++) {
                if (findSegment(aux, i, j)) {
                    for (int k = sz-1; k > i; k--) {
                        if (slopesArray[k] == aux[j]) {
                            list.add(new LineSegment(points[i], points[k]));
                            num++;
                            break;
                        }
                    }
                }
            }

        }
    }

    /**
     * based on sorted slope array: aux
     *
     * condition: 1.
     * @return
     */
    private boolean findSegment(double[] auxArray, int devide, int current) {
         boolean hasSameBefore = false;
         boolean isFirst;
         boolean isCollinear;
         for (int i = 0; i < devide; i++) {
             if (auxArray[i] == auxArray[current]) {
                 hasSameBefore = true;
                 break;
             }
         }
         isFirst = ((current-1) == devide) || auxArray[current] != auxArray[current-1];
         isCollinear = (auxArray[current] == auxArray[current + 2]);
         return (!hasSameBefore) && isFirst && isCollinear;
    }


    public  int numberOfSegments() {
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