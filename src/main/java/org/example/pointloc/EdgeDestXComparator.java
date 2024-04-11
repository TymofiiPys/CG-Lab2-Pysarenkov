package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class for comparing edges by their destinations' x coordinates
 */
public class EdgeDestXComparator implements Comparator<Integer> {
    private final ArrayList<WeightedEdge> edges;

    /**
     * @param edges edges whose indices will be sorted
     */
    public EdgeDestXComparator(ArrayList<WeightedEdge> edges) {
        this.edges = edges;
    }

    @Override
    public int compare(Integer o1, Integer o2) {
        Point2D.Float o1point = edges.get(o1).getDest();
        Point2D.Float o2point = edges.get(o2).getDest();
        Point2D.Float src = edges.get(o1).getSrc();
        float tangent1 = (o1point.y - src.y) != 0 ? (o1point.x - src.x) / (o1point.y - src.y) :
                o1point.x > src.x ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        float tangent2 = (o2point.y - src.y) != 0 ? (o2point.x - src.x) / (o2point.y - src.y) :
                o2point.x > src.x ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        return Float.compare(tangent1, tangent2);
    }
}