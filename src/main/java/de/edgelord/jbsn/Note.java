package de.edgelord.jbsn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

/**
 * A note, loaded from a .jb file,
 * or created programmatically and then
 * saved to a File
 */
public class Note extends Configurations {

    public static final String HEADLINE_KEY = "headline";
    public static final String SUBJECT_KEY = "subject";
    public static final String DATE_KEY = "date";
    public static final String VIEWED_KEY = "viewed";
    public static final String NOTES_FILE_KEY = "notesFile";

    private File configFile;

    /**
     * The constructor.
     *
     * @param file the file of the note
     */
    public Note(final File file) throws IOException {
        super(Files.readAllLines(file.toPath()).toArray(new String[0]));
        this.configFile = file;
        setPreserveOrder(true);
    }

    public Note(final String headline, final String subject, final LocalDate date, final int viewed, final String relativePathOfNote) {
        super();

        setAttribute(HEADLINE_KEY, headline);
        setAttribute(SUBJECT_KEY, subject);
        setAttribute(DATE_KEY, date);
        setAttribute(VIEWED_KEY, viewed);
        setAttribute(NOTES_FILE_KEY, new File(AppConfigManager.APP_CONFIG.getNotesSourcesDir(), relativePathOfNote));
        setPreserveOrder(true);
    }

    public static Note createNote(final String headline, final String subject, final LocalDate date)
            throws IOException, InterruptedException {
        final Note note = new Note(headline, subject, date, 0,
                Notes.createdNoteFile(subject, date, headline));
        note.setConfigFile(new File(AppConfigManager.APP_CONFIG.getNotesDir(),
                note.getAttribute(NOTES_FILE_KEY,
                        new File("/")).getName().split("\\.")[0] + "." + Notes.NOTES_FILE_EXTENSION));
        return note;
    }

    public String getSubject() {
        return Utils.getSubject(getAttribute(SUBJECT_KEY));
    }

    public File getSourceFile() {
        return getAttribute(NOTES_FILE_KEY);
    }

    /**
     * Gets {@link #configFile}.
     *
     * @return the value of {@link #configFile}
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * Sets {@link #configFile}.
     *
     * @param configFile the new value of {@link #configFile}
     */
    public void setConfigFile(final File configFile) {
        this.configFile = configFile;
    }

    @Override
    public Object readAttribute(final String key, final String value) {
        switch (key) {
            case DATE_KEY:
                final String[] dateParts = value.split("\\.", 3);
                final int year = Integer.parseInt(dateParts[2]);
                final int month = Integer.parseInt(dateParts[1]);
                final int day = Integer.parseInt(dateParts[0]);
                return LocalDate.of(year, month, day);

            case VIEWED_KEY:
                return Integer.parseInt(value);

            case NOTES_FILE_KEY:
                return new File(String.format(value, AppConfigManager.APP_CONFIG.getNotesSourcesDir()));
            case HEADLINE_KEY:
            case SUBJECT_KEY:

            default:
                return value;
        }
    }

    @Override
    public String writeAttribute(final String key, final Object value) {
        switch (key) {
            case DATE_KEY:
                final LocalDate date = (LocalDate) value;
                return date.getDayOfMonth() + "." + date.getMonthValue() + "." + date.getYear();

            case VIEWED_KEY:
                return String.valueOf((int) value);

            case NOTES_FILE_KEY:
                final File file = (File) value;
                return file.getPath().replaceFirst(AppConfigManager.APP_CONFIG.getNotesSourcesDir(), "%s");
            case HEADLINE_KEY:
            case SUBJECT_KEY:

            default:
                return value.toString();
        }
    }

    public void syncFile(String... path) throws IOException {
        File out = configFile;
        if (configFile == null) {
            if (path.length == 0) {
                throw new IllegalArgumentException("need config file path when" +
                        "created programmatically");
            } else {
                out = new File(path[0]);
            }
        }

        write(new BufferedWriter(new FileWriter(out))).close();
    }
}
