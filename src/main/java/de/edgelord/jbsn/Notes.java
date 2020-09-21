package de.edgelord.jbsn;

import de.edgelord.jbsn.ui.TableSupply;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Notes {
    public static final String NOTES_FILE_EXTENSION = "jb";
    public static final String TIMESTAMPS_FILE_EXTENSION = "timestamp";
    public static final FileFilter NOTES_SOURCES_FILTER = pathname -> pathname.getName().endsWith("." + NOTES_FILE_EXTENSION);
    public static final FileFilter TIMESTAMPS_FILTER = pathname -> pathname.getName().endsWith("." + TIMESTAMPS_FILE_EXTENSION);
    public static final List<Note> NOTES = new ArrayList<>();
    public static final List<Timestamp> TIMESTAMPS = new ArrayList<>();
    public static File NOTES_SOURCES_DIR;
    public static File NOTES_DIR;
    public static File TIMESTAMPS_DIR;

    /**
     * loads notes and timestamps from respective dirs
     */
    static void loadNotes() throws IOException {
        final long startT = System.currentTimeMillis();
        final File[] notes = NOTES_DIR.listFiles(NOTES_SOURCES_FILTER);
        final File[] timestamps = TIMESTAMPS_DIR.listFiles(TIMESTAMPS_FILTER);

        assert notes != null;
        for (final File file : notes) {
            NOTES.add(new Note(file));
        }

        assert timestamps != null;
        for (final File file : timestamps) {
            TIMESTAMPS.add(new Timestamp(file));
        }
        System.out.println("took " + (System.currentTimeMillis() - startT) / 1000f
                + "s to load " + NOTES.size() + " notes and " + TIMESTAMPS.size() + " timestamps.");
    }

    public static void openNote(final Note note) throws IOException {
        note.setAttribute(Note.VIEWED_KEY, note.getAttribute(Note.VIEWED_KEY, 0) + 1);
        TableSupply.update();
        Desktop.getDesktop().open(note.getSourceFile());
    }

    public static Note getNote(final String subject, final FormattedDate date, final String headline) {
        for (final Note n : NOTES) {
            if (n.getSubject().equals(Utils.getSubject(subject))
                    && n.getAttribute(Note.DATE_KEY).equals(date)
                    && n.getAttribute(Note.HEADLINE_KEY).equals(headline)) {
                return n;
            }
        }
        return null;
    }

    public static Timestamp getTimestamp(final String name, final LocalDate date) {
        for (final Timestamp t : TIMESTAMPS) {
            if (t.getAttribute(ElementConfig.NAME_KEY).equals(name)
                    && t.getDate().equals(date)) {
                return t;
            }
        }
        return null;
    }

    public static void newNote(final Container parent) throws IOException, InterruptedException {
        String[] params = Utils.notesDialog(parent);
        if (params != null) {
            final Note note = Note.createNote(params[1], params[0], Utils.today());
            NOTES.add(note);
            note.syncFile();
            TableSupply.update();
        }
    }

    public static void newTimestamp() throws IOException {
        final Timestamp timestamp = Utils.newTimestampDialog();
        if (timestamp != null) {
            timestamp.setConfigFile(getNextTimestampFile());
            TIMESTAMPS.add(timestamp);
            timestamp.syncFile();
        }
    }

    // backups the actual document
    public static void removeNote(final Note n) throws IOException {
        final File copy = new File(AppConfigManager.APP_CONFIG.getRecentlyDeletedDir(), n.getAttribute(Note.HEADLINE_KEY, "").replaceAll("/[<>:\"/|?*]/", "") + ".pages");
        copy.createNewFile();
        Files.copy(n.getSourceFile().toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.delete(n.getConfigFile().toPath());
        Files.delete(n.getSourceFile().toPath());
        NOTES.remove(n);
        TableSupply.update();
    }

    /**
     * syncs notes and timestamps
     */
    public static void syncNotes() throws IOException {
        for (final Note n : NOTES) {
            n.syncFile();
        }

        for (final Timestamp t : TIMESTAMPS) {
            t.syncFile();
        }
    }

    /**
     * Creates a note file from a template
     * containing the given headline and date.
     * And returns the name of it.
     * Only creates the source, not the configs
     *
     * @return the name of the new note file
     */
    public static String createdNoteFile(final String subject, final LocalDate date,
                                         final String headline) throws IOException, InterruptedException {
        final File file = getNextNotesSourceFile();
        final String absolutePathHFS = Utils.toHFSPath(file.getAbsolutePath());
        final ProcessBuilder processBuilder = new ProcessBuilder("osascript", AppConfigManager.APP_CONFIG.getTemplateScript(),
                absolutePathHFS, subject, Utils.dateToString(date), headline);
        processBuilder.start().waitFor();

        return file.getName();
    }

    // returns the abs path to the next notes source name
    private static File getNextNotesSourceFile() {
        return nextFile(AppConfigManager.APP_CONFIG.getNotesSourcesDir(), NOTES.size(), ".pages");
    }

    // returns the abs path to the next timestamps source name
    private static File getNextTimestampFile() {
        return nextFile(AppConfigManager.APP_CONFIG.getTimestampsDir(), TIMESTAMPS.size(), ".timestamp");
    }

    protected static File nextFile(final String parentDir, final int baseName, final String extensions) {
        File file =  new File(parentDir, baseName + extensions);

        for (int i = 0; file.exists(); i++) {
            file = new File(parentDir, file.getName().replace(extensions, "." + i + extensions));
        }

        return file;
    }
}
