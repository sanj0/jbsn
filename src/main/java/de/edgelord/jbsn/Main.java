package de.edgelord.jbsn;

import de.edgelord.jbsn.ui.*;
import de.edgelord.jbsn.ui.Button;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// jbsn = Just better school notes
public class Main {

    public static final File CONFIGS = new File("/Users/edgelord/jbsn/config.txt");

    public static final String NOTES_FILE_EXTENSION = "jb";
    public static final FileFilter NOTES_SOURCES_FILTER = pathname -> pathname.getName().endsWith("." + NOTES_FILE_EXTENSION);
    public static AppConfigs APP_CONFIGS;
    public static File NOTES_SOURCES_DIR;
    public static File NOTES_DIR;
    public static final String SUBJECT_PLACEHOLDER = "SUBJECT";
    public static final String NAME_PLACEHOLDER = "NAME";
    public static final String DATE_PLACEHOLDER = "DATE";
    public static final String HEADLINE_PLACEHOLDER = "HEADLINE";
    public static final String PAGES = "pages";
    public static final String WORD = "word";
    public static final String LIBRE_OFFICE = "libre office";

    public static final List<Note> NOTES = new ArrayList<>();

    public static void main(String[] args) throws IOException, URISyntaxException {

        if (!Installer.checkInstalled()) {
            Installer.install();
        }
        configs();
        loadNotes();
        //final Note note = Note.createNote("Erste Stunde!", "Deutsch", Date.from(Instant.now()));
        //note.syncFile();
        final Button[] buttons = new Button[4];

        buttons[0] = Button.CREATE_NOTE_BUTTON();
        buttons[1] = Button.VIEW_ALL_NOTES();
        buttons[2] = Button.VIEW_FILTERED_NOTES();
        buttons[3] = Button.VIEW_NOTES_FOR_NEXT_SCHOOL_DAY();

        final GreetingWindow window = new GreetingWindow(buttons);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                syncNotes();
                Schedule.write();
                APP_CONFIGS.write(new BufferedWriter(new FileWriter(CONFIGS))).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    private static void configs() throws IOException {
        APP_CONFIGS = new AppConfigs(CONFIGS);
        NOTES_SOURCES_DIR = new File(String.format(APP_CONFIGS.getNotesSourcesDir(), APP_CONFIGS.getBaseDir()));
        NOTES_DIR = new File(String.format(APP_CONFIGS.getNotesDir(), APP_CONFIGS.getBaseDir()));

        final int noteSourcesCount = NOTES_SOURCES_DIR.listFiles(NOTES_SOURCES_FILTER).length;
        final int notesCount = NOTES_DIR.listFiles(NOTES_SOURCES_FILTER).length;
        if (noteSourcesCount != notesCount) {
            JOptionPane.showMessageDialog(null, "Notes sources" +
                    " and notes configs are out of sync. Adding notes will likely result in data loss.",
                    "Notes library out of sync", JOptionPane.ERROR_MESSAGE);
        }
        Schedule.readFromConfig();

        final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        if (defaults.get("Table.alternateRowColor") == null) {
            defaults.put("Table.alternateRowColor", new Color(240, 240, 240));
        }
    }

    private static void loadNotes() throws IOException {
        final long startT = System.currentTimeMillis();
        final File[] files = NOTES_DIR.listFiles(NOTES_SOURCES_FILTER);

        for (final File file : files) {
            NOTES.add(new Note(file));
        }
        System.out.println("took " + (System.currentTimeMillis() - startT) / 1000f + "s to load " + NOTES.size() + " notes.");
    }

    public static void openNote(final Note note) throws IOException {
        note.setAttribute(Note.VIEWED_KEY, note.getAttribute(Note.VIEWED_KEY, 0) + 1);
        TableSupply.update();
        Desktop.getDesktop().open(note.getSourceFile());
    }

    public static Note getNote(final String subject, final String date, final String headline) {
        for (final Note n : NOTES) {
            if (n.getSubject().equals(Utils.getSubject(subject))
            && Utils.dateToString(n.getAttribute(Note.DATE_KEY)).equals(date)
            && n.getAttribute(Note.HEADLINE_KEY).equals(headline)) {
                return n;
            }
        }

        return null;
    }

    public static void addNote() throws IOException, InterruptedException {
        String[] params = Utils.notesDialog(null);
        if (params == null) {
            return;
        } else {
            final Note note = Note.createNote(params[1], params[0], Utils.today());
            NOTES.add(note);
            note.write(new BufferedWriter(new FileWriter(note.getConfigFile())));
            TableSupply.update();
        }
    }

    // preserves the actual document
    public static void removeNote(final Note n) throws IOException {
        final File copy = new File(APP_CONFIGS.getRecentlyDeletedDir(), n.getAttribute(Note.HEADLINE_KEY, "").replaceAll("/[<>:\"/|?*]/", "") + ".pages");
        copy.createNewFile();
        Files.copy(n.getSourceFile().toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(n.getConfigFile().toPath());
        Files.delete(n.getSourceFile().toPath());
        NOTES.remove(n);
        TableSupply.update();
    }

    public static void syncNotes() throws IOException {
        for (final Note n : NOTES) {
            n.syncFile();
        }
    }

    /**
     * Creates a note file from a template
     * containing the given headline and date.
     * And returns the name of it.
     * Only creates the source, not the configs
     *
     * @return
     */
    public static String createdNoteFile(final String subject, final Date date,
                                         final String headline) throws IOException, InterruptedException {
        final File file = getNextNotesSourceFile();
        final String absolutePathHFS = file.getAbsolutePath().substring(1).replaceAll("/", ":");
        final ProcessBuilder processBuilder = new ProcessBuilder("osascript", APP_CONFIGS.getTemplateScript(),
                absolutePathHFS, subject, Utils.dateToString(date), headline);
        processBuilder.start().waitFor();

        return file.getName();
    }

    // returns the abs path to the next notes source name
    private static File getNextNotesSourceFile() {
        //
        return new File(APP_CONFIGS.getNotesSourcesDir(), (NOTES.size() + ".pages"));
    }

    /**
     * Replaces SUBJECT, NAME, DATE and HEADLINE
     * in the given String an returns it.
     *
     * @return
     */
    public static String template(final String s, final String subject, final String name,
                                  final String date, final String headline) {
        return s.replaceFirst(SUBJECT_PLACEHOLDER, subject).
                replaceFirst(NAME_PLACEHOLDER, name).
                replaceFirst(DATE_PLACEHOLDER, date).
                replaceFirst(HEADLINE_PLACEHOLDER, headline);
    }

    private static void run() {
        try {
            syncNotes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
