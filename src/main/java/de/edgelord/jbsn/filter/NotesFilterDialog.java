package de.edgelord.jbsn.filter;

import de.edgelord.jbsn.Notes;
import de.edgelord.jbsn.Timestamp;
import de.edgelord.jbsn.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

//TODO: create a second map for both enum inputs like Map<String, EnumObject> to avoid weird String matching
public class NotesFilterDialog {

    private static final Map<String, NotesFilter.DateRule> dateRuleMap = createDateRulesMap();
    private static final Map<String, NotesFilter.TimesViewedMatcher> timesViewedMatcherMap = createTimesViewedMatcherMap();

    public static NotesFilter prompt(final Component parent) {
        final JTextField headline = new JTextField();
        final JComboBox<String> subject = Utils.subjectComboBox(true);
        final JComboBox<String> timestamp = Utils.timestampComboBox();
        final JSpinner timesViewed = new JSpinner();
        timesViewed.setValue(-1);
        final JComboBox<String> date = new JComboBox<>(dateRuleMap.keySet().toArray(new String[0]));
        final JComboBox<String> timesViewedMatcher = new JComboBox<>(timesViewedMatcherMap.keySet().toArray(new String[0]));
        Object[] message = {
                "Headline contains:", headline,
                "Subject:", subject,
                "After Timestamp:", timestamp,
                "Times viewed:", timesViewed,
                "    Mode:", timesViewedMatcher,
                "Date:", date
        };

        int option = JOptionPane.showConfirmDialog(parent, message, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            final String headlineText = headline.getText().isEmpty() ? null : headline.getText();
            final String subjectText = subject.getSelectedItem().equals("all") ? null : subject.getSelectedItem().toString();
            Utils.updateLastSubject(subjectText);
            final String[] timestampData = timestamp.getItemAt(timestamp.getSelectedIndex())
                    .split("\\(")[0].split(", ", 2);
            final Timestamp afterTimestamp = timestamp.getSelectedItem().equals(Utils.NO_TIMESTAMP)
                    ? null : Notes.getTimestamp(timestampData[0], Utils.dateFromString(timestampData[1].trim()));
            final int timesViewedValue = Integer.parseInt(timesViewed.getValue().toString());
            final NotesFilter.TimesViewedMatcher timesViewedMode =
                    timesViewedMatcherMap.get(timesViewedMatcher.getSelectedItem().toString());
            final NotesFilter.DateRule dateRule = dateRuleMap.get(date.getSelectedItem().toString());
            return NotesFilter.filter().headlineHas(headlineText).subject(subjectText)
                    .afterTimestamp(afterTimestamp).timesViewed(timesViewedValue, timesViewedMode).dateRule(dateRule);
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

    private static Map<String, NotesFilter.TimesViewedMatcher> createTimesViewedMatcherMap() {
        final Map<String, NotesFilter.TimesViewedMatcher> map = new HashMap<>();

        map.put("less than", NotesFilter.TimesViewedMatcher.LESS_THAN);
        map.put("equals", NotesFilter.TimesViewedMatcher.EQUALS);
        map.put("more than", NotesFilter.TimesViewedMatcher.GREATER_THAN);

        return map;
    }
}
