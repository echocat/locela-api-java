package org.echocat.locela.api.java.utils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public class IterationUtils {

    @Nonnull
    public static <T> Iterator<T> toIterator(@Nonnull RemoveHandler<T> removeHandler, @Nonnull Iterable<? extends T> source) {
        return makeRemovable(removeHandler, source.iterator());
    }

    @Nonnull
    public static  <T> Iterator<T> makeRemovable(@Nonnull final RemoveHandler<T> removeHandler, @Nonnull final Iterator<? extends T> source) {
        final AtomicReference<T> last = new AtomicReference<>();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return source.hasNext();
            }

            @Override
            public T next() {
                final T next = source.next();
                last.set(next);
                return next;
            }

            @Override
            public void remove() {
                final T element = last.get();
                if (element == null) {
                    throw new IllegalStateException();
                }
                removeHandler.remove(element);
            }
        };
    }

    public interface RemoveHandler<T> {

        void remove(@Nonnull T what);

    }


}
