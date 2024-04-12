package org.example.GUI;

import org.example.regtree.Interval;
import org.example.regtree.RTree2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class RegTreeDrawer {
    private RTree2D rtree;
    private final JPanel panelDraw;

    private final int nodesRad = 4;
    private int layer = 0;

    public RegTreeDrawer(JPanel panelDraw) {
        this.panelDraw = panelDraw;
    }

    public void setRTree(RTree2D rtree) {
        this.rtree = rtree;
    }

    public boolean rtreeSet() {
        return rtree != null;
    }

    private int[] offsets() {
        Point2D.Double first = rtree.getPoints().getFirst();
        Point2D.Double last = rtree.getPoints().getLast();
        Point2D.Double center = new Point2D.Double((first.x + last.x) / 2, (first.y + last.y) / 2);

        Point2D.Double panelDrawCenter = new Point2D.Double((double) panelDraw.getWidth() / 2, (double) panelDraw.getHeight() / 2);

        return new int[]{(int) (panelDrawCenter.x - center.x), (int) (panelDrawCenter.y - center.y)};
    }

    public Point2D.Double adaptToPanel(Point2D.Double p) {
        int[] offsets = offsets();
        return new Point2D.Double(p.x + offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public Point2D.Double adaptFromPanel(Point2D.Double p) {
        int[] offsets = offsets();
        return new Point2D.Double(p.x - offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public void drawPoints() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());

        int[] offsets = offsets();
        gr.drawLine(0, panelDraw.getHeight() - offsets[1], panelDraw.getWidth(), panelDraw.getHeight() - offsets[1]);
        gr.drawLine(offsets[0], 0, offsets[0], panelDraw.getHeight());
        gr.drawLine(50 + offsets[0],
                panelDraw.getHeight() - (10 + offsets[1]),
                50 + offsets[0],
                panelDraw.getHeight() - (-10 + offsets[1]));
        gr.drawLine(-10 + offsets[0],
                panelDraw.getHeight() - (50 + offsets[1]),
                10 + offsets[0],
                panelDraw.getHeight() - (50 + offsets[1]));
        gr.drawString("50", 45 + offsets[0], panelDraw.getHeight() - (-25 + offsets[1]));
        gr.drawString("50", -30 + offsets[0], panelDraw.getHeight() - (45 + offsets[1]));

        ArrayList<Point2D.Double> points = rtree.getPoints();
        int k = 1;
        int textXOffset = -15;
        int textYOffset = 15;
        gr.setColor(Color.BLUE);
        for (var point : points) {
            var adapted = adaptToPanel(point);
            gr.fillOval((int) (adapted.x - nodesRad),
                    (int) (adapted.y - nodesRad),
                    2 * nodesRad,
                    2 * nodesRad);
            gr.drawString(k++ + "", (int) adapted.x + textXOffset, (int) adapted.y + textYOffset);
        }
        layer = 0;
    }

    public void drawRectangle(Point2D.Double p1, Point2D.Double p2) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer++;
        drawPoints();
        gr.setColor(Color.RED);
        if (p1.x < p2.x && p1.y < p2.y) {
            gr.drawRect((int) p1.x, (int) p1.y, (int) (p2.x - p1.x), (int) (p2.y - p1.y));
        } else if (p1.x < p2.x && p1.y > p2.y) {
            gr.drawRect((int) p1.x, (int) p2.y, (int) (p2.x - p1.x), (int) (p1.y - p2.y));
        } else if (p1.x > p2.x && p1.y < p2.y) {
            gr.drawRect((int) p2.x, (int) p1.y, (int) (p1.x - p2.x), (int) (p2.y - p1.y));
        } else {
            gr.drawRect((int) p2.x, (int) p2.y, (int) (p1.x - p2.x), (int) (p1.y - p2.y));
        }
    }

    public String regionSearchAsString(Point2D.Double p1, Point2D.Double p2) {
        StringBuilder results = new StringBuilder();

        if (p1.equals(p2)) {
            return "";
        }
        if(p1.y > p2.y) {
            double temp = p1.y;
            p1.y = p2.y;
            p2.y = temp;
        }
        if(p1.x > p2.x) {
            double temp = p1.x;
            p1.x = p2.x;
            p2.x = temp;
        }

        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer++;
        drawPoints();
        drawRectangle(adaptToPanel(p1), adaptToPanel(p2));

        gr.setColor(Color.GREEN);
        ArrayList<Point2D.Double> pointsInRegion = rtree.regionSearch(new Interval(p1.x, p2.x), new Interval(p1.y, p2.y));
        for (var point : pointsInRegion) {
            var adapted = adaptToPanel(point);
            gr.fillOval((int) (adapted.x - nodesRad),
                    (int) (adapted.y - nodesRad),
                    2 * nodesRad,
                    2 * nodesRad);
        }

        ArrayList<Point2D.Double> allPoints = rtree.getPoints();

        for (var point : pointsInRegion) {
            int ind = allPoints.indexOf(point);
            results.append("[").append(ind + 1).append("] (").append(point.x).append(", ").append(point.y).append(")<br>");
        }

        return results.toString();
    }
}
