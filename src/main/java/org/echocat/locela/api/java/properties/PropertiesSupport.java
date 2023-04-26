package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainerSupport;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

@NotThreadSafe
public abstract class PropertiesSupport<V> extends AnnotationContainerSupport implements Properties<V> {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        for (final Property<V> property : this) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            sb.append(property);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Properties)) {
            result = false;
        } else {
            final Properties<?> that = (Properties) o;
            final Set<String> keys = getKeysOf(this);
            final Set<String> thatKeys = getKeysOf(that);
            if (keys.equals(thatKeys)) {
                result = true;
                for (final String key : keys) {
                    if (!get(key).equals(that.get(key))) {
                        result = false;
                        break;
                    }
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @Nonnull
    protected Set<String> getKeysOf(@Nonnull Properties<?> properties) {
        final Set<String> keys = new TreeSet<>();
        for (final Property<?> property : properties) {
            keys.add(property.getId());
        }
        return keys;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (final String key : getKeysOf(this)) {
            result = 31 * result + get(key).hashCode();
        }
        return result;
    }

    @Override
    public Iterator<Property<V>> iterator() {
        // noinspection unchecked
        return (Iterator<Property<V>>) getProperties().iterator();
    }

    @Nonnull
    protected abstract Iterable<? extends Property<V>> getProperties();

}
