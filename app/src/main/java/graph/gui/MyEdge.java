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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + neigh;
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyEdge other = (MyEdge) obj;
        if (neigh != other.neigh)
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }
}
