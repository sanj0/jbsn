package de.edgelord.jbsn.ui;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Utils;
import java.util.*;

// matches all
public class NotesFilter {

    private String headlineHas = null;
    private DateRule dateRule = null;
    private String subject = null;
    private int timesViewed = -1;
    private TimesViewedMatcher timesViewedMatcher = null;

    public NotesFilter() {
    }

    /**
     *
     * @return a new instance of {@code NotesFilter}
     * using the one and only constructor without arguments
     */
    public static NotesFilter filter() {
        return new NotesFilter();
    }

    public static NotesFilter latestBySubjects(final List<String> subjects, final int count) {
        return new NotesFilter() {
            private final Map<String, List<Note>> subjectMap = new HashMap<>();
            {
                for (final String s : subjects) {
                    subjectMap.put(Utils.getSubject(s), new ArrayList<>());
                }
            }
            @Override
            public List<Note> filter(final List<Note> notes) {
                for (final Note n : notes) {
                    final String subject = n.getSubject();
                    if (subjectMap.containsKey(subject)) {
                        subjectMap.get(subject).add(n);
                    }
                }

                final List<Note> filteredNotes = new ArrayList<>();

                for (final List<Note> notesList : subjectMap.values()) {
                    final List<Note> sortedNotes = new ArrayList<>(notesList);
                    sortedNotes.sort(Comparator.comparingInt(note -> (int) note.getAttribute(Note.DATE_KEY, new Date(0)).getTime()));
                    final int size = sortedNotes.size();
                    filteredNotes.addAll(sortedNotes.subList(size < count ? 0 : size - count, size));
                    notesList.clear();
                }
                return filteredNotes;
            }
        };
    }

    public static NotesFilter latestOfSubject(final String subject, final int count) {
        return new NotesFilter() {
            private final NotesFilter notesFilter = new NotesFilter().subject(Utils.getSubject(subject));
            @Override
            public List<Note> filter(final List<Note> notes) {
                final List<Note> mNotes = notesFilter.filter(notes);
                mNotes.sort(Comparator.comparingInt(note -> (int) note.getAttribute(Note.DATE_KEY, new Date(0)).getTime()));
                return mNotes.subList(mNotes.size() - count, mNotes.size());
            }
        }.subject(subject);
    }

    public NotesFilter headlineHas(final String value) {
        setHeadlineHas(value);
        return this;
    }

    public NotesFilter dateRule(final DateRule value) {
        setDateRule(value);
        return this;
    }

    public NotesFilter subject(final String value) {
        setSubject(value);
        return this;
    }

    public NotesFilter timesViewed(final int value, final TimesViewedMatcher matcher) {
        setTimesViewed(value);
        setTimesViewedMatcher(matcher);
        return this;
    }

    public List<Note> filter(final List<Note> notes) {
        final List<Note> filteredNotes = new ArrayList<>();

        for (final Note note : notes) {
            if (matches(note)) {
                filteredNotes.add(note);
            }
        }

        return filteredNotes;
    }

    public boolean matches(final Note note) {
        if (headlineHas != null) {
            if (!note.getAttribute(Note.HEADLINE_KEY, "").contains(headlineHas)) {
                return false;
            }
        }

        if (subject != null) {
            if (!note.getSubject().equalsIgnoreCase(Utils.getSubject(subject))) {
                return false;
            }
        }

        if (dateRule != null) {
            switch (dateRule) {
                case LAST_SCHOOL_DAY:
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(note.getAttribute(Note.DATE_KEY, new Date()));
                    if (Utils.getLastSchoolDay().getValue() != calendar.get(Calendar.DAY_OF_WEEK)) {
                        return false;
                    }
                    break;
                case TODAY:
                    break;
                case LAST_WEEK:
                    break;
                case THIS_WEEK:
                    break;
                case LAST_MONTH:
                    break;
                case THIS_MONTH:
                    break;
            }
        }

        if (timesViewed != -1 && timesViewedMatcher != null) {
            if(!matchesTimesViewed(note.getAttribute(Note.VIEWED_KEY))) {
                return false;
            }
        }
        return true;
    }

    private boolean matchesTimesViewed(final int value) {
        switch (getTimesViewedMatcher()) {
            case EQUALS:
                return timesViewed == value;
            case GREATER_THAN:
                return timesViewed > value;
            case LESS_THAN:
                return timesViewed < value;
            default:
                return false;
        }
    }

    /**
     * Gets {@link #headlineHas}.
     *
     * @return the value of {@link #headlineHas}
     */
    public String getHeadlineHas() {
        return headlineHas;
    }

    /**
     * Sets {@link #headlineHas}.
     *
     * @param headlineHas the new value of {@link #headlineHas}
     */
    public void setHeadlineHas(final String headlineHas) {
        this.headlineHas = headlineHas;
    }

    /**
     * Gets {@link #dateRule}.
     *
     * @return the value of {@link #dateRule}
     */
    public DateRule getDateRule() {
        return dateRule;
    }

    /**
     * Sets {@link #dateRule}.
     *
     * @param dateRule the new value of {@link #dateRule}
     */
    public void setDateRule(final DateRule dateRule) {
        this.dateRule = dateRule;
    }

    /**
     * Gets {@link #subject}.
     *
     * @return the value of {@link #subject}
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets {@link #subject}.
     *
     * @param subject the new value of {@link #subject}
     */
    public void setSubject(final String subject) {
        this.subject = subject;
    }

    /**
     * Gets {@link #timesViewed}.
     *
     * @return the value of {@link #timesViewed}
     */
    public int getTimesViewed() {
        return timesViewed;
    }

    /**
     * Sets {@link #timesViewed}.
     *
     * @param timesViewed the new value of {@link #timesViewed}
     */
    public void setTimesViewed(final int timesViewed) {
        this.timesViewed = timesViewed;
    }

    /**
     * Gets {@link #timesViewedMatcher}.
     *
     * @return the value of {@link #timesViewedMatcher}
     */
    public TimesViewedMatcher getTimesViewedMatcher() {
        return timesViewedMatcher;
    }

    /**
     * Sets {@link #timesViewedMatcher}.
     *
     * @param timesViewedMatcher the new value of {@link #timesViewedMatcher}
     */
    public void setTimesViewedMatcher(final TimesViewedMatcher timesViewedMatcher) {
        this.timesViewedMatcher = timesViewedMatcher;
    }

    public enum TimesViewedMatcher {
        EQUALS,
        GREATER_THAN,
        LESS_THAN
    }

    public enum DateRule {
        LAST_SCHOOL_DAY,
        TODAY,
        LAST_WEEK,
        THIS_WEEK,
        LAST_MONTH,
        THIS_MONTH
    }
}
