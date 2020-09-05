package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.AppConfigManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class PreferencesWindow extends JFrame {

    public static final PreferencesWindow window = new PreferencesWindow();

    private final JPanel sideBar = new JPanel();
    private final JButton filesButton = new JButton("Files");
    private final JButton okButton = new JButton("OK");

    private final JPanel mainPanel = new JPanel();
    private final JPanel filesPanel = new JPanel();
    private final JButton configButton = new JButton("Config");
    private final JButton baseDir = new JButton("Base dir");
    private final JButton notesDir = new JButton("Notes dir");
    private final JButton notesConifgDir = new JButton("Notes config dir");

    public PreferencesWindow() throws HeadlessException {
        super("Prefernces - jbsn");

        setLayout(new BorderLayout());

        // build files panel
        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
        filesPanel.add(configButton);
        filesPanel.add(baseDir);
        filesPanel.add(notesDir);
        filesPanel.add(notesConifgDir);

        // build sidebar
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
        sideBar.add(filesButton);
        sideBar.add(Box.createVerticalStrut(200));
        sideBar.add(okButton);

        mainPanel.add(filesPanel);

        add(sideBar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // add action listeners
        configButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(AppConfigManager.CONFIG);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        baseDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(AppConfigManager.APP_CONFIG.getBaseDir()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        notesDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(AppConfigManager.APP_CONFIG.getNotesSourcesDir()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        notesConifgDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(AppConfigManager.APP_CONFIG.getNotesDir()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        okButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                PreferencesWindow.this.dispatchEvent(new WindowEvent(PreferencesWindow.this, WindowEvent.WINDOW_CLOSING));
            }
        });
    }
}
