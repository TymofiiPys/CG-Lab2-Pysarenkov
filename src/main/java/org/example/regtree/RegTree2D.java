package org.example.regtree;

import org.example.pointloc.Point2DYComparator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class RegTree2D {
    private Node root;
    private final int N;

    public RegTree2D(ArrayList<Point2D.Double> points) {
        this.N = points.size();
        points.sort(null);
        this.root = new Node(1, N + 1, points);
    }

//    public RegTree2D(int coordNum, int k, int left, int right) {
//        this(coordNum, k);
//        this.root = new Node(left, right);
//    }

    private class Node {
        private int leftInclusive;
        private int rightExclusive;
        private TreeSet<Point2D.Double> pointsInInterval;

        private Node leftChild = null;
        private Node rightChild = null;

        public Node(int leftInclusive, int rightExclusive, List<Point2D.Double> pointsInInterval) {
            this.leftInclusive = leftInclusive;
            this.rightExclusive = rightExclusive;
            this.pointsInInterval = new TreeSet<>(new Point2DYComparator());
            this.pointsInInterval.addAll(pointsInInterval);
            if (rightExclusive - leftInclusive > 1) {
                this.leftChild = new Node(leftInclusive,
                        (leftInclusive + rightExclusive) / 2,
                        pointsInInterval.subList(0, pointsInInterval.size() / 2));
                this.rightChild = new Node((leftInclusive + rightExclusive) / 2,
                        rightExclusive,
                        pointsInInterval.subList(pointsInInterval.size() / 2, pointsInInterval.size()));
            }
        }
    }
}
