package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Notes;
import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class TableContextMenu extends JPopupMenu {

    private final List<Note> selectedNotes;
    private final JMenuItem openConfig;
    private final JMenuItem openNotes;
    private final JMenuItem removeNotes;

    public TableContextMenu(final List<Note> selectedNotes) {
        this.selectedNotes = selectedNotes;
        openConfig = new JMenuItem("Open config file(s)");
        openConfig.addActionListener(e -> {
            for (final Note n : selectedNotes) {
                try {
                    Desktop.getDesktop().open(n.getConfigFile());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        openNotes = new JMenuItem("Open note(s)");
        openNotes.addActionListener(e -> {
            for (final Note n : selectedNotes) {
                try {
                    Notes.openNote(n);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        removeNotes = new JMenuItem("Remove note(s)...");
        removeNotes.addActionListener(e -> {
            if (Utils.showDeleteConfirmDialog(selectedNotes.toArray(new Note[0]))) {
                selectedNotes.forEach(n -> {
                    try {
                        Notes.removeNote(n);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            }
        });
        add(openConfig);
        add(openNotes);
        add(removeNotes);
    }
}
