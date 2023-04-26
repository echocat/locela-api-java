package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainerSupport;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public abstract class PropertySupport<V> extends AnnotationContainerSupport implements Property<V> {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(": ").append(get());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (!(o instanceof Property)) {
            result = false;
        } else {
            final Property<?> that = (Property) o;
            if (getId().equals(that.getId())) {
                final V content = get();
                result = content != null ? content.equals(that.get()) : that.get() == null;
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final V content = get();
        return 31
            * getId().hashCode()
            * (content != null ? content.hashCode() : 0);
    }

}
