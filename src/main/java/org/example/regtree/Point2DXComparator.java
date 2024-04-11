package org.example.regtree;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * Class for comparing points by the rule: if x coordinate of point 1 is larger than of point 2, it's larger,
 * if x coordinates are equal then compare by y coordinates
 */
public class Point2DXComparator implements Comparator<Point2D> {

    @Override
    public int compare(Point2D o1, Point2D o2) {
        if(o1.getX() > o2.getX())
            return 1;
        if(o1.getX() < o2.getX())
            return -1;
        return Double.compare(o1.getY(), o2.getY());
    }
}
