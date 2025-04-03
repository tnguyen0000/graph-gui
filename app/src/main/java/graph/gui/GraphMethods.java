package graph.gui;

import java.util.List;

public interface GraphMethods<T extends Object> {
    /* Topological sort
     * 
     * Returns list of vertices in topological order.
     * Throws exception if cycle detected
    */
    public List<T> topologicalSort();
    /* Dijkstra
     * 
     * Returns DirectedWeightedGraph of dijkstra graph.
     * Throws exception if source node doesn't exist
    */
    public DirectedWeightedGraph<T> dijkstra(T source);
    /* Max flow using Ford-Fulkerson algorithm
     * 
     * Returns max flow integer of graph.
     * Throws exception if source node doesn't exist
    */
    public int fordFulkersonMaxFlow(T source, T sink);    
}
