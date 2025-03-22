package graph.gui;

import java.util.List;

public interface GraphMethods<T extends Object> {
    // Topological sort, should throw err if cycle detected
    public List<T> topologicalSort();
    // Union find for components
    // public List<T> components();
    // public Integer edmondsKarp(T source, T sink);
    // public void MST(T source);
    // public void dijkstra(T source);
    // public void bellmanFord(T source);
    
}
