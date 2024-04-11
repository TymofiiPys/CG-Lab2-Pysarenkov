package org.example.regtree;

import junit.framework.TestCase;
import org.example.pointloc.Point2DYComparator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class RTree2DTest extends TestCase {
    public void testCreationSortedPoints() {
        ArrayList<Point2D.Double> points = new ArrayList<>();
        points.add(new Point2D.Double(1, 4));
        points.add(new Point2D.Double(2, 1.5));
        points.add(new Point2D.Double(2.5, 3));
        points.add(new Point2D.Double(3.5, 4.5));
        points.add(new Point2D.Double(5, 2));
        points.add(new Point2D.Double(5.5, 5.7));
        points.add(new Point2D.Double(6.5, 0.6));
        points.add(new Point2D.Double(7, 3.7));
        points.add(new Point2D.Double(8, 4.7));
        points.add(new Point2D.Double(9.5, 2.5));

        RTree2D rtree = new RTree2D(points);
        assertEquals(10, rtree.getN());

        ArrayList<RTree2D.Node> nodes = rtree.nodesPreOrder();
        assertEquals(new Interval(1.0, 11.0), nodes.get(0).getInterval());
        assertEquals(new Interval(2.0, 3.0), nodes.get(4).getInterval());
        assertEquals(new Interval(3.0, 6.0), nodes.get(5).getInterval());
        assertEquals(new Interval(6.0, 11.0), nodes.get(10).getInterval());
        assertEquals(new Interval(10.0, 11.0), nodes.getLast().getInterval());

        TreeSet<Point2D.Double> treeOfNode = new TreeSet<>(new Point2DYComparator());
        treeOfNode.addAll(points);
        assertEquals(treeOfNode.stream().toList(), nodes.get(0).getPointsInInterval().stream().toList());
        treeOfNode.clear();
        treeOfNode.addAll(List.of(points.get(1),
                points.get(4),
                points.get(2),
                points.get(0),
                points.get(3)));
        assertEquals(treeOfNode.stream().toList(), nodes.get(1).getPointsInInterval().stream().toList());
        treeOfNode.clear();
        treeOfNode.addAll(List.of(points.get(6),
                points.get(5)));
        assertEquals(treeOfNode.stream().toList(), nodes.get(11).getPointsInInterval().stream().toList());
        treeOfNode.clear();
        treeOfNode.add(points.getLast());
        assertEquals(treeOfNode.stream().toList(), nodes.getLast().getPointsInInterval().stream().toList());
    }
}