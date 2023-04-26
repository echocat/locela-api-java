package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Annotation {

    @Nonnull
    String getId();

    @Nonnull
    Object[] getArguments();

    interface Factory<T extends Annotation> {

        @Nonnull
        String getId();

        @Nonnull
        Class<T> getResponsibleFor();

        @Nonnull
        T createBy(@Nullable Object... arguments);

        interface Provider extends Iterable<Factory<? extends Annotation>> {

            @Nonnull
            <T extends Annotation> Factory<T> provideBy(@Nonnull Class<T> type) throws UnknownAnnotationException;

            @Nonnull
            Factory<? extends Annotation> provideBy(@Nonnull String id) throws UnknownAnnotationException;

            class UnknownAnnotationException extends IllegalArgumentException {

                public UnknownAnnotationException(String s) {
                    super(s);
                }

            }

        }

    }

}
