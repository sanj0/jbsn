package de.edgelord.jbsn;

import de.edgelord.jbsn.filter.NotesFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
public class Timestamp extends ElementConfig {

    public Timestamp(final String subject, final LocalDate date) {
        super();
        setAttribute(SUBJECT_KEY, subject);
        setAttribute(DATE_KEY, date);
    }

    /**
     * A constructor.
     *
     * @param file the file of the note
     */
    public Timestamp(final File file) throws IOException {
        super(file);
    }

    /**
     * @return a filter that only lets notes through
     * with the given subject and dated after
     * the given date.
     */
    public NotesFilter filter() {
        return new NotesFilter() {
            @Override
            public List<Note> filter(List<Note> notes) {
                final List<Note> filteredNotes = new ArrayList<>();

                for (final Note n : notes) {
                    final String subject = n.getSubject();
                    final LocalDate date = getDate();
                    if (subject == null || n.getSubject().equals(subject)) {
                        if (n.getAttribute(Note.DATE_KEY, LocalDate.now()).isAfter(date)) {
                            filteredNotes.add(n);
                        }
                    }
                }
                return filteredNotes;
            }
        };
    }

    public LocalDate getDate() {
        return getAttribute(DATE_KEY);
    }
}
