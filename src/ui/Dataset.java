package ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Dataset class
 * Stores CSV headers and rows loaded from file Update
 */
public class Dataset {
     // Column names
    public List<String> headers;

    // Each row is a String array (same length as headers)
    public List<String[]> rows;

    public Dataset() {
        headers = new ArrayList<>();
        rows = new ArrayList<>();
    }

    /** Clear all data */
    public void clear() {
        headers.clear();
        rows.clear();
    }

    /** Number of rows */
    public int rowCount() {
        return rows.size();
    }

    /** Number of columns */
    public int columnCount() {
        return headers.size();
    }

    /** Get column index by name */
    public int getColumnIndex(String columnName) {
        return headers.indexOf(columnName);
    }

    /** Check if column exists */
    public boolean hasColumn(String columnName) {
        return headers.contains(columnName);
    }

    /** Get value safely */
    public String getValue(int row, int col) {
        if (row < 0 || row >= rows.size()) return "";
        if (col < 0 || col >= headers.size()) return "";
        return rows.get(row)[col];
    }
}

