package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.*;

public class NotesFilterDialog {

    /**horrible magic constants*/
    private static final String[] dateRules = new String[] {
            "all",
            "last school day",
            "today",
            "last week",
            "this week",
            "last month",
            "this month"
    };

    public static NotesFilter prompt(final Component parent) {
        final JTextField headline = new JTextField();
        final JComboBox<String> subject = Utils.subjectComboBox(true);
        final JComboBox<String> date = new JComboBox<>(dateRules);
        Object[] message = {
                "Headline contains:", headline,
                "Subject:", subject,
                "Date:", date
        };

        int option = JOptionPane.showConfirmDialog(parent, message, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            final String headlineText = headline.getText().isEmpty() ? null : headline.getText();
            final String subjectText = subject.getSelectedItem().equals("all") ? null : subject.getSelectedItem().toString();
            final NotesFilter.DateRule dateRule = forName(date.getSelectedItem().toString());
            return NotesFilter.filter().headlineHas(headlineText).subject(subjectText).dateRule(dateRule);
        } else {
            return null;
        }
    }

    /**horrible magic constants*/
    private static NotesFilter.DateRule forName(final String name) {
        switch (name) {
            case "last school day": return NotesFilter.DateRule.LAST_SCHOOL_DAY;
            case "today": return NotesFilter.DateRule.TODAY;
            case "last week": return NotesFilter.DateRule.LAST_WEEK;
            case "this week": return NotesFilter.DateRule.THIS_WEEK;
            case "last month": return NotesFilter.DateRule.LAST_MONTH;
            case "this month": return NotesFilter.DateRule.THIS_MONTH;
            default: return null;
        }
    }
}
