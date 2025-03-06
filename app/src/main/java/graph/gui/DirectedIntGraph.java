package graph.gui;

import java.util.ArrayList;

/* Assumptions:
 *  Vertex ids range from 0 to n-1 where n is number of vertices
 *  Only unique edges from a given vertex to another vertex (e.g. not multiple edges linking same out-node to same in-node)
 */
public class DirectedIntGraph extends DirectedWeightedGraph<Integer> {
    public DirectedIntGraph(ArrayList<Integer[]> edges, ArrayList<Integer> weights) {
        super(edges, weights);
    }

    public static void main(String[] args) {
        ArrayList<Integer[]> edges = new ArrayList<Integer[]>();
        Integer[] a = {1,2};
        edges.add(a);
        ArrayList<Integer> weights = new ArrayList<Integer>();
        weights.add(1);

        DirectedIntGraph d = new DirectedIntGraph(edges, weights);
    }
}
