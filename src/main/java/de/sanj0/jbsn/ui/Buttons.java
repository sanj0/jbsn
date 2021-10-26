package de.sanj0.jbsn.ui;

import de.sanj0.jbsn.Notes;
import de.sanj0.jbsn.Schedule;
import de.sanj0.jbsn.Utils;
import de.sanj0.jbsn.filter.LatestLessonsOfSubjectFilter;
import de.sanj0.jbsn.filter.NotesFilter;
import de.sanj0.jbsn.filter.NotesFilterDialog;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * A list of static methods to
 * make buttons for the GreetingWindow
 */
public class Buttons {

    public static JButton CREATE_NOTE_BUTTON() {
        final JButton b = new JButton("+ new note");
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                try {
                    Notes.newNote(b.getParent());
                } catch (IOException | InterruptedException | ExecutionException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        return b;
    }

    public static JButton VIEW_ALL_NOTES() {
        final JButton b = new JButton("view all notes");
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                new NotesListWindow(NotesFilter.filter());
            }
        });

        return b;
    }

    public static JButton VIEW_FILTERED_NOTES() {
        final JButton b = new JButton("view filtered notes");
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

    public static JButton VIEW_NOTES_FOR_NEXT_SCHOOL_DAY() {
        final JButton b = new JButton("<html><center>view notes for<br />next school day</center></html>");
        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                new NotesListWindow(new LatestLessonsOfSubjectFilter(Schedule.getScheduleByDayOfWeek(Utils.getNextSchoolDay()), 2));
            }
        });

        return b;
    }
}
