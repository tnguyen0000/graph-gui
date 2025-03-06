package graph.gui;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GraphGui {
    public GraphGui() {
        JFrame frame = new JFrame();
        JPanel mainPanel = new JPanel();

        frame.getContentPane().setPreferredSize(new Dimension(400,400));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        mainPanel.setLayout(new GridLayout(1, 1, 0, 10));

        frame.add(mainPanel, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Graphs");
        frame.pack();
        frame.setVisible(true);
    }
}
