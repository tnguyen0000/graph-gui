package graph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class GraphGui {
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel(new CardLayout()), homePageContainer = new JPanel(), graphPageContainer = new JPanel();
    private JPanel graphPageNodeContainer = new JPanel();
    private JScrollPane graphPageNodeContainerWrapper = new JScrollPane(graphPageNodeContainer);
    private JButton goToStringGraphBtn, goHomeBtn, addNodeBtn, removeNodeBtn;
    // Index of nodes corresponds to index of edges
    private ArrayList<JTextField> nodes;
    private ArrayList<ArrayList<JTextField>> edgeConnections;
    private ArrayList<ArrayList<JTextField>> edgeWeights;

    public GraphGui() {
        frame.getContentPane().setPreferredSize(new Dimension(700,600));

        frame.add(mainPanel, BorderLayout.NORTH);

        createHomePage();
        createGraphPage();

        setUpCards();        

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Graphs");
        frame.pack();
        frame.setVisible(true);
    }

    private void setUpCards() {
        mainPanel.add(homePageContainer, "HomePage");
        mainPanel.add(graphPageContainer, "GraphPage");
    }

    private void createHomePage() {
        homePageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        goToStringGraphBtn = new JButton(new ButtonAction("Create Graph"));

        goToStringGraphBtn.setPreferredSize(new Dimension(700, 50));

        homePageContainer.add(goToStringGraphBtn);
    }

    private void createGraphPage() {
        JPanel goHomeBtnWrapper = new JPanel(new BorderLayout());
        JPanel addRemoveNodeBtnWrapper = new JPanel(new GridLayout(1, 2));
        graphPageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        graphPageContainer.setLayout(new BoxLayout(graphPageContainer, BoxLayout.PAGE_AXIS));        
        goHomeBtn = new JButton(new ButtonAction("Go Home"));
        addNodeBtn = new JButton(new ButtonAction("Add Node"));
        removeNodeBtn = new JButton(new ButtonAction("Remove Node"));
        graphPageNodeContainer.setLayout(new GridLayout(0, 1));
        graphPageNodeContainerWrapper.setPreferredSize(new Dimension(700, 500));
        nodes = new ArrayList<>();
        edgeConnections = new ArrayList<>();
        edgeWeights = new ArrayList<>();

        goHomeBtnWrapper.add(goHomeBtn);
        addRemoveNodeBtnWrapper.add(removeNodeBtn);
        addRemoveNodeBtnWrapper.add(addNodeBtn);
        graphPageContainer.add(goHomeBtnWrapper);
        graphPageContainer.add(addRemoveNodeBtnWrapper);
        graphPageContainer.add(graphPageNodeContainerWrapper);
    }

    private void addNode() {
        JPanel nodeInformation = new JPanel();
        nodeInformation.setPreferredSize(new Dimension(650,200));
        nodeInformation.setLayout(new BoxLayout(nodeInformation, 0));
        JTextField nodeName = new JTextField();
        nodeName.setPreferredSize(new Dimension(100, 200));
        JPanel edgeInformationGrid = new JPanel(new GridLayout(0, 1));
        JPanel edgeInformationBorder = new JPanel(new BorderLayout());
        edgeInformationBorder.add(edgeInformationGrid, BorderLayout.NORTH);
        JScrollPane edgeInformationWrapper = new JScrollPane(edgeInformationBorder);
        edgeInformationWrapper.setPreferredSize(new Dimension(400, 200));
        JPanel addRemoveEdgeContainer = new JPanel();
        addRemoveEdgeContainer.setLayout(new GridLayout(1, 2, 4, 0));
        addRemoveEdgeContainer.setPreferredSize(new Dimension(150,200));
        JButton addEdgeBtn = new JButton("+");
        JButton removeEdgeBtn = new JButton("-");

        ArrayList<JTextField> currNodeEdges = new ArrayList<>();
        edgeConnections.add(currNodeEdges);
        ArrayList<JTextField> currNodeWeights = new ArrayList<>();
        edgeWeights.add(currNodeWeights);

        addEdgeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel edgeContainer = new JPanel();
                JTextField edgeName = new JTextField(10);
                JTextField edgeWeight = new JTextField("1", 10);
                edgeName.setMaximumSize(new Dimension(10, 10));
                currNodeEdges.add(edgeName);
                currNodeWeights.add(edgeWeight);
                edgeContainer.add(edgeName);
                edgeContainer.add(edgeWeight);
                edgeInformationGrid.add(edgeContainer);
                nodeInformation.revalidate();
                nodeInformation.repaint();
            }
        });

        removeEdgeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currNodeEdges.size() > 0 && currNodeWeights.size() > 0) {
                    currNodeEdges.removeLast();
                    currNodeWeights.removeLast();
                }
                if (edgeInformationGrid.getComponentCount() > 0) {
                    edgeInformationGrid.remove(edgeInformationGrid.getComponentCount() - 1);
                }
                nodeInformation.revalidate();
                nodeInformation.repaint();
            }
        });
        nodes.add(nodeName);
        addRemoveEdgeContainer.add(removeEdgeBtn);
        addRemoveEdgeContainer.add(addEdgeBtn);
        nodeInformation.add(nodeName);
        nodeInformation.add(edgeInformationWrapper);
        nodeInformation.add(addRemoveEdgeContainer);
        graphPageNodeContainer.add(nodeInformation);
    }

    private class ButtonAction extends AbstractAction {
        public ButtonAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            CardLayout layout = (CardLayout) mainPanel.getLayout();
            if (e.getSource() == goHomeBtn) {
                layout.show(mainPanel, "HomePage");
            } else if (e.getSource() == goToStringGraphBtn) {
                layout.show(mainPanel, "GraphPage");
            } else if (e.getSource() == addNodeBtn) {
                addNode();
                graphPageNodeContainerWrapper.validate();
            } else if (e.getSource() == removeNodeBtn) {
                if (nodes.size() > 0) {
                    nodes.removeLast();
                    edgeConnections.removeLast();
                    edgeWeights.removeLast();

                    graphPageNodeContainer.remove(graphPageNodeContainer.getComponentCount() - 1);
                    graphPageNodeContainer.revalidate();
                    graphPageContainer.repaint();
                }
            }
        }
    }
}
