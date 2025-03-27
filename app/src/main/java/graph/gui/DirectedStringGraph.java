package graph.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectedStringGraph extends DirectedWeightedGraph<String> {
    public DirectedStringGraph(List<String[]> edges, List<Integer> weights) {
        super(edges, weights);
    }

    public DirectedStringGraph(List<String> nodes, List<String[]> edges, List<Integer> weights) {
        super(nodes, edges, weights);
    }
    
    @Override
    protected void addEdgeSlowSetup(String from, String to, Integer weight) {
        if (from.isEmpty()) {
            throw new IllegalArgumentException("'from' node must not be blank");
        }
        if (to.isEmpty()) {
            throw new IllegalArgumentException("'to' node must not be blank");
        }
        super.addEdgeSlowSetup(from, to, weight);
    }

    @Override
    public DirectedWeightedGraph<String> dijkstra(String source) {
        List<List<MyEdge>> shortestPath = super.dijkstraHelper(source);
        Map<Integer, String> inverseMap = super.getInverseVertexMap();
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>(); 
        for (int i = 0; i < shortestPath.size(); i++) {
            List<MyEdge> neighbours = shortestPath.get(i);
            for (int j = 0; j < neighbours.size(); j++) {
                MyEdge neighbour = neighbours.get(j);
                String[] edge = {inverseMap.get(i), inverseMap.get(neighbour.getNeigh())};
                edges.add(edge);
                weights.add(neighbour.getWeight());
            }
        }
        return new DirectedStringGraph(edges, weights);
    }
}
