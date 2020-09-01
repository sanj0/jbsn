package de.edgelord.jbsn;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Installer {

    private static final String USER_HOME = System.getProperty("user.home");
    private static final File INSTALL_CHECK = new File(USER_HOME, ".jbsnInstalled");

    public static boolean checkInstalled() {
        return INSTALL_CHECK.exists();
    }

    public static void install() throws IOException {

        try {
            Main.APP_CONFIGS = new AppConfigs(new BufferedReader(new InputStreamReader(Installer.class.getClassLoader().getResourceAsStream("config.txt"))));
            new File(Main.APP_CONFIGS.getBaseDir()).mkdirs();
            new File(Main.APP_CONFIGS.getNotesDir()).mkdirs();
            new File(Main.APP_CONFIGS.getNotesSourcesDir()).mkdirs();
            new File(Main.APP_CONFIGS.getRecentlyDeletedDir()).mkdirs();
            new File(Main.APP_CONFIGS.getTemplateScript()).getParentFile().mkdirs();
            // copy script file
            Files.copy(Installer.class.getClassLoader().getResourceAsStream("scripts/pages.scpt"), new File(Main.APP_CONFIGS.getTemplateScript()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            // copy config file
            Files.copy(Installer.class.getClassLoader().getResourceAsStream("config.txt"), new File(Main.APP_CONFIGS.getBaseDir(), "config.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);

            INSTALL_CHECK.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error installing jbsn: " + e.getMessage(), "Couldn't install jbsn", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }

        JOptionPane.showMessageDialog(null, "jbsn is now installed on your system. It is fully usable" +
                " on next launch.", "Installation complete.", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
}
