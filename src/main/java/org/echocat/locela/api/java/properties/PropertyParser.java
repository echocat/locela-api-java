package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PropertyParser {

    @Nonnull
    Property<String> parse(@Nonnull String plain, @Nullable Iterable<Annotation> annotations);

}
