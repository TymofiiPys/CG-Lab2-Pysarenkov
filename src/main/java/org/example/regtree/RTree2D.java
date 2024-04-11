package org.example.regtree;

import org.example.pointloc.Point2DYComparator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class RTree2D{
    private Node root;
    private final int N;

    public RTree2D(ArrayList<Point2D.Double> points) {
        this.N = points.size();
        points.sort(new Point2DXComparator());
        this.root = new Node(1, N + 1, points);
    }

    public int getN() {
        return N;
    }

    public static class Node {
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

        public Interval getInterval(){
            return new Interval(leftInclusive, rightExclusive);
        }

        public TreeSet<Point2D.Double> getPointsInInterval(){
            return pointsInInterval;
        }
    }

    public ArrayList<Node> nodesPreOrder(){
        if (N == 0) return new ArrayList<>();

        ArrayList<Node> queue = new ArrayList<>();
        nodesPreOrder(root, queue);
        return queue;
    }

    private void nodesPreOrder(Node x, ArrayList<Node> queue) {
        if (x == null) return;
        queue.add(x);
        nodesPreOrder(x.leftChild, queue);
        nodesPreOrder(x.rightChild, queue);
    }
}
