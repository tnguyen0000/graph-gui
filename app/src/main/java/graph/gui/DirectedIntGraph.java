package graph.gui;

import java.util.ArrayList;

public class DirectedIntGraph extends DirectedWeightedGraph<Integer> {
    public DirectedIntGraph(ArrayList<Integer[]> edges, ArrayList<Integer> weights) {
        super(edges, weights);
    }
    
    @Override
    public DirectedWeightedGraph<Integer> dijkstra(Integer source) {
        // TODO Auto-generated method stub
        return null;
    }
}
