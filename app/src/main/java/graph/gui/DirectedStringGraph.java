package graph.gui;

import java.util.List;

public class DirectedStringGraph extends DirectedWeightedGraph<String> {
    public DirectedStringGraph(List<String[]> edges, List<Integer> weights) {
        super(edges, weights);
    }
}
