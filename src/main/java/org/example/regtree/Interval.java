package org.example.regtree;

public class Interval {
    public double leftInclusive;
    public double rightExclusive;

    public Interval(double left, double right) {
        this.leftInclusive = left;
        this.rightExclusive = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return Double.compare(leftInclusive, interval.leftInclusive) == 0 && Double.compare(rightExclusive, interval.rightExclusive) == 0;
    }
}
