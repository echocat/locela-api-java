/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.support;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

public class IterationUtils {

    @Nonnull
    public static <T> Iterator<T> toIterator(@Nonnull RemoveHandler<T> removeHandler, @Nonnull Iterable<? extends T> source) {
        return makeRemoveable(removeHandler, source.iterator());
    }

    @Nonnull
    public static  <T> Iterator<T> makeRemoveable(@Nonnull final RemoveHandler<T> removeHandler, @Nonnull final Iterator<? extends T> source) {
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

        public void remove(@Nonnull T what);

    }


}
