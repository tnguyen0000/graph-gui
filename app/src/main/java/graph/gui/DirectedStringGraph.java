package graph.gui;

import java.util.ArrayList;

public class DirectedStringGraph extends DirectedWeightedGraph<String> {
    public DirectedStringGraph(ArrayList<String[]> edges, ArrayList<Integer> weights) {
        super(edges, weights);
    }
}
