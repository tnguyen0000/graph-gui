package graph.gui;

import java.util.List;

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
}
