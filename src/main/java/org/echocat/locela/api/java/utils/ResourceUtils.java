package org.echocat.locela.api.java.utils;

import javax.annotation.Nullable;

public class ResourceUtils {

    public static void closeQuietlyIfAutoCloseable(@Nullable Object autoCloseable) {
        try {
            if (autoCloseable instanceof AutoCloseable) {
                ((AutoCloseable)autoCloseable).close();
            }
        } catch (final Exception ignored) {}
    }

    public static void closeQuietly(@Nullable AutoCloseable autoCloseable) {
        closeQuietlyIfAutoCloseable(autoCloseable);
    }

    public static void closeQuietlyIfAutoCloseable(@Nullable Iterable<?> elements) {
        try {
            if (elements != null) {
                for (final Object element : elements) {
                    closeQuietlyIfAutoCloseable(element);
                }
            }
        } catch (final Exception ignored) {}
    }

    public static void closeQuietly(@Nullable Iterable<? extends AutoCloseable> elements) {
        closeQuietlyIfAutoCloseable(elements);
    }

    public static void closeQuietlyIfAutoCloseable(@Nullable Object... elements) {
        try {
            if (elements != null) {
                for (final Object element : elements) {
                    closeQuietlyIfAutoCloseable(element);
                }
            }
        } catch (final Exception ignored) {}
    }

    public static void closeQuietly(@Nullable AutoCloseable... elements) {
        //noinspection ConfusingArgumentToVarargsMethod
        closeQuietlyIfAutoCloseable(elements);
    }

    private ResourceUtils() {}
}
