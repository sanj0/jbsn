package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class PreferencesWindow extends JFrame {

    private JPanel sideBar = new JPanel();
        private JButton generalButton = new JButton("General");
        private JButton appearanceButton = new JButton("Appearance");
        private JButton filesButton = new JButton("Files");
        private JButton okButton = new JButton("OK");

    private JPanel mainPanel = new JPanel();
        private JPanel generalPanel = new JPanel();
            private JLabel namePanel = new JLabel("Name");
            private JPanel nameSettingsPanel = new JPanel();
                private JTextField nameField = new JTextField(Main.APP_CONFIGS.getName());
                private JButton nameOKButton = new JButton("OK");
        private JPanel appearancePanel = new JPanel();
            private JLabel fontSizeLabel = new JLabel("Font size");
            private JSpinner fontSizeSpinner = new JSpinner();
            private JLabel alternateRowColorLabel = new JLabel("Alternate row color");
            private JButton pickButton = new JButton("Pick");
    private JPanel filesPanel = new JPanel();
        private JButton configButton = new JButton("Config");
        private JButton baseDir = new JButton("Base dir");
        private JButton notesDir = new JButton("Notes dir");
        private JButton notesConifgDir = new JButton("Notes config dir");

    public PreferencesWindow() throws HeadlessException {
        super("Prefernces - jbsn");
        setLayout(new BorderLayout());

        generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.Y_AXIS));
        generalPanel.setVisible(false);
        generalPanel.add(namePanel);
        generalPanel.add(nameSettingsPanel);
        nameSettingsPanel.add(nameField);
        nameSettingsPanel.add(nameOKButton);

        appearancePanel.setLayout(new BoxLayout(appearancePanel, BoxLayout.Y_AXIS));
        appearancePanel.setVisible(false);
        appearancePanel.add(fontSizeLabel);
        appearancePanel.add(fontSizeSpinner);
        appearancePanel.add(alternateRowColorLabel);
        appearancePanel.add(pickButton);

        filesPanel.setLayout(new BoxLayout(filesPanel, BoxLayout.Y_AXIS));
        filesPanel.add(configButton);
        filesPanel.add(baseDir);
        filesPanel.add(notesDir);
        filesPanel.add(notesConifgDir);

        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.black));
        sideBar.add(generalButton);
        sideBar.add(appearanceButton);
        sideBar.add(filesButton);
        sideBar.add(Box.createVerticalStrut(200));
        sideBar.add(okButton);

        mainPanel.add(generalPanel);
        mainPanel.add(appearancePanel);
        mainPanel.add(filesPanel);

        add(sideBar, BorderLayout.WEST);
        add(mainPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        configButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(Main.CONFIGS);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        baseDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(Main.APP_CONFIGS.getBaseDir()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        notesDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(Main.APP_CONFIGS.getNotesSourcesDir()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        notesConifgDir.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                try {
                    Desktop.getDesktop().open(new File(Main.APP_CONFIGS.getNotesDir()));
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
