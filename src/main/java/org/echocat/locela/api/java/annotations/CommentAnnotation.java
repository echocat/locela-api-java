package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Arrays;

@ThreadSafe
public class CommentAnnotation implements Annotation {

    @Nonnull
    private final String _content;

    public CommentAnnotation(@Nonnull String content) {
        _content = content;
    }

    @Nonnull
    @Override
    public String getId() {
        return "comment";
    }

    @Nonnull
    public String getContent() {
        return _content;
    }

    @Nonnull
    @Override
    public Object[] getArguments() {
        return new Object[]{_content};
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            final CommentAnnotation that = (CommentAnnotation) o;
            result = getContent().equals(that.getContent());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getContent().hashCode();
    }

    @Override
    public String toString() {
        return "Comment: " + getContent();
    }

    public static class Factory implements Annotation.Factory<CommentAnnotation> {

        @Nonnull
        @Override
        public String getId() {
            return "comment";
        }

        @Nonnull
        @Override
        public Class<CommentAnnotation> getResponsibleFor() {
            return CommentAnnotation.class;
        }

        @Nonnull
        @Override
        public CommentAnnotation createBy(@Nullable Object... arguments) {
            if (arguments == null || arguments.length != 1 || !(arguments[0] instanceof String)) {
                throw new IllegalArgumentException("Expected an argument of type string but got: " + Arrays.toString(arguments));
            }
            return new CommentAnnotation((String) arguments[0]);
        }

    }
}
