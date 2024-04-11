package org.example.regtree;

public class RegTree {
    private int coordNum;
    private int k;
    private Node root;

    public RegTree(int coordNum, int k) {
        this.coordNum = coordNum;
        this.k = k;
    }

    public RegTree(int k) {
        this.coordNum = 1;
        this.k = k;
    }

    public RegTree(int coordNum, int k, int left, int right) {
        this(coordNum, k);
        this.root = new Node(left, right);
    }

    private class Node {
        private int left;
        private int right;
        private RegTree nextCoordinateTree;

        public Node(int left, int right) {
            this.left = left;
            this.right = right;
            if (coordNum == k)
                this.nextCoordinateTree = null;
            else
                this.nextCoordinateTree = new RegTree(coordNum + 1, k, );
        }
    }
}
