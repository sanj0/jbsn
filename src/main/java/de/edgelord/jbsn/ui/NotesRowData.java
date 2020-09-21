package de.edgelord.jbsn.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NotesRowData {

    private List<Object[]> data;

    public NotesRowData(final Object[][] data) {
        this.data = Arrays.asList(data);
    }

    public NotesRowData(final List<Object[]> data) {
        this.data = data;
    }

    public NotesRowData() {
        this.data = new ArrayList<>();
    }

    public Object[][] exportData() {
        return data.toArray(new Object[0][0]);
    }

    /**
     * Gets {@link #data}.
     *
     * @return the value of {@link #data}
     */
    public List<Object[]> getData() {
        return data;
    }

    /**
     * Sets {@link #data}.
     *
     * @param data the new value of {@link #data}
     */
    public void setData(final List<Object[]> data) {
        this.data = data;
    }
}
