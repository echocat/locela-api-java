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

package org.echocat.locela.api.java.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Locale.US;
import static org.echocat.jomon.runtime.CollectionUtils.asImmutableMap;
import static org.echocat.jomon.runtime.CollectionUtils.asList;

@ThreadSafe
public class MessageFormatterFactory implements FormatterFactory<MessageFormatter> {

    @Nonnull
    private final Map<String, FormatterFactory<?>> _idToFactory;

    public MessageFormatterFactory(@Nullable Iterable<FormatterFactory<?>> factories) {
        _idToFactory = toIdToFactory(factories);
    }

    public MessageFormatterFactory(@Nullable FormatterFactory<?>... factories) {
        this(asList(factories));
    }

    @Nonnull
    protected Map<String, FormatterFactory<?>> toIdToFactory(@Nullable Iterable<FormatterFactory<?>> factories) {
        final Map<String, FormatterFactory<?>> idToFactory = new LinkedHashMap<>();
        idToFactory.put(getId(), this);
        if (factories != null) {
            for (final FormatterFactory<?> factory : factories) {
                final String id = factory.getId();
                if (!idToFactory.containsKey(id)) {
                    idToFactory.put(id, factory);
                }
            }
        }
        return asImmutableMap(idToFactory);
    }

    @Nonnull
    protected FormatterFactory<?> getFactoryBy(@Nonnull String id) throws IllegalArgumentException {
        final FormatterFactory<?> factory = _idToFactory.get(id);
        if (factory == null) {
            throw new IllegalArgumentException("'" + id + "' is unknown.");
        }
        return factory;
    }

    @Nonnull
    public Iterable<FormatterFactory<?>> getFactories() {
        return _idToFactory.values();
    }

    @Nonnull
    @Override
    public String getId() {
        return "message";
    }

    @Nonnull
    public MessageFormatter createBy(@Nullable Locale locale, @Nullable String pattern) {
        return createBy(locale, pattern, this);
    }

    @Nonnull
    @Override
    public MessageFormatter createBy(@Nullable Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root) {
        assertSameRoot(root);
        return createByInternal(locale, pattern);
    }

    protected MessageFormatter createByInternal(@Nullable Locale locale, @Nullable String pattern) {
        return new MessageFormatter(locale != null ? locale : US, pattern, this);
    }

    protected void assertSameRoot(@Nullable FormatterFactory<?> root) {
        //noinspection ObjectEquality
        if (this != root) {
            throw new IllegalStateException("This formatter factory only accepts itself as root formatter factory.");
        }
    }
}
