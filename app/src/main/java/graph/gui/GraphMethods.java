package graph.gui;

import java.util.List;

public interface GraphMethods<T extends Object> {
    /* Topological sort
     * 
     * Returns list of vertices in topological order.
     * Throws exception if cycle detected
    */
    public List<T> topologicalSort();
    // public Integer edmondsKarp(T source, T sink);
    // public void MST(T source);
    // public void dijkstra(T source);
    // public void bellmanFord(T source);
    
}
