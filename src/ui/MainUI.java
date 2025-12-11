package ui;

import sorting.SortRunner; // keep if you'll use it later; safe to remove for now

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // TOP PANEL with buttons + column dropdown
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

        // Table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Button actions
        loadButton.addActionListener(e -> loadCsv());
        evaluateButton.addActionListener(e -> {
            int[] data = getSelectedNumericColumnData();
            if (data == null) {
                JOptionPane.showMessageDialog(this, "Please select a numeric column first.");
                return;
            }
            Map<String, Long> results = SortRunner.runAll(data); // your SortRunner returns times
            // TODO: visualize results in graphs / show best algorithm
            JOptionPane.showMessageDialog(this, "Sorting times: " + results.toString());
        });

    }

    /**
     * Single loadCsv implementation (reads header, then rows, pads missing cells).
     */
    private void loadCsv() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // ---------- READ HEADER (skipping leading blank lines) ----------
            String headerLine;
            do {
                headerLine = br.readLine();
                if (headerLine == null) {
                    JOptionPane.showMessageDialog(this, "CSV file is empty!");
                    return;
                }
                headerLine = headerLine.trim();
            } while (headerLine.isEmpty());

            // Clear table + dataset for new CSV
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            dataset.headers.clear();
            dataset.rows.clear();

            // Parse header and add columns
            List<String> headers = parseCsvLine(headerLine);
            for (String h : headers) {
                tableModel.addColumn(h);
                dataset.headers.add(h);
            }

            // ---------- READ ROWS ----------
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                List<String> values = parseCsvLine(line);

                // pad missing cells so row length == headers length
                while (values.size() < headers.size()) values.add("");

                tableModel.addRow(values.toArray(new String[0]));
                dataset.rows.add(values.toArray(new String[0]));
            }

            populateNumericColumns();
            JOptionPane.showMessageDialog(this, "CSV loaded successfully (" + dataset.rows.size() + " rows).");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading CSV: " + ex.getMessage());
        }
    }

    // very simple CSV parser — split on comma and trim
    // Note: this does NOT handle quoted commas. For the assignment that's acceptable.
    private List<String> parseCsvLine(String line) {
        String[] parts = line.split(",", -1); // -1 keeps trailing empty columns
        List<String> out = new ArrayList<>(parts.length);
        for (String p : parts) out.add(p.trim());
        return out;
    }

    // Fill dropdown with numeric-only columns
    private void populateNumericColumns() {
        columnDropdown.removeAllItems();
        columnDropdown.addItem("Select numeric column");

        for (int col = 0; col < dataset.headers.size(); col++) {
            if (isNumericColumn(col)) {
                columnDropdown.addItem(dataset.headers.get(col));
            }
        }
    }

    // Check every non-empty cell in column — parse as integer
    private boolean isNumericColumn(int colIndex) {
        for (String[] row : dataset.rows) {
            if (colIndex >= row.length) return false;
            String val = row[colIndex].trim();
            if (val.isEmpty()) continue;
            try {
                Integer.parseInt(val);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    // returns null if no numeric column selected
    private int[] getSelectedNumericColumnData() {
        String selected = (String) columnDropdown.getSelectedItem();
        if (selected == null || selected.equals("Select numeric column")) return null;

        int colIndex = dataset.headers.indexOf(selected);
        if (colIndex < 0) return null;

        int n = dataset.rows.size();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            String[] row = dataset.rows.get(i);
            String v = (colIndex < row.length) ? row[colIndex].trim() : "";
            arr[i] = v.isEmpty() ? 0 : Integer.parseInt(v); // or throw/skip if empty
        }
        return arr;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainUI ui = new MainUI();
            ui.setVisible(true);
        });
    }

    // simple container for data
    static class Dataset {
        List<String> headers = new ArrayList<>();
        List<String[]> rows = new ArrayList<>();
    }
}
