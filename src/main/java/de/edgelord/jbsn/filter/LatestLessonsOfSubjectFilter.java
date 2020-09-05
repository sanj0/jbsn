package de.edgelord.jbsn.filter;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Utils;

import java.util.*;

public class LatestLessonsOfSubjectFilter extends NotesFilter {

    private final List<String> subjects;
    private final int days;

    public LatestLessonsOfSubjectFilter(List<String> subjects, int days) {
        this.subjects = subjects;
        this.days = days;
    }

    @Override
    public List<Note> filter(List<Note> notes) {
        final List<Note> filteredNotes = new ArrayList<>();
        final Map<String, List<Date>> lessons = new HashMap<>();

        subjects.forEach(s -> lessons.put(s, new ArrayList<>()));

        // TODO: Make more efficient
        // e.g. containsLessons computing
        for (final Note n : notes) {
            final String subject = n.getSubject();
            if (subjects.contains(subject)) {
                final List<Date> lessonsOfSubject = lessons.get(subject);
                boolean containsLesson = false;
                for (final Date d : lessonsOfSubject) {
                    if (Utils.isSameDay(d, n.getAttribute(Note.DATE_KEY))) {
                        containsLesson = true;
                        break;
                    }
                }

                if (containsLesson) {
                    filteredNotes.add(n);
                } else {
                    if (lessonsOfSubject.size() != days) {
                        lessonsOfSubject.add(n.getAttribute(Note.DATE_KEY));
                        filteredNotes.add(n);
                    }
                }
            }
        }
        return filteredNotes;
    }
}
