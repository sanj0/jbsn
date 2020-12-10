package de.sanj0.jbsn;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class Installer {

    private static final File INSTALL_CHECK_FILE = new File(System.getProperty("user.home"), ".jbsnInstalled");
    private static final ClassLoader CLASS_LOADER = Installer.class.getClassLoader();
    private static final String CONFIGS_RESOURCE_NAME = "config.txt";

    public static boolean checkInstalled() {
        return INSTALL_CHECK_FILE.exists();
    }

    public static void install() {
        try {
            final InputStreamReader configsStream = new InputStreamReader(
                    Objects.requireNonNull(CLASS_LOADER.getResourceAsStream(CONFIGS_RESOURCE_NAME)));
            final BufferedReader configsReader = new BufferedReader(configsStream);
            AppConfigManager.APP_CONFIG = new AppConfig(new BufferedReader(new InputStreamReader(Objects.requireNonNull(Installer.class.getClassLoader().getResourceAsStream("config.txt")))));
            final boolean baseDirCreated = new File(AppConfigManager.APP_CONFIG.getBaseDir()).mkdirs();
            final boolean notesDirCreated = new File(AppConfigManager.APP_CONFIG.getNotesDir()).mkdirs();
            final boolean noteSourcesDirCreated = new File(AppConfigManager.APP_CONFIG.getNotesSourcesDir()).mkdirs();
            final boolean timestampsDirCreated = new File(AppConfigManager.APP_CONFIG.getTimestampsDir()).mkdirs();
            final boolean recentlyDeletedDirCreated = new File(AppConfigManager.APP_CONFIG.getRecentlyDeletedDir()).mkdirs();
            final boolean templateScriptDorCreated = new File(AppConfigManager.APP_CONFIG.getTemplateScript())
                    .getParentFile().mkdirs();
            // copy script file
            copyFromResourcesToFile("scripts/pages.scpt", AppConfigManager.APP_CONFIG.getTemplateScript());
            // copy config file
            copyFromResourcesToFile(CONFIGS_RESOURCE_NAME, AppConfigManager.CONFIG.getAbsolutePath());

            final boolean installCheckFileCreated = INSTALL_CHECK_FILE.createNewFile();

            if (!installCheckFileCreated) {
                JOptionPane.showMessageDialog(null, "Could not create install check file",
                        "Error installing jbsn", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error installing jbsn: " + e.getMessage(), "Couldn't install jbsn", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        JOptionPane.showMessageDialog(null, "jbsn is now installed on your system. It is fully usable" +
                " on next launch.", "Installation complete.", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private static void copyFromResourcesToFile(final String src, final String dst) throws IOException {
        Files.copy(Objects.requireNonNull(CLASS_LOADER.getResourceAsStream(src)), new File(dst).toPath(),
                StandardCopyOption.REPLACE_EXISTING);
    }
}
