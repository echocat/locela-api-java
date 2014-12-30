/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.annotations;

import org.echocat.jomon.runtime.util.IdEnabled;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Annotation extends IdEnabled<String> {

    @Nonnull
    @Override
    public String getId();

    @Nonnull
    public Object[] getArguments();

    public interface Factory<T extends Annotation> extends IdEnabled<String> {

        @Nonnull
        @Override
        public String getId();

        @Nonnull
        public Class<T> getResponsibleFor();

        @Nonnull
        public T createBy(@Nullable Object... arguments);

        public interface Provider extends Iterable<Factory<? extends Annotation>> {

            @Nonnull
            public <T extends Annotation> Factory<T> provideBy(@Nonnull Class<T> type) throws UnknownAnnotationException;

            @Nonnull
            public Factory<? extends Annotation> provideBy(@Nonnull String id) throws UnknownAnnotationException;

            public static class UnknownAnnotationException extends IllegalArgumentException {

                public UnknownAnnotationException(String s) {
                    super(s);
                }

            }

        }

    }

}
