package de.edgelord.jbsn;

import java.time.LocalDate;

public class FormattedDate implements Comparable<FormattedDate> {

    private final LocalDate value;

    public FormattedDate(LocalDate value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Utils.dateToString(value);
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public int compareTo(FormattedDate o) {
        return value.compareTo(o.getValue());
    }
}
