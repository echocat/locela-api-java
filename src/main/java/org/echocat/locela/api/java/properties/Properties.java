package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Properties<V> extends Iterable<Property<V>>, AnnotationContainer {

    @Nullable
    Property<V> get(@Nonnull String id);

    boolean contains(@Nonnull String id);

    void add(@Nonnull Property<? extends V> property);

    void remove(@Nonnull String id);

}
