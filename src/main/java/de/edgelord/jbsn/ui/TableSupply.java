package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Notes;
import de.edgelord.jbsn.Utils;
import de.edgelord.jbsn.filter.NotesFilter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableSupply {

    public static final int DATE_COLUMN = 2;

    private static final Map<JTable, NotesFilter> CLIENTS = new HashMap<>();
    private static final Map<JTable, NotesRowData> DATA = new HashMap<>();
    private static NotesRowData rowData = new NotesRowData();

    public static JTable createClient(final NotesFilter filterRules) {
        final JTable client = new JTable(new CustomTableModel());
        client.addKeyListener(new NotesListDeletionKeyListener());
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        final String open = "Open";
        client.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(enter, open);
        client.getActionMap().put(open, new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                final Note[] notes = Utils.getSelectedNotes((JTable) e.getSource());
                for (final Note n : notes) {
                    try {
                        Notes.openNote(n);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        });
        client.setFont(client.getFont().deriveFont(13f));
        addMatchingNotes(client, filterRules);
        client.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        client.setAutoCreateRowSorter(true);
        CLIENTS.put(client, filterRules);

        return client;
    }

    public static void update() {
        for (final Map.Entry<JTable, NotesFilter> entry : CLIENTS.entrySet()) {
            final JTable client = entry.getKey();
            final NotesFilter filterRules = entry.getValue();
            addMatchingNotes(client, filterRules);
        }
    }

    private static void addMatchingNotes(final JTable client, final NotesFilter rules) {
        final List<Note> filteredNotes = rules.filter(Notes.NOTES);
        DefaultTableModel tableModel = (DefaultTableModel) client.getModel();
        tableModel.setRowCount(0);

        NotesRowData rowData = new NotesRowData(Utils.rowData(filteredNotes));
        DATA.put(client, rowData);
        for (Object[] data : rowData.getData()) {
            tableModel.addRow(data);
        }
    }

    /**
     * Gets {@link #CLIENTS}.
     *
     * @return the value of {@link #CLIENTS}
     */
    public static Map<JTable, NotesFilter> getCLIENTS() {
        return CLIENTS;
    }

    /**
     * Gets {@link #rowData}.
     *
     * @return the value of {@link #rowData}
     */
    public static NotesRowData getRowData() {
        return rowData;
    }

    /**
     * Sets {@link #rowData}.
     *
     * @param rowData the new value of {@link #rowData}
     */
    public static void setRowData(final NotesRowData rowData) {
        TableSupply.rowData = rowData;
    }

    /**
     * Gets {@link #DATA}.
     *
     * @return the value of {@link #DATA}
     */
    public static Map<JTable, NotesRowData> getDATA() {
        return DATA;
    }

    static class CustomTableModel extends DefaultTableModel {

        public CustomTableModel() {
            super(Utils.COLUMNS, 0);
        }

        @Override
        public boolean isCellEditable(final int row, final int column) {
            return false;
        }

        @Override
        public Class<?> getColumnClass(final int columnIndex) {
            if (columnIndex == DATE_COLUMN) {
                return LocalDate.class;
            } else {
                return String.class;
            }
        }
    }
}
