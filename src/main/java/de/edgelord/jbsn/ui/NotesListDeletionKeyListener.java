package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Notes;
import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class NotesListDeletionKeyListener extends KeyAdapter {

    @Override
    public void keyTyped(final KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            Note[] notes = Utils.getSelectedNotes((JTable) e.getSource());
            boolean confirmedDelete = Utils.showDeleteConfirmDialog(notes);

            if (confirmedDelete) {
                for (final Note n : notes) {
                    try {
                        Notes.removeNote(n);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
