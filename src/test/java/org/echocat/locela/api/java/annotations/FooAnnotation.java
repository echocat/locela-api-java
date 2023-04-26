package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FooAnnotation implements Annotation {

    private final boolean _aBoolean;
    private final double _aDouble;
    private final String _aString;

    public FooAnnotation(boolean aBoolean, double aDouble, String aString) {
        _aBoolean = aBoolean;
        _aDouble = aDouble;
        _aString = aString;
    }

    public boolean isABoolean() {
        return _aBoolean;
    }

    public double getADouble() {
        return _aDouble;
    }

    public String getAString() {
        return _aString;
    }

    @Nonnull
    @Override
    public String getId() {
        return "foo";
    }

    @Nonnull
    @Override
    public Object[] getArguments() {
        return new Object[]{_aBoolean, _aDouble, _aString};
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || getClass() != o.getClass()) {
            result = false;
        } else {
            final FooAnnotation that = (FooAnnotation) o;
            if (isABoolean() == that.isABoolean() && getADouble() == that.getADouble()) {
                final String aString = getAString();
                result = aString != null ? aString.equals(that.getAString()) : that.getArguments() == null;
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final String aString = getAString();
        int result;
        result = (isABoolean() ? 1 : 0);
        result = 31 * result + (int) getADouble();
        result = 31 * result + (aString != null ? aString.hashCode() : 0);
        return result;
    }

    public static class Factory implements Annotation.Factory<FooAnnotation> {

        @Nonnull
        @Override
        public String getId() {
            return "foo";
        }

        @Nonnull
        @Override
        public Class<FooAnnotation> getResponsibleFor() {
            return FooAnnotation.class;
        }

        @Nonnull
        @Override
        public FooAnnotation createBy(@Nullable Object... arguments) {
            if (arguments == null || arguments.length != 3) {
                throw new IllegalArgumentException("Expected argument count is 3 but was " + (arguments != null ? arguments.length : 0) + ".");
            }
            if (!(arguments[0] instanceof Boolean)) {
                throw new IllegalArgumentException("Argument #0 was expected as boolean but got: " + arguments[0]);
            }
            if (!(arguments[1] instanceof Double)) {
                throw new IllegalArgumentException("Argument #1 was expected as double but got: " + arguments[1]);
            }
            if (arguments[2] != null && !(arguments[2] instanceof String)) {
                throw new IllegalArgumentException("Argument #2 was expected as string but got: " + arguments[2]);
            }
            return new FooAnnotation((Boolean)arguments[0], (Double)arguments[1], (String)arguments[2]);
        }

    }

}
