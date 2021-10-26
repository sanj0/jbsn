package de.sanj0.jbsn;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * The main configurations of the application.
 * All values are strings.
 */
public class AppConfig extends Configurations {

    public static final String BASE_DIR_KEY = "base-dir";
    public static final String NOTES_DIR_KEY = "notes-dir";
    public static final String NOTES_SOURCES_DIR_KEY = "notes-sources-dir";
    public static final String TEMPLATE_SCRIPT_KEY = "template-script";
    public static final String EXPORT_SCRIPT_KEY = "export-script";
    public static final String TIMESTAMPS_DIR_KEY = "timestamps-dir";
    public static final String RECENTLY_DELETED_DIR_KEY = "recently-deleted-dir";
    public static final String NAME_KEY = "name";
    public static final String SUBJECTS_KEY = "subjects";
    public static final String MONDAY_KEY = "monday";
    public static final String TUESDAY_KEY = "tuesday";
    public static final String WEDNESDAY_KEY = "wednesday";
    public static final String THURSDAY_KEY = "thursday";
    public static final String FRIDAY_KEY = "friday";
    public static final String USER_HOME = "\\{user.home}";

    public AppConfig(final File file) throws IOException {
        super(Files.readAllLines(file.toPath()).toArray(new String[0]));
        setPreserveOrder(true);
    }

    public AppConfig(final BufferedReader reader) {
        super(reader.lines().toArray(String[]::new));
    }

    private static String insertUserHome(final String s) {
        return s.replaceAll(USER_HOME, System.getProperty("user.home"));
    }

    public String getBaseDir() {
        return insertUserHome(getAttribute(BASE_DIR_KEY));
    }

    public String getNotesDir() {
        return insertUserHome(String.format(getAttribute(NOTES_DIR_KEY, ""), getBaseDir()));
    }

    public String getNotesSourcesDir() {
        return insertUserHome(String.format(getAttribute(NOTES_SOURCES_DIR_KEY, ""), getBaseDir()));
    }

    public String getTemplateScript() {
        return insertUserHome(String.format(getAttribute(TEMPLATE_SCRIPT_KEY, ""), getBaseDir()));
    }

    public String getExportScript() {
        return insertUserHome(String.format(getAttribute(EXPORT_SCRIPT_KEY, ""), getBaseDir()));
    }

    public String getTimestampsDir() {
        return insertUserHome(String.format(getAttribute(TIMESTAMPS_DIR_KEY, ""), getBaseDir()));
    }

    public String getRecentlyDeletedDir() {
        return insertUserHome(String.format(getAttribute(RECENTLY_DELETED_DIR_KEY, ""), getBaseDir()));
    }

    public String getName() {
        return getAttribute(NAME_KEY);
    }

    public String getSubjects() {
        return getAttribute(SUBJECTS_KEY);
    }
}
