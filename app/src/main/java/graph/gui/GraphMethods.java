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
    // public Integer edmondsKarp(T source, T sink);
    // public void MST();
    // public void bellmanFord(T source);
    
}
