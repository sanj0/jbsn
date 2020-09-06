package de.edgelord.jbsn.filter;

import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class NotesFilterDialog {

    private static final Map<String, NotesFilter.DateRule> dateRuleMap = createDateRulesMap();

    public static NotesFilter prompt(final Component parent) {
        final JTextField headline = new JTextField();
        final JComboBox<String> subject = Utils.subjectComboBox(true);
        final JComboBox<String> date = new JComboBox<>(dateRuleMap.keySet().toArray(new String[0]));
        Object[] message = {
                "Headline contains:", headline,
                "Subject:", subject,
                "Date:", date
        };

        int option = JOptionPane.showConfirmDialog(parent, message, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            final String headlineText = headline.getText().isEmpty() ? null : headline.getText();
            final String subjectText = subject.getSelectedItem().equals("all") ? null : subject.getSelectedItem().toString();
            final NotesFilter.DateRule dateRule = dateRuleMap.get(date.getSelectedItem().toString());
            return NotesFilter.filter().headlineHas(headlineText).subject(subjectText).dateRule(dateRule);
        } else {
            return null;
        }
    }

    private static Map<String, NotesFilter.DateRule> createDateRulesMap() {
        final Map<String, NotesFilter.DateRule> dateRuleMap = new HashMap<>();

        dateRuleMap.put("all", null);
        dateRuleMap.put("last school day", NotesFilter.DateRule.LAST_SCHOOL_DAY);
        dateRuleMap.put("today", NotesFilter.DateRule.TODAY);
        dateRuleMap.put("last week", NotesFilter.DateRule.LAST_WEEK);
        dateRuleMap.put("this week", NotesFilter.DateRule.THIS_WEEK);
        dateRuleMap.put("last month", NotesFilter.DateRule.LAST_MONTH);
        dateRuleMap.put("this month", NotesFilter.DateRule.THIS_MONTH);

        return dateRuleMap;
    }
}
