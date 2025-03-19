package graph.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/* Assumptions:
 *  Vertex ids range from 0 to n-1 where n is number of vertices
 *  Only unique edges from a given vertex to another vertex (e.g. not multiple edges linking same out-node to same in-node)
 */
abstract class DirectedWeightedGraph<T> {
    public BiMap<T, Integer> vertexMap = HashBiMap.create();
    // Inner Integer[] will be consist of edge[2] where edge[0] = to_node and edge[1] = weight
    public List<List<MyEdge>> adjList = new ArrayList<>();
    public int vertexNum = 0;

    // This is for fast setup e.g. will add nodes based on nodes appearing on edge list
    protected DirectedWeightedGraph(List<T[]> edges, List<Integer> weights) {
        // e.g edges = [[1,2],[2,3]], weights = [10,50] for graph that connects '1' to '2' with edge of weight 10, and '2' to '3' with weight 50
        for (int i = 0; i < edges.size(); i++) {
            addEdgeFastSetup(edges.get(i)[0], edges.get(i)[1], weights.get(i));
        }
    }
    
    // For slow setup e.g. will first add nodes from nodes list then add edges
    protected DirectedWeightedGraph(List<T> nodes, List<T[]> edges, List<Integer> weights) {
        // e.g edges = [[1,2],[2,3]], weights = [10,50] for graph that connects '1' to '2' with edge of weight 10, and '2' to '3' with weight 50
        for (int i = 0; i < nodes.size(); i++) {
            addNode(nodes.get(i));
        }
        for (int i = 0; i < edges.size(); i++) {
            addEdgeSlowSetup(edges.get(i)[0], edges.get(i)[1], weights.get(i));
        }
    }

    private void addNode(T node) {
        int id = vertexNum;
        if (vertexMap.containsKey(node)) {
            throw new IllegalArgumentException(String.format("Duplicate node %s", node));
        }
        vertexMap.put(node, id);
        vertexNum++;
        adjList.add(new ArrayList<MyEdge>());
    }

    public void addEdgeFastSetup(T from, T to, Integer weight) {
        if (!nodeExists(from)) {
            addNode(from);
        }
        if (!nodeExists(to)) {
            addNode(to);
        }
        if (edgeExists(from, to)) {
            return;
        }
        int nodeFromId = vertexMap.get(from);
        int nodeToId = vertexMap.get(to);
        MyEdge newEdge = new MyEdge(nodeToId, weight);
        List<MyEdge> fromEdges = adjList.get(nodeFromId);
        fromEdges.add(newEdge);
    }

    protected void addEdgeSlowSetup(T from, T to, Integer weight) {
        if (!nodeExists(from)) {
            throw new IllegalArgumentException("'from' node must exist");
        }
        if (!nodeExists(to)) {
            throw new IllegalArgumentException("'to' node must exist");
        }
        if (edgeExists(from, to)) {
            return;
        }
        int nodeFromId = vertexMap.get(from);
        int nodeToId = vertexMap.get(to);
        MyEdge newEdge = new MyEdge(nodeToId, weight);
        List<MyEdge> fromEdges = adjList.get(nodeFromId);
        fromEdges.add(newEdge);
    }

    public boolean nodeExists(T node) {
        return vertexMap.containsKey(node);
    }

    public boolean edgeExists(T from, T to) {
        if (!nodeExists(from) || !nodeExists(to)) {
            return false;
        }
        int nodeFromId = vertexMap.get(from);
        int nodeToId = vertexMap.get(to);
        List<MyEdge> fromEdges = adjList.get(nodeFromId);
        for (MyEdge edge: fromEdges) {
            if (edge.getNeigh() == nodeToId) {
                return true;
            }
        }
        return false;
    }
    public Integer getEdgeWeight(T from, T to) {
        if (!nodeExists(from) || !nodeExists(to)) {
            throw new IllegalArgumentException("'from' AND 'to' node must exist");
        }
        int nodeFromId = vertexMap.get(from);
        int nodeToId = vertexMap.get(to);
        List<MyEdge> fromEdges = adjList.get(nodeFromId);
        for (MyEdge edge: fromEdges) {
            if (edge.getNeigh() == nodeToId) {
                return edge.getWeight();
            }
        }
        throw new IllegalArgumentException("edge must exist");
    }

    public Map<T, Integer> getVertexMap() {
        return vertexMap;
    }

    // As vertex name and vertex id are unique, the map can be inversed
    public Map<Integer, T> getInverseVertexMap() {
        return vertexMap.inverse();
    }

    public List<List<MyEdge>> getAdjList() {
        return adjList;
    }

    public int getLength() {
        return vertexNum;
    }
}
