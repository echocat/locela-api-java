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

package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class StandardProperty<V> extends PropertySupport<V> {

    @Nonnull
    public static <V> StandardProperty<V> property(@Nonnull String id, @Nonnull Class<V> ofType) {
        return new StandardProperty<>(id, ofType);
    }

    @Nonnull
    public static StandardProperty<String> property(@Nonnull String id) {
        return property(id, String.class);
    }

    @Nonnull
    private final String _id;
    @Nonnull
    private final Class<V> _type;

    @Nullable
    private volatile V _content;

    public StandardProperty(@Nonnull String id, @Nonnull Class<V> type) {
        _id = id;
        _type = type;
    }

    @Nonnull
    public StandardProperty<V> withAnnotations(@Nullable Annotation... annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    public StandardProperty<V> withAnnotations(@Nullable Iterable<? extends Annotation> annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    @Override
    public String getId() {
        return _id;
    }

    @Nullable
    @Override
    public V get() {
        return _content;
    }

    @Override
    public StandardProperty<V> set(@Nullable V content) throws IllegalArgumentException {
        if (content != null && !getType().isInstance(content)) {
            throw new IllegalArgumentException("Expected content of type '" + getType().getName() + "' but got: " + content);
        }
        _content = content;
        return this;
    }

    @Override
    public Class<V> getType() {
        return _type;
    }

}
