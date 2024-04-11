package org.example.pointloc;

import java.awt.geom.Point2D;
import java.util.Objects;

/**
 * The Edge class represents an unweighted directed edge.
 *  Intended for extension by WeightedEdge class.
 */
public class Edge  {
    private final Point2D.Float src;
    private final Point2D.Float dest;

    public Edge(Point2D.Float src, Point2D.Float dest) {
        this.src = src;
        this.dest = dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(src, edge.src) && Objects.equals(dest, edge.dest);
    }

    public Point2D.Float getSrc() {
        return src;
    }

    public Point2D.Float getDest() {
        return dest;
    }

    boolean intersect(Edge other) {
        Point2D.Float p1 = this.src;
        Point2D.Float p2 = this.dest;
        Point2D.Float p3 = other.src;
        Point2D.Float p4 = other.dest;

        int o1 = orientation(p1, p2, p3);
        int o2 = orientation(p1, p2, p4);
        int o3 = orientation(p3, p4, p1);
        int o4 = orientation(p3, p4, p2);

        // sources or destinations are equal or destination of one is source of the other
        if(p1 == p3 || p2 == p4 || p1 == p4 || p2 == p3)
            return false;

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special cases
        // p1, p2 and p3 are collinear and p3 lies on segment p1-p2
        if (o1 == 0 && onSegment(p1, p2, p3))
            return true;

        // p1, p2 and p4 are collinear and p4 lies on segment p1-p2
        if (o2 == 0 && onSegment(p1, p2, p4))
            return true;

        // p3, p4 and p1 are collinear and p1 lies on segment p3-p4
        if (o3 == 0 && onSegment(p3, p4, p1))
            return true;

        // p3, p4 and p2 are collinear and p2 lies on segment p3-p4
        if (o4 == 0 && onSegment(p3, p4, p2))
            return true;

        return false;
    }

    // Helper method to calculate orientation of triplet (p, q, r)
    private int orientation(Point2D.Float p, Point2D.Float q, Point2D.Float r) {
        float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (val == 0)
            return 0; // Collinear
        return (val > 0) ? 1 : 2; // Clockwise or Counterclockwise
    }

    // Helper method to check if point q lies on segment p-r
    private boolean onSegment(Point2D.Float p, Point2D.Float q, Point2D.Float r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
                q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }
}
