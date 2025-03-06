package graph.gui;

import java.util.ArrayList;

public interface GraphMethods<T> {
    public ArrayList<T> topologicalSort();
    public ArrayList<T> components();
    // public Integer edmondsKarp();
    // public void djistra();
    
}
