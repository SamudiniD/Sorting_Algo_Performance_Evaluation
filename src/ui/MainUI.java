package ui;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import sorting.SortRunner;

public class MainUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> columnDropdown;
    private JButton loadButton;
    private JButton evaluateButton;

    private final Dataset dataset = new Dataset();

    public MainUI() {
        super("Sorting Performance Evaluator");
        initUi();
    }

    private void initUi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        loadButton = new JButton("Load CSV");
        columnDropdown = new JComboBox<>();
        columnDropdown.setPreferredSize(new Dimension(240, 24));
        evaluateButton = new JButton("Evaluate Sorting");

        top.add(loadButton);
        top.add(new JLabel("Column:"));
        top.add(columnDropdown);
        top.add(evaluateButton);

        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadButton.addActionListener(e -> loadCsv());
        evaluateButton.addActionListener(e -> evaluateSorting());
    }

    private void loadCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String headerLine;
            do {
                headerLine = br.readLine();
                if (headerLine == null) {
                    JOptionPane.showMessageDialog(this, "CSV is empty!");
                    return;
                }
                headerLine = headerLine.trim();
            } while (headerLine.isEmpty());

            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            dataset.headers.clear();
            dataset.rows.clear();

            List<String> headers = parseCsvLine(headerLine);
            for (String h : headers) {
                tableModel.addColumn(h);
                dataset.headers.add(h);
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> values = parseCsvLine(line);
                while (values.size() < headers.size()) values.add("");

                tableModel.addRow(values.toArray(new String[0]));
                dataset.rows.add(values.toArray(new String[0]));
            }

            populateNumericColumns();

            JOptionPane.showMessageDialog(this,
                    "CSV loaded (" + dataset.rows.size() + " rows)");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private List<String> parseCsvLine(String line) {
        String[] parts = line.split(",", -1);
        List<String> out = new ArrayList<>();
        for (String p : parts) out.add(p.trim());
        return out;
    }

    private void populateNumericColumns() {
        columnDropdown.removeAllItems();
        columnDropdown.addItem("Select numeric column");

        for (int col = 0; col < dataset.headers.size(); col++) {
            if (isNumericColumn(col)) {
                columnDropdown.addItem(dataset.headers.get(col));
            }
        }
    }

    // ✅ FIXED: allow decimal numbers
    private boolean isNumericColumn(int colIndex) {
        for (String[] row : dataset.rows) {
            String val = row[colIndex].trim();
            if (val.isEmpty()) continue;
            try {
                Double.parseDouble(val);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    // Convert double → int for sorting
    private int[] getSelectedNumericColumnData() {
        String selected = (String) columnDropdown.getSelectedItem();
        if (selected == null || selected.equals("Select numeric column"))
            return null;

        int colIndex = dataset.headers.indexOf(selected);
        int[] arr = new int[dataset.rows.size()];

        for (int i = 0; i < dataset.rows.size(); i++) {
            String val = dataset.rows.get(i)[colIndex].trim();
            double d = val.isEmpty() ? 0 : Double.parseDouble(val);
            arr[i] = (int) Math.round(d);
        }
        return arr;
    }

    private void evaluateSorting() {
        int[] data = getSelectedNumericColumnData();

        if (data == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a numeric column first.");
            return;
        }

        Map<String, Long> results = SortRunner.runAll(data);

        StringBuilder sb = new StringBuilder();
        sb.append("Sorting Performance (ns)\n\n");

        String bestAlgo = null;
        long bestTime = Long.MAX_VALUE;

        for (var e : results.entrySet()) {
            sb.append(e.getKey()).append(" : ")
              .append(e.getValue()).append("\n");

            if (e.getValue() < bestTime) {
                bestTime = e.getValue();
                bestAlgo = e.getKey();
            }
        }

        sb.append("\nBest Algorithm: ")
          .append(bestAlgo)
          .append(" (").append(bestTime).append(" ns)");

        JOptionPane.showMessageDialog(this, sb.toString(),
                "Sorting Results", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainUI().setVisible(true));
    }

    static class Dataset {
        List<String> headers = new ArrayList<>();
        List<String[]> rows = new ArrayList<>();
    }
}
