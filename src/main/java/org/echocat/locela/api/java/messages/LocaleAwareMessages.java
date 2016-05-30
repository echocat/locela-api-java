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

package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.support.FilterDuplicatesMessagesIterator;
import org.echocat.locela.api.java.support.LocaleHierarchy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static org.echocat.locela.api.java.support.CollectionUtils.asList;

public class LocaleAwareMessages extends MessagesSupport {

    @Nonnull
    private final LocaleHierarchy _locales;
    @Nonnull
    private final Map<Locale, Messages> _localeToMessages;

    public LocaleAwareMessages(@Nullable Locale locale, @Nullable Map<Locale, Messages> localeToMessages, @Nullable Locale... fallbackLocales) {
        this(locale, localeToMessages, asList(fallbackLocales));
    }

    public LocaleAwareMessages(@Nullable Locale locale, @Nullable Map<Locale, Messages> localeToMessages, @Nullable Iterable<Locale> fallbackLocales) {
        _locales = new LocaleHierarchy(locale, fallbackLocales);
        _localeToMessages = localeToMessages != null ? localeToMessages : Collections.<Locale, Messages>emptyMap();
    }

    @Nullable
    @Override
    public Message find(@Nonnull String id) {
        Message message = null;
        for (final Locale locale : locales()) {
            message = find(id, locale);
            if (message != null) {
                break;
            }
        }
        return message;
    }

    @Nullable
    protected Message find(@Nonnull String id, @Nullable Locale locale) {
        final Messages messages = localeToMessages().get(locale);
        return messages != null ? messages.find(id) : null;
    }

    @Override
    public boolean contains(@Nonnull String id) {
        boolean found = false;
        for (final Locale locale : locales()) {
            found = contains(id, locale);
            if (found) {
                break;
            }
        }
        return found;
    }

    protected boolean contains(@Nonnull String id, @Nullable Locale locale) {
        final Messages messages = localeToMessages().get(locale);
        return messages != null && messages.contains(id);
    }

    @Override
    public Iterator<Message> iterator() {
        final Iterator<Message> i = new FilterDuplicatesMessagesIterator(localeToMessages().values());
        return new Iterator<Message>() {

            @Nullable
            private Message _next;

            @Override
            public boolean hasNext() {
                while (_next == null && i.hasNext()) {
                    final Message source = i.next();
                    _next = find(source.getId());
                }
                return _next != null;
            }

            @Override
            public Message next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final Message current = _next;
                _next = null;
                return current;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

    @Nullable
    public Locale getLocale() {
        return locales().getStart();
    }

    @Nonnull
    protected LocaleHierarchy locales() {
        return _locales;
    }

    @Nonnull
    protected Map<Locale, Messages> localeToMessages() {
        return _localeToMessages;
    }

    @Override
    public String toString() {
        return getLocale() + ": " + super.toString();
    }

}
