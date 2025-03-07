package graph.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphGui implements ActionListener {
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel(new CardLayout()), homePageContainer = new JPanel(), graphPageContainer = new JPanel();
    private JButton goToStringGraphBtn, goHomeBtn;

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
        goToStringGraphBtn = new JButton("Create graph");
        goToStringGraphBtn.addActionListener(this);

        homePageContainer.add(goToStringGraphBtn);
    }

    private void createGraphPage() {
        graphPageContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        graphPageContainer.setLayout(new GridLayout(3, 1, 0, 10));
        goHomeBtn = new JButton("Go home");
        goHomeBtn.addActionListener(this);

        graphPageContainer.add(goHomeBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout layout = (CardLayout) mainPanel.getLayout();
        if (e.getSource() == goHomeBtn) {
            layout.show(mainPanel, "HomePage");
        } else if (e.getSource() == goToStringGraphBtn) {
            layout.show(mainPanel, "GraphPage");
        }
    }
}
