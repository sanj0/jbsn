package de.sanj0.jbsn;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The captain always leaves the sinking last
 */
public class Captain extends Thread {
    @Override
    public void run() {
        try {
            Notes.syncNotes();
            Schedule.write();
            AppConfigManager.APP_CONFIG.write(new BufferedWriter(new FileWriter(AppConfigManager.CONFIG))).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
