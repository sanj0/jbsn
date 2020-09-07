package de.edgelord.jbsn;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * A note, loaded from a .jb file,
 * or created programmatically and then
 * saved to a File
 */
public class Note extends ElementConfig {

    public Note(final File file) throws IOException {
        super(file);
    }

    public Note(final String headline, final String subject, final LocalDate date, final int viewed, final String relativePathOfNote) {
        super();

        setAttribute(HEADLINE_KEY, headline);
        setAttribute(SUBJECT_KEY, subject);
        setAttribute(DATE_KEY, date);
        setAttribute(VIEWED_KEY, viewed);
        setAttribute(NOTES_FILE_KEY, new File(AppConfigManager.APP_CONFIG.getNotesSourcesDir(), relativePathOfNote));
        setPreserveOrder(true);
    }

    public static Note createNote(final String headline, final String subject, final LocalDate date)
            throws IOException, InterruptedException {
        final Note note = new Note(headline, subject, date, 0,
                Notes.createdNoteFile(subject, date, headline));
        note.setConfigFile(new File(AppConfigManager.APP_CONFIG.getNotesDir(),
                note.getAttribute(NOTES_FILE_KEY,
                        new File("/")).getName().split("\\.")[0] + "." + Notes.NOTES_FILE_EXTENSION));
        return note;
    }
}
