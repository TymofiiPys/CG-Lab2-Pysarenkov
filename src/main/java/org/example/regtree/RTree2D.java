package org.example.regtree;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Stream;

public class RTree2D {
    private Node root;
    private final int N;

    private final ArrayList<Point2D.Double> points;

    public RTree2D(ArrayList<Point2D.Double> points) {
        this.N = points.size();
        points.sort(new Point2DXComparator());
        this.points = points;
        this.root = new Node(1, N + 1, points);
    }

    public static RTree2D readFromFile(String filename) {
        Stream<String> fileLines;
        List<String> lines;
        try {
            fileLines = Files.lines(Paths.get(filename));
            lines = fileLines.toList();
            fileLines.close();
        } catch (IOException e) {
            throw new RuntimeException("Literally impossible to get this one, man", e);
        }

        ArrayList<Point2D.Double> nodes = new ArrayList<>();
        Scanner lineScanner;
        double x, y;
        for (String line : lines) {
            lineScanner = new Scanner(line);
            try {
                x = lineScanner.nextDouble();
                y = lineScanner.nextDouble();
            } catch (Exception e) {
                throw new IllegalArgumentException("File opened has wrong format");
            }
            nodes.add(new Point2D.Double(x, y));
        }

        return new RTree2D(nodes);
    }

    public int getN() {
        return N;
    }

    private void normalizeInterval(Interval interval) {
        boolean foundLeft = false, foundRight = false;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x > interval.leftInclusive) {
                interval.leftInclusive = i + 1;
                foundLeft = true;
                break;
            }
        }

        for (int i = points.size() - 1; i >= 0; i--) {
            if (points.get(i).x < interval.rightExclusive) {
                interval.rightExclusive = i + 2;
                foundRight = true;
                break;
            }
        }

        if (!foundLeft) {
            interval.leftInclusive = N + 1;
            interval.rightExclusive = N + 1;
        }
        if (!foundRight) {
            interval.leftInclusive = 0;
            interval.rightExclusive = 0;
        }
    }

    public ArrayList<Point2D.Double> regionSearch(Interval horizontal, Interval vertical) {
        if (horizontal.leftInclusive > horizontal.rightExclusive ||
                vertical.leftInclusive > vertical.rightExclusive)
            throw new IllegalArgumentException();
        ArrayList<Point2D.Double> pointsInRegion = new ArrayList<>();
        normalizeInterval(horizontal);
        if (horizontal.rightExclusive >= 1 && horizontal.leftInclusive <= N)
            this.root.intervalSearch(horizontal, vertical, pointsInRegion);
        return pointsInRegion;
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

        public Interval getInterval() {
            return new Interval(leftInclusive, rightExclusive);
        }

        public TreeSet<Point2D.Double> getPointsInInterval() {
            return pointsInInterval;
        }

        private void intervalSearch(Interval horizontal, Interval vertical, ArrayList<Point2D.Double> pointsInRegion) {
            if (horizontal.leftInclusive <= this.leftInclusive &&
                    this.rightExclusive <= horizontal.rightExclusive) {
                pointsInRegion.addAll(this.pointsInInterval.subSet(new Point2D.Double(horizontal.leftInclusive, vertical.leftInclusive - 0),
                        new Point2D.Double(horizontal.rightExclusive, vertical.rightExclusive - 0)));
            } else {
                int half = (this.leftInclusive + this.rightExclusive) / 2;
                if (horizontal.leftInclusive < half && this.leftChild != null)
                    this.leftChild.intervalSearch(horizontal, vertical, pointsInRegion);
                if (half < horizontal.rightExclusive && this.leftChild != null)
                    this.rightChild.intervalSearch(horizontal, vertical, pointsInRegion);
            }
        }
    }

    public ArrayList<Node> nodesPreOrder() {
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

    public ArrayList<Point2D.Double> getPoints() {
        return points;
    }
}
