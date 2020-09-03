package de.edgelord.jbsn;

import de.edgelord.jbsn.ui.NotesFilter;
import de.edgelord.jbsn.ui.NotesListWindow;
import de.edgelord.jbsn.ui.TableSupply;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class Utils {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EE dd.MM.yyyy", Locale.GERMANY);

    private static String[] SUBJECTS;
    private static Map<String, String> SUBJECT_MAP = new HashMap<>();

    public static final String[] COLUMNS = new String[] {"subject", "headline", "date", "times viewed"};

    static {
        final String[] subjectMappings = Main.APP_CONFIGS.getSubjects().split(",");
        SUBJECTS = new String[subjectMappings.length];

        int i = 0;
        for (final String s : subjectMappings) {
            final String[] parts = s.split(":");
            SUBJECTS[i] = parts[1];
            SUBJECT_MAP.put(parts[0], parts[1]);
            i++;
        }
        Arrays.sort(SUBJECTS, java.text.Collator.getInstance());
    }

    public static void openPreferences() {

    }

    public static DayOfWeek getNextSchoolDay() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek >= Calendar.FRIDAY) {
            return DayOfWeek.MONDAY;
        } else {
            // DayOfWeek starts at 1 while the Calender
            // values start at 0. API are not really compatible,
            // but I don't want to refactor the code,
            // so this ugly line exists, where the next
            // day is value-equal to the current day
            // but the next day because Calender constants
            // and DayOfWeek values are value-shifted by
            // one.
            return DayOfWeek.of(dayOfWeek);
        }
    }

    public static DayOfWeek getLastSchoolDay() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.MONDAY) {
            return DayOfWeek.FRIDAY;
        } else {
            return DayOfWeek.of(dayOfWeek - 1);
        }
    }

    public static String getSubject(final String subject) {
        final String s = subject.trim();
        return SUBJECT_MAP.getOrDefault(s, s);
    }

    public static String subjectPrompt(final Component parent) {
        final JComboBox subject = new JComboBox(SUBJECTS);
        AutoCompleteDecorator.decorate(subject);
        Object[] message = {
                "Subject:", subject,
        };

        int option = JOptionPane.showConfirmDialog(parent, message, "", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subjectText = getSubject((String) subject.getSelectedItem());
            if (subjectText.length() <= 2) {
                subjectText = SUBJECT_MAP.getOrDefault(subjectText, subjectText);
            }
            return subjectText;
        } else {
            return null;
        }
    }

    public static Note[] getSelectedNotes(final JTable list) {
        int[] selectedRows = list.getSelectedRows();
        Note[] selectedNotes = new Note[selectedRows.length];
        int i = 0;
        for (int row : selectedRows) {
            final String[] noteData = TableSupply.getDATA().get(list).getData().get(row);
            final Note note = Main.getNote(noteData[0], noteData[2], noteData[1]);
            selectedNotes[i] = note;
            i++;
        }

        return selectedNotes;
    }

    public static List<Note> getSelectedNotesAsList(final JTable list) {
        return Arrays.asList(getSelectedNotes(list));
    }

    public static String[][] rowData(final List<Note> notes) {
        final String[][] data = new String[notes.size()][3];

        int i = 0;
        for (final Note n : notes) {
            data[i] = new String[] {
                    n.getSubject(),
                    n.getAttribute(Note.HEADLINE_KEY),
                    Utils.dateToString(n.getAttribute(Note.DATE_KEY)),
                    n.getAttribute(Note.VIEWED_KEY).toString()
            };
            i++;
        }

        return data;
    }

    public static JComboBox<String> subjectComboBox(final boolean addAllOption) {
        final JComboBox<String> subject = new JComboBox<>(SUBJECTS);
        if (addAllOption) {
            subject.addItem("all");
            subject.setSelectedItem("all");
        }
        //AutoCompleteDecorator.decorate(subject);
        return subject;
    }

    public static void notePicker(final NotesFilter filterRules, final Consumer<Note> action) {
        new NotesListWindow(filterRules) {
            @Override
            public void onDoubleClick(final Note note) {
                action.accept(note);
            }
        };
    }

    public static String[] notesDialog(final Container parent) {
        final JComboBox<String> subject = subjectComboBox(false);
        final JTextField headline = new JTextField();
        Object[] message = {
                "Subject:", subject,
                "Headline:", headline
        };

        int option = JOptionPane.showConfirmDialog(parent, message, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subjectText = (String) subject.getSelectedItem();
            subjectText = getSubject(subjectText);
            return new String[]{subjectText, headline.getText()};
        } else {
            return null;
        }
    }

    public static boolean showDeleteConfirmDialog(final Note[] notes) {
        if (notes.length == 1) {
            String message = "Do you really want to delete the note of subject " + notes[0].getSubject()
                    + " from " + Utils.dateToString(notes[0].getAttribute(Note.DATE_KEY)) + " (the actual document file stays untouched)?";
            String title = "Confirm deletion of note \"" + notes[0].getAttribute(Note.HEADLINE_KEY) + "\"?";
            return JOptionPane.showConfirmDialog(null, message, title,
                            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
        } else if (notes.length != 0) {
            final Set<String> subjects = new HashSet<>();
            for (final Note n : notes) {
                subjects.add(n.getSubject());
            }
            String message = "Do you really want to delete " + notes.length + " notes of " + subjects.size() + " subjects (the actual document files stay untouched)?";
            String title = "Confirm deletion of " + notes.length + " notes?";
            return JOptionPane.showConfirmDialog(null, message, title,
                            JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
        }

        return false;
    }

    public static Date today() {
        return Date.from(Instant.now());
    }

    public static String quote(final String s) {
        return "'" + s + "'";
    }

    public static String dateToString(final Date date) {
        return DATE_FORMAT.format(date);
    }
}
