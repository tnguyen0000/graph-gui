package graph.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/* Assumptions:
 *  Vertex ids range from 0 to n-1 where n is number of vertices
 *  Only unique edges from a given vertex to another vertex (e.g. not multiple edges linking same out-node to same in-node)
 */
abstract class DirectedWeightedGraph<T> {
    public Map<T, Integer> vertexDict = new HashMap<T, Integer>();
    // Inner Integer[] will be consist of edge[2] where edge[0] = to_node and edge[1] = weight
    public ArrayList<ArrayList<Integer[]>> adjList = new ArrayList<ArrayList<Integer[]>>();
    public Integer vertexNum = 0;

    protected DirectedWeightedGraph(ArrayList<T[]> edges, ArrayList<Integer> weights) {
        // e.g edges = Arraylist[[1, 2]], weights = [10] for graph that connects '1' to '2' with edge of weight 10
        for (int i = 0; i < edges.size(); i++) {
            addEdge(edges.get(i)[0], edges.get(i)[1], weights.get(i));
        }
    }

    private void addNode(T node) {
        Integer id = vertexNum;
        vertexDict.put(node, id);
        vertexNum++;
        adjList.add(new ArrayList<Integer[]>());
    }

    public void addEdge(T from, T to, Integer weight) {
        if (!nodeExists(from)) {
            addNode(from);
        }
        if (!nodeExists(to)) {
            addNode(to);
        }
        if (edgeExists(from, to)) {
            return;
        }
        Integer nodeFromId = vertexDict.get(from);
        Integer nodeToId = vertexDict.get(to);
        Integer[] newEdge = {nodeToId, weight};
        ArrayList<Integer[]> fromEdges = adjList.get(nodeFromId);
        fromEdges.add(newEdge);
    }

    public boolean nodeExists(T node) {
        return vertexDict.containsKey(node);
    }

    public boolean edgeExists(T from, T to) {
        if (!nodeExists(from) || !nodeExists(to)) {
            return nodeExists(from) && nodeExists(to);
        }
        Integer nodeFromId = vertexDict.get(from);
        Integer nodeToId = vertexDict.get(to);
        ArrayList<Integer[]> fromEdges = adjList.get(nodeFromId);
        for (Integer[] edge: fromEdges) {
            if (edge[0] == nodeToId) {
                return true;
            }
        }
        return false;
    }
    public Integer getEdgeWeight(T from, T to) {
        // Assumes that nodes AND edge between them exists
        if (!nodeExists(from) || !nodeExists(to)) {
            throw new IllegalArgumentException("'from' AND 'to' node must exist");
        }
        Integer nodeFromId = vertexDict.get(from);
        Integer nodeToId = vertexDict.get(to);
        ArrayList<Integer[]> fromEdges = adjList.get(nodeFromId);
        for (Integer[] edge: fromEdges) {
            if (edge[0] == nodeToId) {
                return edge[1];
            }
        }
        throw new IllegalArgumentException("edge must exist");
    }
}
