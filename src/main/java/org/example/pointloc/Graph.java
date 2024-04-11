package org.example.pointloc;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * The Graph class represents a directed weighted graph with additional functions such as
 * weight balancing and chain set retrieval
 */
public class Graph {
    private final ArrayList<Point2D.Float> nodes;
    /**
     * Amount of nodes in the graph
     */
    private final int N;
    private final ArrayList<WeightedEdge> edges;

    /**
     * Array of lists of incoming edges indices.
     * ith element corresponds to list of incoming edge indices for ith node.
     */
    private final ArrayList<LinkedList<Integer>> incomingEdgeIndices;
    /**
     * Array of lists of outgoing edges indices.
     * ith element corresponds to list of outgoing edge indices for ith node.
     */
    private final ArrayList<LinkedList<Integer>> outgoingEdgeIndices;
    private ArrayList<ArrayList<WeightedEdge>> chains;
    private boolean balanced = false;
    private boolean chainsFound = false;
    private Boolean isChainsMethodApplicable = null;

    public Graph(ArrayList<Point2D.Float> nodes, ArrayList<Edge> edges) {
        if (nodes == null) {
            throw new IllegalArgumentException("nodes is null");
        }
        if (edges == null) {
            throw new IllegalArgumentException("edges is null");
        }
        this.N = nodes.size();

        // nodes are deep-copied from the respective parameter and sorted
        ArrayList<Point2D.Float> sortedNodes = new ArrayList<>(N);
        for (int i = 0; i < N; i++) {
            Point2D.Float curPt = new Point2D.Float();
            curPt.setLocation(nodes.get(i));
            if (!sortedNodes.contains(curPt))
                sortedNodes.add(i, curPt);
        }
        sortedNodes.sort(new Point2DYComparator());
        this.nodes = sortedNodes;

        // edges are deep-copied from the respective parameter
        this.edges = new ArrayList<>(edges.size());

        this.incomingEdgeIndices = new ArrayList<>();
        this.outgoingEdgeIndices = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            incomingEdgeIndices.add(new LinkedList<>());
            outgoingEdgeIndices.add(new LinkedList<>());
        }

        int k = 0;
        for (Edge edge : edges) {
            int srcInd = this.nodes.indexOf(edge.getSrc());
            int destInd = this.nodes.indexOf(edge.getDest());
            //swap indices if the srcInd-th point is larger than destInd-th,
            //use the fact that nodes array is already sorted
            if (srcInd > destInd) {
                int temp = srcInd;
                srcInd = destInd;
                destInd = temp;
            }
            WeightedEdge edgeToAdd = new WeightedEdge(this.nodes.get(srcInd),
                    this.nodes.get(destInd));
            if (!this.edges.contains(edgeToAdd))
                this.edges.add(k, edgeToAdd);
            addToNodeLists(srcInd, destInd, k);
            k++;
        }
//        checkChainMethodApplicabilityEdges();
        regularize();
    }

    /**
     * Read a graph from file. File should be in such format that nodes go first and described by
     * two float numbers (coordinates), one line per node.
     * To finish reading nodes, file should contain an empty line, after which goes list of edges.
     * Edges are described by two integers (source and destination indices in read list).
     *
     * @param filename path to the file containing graph properties
     * @return Graph object read from the file
     */
    public static Graph readFromFile(String filename) {
        Stream<String> fileLines;
        List<String> lines;
        try {
            fileLines = Files.lines(Paths.get(filename));
            lines = fileLines.toList();
            fileLines.close();
        } catch (IOException e) {
            throw new RuntimeException("Literally impossible to get this one, man", e);
        }

        ArrayList<Point2D.Float> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        Scanner lineScanner;
        float x, y;
        int pt1, pt2;
        boolean readEdges = false;
        for (String line : lines) {
            if (line.isEmpty()) {
                readEdges = true;
                continue;
            }
            lineScanner = new Scanner(line);
            if (!readEdges) {
                try {
                    x = lineScanner.nextFloat();
                    y = lineScanner.nextFloat();
                } catch (Exception e) {
                    throw new IllegalArgumentException("File opened has wrong format");
                }
                nodes.add(new Point2D.Float(x, y));
            } else {
                try {
                    pt1 = lineScanner.nextInt();
                    pt2 = lineScanner.nextInt();
                } catch (Exception e) {
                    throw new IllegalArgumentException("File opened has wrong format");
                }
                try {
                    edges.add(new Edge(nodes.get(pt1), nodes.get(pt2)));
                } catch (IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Wrong indices of points in edge section");
                }
            }
        }

        return new Graph(nodes, edges);
    }

    /**
     * Add edge index to list of outgoing edges from srcNodeIndex-th node
     * and to list of incoming edges to destNodeIndex-th node
     * Lists are sorted after addition.
     *
     * @param srcNodeIndex  index of source node for the edge
     * @param destNodeIndex index of destination node for the edge
     * @param edgeIndex     index of the added edge
     */
    private void addToNodeLists(int srcNodeIndex, int destNodeIndex, int edgeIndex) {
        outgoingEdgeIndices.get(srcNodeIndex).add(edgeIndex);
        incomingEdgeIndices.get(destNodeIndex).add(edgeIndex);

        outgoingEdgeIndices.get(srcNodeIndex).sort(new EdgeDestXComparator(edges));
        incomingEdgeIndices.get(destNodeIndex).sort(new EdgeSrcXComparator(edges));
    }

    private void regularize() {
        for (int i = nodes.size() - 2; i >= 0; i--) {
            if (outgoingEdgeIndices.get(i).isEmpty()) {
                WeightedEdge edgeToAdd = new WeightedEdge(this.nodes.get(i),
                        this.nodes.get(i+1));
                edgeToAdd.setRegularized(true);
                edges.add(edgeToAdd);
                addToNodeLists(i, i+1, edges.size() - 1);
            }
        }

        for (int i = 1; i < nodes.size(); i++) {
            if (incomingEdgeIndices.get(i).isEmpty()) {
                WeightedEdge edgeToAdd = new WeightedEdge(this.nodes.get(i-1),
                        this.nodes.get(i));
                edgeToAdd.setRegularized(true);
                edges.add(edgeToAdd);
                addToNodeLists(i - 1, i, edges.size() - 1);
            }
        }
    }

    /**
     * @param in index of node in the nodes array
     * @return total weight of edges incoming to the node
     */
    private int computeWIn(int in) {
        int wIn = 0;
        for (int index : incomingEdgeIndices.get(in)) {
            wIn += edges.get(index).getWeight();
        }
        return wIn;
    }

    /**
     * @param out index of node in the nodes array
     * @return total weight of edges outgoing from the node
     */
    private int computeWOut(int out) {
        int wOut = 0;
        for (int index : outgoingEdgeIndices.get(out)) {
            wOut += edges.get(index).getWeight();
        }
        return wOut;
    }

    private WeightedEdge leftMostEdgeFromPoint(int out) {
        return edges.get(outgoingEdgeIndices.get(out).get(0));
    }

    /**
     * @param out     index of node
     * @param weights weights of edges
     * @return the leftmost edge with positive weight index outgoing from out-th node
     */
    private int leftMostEdgeIndFromPoint(int out, int[] weights) {
        for (int index : outgoingEdgeIndices.get(out)) {
            if (weights[index] > 0) {
                return index;
            }
        }
        return -1;
    }

    private WeightedEdge leftMostEdgeToPoint(int to) {
        return edges.get(incomingEdgeIndices.get(to).get(0));
    }

    /**
     * Does graph weight-balancing using information about
     * incoming and outgoing edges from each node except the first and the last
     */
    public void weightBalancing() {
        if (isChainsMethodApplicable != null && !isChainsMethodApplicable) {
            return;
        }
        for (var edge : edges) {
            edge.setWeight(1);
        }

        int wIn, wOut;

        for (int i = 1; i < N - 1; i++) {
            wIn = computeWIn(i);
            wOut = computeWOut(i);
            if (wIn > wOut) {
                WeightedEdge d1 = leftMostEdgeFromPoint(i);
                d1.setWeight(wIn - wOut + 1);
            }
        }

        for (int i = N - 2; i > 0; i--) {
            wIn = computeWIn(i);
            wOut = computeWOut(i);
            if (wOut > wIn) {
                WeightedEdge d2 = leftMostEdgeToPoint(i);
                d2.setWeight(wOut - wIn + d2.getWeight());
            }
        }

        balanced = true;
    }

    /**
     * @param weights edge weights
     * @return true if there are positive weight edges outgoing from first node
     */
    private boolean presentEdgesFrom0(int[] weights) {
        for (int index : outgoingEdgeIndices.get(0)) {
            if (weights[index] > 0) {
                return true;
            }
        }
        return false;
    }

    private void checkChainMethodApplicabilityEdges() {
        // Check for method applicability when the graph has a node
        //  higher than 0th but can't be reached
        for (int i = 1; i < nodes.size(); i++) {
            if (incomingEdgeIndices.get(i).isEmpty()) {
                isChainsMethodApplicable = false;
                return;
            }
        }

        // when the graph has a node lower than nth
        // but has no outgoing edges
        for (int i = 0; i < nodes.size() - 1; i++) {
            if (outgoingEdgeIndices.get(i).isEmpty()) {
                isChainsMethodApplicable = false;
                return;
            }
        }
    }

    private void checkChainMethodApplicabilityChains() {
        // Graph chains must be monotonous relative to a line.
        // For simplicity, Y axis is chosen to be this line

        Point2D.Float prevPoint;
        for (ArrayList<WeightedEdge> chain : chains) {
            prevPoint = chain.getFirst().getSrc();
            for (WeightedEdge edge : chain) {
                if (edge.getDest().y < prevPoint.y) {
                    isChainsMethodApplicable = false;
                    return;
                }
                prevPoint = edge.getDest();
            }
        }

        // Moreover, for any pair of chains Ci and Cj,
        // all of Ci's nodes which are not Cj's nodes
        // must be on the same side relative to Cj.
        // To check that, search for intersections between edges
        for (int i = 0; i < chains.size(); i++) {
            for (int j = i + 1; j < chains.size(); j++) {
                for (WeightedEdge edgeI : chains.get(i)) {
                    for (WeightedEdge edgeJ : chains.get(j)) {
                        if (edgeI.intersect(edgeJ)) {
                            isChainsMethodApplicable = false;
                            return;
                        }
                    }
                }
            }
        }
        isChainsMethodApplicable = true;
    }

    public ArrayList<ArrayList<WeightedEdge>> getChains() {
        if (isChainsMethodApplicable != null && !isChainsMethodApplicable) {
            return null;
        }
        if (!balanced) {
            throw new RuntimeException("The graph must be balanced first!");
        }
        if (chainsFound) {
            return chains;
        }
        // weights are copied to a separate array
        // so that weights in edges array are not changed
        int[] weightsCopy = new int[edges.size()];
        int k = 0;
        for (var edge : edges) {
            weightsCopy[k++] = edge.getWeight();
        }

        ArrayList<ArrayList<WeightedEdge>> chains = new ArrayList<>();
        ArrayList<WeightedEdge> curChain;
        int curSource = 0;
        WeightedEdge edgeToAdd;
        while (presentEdgesFrom0(weightsCopy)) {
            curChain = new ArrayList<>();
            while (curSource != N - 1) {
                int edgeIndex = leftMostEdgeIndFromPoint(curSource, weightsCopy);
                edgeToAdd = edges.get(edgeIndex);
                curChain.add(edgeToAdd);
                weightsCopy[edgeIndex]--;
                curSource = nodes.indexOf(edgeToAdd.getDest());
            }
            chains.add(curChain);
            curSource = 0;
        }
        if (!chains.isEmpty()) {
            chainsFound = true;
            this.chains = chains;
            checkChainMethodApplicabilityChains();
        }
        return chains;
    }

    /**
     * find between which chains lies the point
     *
     * @param point point to locate
     * @return indices of chains between which lies the point
     */
    public ArrayList<Integer>[] pointLocation(Point2D.Float point) {
        if (isChainsMethodApplicable == null) {
            throw new RuntimeException("Chains not found yet.");
        }
        if (!isChainsMethodApplicable) {
            throw new RuntimeException("Chain method is not applicable to the graph.");
        }
        ArrayList<Integer>[] chainsBetween = new ArrayList[2];
        ArrayList<Integer> leftSide = chainsBetween[0] = new ArrayList<>();
        ArrayList<Integer> rightSide = chainsBetween[1] = new ArrayList<>();
        boolean onEdge = false;
        boolean leftToLeftmost = true;
        if (point.y > nodes.getLast().y) {
            leftSide.add(-3);
            return chainsBetween;
        }
        if (point.y < nodes.getFirst().y) {
            leftSide.add(-4);
            return chainsBetween;
        }
        for (int i = 0; i < chains.size(); i++) {
            var curChain = chains.get(i);
            for (WeightedEdge weightedEdge : curChain) {
                Point2D.Float src = weightedEdge.getSrc();
                Point2D.Float dest = weightedEdge.getDest();
                if (src.getY() < point.getY() && dest.getY() > point.getY()) {
                    double doubledSquare = src.getX() * dest.getY()
                            + point.getX() * src.getY()
                            + dest.getX() * point.getY()
                            - point.getX() * dest.getY()
                            - src.getX() * point.getY()
                            - dest.getX() * src.getY();
                    // if less than 0 - we have a clockwise turn -
                    // the point is right to current chain.
                    // if we already found a chain to the left of the point,
                    // empty the list as the current one is closer
                    if (doubledSquare < 0) {
                        if (i == 0) {
                            leftToLeftmost = false;
                        }
                        leftSide.add(0, i);
                        break;
                    }
                    // if more than 0 - we have a counterclockwise turn -
                    // the point is left to current chain
                    if (doubledSquare > 0) {
                        // if we already know that the point lies on the edge(s)
                        // and the list of chains containing it is now fulfilled,
                        // then we won't have to check remaining chains
                        if (!onEdge)
                            rightSide.add(i);
                        // return as only the current one can be the closest to the right of the point
                        return chainsBetween;
                    }
                    // if equal to 0, the point lies on the edge(s)
                    if (!onEdge) {
                        onEdge = true;
                        leftSide.clear();
                    }
                    leftSide.add(i);
                    break;
                }
            }
        }
        // we run through full cycle only when the point
        // is not left to even the rightmost chain
        leftSide.add(0, -1);
        return chainsBetween;
    }

    public Boolean getIsChainsMethodApplicable() {
        return isChainsMethodApplicable;
    }

    public ArrayList<WeightedEdge> getEdges() {
        return edges;
    }

    public ArrayList<Point2D.Float> getNodes() {
        return nodes;
    }
}
