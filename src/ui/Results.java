package ui;

import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class Results extends JFrame {
        /**Create graph panel for execution time visualization
        Add algorithm names & chart titles
        Display execution times & best algorithm text - updated**/

    public Results(Map<String, Long> results) {
        setTitle("Sorting Results & Graph");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        // ---------- LEFT SIDE: Text Results ----------
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

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

        // ---------- RIGHT SIDE: Graph Panel ----------
        add(new GraphWindow.GraphPanel(results), BorderLayout.CENTER);

        setVisible(true);
    }
}
