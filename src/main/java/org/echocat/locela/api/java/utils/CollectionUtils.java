package org.echocat.locela.api.java.utils;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static java.util.Collections.*;

public class CollectionUtils {


    /**
     * Returns the given map enriched
     * with the mappings <code>a[0] =&gt; a[1], a[2] =&gt; a[3], ...</code>.
     * @param a the elements to construct a {@link Map} from.
     * @return a {@link Map} constructed of the specified elements.
     */
    @Nonnull
    public static <K, V> Map<K, V> putAll(@Nonnull Map<K, V> original, @Nullable Object... a) {
        if (a != null) {
            final int length = a.length;
            if (length % 2 == 1) {
                throw new IllegalArgumentException("You must provide an even number of arguments.");
            }
            for (int i = 0; i < length; i += 2) {
                // noinspection unchecked
                original.put((K) a[i], (V) a[i + 1]);
            }
        }
        return original;
    }

    /**
     * Returns a {@link LinkedHashMap}
     * with the mappings <code>a[0] =&gt; a[1], a[2] =&gt; a[3], ...</code>.
     * @param a the elements to construct a {@link Map} from.
     * @return a {@link Map} constructed of the specified elements.
     */
    @Nonnull
    public static <K, V> Map<K, V> asMap(@Nullable Object... a) {
        return putAll(new LinkedHashMap<>(), a);
    }

    /**
     * Returns the given map enriched
     * with the mappings <code>a[0] =&gt; a[1], a[2] =&gt; a[3], ...</code>.
     * @param a the elements to construct a {@link Map} from.
     * @return an immutable {@link Map} constructed of the specified elements.
     */
    @Nonnull
    public static <K, V> Map<K, V> putAllAndMakeImmutable(@Nonnull Map<K, V> original, @Nullable Object... a) {
        return asImmutableMap(putAll(original, a));
    }

    /**
     * Returns a {@link LinkedHashMap}
     * with the mappings <code>a[0] =&gt; a[1], a[2] =&gt; a[3], ...</code>.
     * @param a the elements to construct a {@link Map} from.
     * @return an immutable {@link Map} constructed of the specified elements.
     */
    @Nonnull
    public static <K, V> Map<K, V> asImmutableMap(@Nullable Object... a) {
        return putAllAndMakeImmutable(new LinkedHashMap<>(), a);
    }

    @Nonnull
    public static <K, V> Map<K, V> asImmutableMap(@Nullable Map<K, V> map) {
        return map != null ? unmodifiableMap(map) : Collections.emptyMap();
    }

    @SafeVarargs
    @Nonnull
    public static <T, C extends Collection<T>> C addAll(@Nonnull C to, @Nullable T... elements) {
        if (elements != null) {
            Collections.addAll(to, elements);
        }
        return to;
    }

    @Nonnull
    public static <T, C extends Collection<T>> C addAll(@Nonnull C to, @Nullable Iterable<T> elements) {
        if (elements != null) {
            try {
                addAll(to, elements.iterator());
            } finally {
                ResourceUtils.closeQuietlyIfAutoCloseable(elements);
            }
        }
        return to;
    }

    @Nonnull
    public static <T, C extends Collection<T>> C addAll(@Nonnull C to, @Nullable Iterator<T> elements) {
        if (elements != null) {
            try {
                while (elements.hasNext()) {
                    to.add(elements.next());
                }
            } finally {
                ResourceUtils.closeQuietlyIfAutoCloseable(elements);
            }
        }
        return to;
    }

    @Nonnull
    public static <T> Collection<T> asCollection(@Nullable Iterable<T> in) {
        final Collection<T> result;
        if (in instanceof Collection) {
            result = (Collection<T>) in;
        } else {
            result = asList(in);
        }
        return result;
    }

    /**
     * Returns a {@link List} containing the given <code>objects</code>,
     * returns an empty List, if <code>objects</code> is null.
     */
    @SafeVarargs
    @Nonnull
    public static <T> List<T> asList(@Nullable T... objects) {
        final List<T> result;
        if (objects == null) {
            result = new ArrayList<>();
        } else {
            final int initialCapacity = Math.max(16, ((objects.length + 2) / 3) * 4);
            result = new ArrayList<>(initialCapacity);
            result.addAll(new ArrayWrapper<>(objects));
        }
        return result;
    }

    /**
     * Returns an unmodifiable {@link List} containing the given <code>objects</code>,
     * returns an empty {@link List}, if <code>objects</code> is null.
     */
    @SafeVarargs
    @Nonnull
    public static <T> List<T> asImmutableList(@Nullable T... objects) {
        return unmodifiableList(asList(objects));
    }

    @Nonnull
    public static <T> List<T> asList(@Nullable Iterator<T> iterator) {
        final List<T> result = new ArrayList<>();
        try {
            if (iterator != null) {
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                }
            }
        } finally {
            ResourceUtils.closeQuietlyIfAutoCloseable(iterator);
        }
        return result;
    }

    @Nonnull
    public static <T> List<T> asList(@Nullable Iterable<T> in) {
        final List<T> result;
        if (in instanceof List) {
            result = (List<T>) in;
        } else if (in instanceof Collection) {
            result = new ArrayList<>((Collection<T>) in);
        } else {
            result = new ArrayList<>();
            addAll(result, in);
        }
        return result;
    }

    @Nonnull
    public static <T> List<T> asList(@Nullable Enumeration<T> in) {
        final List<T> result = new ArrayList<>();
        while (in != null && in.hasMoreElements()) {
            result.add(in.nextElement());
        }
        return result;
    }

    @Nonnull
    public static <T> List<T> asImmutableList(@Nullable Iterable<T> in) {
        return unmodifiableList(asList(in));
    }


    @Nonnull
    public static <T> Set<T> asSet(@Nullable Iterable<T> in) {
        final Set<T> result;
        if (in instanceof Set) {
            result = (Set<T>) in;
        } else if (in instanceof Collection) {
            result = new LinkedHashSet<>((Collection<T>) in);
        } else {
            result = addAll(new LinkedHashSet<>(), in);
        }
        return result;
    }

    @Nonnull
    public static <T> Set<T> asImmutableSet(@Nullable Iterable<T> in) {
        return unmodifiableSet(asSet(in));
    }

    @Nonnull
    public static <T> CloseableIterator<T> emptyIterator() {
        return new CloseableIterator<T>() {
            @Override public boolean hasNext() { return false; }
            @Override public T next() { throw new NoSuchElementException(); }
            @Override public void remove() { throw new UnsupportedOperationException(); }
            @Override public void close() {}
        };
    }

    @Nonnegative
    public static <T> long countElementsOf(@Nullable Iterable<T> iterable) {
        return iterable != null ? countElementsOf(iterable.iterator()) : 0;
    }

    @Nonnegative
    public static <T> long countElementsOf(@Nullable Iterator<T> iterator) {
        long count = 0;
        try {
            if (iterator != null) {
                while (iterator.hasNext()) {
                    iterator.next();
                    count++;
                }
            }
        } finally {
            ResourceUtils.closeQuietlyIfAutoCloseable(iterator);
        }
        return count;
    }

    /**
     * This class is similar to the class which is returned by {@link Arrays#asList}
     * but does not clone the wrapped array if {@link #toArray} is called.
     */
    private static class ArrayWrapper<E> extends AbstractList<E> {
        private final E[] _wrappedArray;

        private ArrayWrapper(E[] arrayToWrap) {
            _wrappedArray = arrayToWrap;
        }

        @Override
        public E get(int index) {
            return _wrappedArray[index];
        }

        @Override
        public int size() {
            return _wrappedArray.length;
        }

        /**
         * Returns the wrapped array.
         */
        @Override
        public Object[] toArray() {
            return _wrappedArray;
        }
    }

    private CollectionUtils() {}

}
