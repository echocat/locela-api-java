package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;

public interface AnnotationContainer {

    @Nonnull
    Iterable<Annotation> annotations();

    @Nonnull
    <T extends Annotation> Iterable<T> annotations(@Nonnull Class<? extends T> ofType);

    void addAnnotation(@Nonnull Annotation annotation);

    void removeAnnotation(@Nonnull Annotation annotation);

    void removeAnnotations(@Nonnull Class<? extends Annotation> ofType);

}
