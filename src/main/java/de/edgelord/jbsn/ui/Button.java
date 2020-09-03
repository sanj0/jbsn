package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Main;
import de.edgelord.jbsn.Schedule;
import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * A standard button for the ui.
 * It has text and an icon or only text or icon.
 */
public class Button extends JButton {

    public Button(final String text, final Icon icon) {
        if (icon == null) {
            if (text == null) {
                throw new IllegalArgumentException("Button has to either have text or an icon or both");
            } else {
                setText(text);
            }
        } else if (text == null) {
            setIcon(icon);
        } else {
            setIcon(icon);
            setText(text);
        }
    }

    public static Button CREATE_NOTE_BUTTON() {
        final Button b = new Button("+ new note", null);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Main.addNote();
                } catch (IOException | InterruptedException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        return b;
    }

    public static Button VIEW_ALL_NOTES() {
        final Button b = new Button("view all notes", null);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                new NotesListWindow(NotesFilter.filter());
            }
        });

        return b;
    }

    public static Button VIEW_FILTERED_NOTES() {
        final Button b = new Button("view filtered notes", null);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final NotesFilter filter = NotesFilterDialog.prompt(b.getParent());
                if (filter != null) {
                    new NotesListWindow(filter);
                }
            }
        });

        return b;
    }

    public static Button VIEW_NOTES_FOR_NEXT_SCHOOL_DAY() {
        final Button b = new Button("<html><center>view notes for<br />next school day</center></html>", null);
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                new NotesListWindow(NotesFilter.latestBySubjects(Schedule.getScheduleByDayOfWeek(Utils.getNextSchoolDay()), 2));
            }
        });

        return b;
    }
}
