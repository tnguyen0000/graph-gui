package graph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class GraphGui {
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel(new CardLayout()), homePageContainer = new JPanel(), graphPageContainer = new JPanel();
    private JPanel graphPageNodeContainer = new JPanel(), graphVisualisationCards = new JPanel(new CardLayout()), graphVisualContainer = new JPanel(new BorderLayout());
    private JScrollPane graphPageNodeContainerWrapper = new JScrollPane(graphPageNodeContainer), graphPageContainerScroll = new JScrollPane(graphPageContainer);
    private JButton goToStringGraphBtn, goHomeBtn, addNodeBtn, removeNodeBtn, resetGraphBtn, finishGraphBtn;
    JLabel graphErrorLabel = new JLabel();
    // Index of nodes corresponds to index of edges
    private List<JTextField> nodeTextFields;
    private List<List<EdgeJTextField>> edgeJTextFields; // edgeTextFields[v] = adjacency list of node v which shows neighbour connection + weight
    private DirectedStringGraph directedStringGraph;

    // Initialise GraphGui
    public GraphGui() {
        frame.add(mainPanel, BorderLayout.NORTH);

        createHomePage();
        createGraphPage();

        setUpCards();        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Graphs");
        frame.pack();
        frame.setVisible(true);
    }

    // Sets up card layouts of GUI app
    private void setUpCards() {
        // Sets up two page card layouts
        mainPanel.add(homePageContainer, "HomePage");
        mainPanel.add(graphPageContainerScroll, "GraphPage");

        // Sets up the card for graph visualisation / graph error
        graphVisualisationCards.add(graphVisualContainer, "GraphVisual");
        graphVisualisationCards.add(graphErrorLabel, "GraphError");
    }

    // Sets up the home page
    private void createHomePage() {
        homePageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        goToStringGraphBtn = new JButton(new ButtonAction("Create Graph"));

        goToStringGraphBtn.setPreferredSize(new Dimension(700, 50));

        homePageContainer.add(goToStringGraphBtn);
    }

    // Sets up the graph creation/rendering page
    private void createGraphPage() {
        JPanel goHomeBtnWrapper = new JPanel(new BorderLayout());
        JPanel addRemoveNodeBtnWrapper = new JPanel(new GridLayout(1, 2));
        JPanel resetGraphBtnWrapper = new JPanel(new BorderLayout());
        JPanel finishGraphBtnWrapper = new JPanel(new BorderLayout());
        graphPageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        graphPageContainer.setLayout(new BoxLayout(graphPageContainer, BoxLayout.Y_AXIS));
        //graphPageContainer.setPreferredSize(new Dimension(100, 600));
        goHomeBtn = new JButton(new ButtonAction("Go Home"));
        graphPageMainButtonsFunctionality();
        graphPageNodeContainer.setLayout(new GridLayout(0, 1));
        graphPageNodeContainerWrapper.setPreferredSize(new Dimension(700, 500));
        nodeTextFields = new ArrayList<>();
        edgeJTextFields = new ArrayList<>();

        goHomeBtnWrapper.add(goHomeBtn);
        resetGraphBtnWrapper.add(resetGraphBtn);
        finishGraphBtnWrapper.add(finishGraphBtn);
        addRemoveNodeBtnWrapper.add(removeNodeBtn);
        addRemoveNodeBtnWrapper.add(addNodeBtn);
        graphPageContainer.add(goHomeBtnWrapper);
        graphPageContainer.add(addRemoveNodeBtnWrapper);
        graphPageContainer.add(graphPageNodeContainerWrapper);
        graphPageContainer.add(resetGraphBtnWrapper);
        // Rigid area for space between finishing graph / resetting graph
        graphPageContainer.add(Box.createRigidArea(new Dimension(0,20)));
        graphPageContainer.add(finishGraphBtnWrapper);

        graphPageContainer.add(graphVisualisationCards);
    }

    // Adds button functionality to graph page
    private void graphPageMainButtonsFunctionality() {
        addNodeBtn = new JButton("Add Node");
        addNodeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNode();
                graphPageNodeContainerWrapper.validate();
            }
        });

        removeNodeBtn = new JButton("Remove Node");
        removeNodeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (nodeTextFields.size() > 0) {
                    nodeTextFields.removeLast();
                    edgeJTextFields.removeLast();
                    graphPageNodeContainer.remove(graphPageNodeContainer.getComponentCount() - 1);
                    graphPageContainer.revalidate();
                    graphPageContainer.repaint();
                }
            }
        });

        resetGraphBtn = new JButton("Reset Graph");
        resetGraphBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphVisualisationCards.setVisible(false);
                nodeTextFields.clear();
                edgeJTextFields.clear();
                graphPageNodeContainer.removeAll();
                graphPageNodeContainer.revalidate();
                graphPageContainer.repaint();
            }
        });

        finishGraphBtn = new JButton("Finish Graph");
        finishGraphBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (graphVisualContainer.getComponentCount() > 0) {
                        graphVisualContainer.removeAll();
                    }
                    if (nodeTextFields.size() == 0) {
                        return;
                    }
                    validateAndSetGraph();
                    addGraphVisualisation();
                    CardLayout layout = (CardLayout) graphVisualisationCards.getLayout();
                    layout.show(graphVisualisationCards, "GraphVisual");
                    graphVisualisationCards.setVisible(true);
                } catch (Exception error) {
                    CardLayout layout = (CardLayout) graphVisualisationCards.getLayout();
                    graphErrorLabel.setText(error.toString());
                    layout.show(graphVisualisationCards, "GraphError");
                    graphVisualisationCards.setVisible(true);
                } finally {
                    graphPageContainer.repaint();
                    graphPageContainer.revalidate();
                }
            }
        });
    }

    // Functionality for adding nodes to graph
    private void addNode() {
        JPanel nodeInformation = new JPanel();
        nodeInformation.setPreferredSize(new Dimension(650,100));
        nodeInformation.setLayout(new BoxLayout(nodeInformation, 0));
        JPanel nodeNameContainer = new JPanel(new GridLayout(1, 2));
        nodeNameContainer.setPreferredSize(new Dimension(100, 200));
        nodeNameContainer.add(new JLabel(String.valueOf(nodeTextFields.size())));
        JTextField nodeName = new JTextField(String.valueOf(nodeTextFields.size()));
        nodeNameContainer.add(nodeName);
        nodeNameContainer.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        nodeTextFields.add(nodeName);
        nodeInformation.add(nodeNameContainer);
        this.addEdgeInformation(nodeInformation);
        graphPageNodeContainer.add(nodeInformation);
    }

    // Extension of addNode() which adds edge information for added node
    private void addEdgeInformation(JPanel nodeInformation) {
        // edgeInformationBorder exists to prevent GridLayout from evenly spacing added edges in panel
        JPanel edgeInformationBorder = new JPanel(new BorderLayout());
        JPanel edgeInformationGrid = new JPanel(new GridLayout(0, 1));
        edgeInformationBorder.add(edgeInformationGrid, BorderLayout.NORTH);
        JScrollPane edgeInformationWrapper = new JScrollPane(edgeInformationBorder);
        edgeInformationWrapper.setPreferredSize(new Dimension(400, 200));
        JPanel addRemoveEdgeContainer = new JPanel();
        addRemoveEdgeContainer.setLayout(new GridLayout(1, 2, 4, 0));
        addRemoveEdgeContainer.setPreferredSize(new Dimension(150,200));
        JButton addEdgeBtn = new JButton("+");
        JButton removeEdgeBtn = new JButton("-");

        List<EdgeJTextField> currNodeEdges = new ArrayList<>();
        edgeJTextFields.add(currNodeEdges);

        // Functionality to add edge to node
        addEdgeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel edgeContainer = new JPanel();
                JLabel edgeNum = new JLabel(String.valueOf(currNodeEdges.size() + 1));
                JTextField edgeName = new JTextField(10);
                JTextField edgeWeight = new JTextField("1", 10);
                edgeName.setMaximumSize(new Dimension(10, 10));
                currNodeEdges.add(new EdgeJTextField(edgeName, edgeWeight));
                edgeContainer.add(edgeNum);
                edgeContainer.add(edgeName);
                edgeContainer.add(edgeWeight);
                edgeInformationGrid.add(edgeContainer);
                nodeInformation.revalidate();
                nodeInformation.repaint();
            }
        });

        // Functionality to remove edge from node
        removeEdgeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currNodeEdges.size() > 0) {
                    currNodeEdges.removeLast();
                }
                if (edgeInformationGrid.getComponentCount() > 0) {
                    edgeInformationGrid.remove(edgeInformationGrid.getComponentCount() - 1);
                }
                nodeInformation.revalidate();
                nodeInformation.repaint();
            }
        });

        addRemoveEdgeContainer.add(removeEdgeBtn);
        addRemoveEdgeContainer.add(addEdgeBtn);
        nodeInformation.add(edgeInformationWrapper);
        nodeInformation.add(addRemoveEdgeContainer);
    }

    // Converts user inputs to DirectedStringGraph for validation
    private void validateAndSetGraph() {
        List<String> nodeStrings = nodeTextFields.stream().map(node -> (node.getText())).collect(Collectors.toList());
        if (nodeStrings.size() == 0) {
            return;
        }
        List<String[]> edges = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        for (int i = 0; i < edgeJTextFields.size(); i++) {
            int j = i;
            edgeJTextFields.get(i).forEach(node -> {
                String neighString = node.getNeigh(); 
                String[] resEdge = {nodeStrings.get(j), neighString};
                edges.add(resEdge);
                int edgeWeight = node.getWeight();
                weights.add(edgeWeight);
            });
        }
        directedStringGraph = new DirectedStringGraph(nodeStrings, edges, weights);
    }

    // Sets up the graph visualiser
    private void addGraphVisualisation() {
        Graph<String, MyEdge> g = convertToJUNGGraph(this.directedStringGraph);
        VisualizationViewer<String, MyEdge> vv = createVisualizationViewer(g);
        GraphZoomScrollPane graphScrollPane = new GraphZoomScrollPane(vv);
        graphVisualContainer.add(graphScrollPane, BorderLayout.NORTH);

        // TODO!: add additional graph functionality e.g. algos
        //JButton test = new JButton("ads");
        //JButton test2 = new JButton("2");
        //graphVisualContainer.add(test, BorderLayout.CENTER);
        //graphVisualContainer.add(test2, BorderLayout.SOUTH);
    }

    // Converts DirectedStringGraph to JUNG Graph
    private Graph<String, MyEdge> convertToJUNGGraph(DirectedStringGraph graph) {
        Graph<String, MyEdge> g = new DirectedSparseGraph<>();
        Map<Integer, String> vertexInverseMap = graph.getInverseVertexMap();
        for (String vertexName : vertexInverseMap.values()) {
            g.addVertex(vertexName);
        }
        List<List<MyEdge>> adjListNodes = graph.getAdjList();
        for (int nodeId = 0; nodeId < graph.vertexNum; nodeId++) {
            List<MyEdge> adjListEdges = adjListNodes.get(nodeId);
            for (int neighNum = 0; neighNum < adjListEdges.size(); neighNum++) {
                MyEdge neighEdge = adjListEdges.get(neighNum);
                String neighString = vertexInverseMap.get(neighEdge.getNeigh());
                g.addEdge(neighEdge, vertexInverseMap.get(nodeId), neighString);
            }
        }
        return g;
    }

    // Creates and returns visualisation viewer
    private VisualizationViewer<String, MyEdge> createVisualizationViewer(Graph<String, MyEdge> g) {
        Layout<String, MyEdge> layout = new FRLayout<String, MyEdge>(g);
        VisualizationViewer<String, MyEdge> vv = new VisualizationViewer<String, MyEdge>(layout, new Dimension(500,500));
        vv.getRenderContext().setVertexLabelTransformer(new Transformer<String, String>() {
            @Override
            public String transform(String arg0) {
                return arg0;
            }
        });
        vv.getRenderContext().setEdgeLabelTransformer(new Transformer<MyEdge, String>() {
            @Override
            public String transform(MyEdge arg0) {
                return arg0.getWeight().toString();
            }
        });
        DefaultModalGraphMouse<String, MyEdge> gm = new DefaultModalGraphMouse<>();
        vv.setGraphMouse(gm);
        return vv;
    }

    // Container class for edge connection + weight
    private class EdgeJTextField {
        JTextField neighField;
        JTextField weightField;
        public EdgeJTextField(JTextField neighField, JTextField weightField) {
            this.neighField = neighField;
            this.weightField = weightField;
        }

        public String getNeigh() {
            return neighField.getText();
        }

        public Integer getWeight() {
            int weight = 1;
            if (!weightField.getText().isEmpty()) {
                weight = Integer.parseInt(weightField.getText());
            }
            return weight;
        }
    }

    // Container class for navigation buttons
    private class ButtonAction extends AbstractAction {
        public ButtonAction(String name) {
            super(name);
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            CardLayout layout = (CardLayout) mainPanel.getLayout();
            if (event.getSource() == goHomeBtn) {
                layout.show(mainPanel, "HomePage");
            } else if (event.getSource() == goToStringGraphBtn) {
                layout.show(mainPanel, "GraphPage");
            }
        }
    }
}
