package de.edgelord.jbsn;

import de.edgelord.jbsn.filter.NotesFilter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A timestamp is used to mark a specific point
 * in time for a specific subject, useful e.g.
 * for class test; after writing one,
 * the user can add a timestamp for the
 * subject and later get a list of
 * all notes taken after the timestamp
 * (-> after the last class test), to see
 * what's relevant for the next timestamp.
 */
public class Timestamp {

    private final String subject;
    private final LocalDate date;

    public Timestamp(final String subject, final LocalDate date) {
        this.subject = subject;
        this.date = date;
    }

    /**
     * @return a filter that only lets notes through
     * with the given {@link #subject} and dated after
     * the given {@link #date}
     */
    public NotesFilter filter() {
        return new NotesFilter() {
            @Override
            public List<Note> filter(List<Note> notes) {
                final List<Note> filteredNotes = new ArrayList<>();

                for (final Note n : notes) {
                    if (n.getSubject().equals(subject)) {
                        if (n.getAttribute(Note.DATE_KEY, LocalDate.now()).isAfter(date)) {
                            filteredNotes.add(n);
                        }
                    }
                }
                return filteredNotes;
            }
        };
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getDate() {
        return date;
    }
}
