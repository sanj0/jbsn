package de.edgelord.jbsn;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;

/**
 * Configs for Notes and Timestamps
 */
public class ElementConfig extends Configurations {

    public static final String HEADLINE_KEY = "headline";
    public static final String SUBJECT_KEY = "subject";
    public static final String DATE_KEY = "date";
    public static final String VIEWED_KEY = "viewed";
    public static final String NOTES_FILE_KEY = "notesFile";
    public static final String NAME_KEY = "name";

    private File configFile;

    /**
     * A constructor to initialize the
     * element from the given config file.
     *
     * @param file the file of the note
     */
    public ElementConfig(final File file) throws IOException {
        super(Files.readAllLines(file.toPath()).toArray(new String[0]));
        this.configFile = file;
        setPreserveOrder(true);
    }

    /**
     * A constructor to initialize the
     * element from given data.
     */
    public ElementConfig() {
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

    public String getSubject() {
        return Utils.getSubject(getAttribute(SUBJECT_KEY));
    }

    public File getSourceFile() {
        return getAttribute(NOTES_FILE_KEY);
    }

    @Override
    public Object readAttribute(final String key, final String value) {
        switch (key) {
            case DATE_KEY:
                return Utils.dateFromString(value);

            case VIEWED_KEY:
                return Integer.parseInt(value);

            case NOTES_FILE_KEY:
                return new File(String.format(value, AppConfigManager.APP_CONFIG.getNotesSourcesDir()));
            case HEADLINE_KEY:
            case SUBJECT_KEY:
            case NAME_KEY:

            default:
                return value;
        }
    }

    @Override
    public String writeAttribute(final String key, final Object value) {
        switch (key) {
            case DATE_KEY:
                return Utils.stringFromDate((LocalDate) value);

            case VIEWED_KEY:
                return String.valueOf((int) value);

            case NOTES_FILE_KEY:
                final File file = (File) value;
                return file.getPath().replaceFirst(AppConfigManager.APP_CONFIG.getNotesSourcesDir(), "%s");
            case HEADLINE_KEY:
            case SUBJECT_KEY:
            case NAME_KEY:

            default:
                return value.toString();
        }
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
}
