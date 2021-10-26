package de.sanj0.jbsn.ui;

import de.sanj0.jbsn.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TableContextMenu extends JPopupMenu {

    private final List<Note> selectedNotes;
    private final JMenuItem openConfig;
    private final JMenuItem openNotes;
    private final JMenuItem removeNotes;
    private final JMenuItem exportNotes;

    public TableContextMenu(final List<Note> selectedNotes, JFrame parent) {
        this.selectedNotes = selectedNotes;
        openConfig = new JMenuItem("Open config file(s)");
        openConfig.addActionListener(e -> {
            for (final Note n : selectedNotes) {
                try {
                    Desktop.getDesktop().open(n.getConfigFile());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        openNotes = new JMenuItem("Open note(s)");
        openNotes.addActionListener(e -> {
            for (final Note n : selectedNotes) {
                try {
                    Notes.openNote(n);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        removeNotes = new JMenuItem("Remove note(s)...");
        removeNotes.addActionListener(e -> {
            if (Utils.showDeleteConfirmDialog(selectedNotes.toArray(new Note[0]))) {
                selectedNotes.forEach(n -> {
                    try {
                        Notes.removeNote(n);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                });
            }
        });

        exportNotes = new JMenuItem("Export note(s) as pdf");
        exportNotes.addActionListener(e -> {
            final JFileChooser exportDirChooser = new JFileChooser();
            exportDirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
            exportDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (exportDirChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
                final File dir = exportDirChooser.getSelectedFile();
                final JDialog dialog = new JDialog(parent, "Exporting...", true);
                JProgressBar progress = new JProgressBar(0, selectedNotes.size());
                dialog.add(BorderLayout.CENTER, progress);
                dialog.add(BorderLayout.NORTH, new JLabel("Exporting " + selectedNotes.size() + " items to " + dir.getAbsolutePath()));
                dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                dialog.setSize(300, 75);
                dialog.setLocationRelativeTo(parent);
                new Thread(() -> dialog.setVisible(true)).start();
                for (int i = 0; i < selectedNotes.size(); i++) {
                    final Note n = selectedNotes.get(i);
                    final String name = n.getDate() + "-" + n.getAttribute(ElementConfig.HEADLINE_KEY) + "-" + n.getSubject() + ".pdf";
                    try {
                        Utils.executeApplescript(AppConfigManager.APP_CONFIG.getExportScript(), Utils.toHFSPath(n.getSourceFile().getAbsolutePath()), Utils.toHFSPath(new File(dir, name).getAbsolutePath()));
                    } catch (IOException | InterruptedException ioException) {
                        ioException.printStackTrace();
                    }
                    int finalI = i;
                    Thread thread = new Thread(() -> progress.setValue(finalI));
                    thread.start();
                }
                dialog.setVisible(false);
                dialog.dispose();
            }
        });

        add(openConfig);
        add(openNotes);
        add(removeNotes);
        add(exportNotes);
    }
}
