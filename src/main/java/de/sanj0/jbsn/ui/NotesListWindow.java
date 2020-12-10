package de.sanj0.jbsn.ui;

import de.sanj0.jbsn.Note;
import de.sanj0.jbsn.Notes;
import de.sanj0.jbsn.Utils;
import de.sanj0.jbsn.filter.NotesFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class NotesListWindow extends JFrame {

    private final JTable table;
    private final JScrollPane pane;

    public NotesListWindow(final NotesFilter filterRules) {
        super("jbsn - Notes");

        table = TableSupply.createClient(filterRules);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {

                if (mouseEvent.isPopupTrigger()) {
                    new TableContextMenu(Utils.getSelectedNotesAsList(table)).show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                } else if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1 && table.getSelectedRow() != -1) {
                    final Note note = Utils.getSelectedNotes(table)[0];
                    try {
                        onDoubleClick(note);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        pane = new JScrollPane(table);
        setLayout(new BorderLayout());
        add(pane, BorderLayout.CENTER);

        setMinimumSize(new Dimension(1000, 500));
        pack();
        setMinimumSize(new Dimension(450, 225));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void onDoubleClick(final Note note) throws IOException {
        Notes.openNote(note);
    }
}
