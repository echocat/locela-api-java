package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class StandardProperty<V> extends PropertySupport<V> {

    @Nonnull
    public static <V> StandardProperty<V> property(@Nonnull String id, @Nonnull Class<V> ofType) {
        return new StandardProperty<>(id, ofType);
    }

    @Nonnull
    public static StandardProperty<String> property(@Nonnull String id) {
        return property(id, String.class);
    }

    @Nonnull
    private final String _id;
    @Nonnull
    private final Class<V> _type;

    @Nullable
    private volatile V _content;

    public StandardProperty(@Nonnull String id, @Nonnull Class<V> type) {
        _id = id;
        _type = type;
    }

    @Nonnull
    public StandardProperty<V> withAnnotations(@Nullable Annotation... annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    public StandardProperty<V> withAnnotations(@Nullable Iterable<? extends Annotation> annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    @Override
    public String getId() {
        return _id;
    }

    @Nullable
    @Override
    public V get() {
        return _content;
    }

    @Override
    public StandardProperty<V> set(@Nullable V content) throws IllegalArgumentException {
        if (content != null && !getType().isInstance(content)) {
            throw new IllegalArgumentException("Expected content of type '" + getType().getName() + "' but got: " + content);
        }
        _content = content;
        return this;
    }

    @Override
    public Class<V> getType() {
        return _type;
    }

}
