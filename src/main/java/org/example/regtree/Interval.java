package org.example.regtree;

public class Interval {
    private double left;
    private double right;

    public Interval(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }
}
