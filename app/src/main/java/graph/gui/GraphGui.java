package graph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GraphGui {
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel(new CardLayout()), homePageContainer = new JPanel(), graphPageContainer = new JPanel();
    private Box graphPageNodeContainer = Box.createVerticalBox();
    private JScrollPane graphPageNodeContainerWrapper = new JScrollPane(graphPageNodeContainer);
    private JButton goToStringGraphBtn, goHomeBtn, addNodeBtn;
    // Index of nodes corresponds to index of edges
    private ArrayList<String> nodes;
    private ArrayList<ArrayList<String>> edgeConnections;
    private ArrayList<ArrayList<Integer>> edgeWeights;

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
        homePageContainer.setLayout(new GridLayout(1, 1, 0, 10));
        goToStringGraphBtn = new JButton(new ButtonAction("Create Graph"));

        homePageContainer.add(goToStringGraphBtn);
    }

    private void createGraphPage() {
        graphPageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        graphPageContainer.setLayout(new GridLayout(3, 1, 0, 10));
        goHomeBtn = new JButton(new ButtonAction("Go Home"));
        addNodeBtn = new JButton(new ButtonAction("Add Node"));
        graphPageNodeContainerWrapper.setPreferredSize(new Dimension(0, 100));


        graphPageContainer.add(goHomeBtn);
        graphPageContainer.add(addNodeBtn);
        graphPageContainer.add(graphPageNodeContainerWrapper);
    }

    private void addNode() {
        JPanel nodeInformation = new JPanel();
        nodeInformation.setLayout(new GridLayout(1, 2, 0, 10));
        TextField nodeName = new TextField();
        JPanel edgeInformation = new JPanel();
        nodeInformation.add(nodeName);
        nodeInformation.add(edgeInformation);
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
            }
        }
    }
}
