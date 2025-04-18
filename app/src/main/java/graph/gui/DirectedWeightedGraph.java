package graph.gui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/* Assumptions:
 *  Vertex ids range from 0 to n-1 where n is number of vertices
 *  Only unique edges from a given vertex to another vertex (e.g. not multiple edges linking same out-node to same in-node)
 */
abstract class DirectedWeightedGraph<T extends Object> implements GraphMethods<T> {
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

    @Override
    public List<T> topologicalSort() {
        // Map of nodeId to number of in-degrees
        Map<Integer, Integer> inDegrees = new HashMap<>();
        for (int i = 0; i < adjList.size(); i++) {
            inDegrees.putIfAbsent(i, 0);
            List<MyEdge> neighbours = adjList.get(i);
            for (int j = 0; j < neighbours.size(); j++) {
                int inDegree = inDegrees.getOrDefault(neighbours.get(j).getNeigh(), 0);
                inDegrees.put(neighbours.get(j).getNeigh(), inDegree + 1);
            }
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < inDegrees.size(); i++) {
            int inDegree = inDegrees.get(i);
            if (inDegree == 0) {
                q.add(i);
            }
        }

        ArrayList<Integer> resIds = new ArrayList<>();
        while (q.size() > 0) {
            int pop = q.poll();
            resIds.add(pop);
            for (MyEdge edge : adjList.get(pop)) {
                int neighId = edge.getNeigh();
                int newInDegree = inDegrees.get(neighId) - 1;
                if (newInDegree == 0) {
                    q.add(neighId);
                }
                inDegrees.put(neighId, newInDegree);
            }
        }

        if (resIds.size() != vertexNum) {
            // Exception may not be the right one but it's the best for now
            throw new IllegalArgumentException("Cycle detected in graph.");
        }

        Map<Integer, T> inverse = getInverseVertexMap();
        return resIds.stream().map(x -> (inverse.get(x))).collect(Collectors.toList());
    }

    // Returns shortest path from source to other connected nodes through an adjacency list
    protected List<List<MyEdge>> dijkstraHelper(T source) {
        if (!vertexMap.containsKey(source)) {
            throw new IllegalArgumentException("Source node invalid");
        }
        int[] prev = new int[vertexNum];
        Arrays.fill(prev, -1);
        int[] distArr = new int[vertexNum];
        Arrays.fill(distArr, Integer.MAX_VALUE);
        PriorityQueue<Helpers.Pair> pq = new PriorityQueue<>(new Helpers.PairComparator());
        int sourceId = vertexMap.get(source);
        distArr[sourceId] = Integer.MIN_VALUE;
        pq.add(new Helpers.Pair(0, sourceId));
        List<List<MyEdge>> adjList = getAdjList();
        while (pq.size() != 0) {
            Helpers.Pair pop = pq.poll();
            int dist = pop.getDistance();
            int currNodeId = pop.currNodeId();
            for (MyEdge neighbour : adjList.get(currNodeId)) {
                int newDist = dist + neighbour.getWeight();
                int neighbourId = neighbour.getNeigh();
                if (newDist < distArr[neighbourId]) {
                    pq.add(new Helpers.Pair(newDist, neighbourId));
                    distArr[neighbourId] = newDist;
                    prev[neighbourId] = currNodeId;
                }
            }
        }
        Map<Integer, T> inverseMap = getInverseVertexMap();
        List<List<MyEdge>> newAdjList = new ArrayList<>();
        // Creating new adjacency list
        for (int i = 0; i < vertexNum; i++) { 
            List<MyEdge> neighbours = new ArrayList<>();
            newAdjList.add(neighbours);
        }
        // Adding shortest path edges to adjacency list
        for (int i = 0; i < prev.length; i++) {
            if (prev[i] > -1) {
                T v = inverseMap.get(prev[i]);
                T u = inverseMap.get(i);
                int weight = this.getEdgeWeight(v, u);
                newAdjList.get(prev[i]).add(new MyEdge(i, weight));
            }
        }
        return newAdjList;
    }

    @Override
    public int fordFulkersonMaxFlow(T source, T sink) {
        if (!nodeExists(source) || !nodeExists(sink)) {
            throw new IllegalArgumentException("'Source' AND 'Sink' node must exist");
        }
        if (source.equals(sink)) {
            throw new IllegalArgumentException("Source and Sink cannot be the same!");
        }
        int[][] matrix = new int[vertexNum][vertexNum];
        for (int v = 0; v < vertexNum; v++) {
            for (MyEdge edge: adjList.get(v)) {
                int neighId = edge.getNeigh();
                int weight = edge.getWeight();
                matrix[v][neighId] = weight;
            }
        }

        int sourceId = vertexMap.get(source);
        int sinkId = vertexMap.get(sink);
        int[] parent = new int[vertexNum];
        int maxFlow = 0;
        while (fordFulkersonBFS(matrix, sourceId, sinkId, parent)) {
            int currFlow = Integer.MAX_VALUE;
            int search = sinkId;
            while(search != sourceId) {
                int prev = parent[search];
                currFlow = Math.min(currFlow, matrix[prev][search]);
                search = parent[search];
            }

            search = sinkId;
            while(search != sourceId) {
                int prev = parent[search];
                matrix[prev][search] -= currFlow;
                matrix[search][prev] += currFlow;
                search = parent[search];
            }
            maxFlow += currFlow;
        }
        return maxFlow;
    }

    /**
     * Breadth first search to find if augmenting path exists for Ford-Fulkerson
     * @param matrix matrix form of graph
     * @param source source vertex id
     * @param sink sink vertex id
     * @param parent array that holds vertex id used to access index-id vertex
     * @return boolean if path exists from source to sink
     */
    private boolean fordFulkersonBFS(int[][] matrix, int source, int sink, int[] parent) {
        boolean[] vis = new boolean[vertexNum];
        Queue<Integer> q = new ArrayDeque<>();
        q.add(source);
        vis[source] = true;
        parent[source] = -1;
        while (!q.isEmpty()) {
            int popId = q.poll();
            for (int neighId = 0; neighId < vertexNum; neighId++) {
                if (!vis[neighId] && matrix[popId][neighId] > 0) {
                    q.add(neighId);
                    parent[neighId] = popId;
                    vis[neighId] = true;
                }
            }
        }

        return vis[sink];
    }
}
