package de.edgelord.jbsn.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesRowData {

    private List<String[]> data;

    public NotesRowData(final String[][] data) {
        this.data = Arrays.asList(data);
    }

    public NotesRowData(final List<String[]> data) {
        this.data = data;
    }

    public NotesRowData() {
        this.data = new ArrayList<>();
    }

    public String[][] exportData() {
        return data.toArray(new String[0][0]);
    }

    /**
     * Gets {@link #data}.
     *
     * @return the value of {@link #data}
     */
    public List<String[]> getData() {
        return data;
    }

    /**
     * Sets {@link #data}.
     *
     * @param data the new value of {@link #data}
     */
    public void setData(final List<String[]> data) {
        this.data = data;
    }
}
