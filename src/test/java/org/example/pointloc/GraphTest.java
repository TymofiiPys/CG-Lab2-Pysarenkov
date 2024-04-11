package org.example.pointloc;

import junit.framework.TestCase;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

public class GraphTest extends TestCase {

    public ArrayList<Point2D.Float> getNodes() {
        ArrayList<Point2D.Float> nodes = new ArrayList<>();
        //in comments - indices when sorted
        nodes.add(new Point2D.Float(1, 8)); // 7
        nodes.add(new Point2D.Float(2, 4)); // 2
        nodes.add(new Point2D.Float(3, 1)); // 0
        nodes.add(new Point2D.Float(4.5f, 4.5f)); // 3
        nodes.add(new Point2D.Float(5, 6.5f)); // 5
        nodes.add(new Point2D.Float(5.5f, 9)); // 8
        nodes.add(new Point2D.Float(8, 7)); // 6
        nodes.add(new Point2D.Float(8.5f, 6)); // 4
        nodes.add(new Point2D.Float(9, 2)); // 1
        return nodes;
    }

    public ArrayList<Edge> getEdges(ArrayList<Point2D.Float> nodes) {
        ArrayList<Edge> edges = new ArrayList<>();
        //in comments - indices of src and dest points
        edges.add(new Edge(nodes.get(0), nodes.get(5))); // 7 -> 8
        edges.add(new Edge(nodes.get(0), nodes.get(1))); // 2 -> 7
        edges.add(new Edge(nodes.get(1), nodes.get(2))); // 0 -> 2
        edges.add(new Edge(nodes.get(1), nodes.get(3))); // 2 -> 3
        edges.add(new Edge(nodes.get(1), nodes.get(4))); // 2 -> 5
        edges.add(new Edge(nodes.get(2), nodes.get(3))); // 0 -> 3
        edges.add(new Edge(nodes.get(2), nodes.get(7))); // 0 -> 4
        edges.add(new Edge(nodes.get(2), nodes.get(8))); // 0 -> 1
        edges.add(new Edge(nodes.get(3), nodes.get(4))); // 3 -> 5
        edges.add(new Edge(nodes.get(3), nodes.get(7))); // 3 -> 4
        edges.add(new Edge(nodes.get(4), nodes.get(5))); // 5 -> 8
        edges.add(new Edge(nodes.get(4), nodes.get(6))); // 5 -> 6
        edges.add(new Edge(nodes.get(5), nodes.get(6))); // 6 -> 8
        edges.add(new Edge(nodes.get(6), nodes.get(7))); // 4 -> 6
        edges.add(new Edge(nodes.get(7), nodes.get(8))); // 1 -> 4
        edges.add(new Edge(nodes.get(4), nodes.get(7))); // 4 -> 5

        return edges;
    }

    public void testSortPoints() {
        ArrayList<Point2D.Float> nodes = getNodes();
        ArrayList<Point2D.Float> sortedNodes = new ArrayList<>(nodes);
        sortedNodes.sort(new Point2DYComparator());
        assertEquals(nodes.get(0), sortedNodes.get(7));
        assertEquals(nodes.get(1), sortedNodes.get(2));
        assertEquals(nodes.get(2), sortedNodes.get(0));
        assertEquals(nodes.get(3), sortedNodes.get(3));
        assertEquals(nodes.get(4), sortedNodes.get(5));
        assertEquals(nodes.get(5), sortedNodes.get(8));
        assertEquals(nodes.get(6), sortedNodes.get(6));
        assertEquals(nodes.get(7), sortedNodes.get(4));
        assertEquals(nodes.get(8), sortedNodes.get(1));
    }

    public void testWeightBalancing() {
        ArrayList<Point2D.Float> nodes = getNodes();
        ArrayList<Edge> edges = getEdges(nodes);

        Graph gr = new Graph(nodes, edges);

        gr.weightBalancing();
        ArrayList<WeightedEdge> weightedEdges = gr.getEdges();
        assertEquals(1, weightedEdges.get(0).getWeight());
        assertEquals(1, weightedEdges.get(1).getWeight());
        assertEquals(3, weightedEdges.get(2).getWeight());
        assertEquals(1, weightedEdges.get(3).getWeight());
        assertEquals(1, weightedEdges.get(4).getWeight());
        assertEquals(1, weightedEdges.get(5).getWeight());
        assertEquals(1, weightedEdges.get(6).getWeight());
        assertEquals(1, weightedEdges.get(7).getWeight());
        assertEquals(1, weightedEdges.get(8).getWeight());
        assertEquals(1, weightedEdges.get(9).getWeight());
        assertEquals(3, weightedEdges.get(10).getWeight());
        assertEquals(1, weightedEdges.get(11).getWeight());
        assertEquals(2, weightedEdges.get(12).getWeight());
        assertEquals(1, weightedEdges.get(13).getWeight());
        assertEquals(1, weightedEdges.get(14).getWeight());
        assertEquals(2, weightedEdges.get(15).getWeight());
    }

    public void testGetChains() {
        ArrayList<Point2D.Float> nodes = getNodes();
        ArrayList<Edge> edges = getEdges(nodes);

        Graph gr = new Graph(nodes, edges);
        boolean thrown = false;
        try {
            gr.getChains();
        } catch (Exception e) {
            thrown = true;
        }
        assertTrue(thrown);
        thrown = false;
        gr.weightBalancing();

        ArrayList<ArrayList<WeightedEdge>> chains = null;
        try {
            chains = gr.getChains();
        } catch (Exception e) {
            thrown = true;
        }
        assertFalse(thrown);
        assertNotNull(chains);
        assertEquals(6, chains.size());
    }
}