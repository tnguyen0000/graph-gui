package graph.gui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DirectedStringGraphTest {
    @Test
    @DisplayName("Basic 3 Node Graph with 2 Edges, Slow Setup")
    public void basic3NodeGraphSlowSetup() {
        // Graph = 1->2->3
        List<String> nodes = new ArrayList<>();
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        nodes.addAll(Arrays.asList("1","2","3"));
        String[] fstEdge = {"1","2"};
        String[] sndEdge = {"2","3"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge));
        weights.addAll(Arrays.asList(1,5));
        
        DirectedStringGraph graph = new DirectedStringGraph(nodes, edges, weights);
        Map<String, Integer> expectedNodes = Map.of(
            "1", 0,
            "2", 1,
            "3", 2
        );
        // Nodes
        assertTrue(graph.getVertexMap().equals(expectedNodes));

        // Edges
        MyEdge fstExpected = new MyEdge(1, 1);
        MyEdge sndExpected = new MyEdge(2, 5);
        List<MyEdge> fstList = new ArrayList<>();
        fstList.add(fstExpected);
        List<MyEdge> sndList = new ArrayList<>();
        sndList.add(sndExpected);
        List<List<MyEdge>> expectedEdges = new ArrayList<>();
        expectedEdges.add(fstList);
        expectedEdges.add(sndList);
        expectedEdges.add(new ArrayList<MyEdge>());
        List<List<MyEdge>> actualEdges = graph.getAdjList();
        // Using bulky assertAll instead of assertIterableEquals as implementing equals function in MyEdge would break JUNG graph in main class
        assertAll("Test expected adj list = actual adj list",
            () -> assertEquals(expectedEdges.get(0).get(0).getNeigh(), actualEdges.get(0).get(0).getNeigh()),
            () -> assertEquals(expectedEdges.get(1).get(0).getNeigh(), actualEdges.get(1).get(0).getNeigh()),
            () -> assertEquals(expectedEdges.get(2), actualEdges.get(2))
        );
    }

}
