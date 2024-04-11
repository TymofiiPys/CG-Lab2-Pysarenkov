package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class for comparing edges by their sources' x coordinates
 */
public class EdgeSrcXComparator implements Comparator<Integer> {
    private final ArrayList<WeightedEdge> edges;

    /**
     * @param edges edges whose indices will be sorted
     */
    public EdgeSrcXComparator(ArrayList<WeightedEdge> edges) {
        this.edges = edges;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        Point2D.Float o1point = edges.get(o1).getSrc();
        Point2D.Float o2point = edges.get(o2).getSrc();
        Point2D.Float dest = edges.get(o1).getDest();
        float tangent1 = (o1point.y - dest.y) != 0 ? (o1point.x - dest.x) / (o1point.y - dest.y) :
                o1point.x > dest.x ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        float tangent2 = (o2point.y - dest.y) != 0 ? (o2point.x - dest.x) / (o2point.y - dest.y) :
                o2point.x > dest.x ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        return Float.compare(tangent2, tangent1);
    }
}
