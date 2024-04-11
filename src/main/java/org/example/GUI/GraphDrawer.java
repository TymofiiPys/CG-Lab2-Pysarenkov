package org.example.GUI;

import org.example.pointloc.Edge;
import org.example.pointloc.Graph;
import org.example.pointloc.WeightedEdge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import java.lang.Math.*;
import java.util.Optional;
import java.util.Random;
import java.util.Vector;

/**
 * Class for drawing graphs
 */
public class GraphDrawer {
    private Graph graph;
    private final JPanel panelDraw;
    private final int nodesRad = 4;
    private int layer = 0;

    /**
     * @param panelDraw a {@code JPanel} on which graphs will be drawn
     */
    public GraphDrawer(JPanel panelDraw) {
        this.panelDraw = panelDraw;
    }

    /**
     * Change graph to draw and do all necessary operations for further point location
     *
     * @param gr a new graph
     */
    public void setGraph(Graph gr) {
        this.graph = gr;
        graph.weightBalancing();
        graph.getChains();
    }

    public Boolean graphChainMethodApplicable() {
        if (graph == null) return null;
        return graph.getIsChainsMethodApplicable();
    }

    /**
     * @return offsets which will be used to draw graph in the centre of panelDraw
     */
    private int[] offsets() {
        Point2D.Float first = graph.getNodes().getFirst();
        Point2D.Float last = graph.getNodes().getLast();
        Point2D.Float center = new Point2D.Float((first.x + last.x) / 2, (first.y + last.y) / 2);

        Point2D.Float panelDrawCenter = new Point2D.Float((float) panelDraw.getWidth() / 2, (float) panelDraw.getHeight() / 2);

        return new int[]{(int) (panelDrawCenter.x - center.x), (int) (panelDrawCenter.y - center.y)};
    }

    public Point2D.Float adaptToPanel(Point2D.Float p) {
        int[] offsets = offsets();
        return new Point2D.Float(p.x + offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public Point2D.Float adaptFromPanel(Point2D.Float p) {
        int[] offsets = offsets();
        return new Point2D.Float(p.x - offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public void drawGraph(boolean drawEdges) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        ArrayList<WeightedEdge> edges = graph.getEdges();
        int[] offsets = offsets();
        //axes
        gr.drawLine(0, panelDraw.getHeight() - offsets[1], panelDraw.getWidth(), panelDraw.getHeight() - offsets[1]);
        gr.drawLine(offsets[0], 0, offsets[0], panelDraw.getHeight());
        if (drawEdges) {
            for (WeightedEdge edge : edges) {
                Point2D.Float pt1 = adaptToPanel(edge.getSrc());
                Point2D.Float pt2 = adaptToPanel(edge.getDest());
                if (edge.isRegularized()) {
                    gr.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                            0, new float[]{9}, 0));
                }
                gr.drawLine((int) pt1.x,
                        (int) pt1.y,
                        (int) pt2.x,
                        (int) pt2.y);
                if (edge.isRegularized()) {
                    gr.setStroke(new BasicStroke());
                }
            }
            ArrayList<Point2D.Float> nodes = graph.getNodes();
            gr.setColor(Color.RED);
            for (Point2D.Float node : nodes) {
                Point2D.Float adapted = adaptToPanel(node);
                gr.fillOval((int) (adapted.x - nodesRad),
                        (int) (adapted.y - nodesRad),
                        2 * nodesRad,
                        2 * nodesRad);
            }
        }
        layer = 0;
    }

    public void drawDirectionsOnEdge(Edge edge) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.setColor(Color.decode("#3d823f"));
        Point2D.Float pt1 = adaptToPanel(edge.getSrc());
        Point2D.Float pt2 = adaptToPanel(edge.getDest());
        //minus because jpanel's origin is on left top, not bottom
        float tangent = -(pt1.y - pt2.y) / (pt1.x - pt2.x);
        double angle = Math.atan(tangent);
        double arrowSticksAngle = 20;
        double angle1 = angle + Math.toRadians(arrowSticksAngle);
        double angle2 = angle - Math.toRadians(arrowSticksAngle);
        int mult1 = angle < 0 ? -1 : 1;
        int mult2 = angle < 0 ? -1 : 1;
        float arrowSticksLength = 12;
        int x1 = (int) (pt2.x - arrowSticksLength * mult1 * Math.cos(angle1));
        int y1 = (int) (pt2.y + arrowSticksLength * mult1 * Math.sin(angle1));
        int x2 = (int) (pt2.x - arrowSticksLength * mult2 * Math.cos(angle2));
        int y2 = (int) (pt2.y + arrowSticksLength * mult2 * Math.sin(angle2));
//        gr.drawLine((int) pt2.x,
//                (int) pt2.y,
//                (int) (pt2.x + arrowSticksLength * Math.cos(angle + Math.toRadians(arrowSticksAngle))),
//                (int) (pt2.x + arrowSticksLength * Math.sin(angle + Math.toRadians(arrowSticksAngle))));
        gr.drawLine((int) pt2.x,
                (int) pt2.y,
                x1,
                y1);
        gr.drawLine((int) pt2.x,
                (int) pt2.y,
                x2,
                y2);
//                gr.drawLine((int) pt2.x,
//                (int) pt2.y,
//                (int) (pt2.x + arrowSticksLength * Math.cos(angle - Math.toRadians(arrowSticksAngle))),
//                (int) (pt2.x + arrowSticksLength * Math.sin(angle - Math.toRadians(arrowSticksAngle))));
    }

    public void drawDirectedEnumeratedGraph(boolean drawEdges) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        if (layer == 0)
            gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer++;
        drawGraph(drawEdges);
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        int k = 0;
        for (Point2D.Float node : nodes) {
            Point2D.Float adapted = adaptToPanel(node);
            int textXOffset = -25;
            gr.drawString(k++ + "", (int) adapted.x + textXOffset, (int) adapted.y);
        }
        graph.getEdges().forEach(this::drawDirectionsOnEdge);
    }

    public void drawChains() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        layer = 1;
//        ArrayList<ArrayList<WeightedEdge>> chainsOrig = graph.getChains();
        ArrayList<ArrayList<WeightedEdge>> chains = graph.getChains();
//        for (var chain : chainsOrig) {
//            ArrayList<WeightedEdge> chainCopy = new ArrayList<>();
//            for (var edge : chain) {
//                chainCopy.add(new WeightedEdge(edge));
//            }
//            chains.add(chainCopy);
//        }
        gr.setStroke(new BasicStroke(2.0f));
        int colorSeed = 2;
        Random colorRand = new Random(colorSeed);
        for (WeightedEdge edge : graph.getEdges()) {
            edge.setDrawWeight(edge.getWeight());
        }
        for (ArrayList<WeightedEdge> chain : chains.reversed()) {
            gr.setColor(
                    new Color(
                            colorRand.nextInt(150) + 100,
                            colorRand.nextInt(150) + 100,
                            colorRand.nextInt(150) + 100
                    )
            );
            for (WeightedEdge edge : chain) {
                Point2D.Float pt1 = adaptToPanel(edge.getSrc());
                Point2D.Float pt2 = adaptToPanel(edge.getDest());
                double tangent = Math.atan(-(pt2.y - pt1.y) / (pt2.x - pt1.x));
                double sin = Math.sin(tangent);
                double cos = Math.cos(tangent);
                if (edge.isRegularized()) {
                    gr.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                            0, new float[]{9}, 0));
                }
                gr.drawLine((int) (pt1.x + 2 * (edge.getDrawWeight() - 1)),
                        (int) (pt1.y - 2 * (edge.getDrawWeight() - 1)),
                        (int) (pt2.x + 2 * (edge.getDrawWeight() - 1)),
                        (int) (pt2.y - 2 * (edge.getDrawWeight() - 1)));
                if (edge.isRegularized()) {
                    gr.setStroke(new BasicStroke());
                }
                edge.setDrawWeight(edge.getDrawWeight() - 1);
            }
        }
        drawDirectedEnumeratedGraph(false);
    }

    public void drawPoint(Point2D p) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.setColor(Color.BLUE);
        gr.fillOval((int) p.getX() - nodesRad, (int) p.getY() - nodesRad, 2 * nodesRad, 2 * nodesRad);
    }

    public ArrayList<Integer>[] pointLocation(Point2D.Float p) {
        return graph.pointLocation(p);
    }

    public boolean graphSet() {
        return graph != null;
    }

    public String getChainList() {
        StringBuilder chainList = new StringBuilder();

        ArrayList<ArrayList<WeightedEdge>> chains = graph.getChains();
        ArrayList<Point2D.Float> nodes = graph.getNodes();
        for (int i = 0; i < chains.size(); i++) {
            var chain = chains.get(i);
            chainList.append("[").append(i).append("] : ");
            for (WeightedEdge edge : chain) {
                chainList.append(nodes.indexOf(edge.getSrc())).append(", ");
            }
            chainList.append(nodes.size() - 1).append("<br>");
        }

        return chainList.toString();
    }
}
