package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Main;
import de.edgelord.jbsn.Note;
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
                        Main.removeNote(n);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
