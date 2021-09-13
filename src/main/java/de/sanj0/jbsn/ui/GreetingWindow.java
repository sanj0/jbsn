package de.sanj0.jbsn.ui;

import de.sanj0.jbsn.Notes;
import de.sanj0.jbsn.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * A JFrame that contains four centred buttons
 * which is to be displayed on program startup.
 */
public class GreetingWindow extends JFrame {

    private final JButton addNoteButton = Buttons.CREATE_NOTE_BUTTON();
    private final JButton viewAllNotesButton = Buttons.VIEW_ALL_NOTES();
    private final JButton viewFilteredNotesButton = Buttons.VIEW_FILTERED_NOTES();
    private final JButton notesForNextSchoolDayButton = Buttons.VIEW_NOTES_FOR_NEXT_SCHOOL_DAY();

    public GreetingWindow() {
        super("jbsn - just better school notes");

        setPreferredSize(new Dimension(750, 750));
        add(new SettingsButtonPanel());

        setLayout(new GridLayout(4, 4));

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                System.out.println("ping");
            }
        });

        //Add buttons and placeholders in order
        // to build the layout
        Utils.placeholderHere(this, 4);
        add(addNoteButton);
        add(viewAllNotesButton);
        Utils.placeholderHere(this, 2);
        add(viewFilteredNotesButton);
        add(notesForNextSchoolDayButton);
        Utils.placeholderHere(this, 5);

        // make keyboard shortcuts possible
        // without adding a listener to all
        // component due to focus by adding
        // a custom KeyEventDispatcher
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyboardShortcutsDispatcher());

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private class SettingsButtonPanel extends JPanel {
        public SettingsButtonPanel() {
            final JButton settingsButton = new JButton("<html><center>&#9881;<center></html>");
            settingsButton.setFont(getFont().deriveFont(35f));
            settingsButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PreferencesWindow.window.setVisible(true);
                }
            });

            // build layout with the button
            // and placeholders
            setLayout(new GridLayout(2, 2));
            add(settingsButton);
            Utils.placeholderHere(this, 3);
        }
    }

    private class KeyboardShortcutsDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_N && e.isMetaDown()) {
                    if (e.isAltDown()) {
                        try {
                            Notes.newTimestamp();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    } else {
                        try {
                            Notes.newNote(GreetingWindow.this);
                        } catch (IOException | InterruptedException | ExecutionException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
            return false;
        }
    }
}
