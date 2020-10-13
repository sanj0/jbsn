package de.edgelord.jbsn.filter;

import de.edgelord.jbsn.Note;
import de.edgelord.jbsn.Utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        final Map<String, List<LocalDate>> lessons = new HashMap<>();

        subjects.forEach(s -> lessons.put(s, new ArrayList<>()));

        // 1. sort notes into lists
        // 2. sort

        // TODO: Make more efficient
        // e.g. containsLessons computing
        for (final Note n : notes) {
            final String subject = n.getSubject();
            if (subjects.contains(subject)) {
                final List<LocalDate> lessonsOfSubject = lessons.get(subject);
                boolean containsLesson = false;
                for (final LocalDate d : lessonsOfSubject) {
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
