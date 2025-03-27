package graph.gui;

import java.util.Comparator;

public final class Helpers {
    static public class Pair {
        int distance;
        int currNodeId;
        public Pair(int distance, int currNodeId) {
            this.distance = distance;
            this.currNodeId = currNodeId;
        }

        public int getDistance() {
            return distance;
        }
        public int currNodeId() {
            return currNodeId;
        }

        @Override
        public String toString() {
            return distance + " " + currNodeId;
        }
        
    }
    static public class PairComparator implements Comparator<Pair> {

        @Override
        public int compare(Pair edge1, Pair edge2) {
            int res = edge1.getDistance() - edge2.getDistance();
            if (res != 0) {
                return (int) res;
            }
            return edge1.currNodeId() - edge2.currNodeId();
        }
    }
}
