package graph.gui;

public class MyEdge {
    int neigh;
    int weight;
    public MyEdge(int neigh, int weight) {
        this.neigh = neigh;
        this.weight = weight;
    }

    public Integer getNeigh() {
        return neigh;
    }
    public Integer getWeight() {
        return weight;
    }

}
