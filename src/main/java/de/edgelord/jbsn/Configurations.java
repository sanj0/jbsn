package de.edgelord.jbsn;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to hold configurations
 * read from a config file, with
 * method to read and write configs.
 * Attributes can only take up one line.
 * Overwrite methods {@link #readAttribute(String, String)}
 * and {@link #writeAttribute(String, Object)} to manipulate
 * the way objects and text is interpreted and turned
 * into objects/strings.
 */
public class Configurations {

    /**
     * A map holding all read attributes
     * and all attributes to be written to a file
     * using {@link #write(java.io.BufferedWriter)}
     */
    private final Map<String, Object> attributes = new HashMap<>();
    /**
     * Contains the order of config
     * for file-writing when {@link #preserveOrder}
     * is <code>true</code>.
     */
    private final Map<Integer, String> order = new HashMap<>();
    /**
     * Set to to <code>true</code> if the order
     * of key/value pairs should be preserved
     * when the configs are written to a file.
     * The approach to this is probably no the
     * most efficient.
     */
    private boolean preserveOrder = false;

    /**
     * The constructor.
     *
     * @param lines the lines of the config file
     */
    public Configurations(final String... lines) {
        int index = 0;
        for (final String line : lines) {
            final String[] parts = line.split("<-:", 2)[0].split(":", 2);
            final String key = parts[0].trim();
            attributes.put(key, readAttribute(key, parts[1].trim()));
            order.put(index++, key);
        }
    }

    /**
     * Reads the given String
     * and returns a resulting object.
     * Default implementation returns
     * the given String.
     *
     * @param key   the key of the read attribute
     * @param value the given String to receive an object from
     * @return an object that is a result of the read
     * String
     */
    public Object readAttribute(final String key, final String value) {
        return value;
    }

    /**
     * Writes the given object as a string.
     * Default implementation returns the toString
     * representation of the object.
     *
     * @param key   the key of the attribute to write
     * @param value the object to write
     * @return the string as which to write the object
     */
    public String writeAttribute(final String key, final Object value) {
        return value.toString();
    }

    public BufferedWriter write(final BufferedWriter writer) throws IOException {
        if (preserveOrder) {
            for (int i = 0; i < order.size(); i++) {
                final String key = order.get(i);
                writeLine(writer, key, attributes.getOrDefault(key, "null"));
            }
        } else {
            for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
                final String key = entry.getKey();
                final Object value = entry.getValue();

                writeLine(writer, key, value);
            }
        }

        return writer;
    }

    private void writeLine(final BufferedWriter writer, final String key, final Object value) throws IOException {
        writer.write(key + ": " + writeAttribute(key, value));
        writer.write(System.lineSeparator());
    }

    public <T> T getAttribute(final String key) {
        final Object v = attributes.get(key);
        return (T) v;
    }

    public <T> T getAttribute(final String key, final T type) {
        return getAttribute(key);
    }

    public void setAttribute(final String key, final Object o) {
        if (attributes.containsKey(key)) {
            final List<Integer> keys = getKeysForValue(order, key);
            if (keys.size() == 1) {
                order.put(keys.get(0), key);
            } else {
                System.err.println("Warning: Multiple keys for value " + o.toString() + " panic-rebuilding attributes order!");
                int index = 0;
                for (final Map.Entry<String, Object> entry : attributes.entrySet()) {
                    order.put(index, entry.getKey());
                    index++;
                }
            }
            attributes.put(key, o);
        } else {
            attributes.put(key, o);
            order.put(order.size(), key);
        }
    }

    private <K, V> List<K> getKeysForValue(final Map<K, V> map, final V value) {
        final List<K> keys = new ArrayList<>();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            final K k = entry.getKey();
            final V v = entry.getValue();

            if (v.equals(value)) {
                keys.add(k);
            }
        }

        return keys;
    }

    /**
     * Gets {@link #preserveOrder}.
     *
     * @return the value of {@link #preserveOrder}
     */
    public boolean isPreserveOrder() {
        return preserveOrder;
    }

    /**
     * Sets {@link #preserveOrder}.
     *
     * @param preserveOrder the new value of {@link #preserveOrder}
     */
    public void setPreserveOrder(final boolean preserveOrder) {
        this.preserveOrder = preserveOrder;
    }
}
