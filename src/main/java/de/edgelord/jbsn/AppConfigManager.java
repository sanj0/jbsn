package de.edgelord.jbsn;

import de.edgelord.jbsn.ui.LookAndFeelSetter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AppConfigManager {
    public static final File CONFIG = new File(System.getProperty("user.home"), "jbsn/config.txt");
    public static AppConfig APP_CONFIG;

    static void configs() throws IOException {
        APP_CONFIG = new AppConfig(CONFIG);
        Notes.NOTES_SOURCES_DIR = new File(APP_CONFIG.getNotesSourcesDir());
        Notes.NOTES_DIR = new File(APP_CONFIG.getNotesDir());
        Notes.TIMESTAMPS_DIR = new File(APP_CONFIG.getTimestampsDir());

        final int noteSourcesCount = Objects.requireNonNull(
                Notes.NOTES_SOURCES_DIR.listFiles((dir, name) -> name.endsWith(".pages"))).length;
        final int notesCount = Objects.requireNonNull(Notes.NOTES_DIR.listFiles(Notes.NOTES_SOURCES_FILTER)).length;
        if (noteSourcesCount != notesCount) {
            JOptionPane.showMessageDialog(null, "Notes sources" +
                            " and notes configs are out of sync. Adding notes will likely result in data loss.",
                    "Notes library out of sync", JOptionPane.ERROR_MESSAGE);
        }
        Schedule.readFromConfig();

        LookAndFeelSetter.setNimbusLookAndFeel();
        final UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        defaults.put("Table.alternateRowColor", new Color(100, 100, 135));
    }
}
