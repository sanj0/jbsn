package de.edgelord.jbsn;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

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

    public Timestamp(final String subject, final String name) {
        super();
        setAttribute(SUBJECT_KEY, subject);
        setAttribute(DATE_KEY, Utils.today());
        setAttribute(NAME_KEY, name);
    }

    /**
     * A constructor.
     *
     * @param file the file of the note
     */
    public Timestamp(final File file) throws IOException {
        super(file);
    }

    public LocalDate getDate() {
        return getAttribute(DATE_KEY);
    }
}
