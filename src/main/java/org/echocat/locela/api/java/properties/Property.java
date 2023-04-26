package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Property<V> extends AnnotationContainer {

    @Nonnull
    String getId();

    @Nullable
    V get();

    Property<V> set(@Nullable V content) throws IllegalArgumentException;

    Class<V> getType();

}
