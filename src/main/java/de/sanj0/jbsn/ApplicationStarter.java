package de.sanj0.jbsn;

import de.sanj0.jbsn.ui.GreetingWindow;

import java.io.IOException;

public class ApplicationStarter {
    public static void start() throws IOException {
        if (!Installer.checkInstalled()) {
            Installer.install();
        }

        AppConfigManager.configs();
        Notes.loadNotes();

        final GreetingWindow window = new GreetingWindow();
        window.setVisible(true);

        Runtime.getRuntime().addShutdownHook(new Captain());
    }
}
