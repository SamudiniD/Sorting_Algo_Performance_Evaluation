package ui;

import javax.swing.*;
import java.awt.*;          // <-- Missing import (BorderLayout)
import java.util.Map;

public class Results extends JFrame {

    public Results(Map<String, Long> results) {

        setTitle("Sorting Results & Graph");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ---------- LEFT SIDE: Text Results ----------
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        StringBuilder sb = new StringBuilder("Sorting Performance (ns)\n\n");

        long best = Long.MAX_VALUE;
        String bestAlgo = null;

        for (Map.Entry<String, Long> e : results.entrySet()) {
            sb.append(e.getKey()).append(" : ").append(e.getValue()).append("\n");
            if (e.getValue() < best) {
                best = e.getValue();
                bestAlgo = e.getKey();
            }
        }

        sb.append("\nBest Algorithm: ")
                .append(bestAlgo)
                .append(" (")
                .append(best)
                .append(" ns)");

        textArea.setText(sb.toString());

        JScrollPane textScroll = new JScrollPane(textArea);
        textScroll.setPreferredSize(new Dimension(300, 600));

        add(textScroll, BorderLayout.WEST);

        // ---------- RIGHT SIDE: Graph ----------
        GraphWindow.GraphPanel graphPanel = new GraphWindow.GraphPanel(results);
        add(graphPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}
