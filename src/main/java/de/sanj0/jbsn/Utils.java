package de.sanj0.jbsn;

import de.sanj0.jbsn.filter.NotesFilter;
import de.sanj0.jbsn.ui.NotesListWindow;
import de.sanj0.jbsn.ui.RequestFocusListener;
import de.sanj0.jbsn.ui.TableSupply;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

/**
 * Bin for all static util methods
 */
public class Utils {

    public static final String[] COLUMNS = new String[]{"subject", "headline", "date", "times viewed"};
    public static final String NO_TIMESTAMP = "No timestamp";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("EE dd.MM.yyy", Locale.GERMANY);
    private static final Map<String, String> SUBJECT_MAP = new HashMap<>();
    private static final String[] SUBJECTS = readSubjects();
    private static String lastSubject = null;
    private static boolean lastSubjectWasAll = false;

    private static String[] readSubjects() {
        final String[] subjectMappings = AppConfigManager.APP_CONFIG.getSubjects().split(",");
        final String[] subjects = new String[subjectMappings.length];

        int i = 0;
        for (final String s : subjectMappings) {
            final String[] parts = s.split(":");
            subjects[i] = parts[1];
            SUBJECT_MAP.put(parts[0], parts[1]);
            i++;
        }
        Arrays.sort(subjects, java.text.Collator.getInstance());

        return subjects;
    }

    public static int executeApplescript(final String scriptPath, final String... args) throws IOException, InterruptedException {
        final String[] argv = new String[args.length + 2];
        argv[0] = "osascript";
        argv[1] = scriptPath;
        if (args.length > 0)
            System.arraycopy(args, 0, argv, 2, argv.length - 2);
        System.out.println("ex% " + Arrays.toString(argv));

        final ProcessBuilder processBuilder = new ProcessBuilder(argv);
        return processBuilder.start().waitFor();
    }

    /**
     * format: dd.MM.yyyy
     */
    public static LocalDate dateFromString(final String value) {
        final String[] dateParts = value.split("\\.", 3);
        final int year = Integer.parseInt(dateParts[2]);
        final int month = Integer.parseInt(dateParts[1]);
        final int day = Integer.parseInt(dateParts[0]);
        return LocalDate.of(year, month, day);
    }

    /**
     * format: dd.MM.yyyy
     */
    public static String stringFromDate(final LocalDate date) {
        return date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();
    }

    public static void placeholderHere(final Container component, final int count) {
        for (int i = 0; i < count; i++) {
            component.add(Box.createGlue());
        }
    }

    public static String toHFSPath(final String posixPath) {
        if (posixPath.startsWith("/")) {
            return posixPath.substring(1).replaceAll("/", ":");
        } else {
            return posixPath.replaceAll("/", ":");
        }
    }

    public static boolean isSameDay(final LocalDate aDate, final LocalDate anotherDate) {
        return aDate.isEqual(anotherDate);
    }

    public static int rotateValue(final int value, final int shift, final int min, final int max) {
        if (value + shift > max) {
            return min + (shift - max + value);
        } else {
            return Math.max(min, value + shift);
        }
    }

    /**
     *
     * @return the current day of week or the next school day,
     * if the current day is not a school day
     */
    public static DayOfWeek getCurrentSchoolDay() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek >= Calendar.FRIDAY) {
            return DayOfWeek.MONDAY;
        } else {
            return DayOfWeek.of(rotateValue(dayOfWeek, -1, 1, 7));
        }
    }

    public static DayOfWeek getNextSchoolDay() {
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek >= Calendar.FRIDAY) {
            return DayOfWeek.MONDAY;
        } else {
            // DayOfWeek starts at 1 while the Calender
            // values start at 0. Hence APIs are not really
            // compatible, but I don't want to refactor the code,
            // so this ugly line exists, where the next
            // day is value-equal to the current day
            // but the next day because Calender constants
            // and DayOfWeek values are shifted by
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

    public static Note[] getSelectedNotes(final JTable list) {
        int[] selectedRows = list.getSelectedRows();
        Note[] selectedNotes = new Note[selectedRows.length];
        int i = 0;
        for (int row : selectedRows) {
            final String[] noteData = TableSupply.getDATA().get(list).getData().get(row);
            final Note note = Notes.getNote(noteData[0], noteData[2], noteData[1]);
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
            data[i] = new String[]{
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
            if (lastSubjectWasAll) {
                subject.setSelectedItem("all");
            } else {
                subject.setSelectedItem(lastSubject);
            }
        } else {
            subject.setSelectedItem(lastSubject);
        }

        return subject;
    }

    public static JComboBox<String> timestampComboBox() {
        final List<String> timestamps = new ArrayList<>();
        Notes.TIMESTAMPS.forEach(t -> timestamps.add(t.getAttribute(ElementConfig.NAME_KEY) + ", " +
                Utils.stringFromDate(t.getDate()) + " (" + t.getSubject() + ")"));
        timestamps.add(NO_TIMESTAMP);
        final JComboBox<String> comboBox = new JComboBox<>(timestamps.toArray(new String[0]));
        comboBox.setSelectedIndex(timestamps.size() - 1);
        return comboBox;
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
        headline.addAncestorListener(new RequestFocusListener());
        Object[] message = {
                "Subject:", subject,
                "Headline:", headline
        };

        headline.requestFocusInWindow();
        int option = JOptionPane.showConfirmDialog(parent, message, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subjectText = (String) subject.getSelectedItem();
            updateLastSubject(subjectText);
            subjectText = getSubject(subjectText);
            return new String[]{subjectText, headline.getText()};
        } else {
            return null;
        }
    }

    public static void updateLastSubject(final String subject) {
        if (subject == null || subject.equals("all")) {
            lastSubjectWasAll = true;
        } else {
            lastSubject = subject;
        }
    }

    public static Timestamp newTimestampDialog() {
        final JComboBox<String> subject = Utils.subjectComboBox(true);
        final JTextField name = new JTextField();
        name.addAncestorListener(new RequestFocusListener());
        Object[] message = {
                "Subject:", subject,
                "Name:", name
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add Timestamp", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            final String subjectText = subject.getSelectedItem().toString();
            updateLastSubject(subjectText);
            return new Timestamp(subjectText, name.getText());
        } else {
            return null;
        }
    }

    public static boolean showDeleteConfirmDialog(final Note[] notes) {
        if (notes.length == 1) {
            String message = "Do you really want to delete the note of subject " + notes[0].getSubject()
                    + " from " + Utils.dateToString(notes[0].getAttribute(Note.DATE_KEY)) + "?";
            String title = "Confirm deletion of note \"" + notes[0].getAttribute(Note.HEADLINE_KEY) + "\"?";
            return JOptionPane.showConfirmDialog(null, message, title,
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
        } else if (notes.length != 0) {
            final Set<String> subjects = new HashSet<>();
            for (final Note n : notes) {
                subjects.add(n.getSubject());
            }
            String message = "Do you really want to delete " + notes.length + " notes of " + subjects.size() + " subjects?";
            String title = "Confirm deletion of " + notes.length + " notes?";
            return JOptionPane.showConfirmDialog(null, message, title,
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
        }

        return false;
    }

    public static LocalDate today() {
        return LocalDate.now();
    }

    public static String dateToString(final LocalDate date) {
        return date.format(DATE_FORMAT);
    }
}
