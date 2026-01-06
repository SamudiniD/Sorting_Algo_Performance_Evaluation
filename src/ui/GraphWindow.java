// for testing results genaration
package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphWindow extends JFrame {

    private Map<String, Long> results;

    public GraphWindow(Map<String, Long> results) {
        this.results = results;

        setTitle("Sorting Algorithm Performance");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new GraphPanel(results));
        setVisible(true);
    }

    // Panel that draws the bar chart
    static class GraphPanel extends JPanel {

        private Map<String, Long> results;

        public GraphPanel(Map<String, Long> results) {
            this.results = results;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (results == null || results.isEmpty()) {
                g.drawString("No data available", 20, 20);
                return;
            }

            // Chart settings
            int x = 50;
            int yBase = getHeight() - 60;
            int barWidth = 80;

            long maxValue = results.values().stream().mapToLong(v -> v).max().orElse(1);

            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Execution Time Comparison (ms)", 20, 20);

            // Draw bars
            for (Map.Entry<String, Long> entry : results.entrySet()) {
                String name = entry.getKey();
                long time = entry.getValue();

                int barHeight = (int) ((double) time / maxValue * 200);

                g.setColor(Color.BLUE);
                g.fillRect(x, yBase - barHeight, barWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawRect(x, yBase - barHeight, barWidth, barHeight);

                g.drawString(name, x, yBase + 20);
                g.drawString(time + "ms", x, yBase - barHeight - 5);

                x += barWidth + 30;
            }
        }
    }
}

