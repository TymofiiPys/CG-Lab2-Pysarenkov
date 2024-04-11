package org.example.pointloc;

import java.awt.geom.Point2D;

/**
 * The WeightedEdge class represents weighted directed edge
 */
public class WeightedEdge extends Edge {
    private int weight;
    private int drawWeight;
    private boolean regularized = false;

    public WeightedEdge(Point2D.Float src, Point2D.Float dest) {
        super(src, dest);
    }

    public WeightedEdge(WeightedEdge e) {
        super(e.getSrc(), e.getDest());
        this.weight = e.getWeight();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDrawWeight() {
        return drawWeight;
    }

    public void setDrawWeight(int drawWeight) {
        this.drawWeight = drawWeight;
    }

    public boolean isRegularized() {
        return regularized;
    }

    public void setRegularized(boolean regularized) {
        this.regularized = regularized;
    }
}
