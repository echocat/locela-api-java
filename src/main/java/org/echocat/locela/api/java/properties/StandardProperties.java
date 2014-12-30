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
import java.util.LinkedHashMap;
import java.util.Map;

@NotThreadSafe
public class StandardProperties<V> extends PropertiesSupport<V> {

    @Nonnull
    public static <V> StandardProperties<V> properties(@Nullable Property<V>... properties) {
        return new StandardProperties<V>().withProperties(properties);
    }

    @Nonnull
    private final Map<String, Property<V>> _idToProperty = new LinkedHashMap<>();

    @Nonnull
    protected Map<String, Property<V>> idToProperty() {
        return _idToProperty;
    }

    @Override
    public void add(@Nonnull Property<? extends V> property) {
        // noinspection unchecked
        idToProperty().put(property.getId(), (Property<V>) property);
    }

    @Nullable
    @Override
    public Property<V> get(@Nonnull String id) {
        return idToProperty().get(id);
    }

    @Override
    public boolean contains(@Nonnull String id) {
        return idToProperty().containsKey(id);
    }

    @Override
    public void remove(@Nonnull String id) {
        idToProperty().remove(id);
    }

    @Nonnull
    @Override
    protected Iterable<? extends Property<V>> getProperties() {
        return idToProperty().values();
    }

    @Nonnull
    public StandardProperties<V> withAnnotations(@Nullable Annotation... annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    public StandardProperties<V> withAnnotations(@Nullable Iterable<? extends Annotation> annotations) {
        addAnnotations(annotations);
        return this;
    }

    @Nonnull
    public StandardProperties<V> withProperties(@Nullable Property<?>... properties) {
        if (properties != null) {
            for (final Property<?> property : properties) {
                // noinspection unchecked
                _idToProperty.put(property.getId(), (Property<V>) property);
            }
        }
        return this;
    }

    @Nonnull
    public StandardProperties<V> withProperties(@Nullable Iterable<? extends Property<? extends V>> properties) {
        if (properties != null) {
            for (final Property<?> property : properties) {
                // noinspection unchecked
                _idToProperty.put(property.getId(), (Property<V>) property);
            }
        }
        return this;
    }

}
