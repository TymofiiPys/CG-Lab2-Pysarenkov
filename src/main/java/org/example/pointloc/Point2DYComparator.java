package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Comparator;

/**
 * Class for comparing points by the rule: if y coordinate of point 1 is larger than of point 2, it's larger,
 * if y coordinates are equal then compare by x coordinates
 */
public class Point2DYComparator implements Comparator<Point2D> {

    @Override
    public int compare(Point2D o1, Point2D o2) {
        if(o1.getY() > o2.getY())
            return 1;
        if(o1.getY() < o2.getY())
            return -1;
        return Double.compare(o1.getX(), o2.getX());
    }
}
