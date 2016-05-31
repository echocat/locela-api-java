/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2016 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.utils;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.echocat.locela.api.java.messages.MessagesSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.echocat.locela.api.java.utils.CollectionUtils.emptyIterator;

public class MessagesUtils {

    @Nonnull
    private static final Messages EMPTY_MESSAGES = new EmptyMessages();

    @Nonnull
    public static Messages emptyMessages() {
        return EMPTY_MESSAGES;
    }

    @Nonnull
    public static ResourceBundle toResourceBundle(@Nullable Messages messages) {
        return toResourceBundle(messages, null);
    }

    @Nonnull
    public static ResourceBundle toResourceBundle(@Nullable Messages messages, @Nullable Locale locale) {
        return new MessagesBasedResourceBundle(messages, locale);
    }

    private static class EmptyMessages extends MessagesSupport {

        @Nullable
        @Override
        public Message find(@Nonnull String id) {
            return null;
        }

        @Override
        public boolean contains(@Nonnull String id) {
            return false;
        }

        @Override
        public Iterator<Message> iterator() {
            return emptyIterator();
        }
    }

    protected static class MessagesBasedResourceBundle extends ResourceBundle {

        @Nullable
        private final Messages _messages;
        @Nullable
        private final Locale _locale;

        public MessagesBasedResourceBundle(@Nullable  Messages messages, @Nullable Locale locale) {
            _locale = locale;
            _messages = messages != null ? messages : emptyMessages();
        }

        @Override
        protected Object handleGetObject(String key) {
            if (!_messages.contains(key)) {
                return null;
            }
            return _messages.get(key).get();
        }

        @Override
        public Enumeration<String> getKeys() {
            final Iterator<Message> source = _messages.iterator();
            return new Enumeration<String>() {
                @Override
                public boolean hasMoreElements() {
                    return source.hasNext();
                }

                @Override
                public String nextElement() {
                    return source.next().getId();
                }
            };
        }

        @Override
        public Locale getLocale() {
            return _locale;
        }
    }

}
