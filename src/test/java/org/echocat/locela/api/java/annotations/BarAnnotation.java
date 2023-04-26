package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarAnnotation implements Annotation {

    @Nonnull
    @Override
    public String getId() {
        return "bar";
    }

    @Nonnull
    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else {
            result = !(o == null || getClass() != o.getClass());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static class Factory implements Annotation.Factory<BarAnnotation> {

        @Nonnull
        @Override
        public String getId() {
            return "bar";
        }

        @Nonnull
        @Override
        public Class<BarAnnotation> getResponsibleFor() {
            return BarAnnotation.class;
        }

        @Nonnull
        @Override
        public BarAnnotation createBy(@Nullable Object... arguments) {
            if (arguments != null && arguments.length != 0) {
                throw new IllegalArgumentException("Expected argument count is 0 but was " + (arguments != null ? arguments.length : 0) + ".");
            }
            return new BarAnnotation();
        }

    }

}
