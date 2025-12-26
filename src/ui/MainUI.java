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

        // ---------- TOP PANEL ----------
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

        // ---------- TABLE ----------
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ---------- BUTTON ACTIONS ----------
        loadButton.addActionListener(e -> loadCsv());
        evaluateButton.addActionListener(e -> evaluateSorting());
    }

    // ================= CSV LOADING =================
    private void loadCsv() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String headerLine;
            do {
                headerLine = br.readLine();
                if (headerLine == null) {
                    JOptionPane.showMessageDialog(this, "CSV file is empty!");
                    return;
                }
                headerLine = headerLine.trim();
            } while (headerLine.isEmpty());

            // clear old data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            dataset.headers.clear();
            dataset.rows.clear();

            // headers
            List<String> headers = parseCsvLine(headerLine);
            for (String h : headers) {
                tableModel.addColumn(h);
                dataset.headers.add(h);
            }

            // rows
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
                    "CSV loaded successfully (" + dataset.rows.size() + " rows)");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading CSV: " + ex.getMessage());
        }
    }

    private List<String> parseCsvLine(String line) {
        String[] parts = line.split(",", -1);
        List<String> out = new ArrayList<>();
        for (String p : parts) out.add(p.trim());
        return out;
    }

    // ================= NUMERIC COLUMN HANDLING =================
    private void populateNumericColumns() {
        columnDropdown.removeAllItems();
        columnDropdown.addItem("Select numeric column");

        for (int i = 0; i < dataset.headers.size(); i++) {
            if (isNumericColumn(i)) {
                columnDropdown.addItem(dataset.headers.get(i));
            }
        }
    }

    private boolean isNumericColumn(int colIndex) {
        for (String[] row : dataset.rows) {
            if (colIndex >= row.length) return false;
            String val = row[colIndex].trim();
            if (val.isEmpty()) continue;
            try {
                Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private int[] getSelectedNumericColumnData() {
        String selected = (String) columnDropdown.getSelectedItem();
        if (selected == null || selected.equals("Select numeric column")) return null;

        int colIndex = dataset.headers.indexOf(selected);
        if (colIndex < 0) return null;

        int[] data = new int[dataset.rows.size()];
        for (int i = 0; i < dataset.rows.size(); i++) {
            String val = dataset.rows.get(i)[colIndex].trim();
            data[i] = val.isEmpty() ? 0 : Integer.parseInt(val);
        }
        return data;
    }

    // ================= EVALUATE SORTING =================
    private void evaluateSorting() {
        int[] data = getSelectedNumericColumnData();

        if (data == null || data.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select a numeric column first.",
                    "No Column Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Map<String, Long> results = SortRunner.runAll(data);

        StringBuilder sb = new StringBuilder();
        sb.append("Sorting Performance (nanoseconds)\n\n");

        String bestAlgo = null;
        long bestTime = Long.MAX_VALUE;

        for (Map.Entry<String, Long> entry : results.entrySet()) {
            sb.append(entry.getKey())
              .append(" : ")
              .append(entry.getValue())
              .append("\n");

            if (entry.getValue() < bestTime) {
                bestTime = entry.getValue();
                bestAlgo = entry.getKey();
            }
        }

        sb.append("\nBest Algorithm: ")
          .append(bestAlgo)
          .append(" (")
          .append(bestTime)
          .append(" ns)");

        JOptionPane.showMessageDialog(this,
                sb.toString(),
                "Sorting Results",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= MAIN =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainUI().setVisible(true);
        });
    }
}
