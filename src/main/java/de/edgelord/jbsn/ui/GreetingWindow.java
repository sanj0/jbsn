package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A JFrame that contains four centred buttons
 * which is to be displayed on program startup.
 */
public class GreetingWindow extends JFrame {

    public GreetingWindow(final Button... buttons) {
        super("jbsn");

        setPreferredSize(new Dimension(750, 750));
        /*ugly af*/
        add(new JPanel(new GridLayout(2, 2)) {
            {
                add(new JButton("<html><center>&#9881;<center></html>") {
                    {
                        setFont(getFont().deriveFont(50f));
                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(final MouseEvent e) {
                                Utils.openPreferences();
                            }
                        });
                        addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(final MouseEvent e) {
                                new PreferencesWindow();
                            }
                        });
                    }
                });
                for (int i = 0; i < 3; i++) {
                    add(Box.createGlue());
                }
            }
        });

        if (buttons.length <  4) {
            throw new IllegalArgumentException("GreetingWindow needs four Buttons");
        } else {
            setLayout(new GridLayout(4, 4));

            int buttonIndex = 0;
            for (int i = 1; i <= 15; i++) {
                if (i <= 4 || i == 7 || i == 8 || i >= 11) {
                    add(Box.createGlue());
                } else {
                    add(buttons[buttonIndex++]);
                }
            }
        }

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
