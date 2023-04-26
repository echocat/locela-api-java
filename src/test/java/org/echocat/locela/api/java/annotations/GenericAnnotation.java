package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GenericAnnotation<T> implements Annotation {

    private final String _id;
    private final T _value;

    public GenericAnnotation(String id, T value) {
        _id = id;
        _value = value;
    }

    public T getValue() {
        return _value;
    }

    @Nonnull
    @Override
    public String getId() {
        return _id;
    }

    @Nonnull
    @Override
    public Object[] getArguments() {
        return new Object[]{_value};
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            final GenericAnnotation<?> that = (GenericAnnotation) o;
            if (getId().equals(that.getId())) {
                final Object value = getValue();
                result = value != null ? value.equals(that.getValue()) : that.getValue() == null;
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final Object value = getValue();
        int result;
        result = getId().hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getId() + ": " + getValue();
    }

    public static class Factory<H extends GenericAnnotation<V>, V> implements Annotation.Factory<H> {

        @Nonnull
        private final String _id;

        public Factory(@Nonnull String id) {
            _id = id;
        }

        @Nonnull
        @Override
        public String getId() {
            return _id;
        }

        @Nonnull
        @Override
        public Class<H> getResponsibleFor() {
            //noinspection unchecked,RedundantCast
            return (Class<H>) (Class) GenericAnnotation.class;
        }

        @Nonnull
        @Override
        public H createBy(@Nullable Object... arguments) {
            if (arguments == null || arguments.length != 1) {
                throw new IllegalArgumentException("Expected argument count is 1 but was " + (arguments != null ? arguments.length : 0) + ".");
            }
            // noinspection unchecked
            return (H) new GenericAnnotation<>(getId(), (V) arguments[0]);
        }

    }

    public static class StringAnnotationFactory extends Factory<GenericAnnotation<String>, String> {

        public StringAnnotationFactory() {
            super("string");
        }

    }

    public static class BooleanAnnotationFactory extends Factory<GenericAnnotation<Boolean>, Boolean> {

        public BooleanAnnotationFactory() {
            super("boolean");
        }

    }

    public static class DoubleAnnotationFactory extends Factory<GenericAnnotation<Double>, Double> {

        public DoubleAnnotationFactory() {
            super("double");
        }

    }

}
