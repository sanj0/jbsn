package de.edgelord.jbsn;

import java.util.Date;

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
    private final Date date;

    public Timestamp(String subject, Date date) {
        this.subject = subject;
        this.date = date;
    }
}
