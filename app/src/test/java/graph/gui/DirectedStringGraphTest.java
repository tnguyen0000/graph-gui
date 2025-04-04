package graph.gui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    @Test
    @DisplayName("Topological sort on graph with 4 nodes")
    public void topologicalSort() {
        // Graph = 2->{1,3}->4
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"2","1"};
        String[] sndEdge = {"2","3"};
        String[] thrdEdge = {"1","4"};
        String[] frthEdge = {"3","4"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge, thrdEdge, frthEdge));
        weights.addAll(Arrays.asList(1,1,1,1));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        List<String> actual = graph.topologicalSort();
        List<String> expected = new ArrayList<>();
        expected.addAll(Arrays.asList("2","1","3","4"));
        assertIterableEquals(expected, actual);
    }

    @Test
    @DisplayName("Dijkstra on simple graph with 4 nodes")
    public void dijkstraAlgoSimple() {
        // Graph = 2->{1,3}->4->3
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"1","2"};
        String[] sndEdge = {"1","3"};
        String[] thrdEdge = {"2","4"};
        String[] frthEdge = {"3","4"};
        String[] fifthEdge = {"4","3"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge, thrdEdge, frthEdge, fifthEdge));
        weights.addAll(Arrays.asList(1,100,1,1,3));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        DirectedStringGraph dijkstraGraph = (DirectedStringGraph) graph.dijkstra("1");
        assertAll("Test expected edge weight = actual weight",
            () -> assertEquals(1, dijkstraGraph.getEdgeWeight("1", "2")),
            () -> assertEquals(1, dijkstraGraph.getEdgeWeight("2", "4")),
            () -> assertEquals(3, dijkstraGraph.getEdgeWeight("4", "3"))
        );
        assertTrue(graph.edgeExists("3", "4"));
        assertThrows(IllegalArgumentException.class, () -> {
            dijkstraGraph.getEdgeWeight("3", "4");
        });
    }

    @Test
    @DisplayName("Dijkstra on graph with no paths")
    public void dijkstraAlgoNoPaths() {
        // Graph = 2->{1,3}->4
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"2","1"};
        String[] sndEdge = {"2","3"};
        String[] thrdEdge = {"1","4"};
        String[] frthEdge = {"3","4"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge, thrdEdge, frthEdge));
        weights.addAll(Arrays.asList(100,1,1,1));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        DirectedStringGraph dijkstraGraph = (DirectedStringGraph) graph.dijkstra("4"); //
        assertThrows(IllegalArgumentException.class, () -> {
            dijkstraGraph.getEdgeWeight("4", "3");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            dijkstraGraph.getEdgeWeight("4", "2");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            dijkstraGraph.getEdgeWeight("3", "4");
        });
    }

    @Test
    @DisplayName("Simple Ford-Fulkerson where bottleneck edge is final edge")
    public void fordFulkersonSimpleOnePath() {
        // Graph = 1->2->3
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"1","2"};
        String[] sndEdge = {"2","3"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge));
        weights.addAll(Arrays.asList(100,2));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        int result = graph.fordFulkersonMaxFlow("1", "3");
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Simple Ford-Fulkerson with multiple edges")
    public void fordFulkersonSimpleMultiplePath() {
        // Graph = 1->{2,3}->4
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"1","2"};
        String[] sndEdge = {"1","3"};
        String[] thrdEdge = {"2","4"};
        String[] frthEdge = {"3","4"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge, thrdEdge, frthEdge));
        weights.addAll(Arrays.asList(2,10,30,10));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        int result = graph.fordFulkersonMaxFlow("1", "4");
        assertEquals(12, result);
    }

    @Test
    @DisplayName("Simple Ford-Fulkerson with linking edge")
    public void fordFulkersonMultiplePath() {
        // Graph = 1->{2,3}->4
        /*        __->[2]__
         *     2/     ^    \30
         * [1]-|     5|    |->[4]
         *    14\__->[3]__/10
         */
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        String[] fstEdge = {"1","2"};
        String[] sndEdge = {"1","3"};
        String[] thrdEdge = {"2","4"};
        String[] frthEdge = {"3","4"};
        String[] linkEdge = {"3","2"};
        edges.addAll(Arrays.asList(fstEdge, sndEdge, thrdEdge, frthEdge, linkEdge));
        weights.addAll(Arrays.asList(2,14,30,10,5));
        DirectedStringGraph graph = new DirectedStringGraph(edges, weights);
        int result = graph.fordFulkersonMaxFlow("1", "4");
        assertEquals(16, result);
    }
}
