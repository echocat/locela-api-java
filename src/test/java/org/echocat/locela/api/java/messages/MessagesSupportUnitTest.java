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

package org.echocat.locela.api.java.messages;

import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.Map.Entry;

import static java.util.Locale.US;
import static org.echocat.jomon.runtime.CollectionUtils.asMap;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.MessagesSupportUnitTest.MessageImpl.message;
import static org.echocat.locela.api.java.messages.MessagesSupportUnitTest.MessagesImpl.messages;

public class MessagesSupportUnitTest {

    @Test
    public void testEquals() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).equals(messages(null,
            "b", "2",
            "a", "1"
        )), is(true));

        assertThat(messages(US,
            "a", "1",
            "b", "2"
        ).equals(messages(US,
            "b", "2",
            "a", "1"
        )), is(true));

        assertThat(messages(null).equals(messages(null)), is(true));

        assertThat(messages(US).equals(messages(US)), is(true));
    }

    @Test
    public void testEqualsDoesNotMatchOnContent() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).equals(messages(null,
            "b", "3",
            "a", "1"
        )), is(false));

        assertThat(messages(US,
            "a", "1",
            "b", "2"
        ).equals(messages(US,
            "b", "3",
            "a", "1"
        )), is(false));
    }

    @Test
    public void testEqualsDoesNotMatchOnKeys() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).equals(messages(null,
            "a", "1"
        )), is(false));

        assertThat(messages(US,
            "a", "1",
            "b", "2"
        ).equals(messages(US,
            "a", "1"
        )), is(false));
    }

    @Test
    public void testEqualsDoesNotMatchOnLocale() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).equals(messages(US,
            "a", "1",
            "b", "2"
        )), is(false));
    }

    @Test
    public void testEqualsOnSame() throws Exception {
        final Messages messages = messages(null,
            "a", "1",
            "b", "2"
        );
        // noinspection EqualsWithItself
        assertThat(messages.equals(messages), is(true));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        // noinspection ObjectEqualsNull
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).equals(null), is(false));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).hashCode(), is(messages(null,
            "b", "2",
            "a", "1"
        ).hashCode()));

        assertThat(messages(US,
            "a", "1",
            "b", "2"
        ).hashCode(), is(messages(US,
            "b", "2",
            "a", "1"
        ).hashCode()));

        assertThat(messages(null).hashCode(), is(messages(null).hashCode()));

        assertThat(messages(US).hashCode(), is(messages(US).hashCode()));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(messages(null,
            "a", "1",
            "b", "2"
        ).toString(), is("{a = 1, b = 2}"));

        assertThat(messages(US,
            "a", "1",
            "b", "2"
        ).toString(), is("(en_US){a = 1, b = 2}"));

        assertThat(messages(null).toString(), is("{}"));
        assertThat(messages(US).toString(), is("(en_US){}"));
    }

    protected static class MessagesImpl extends MessagesSupport {

        @Nonnull
        protected static Messages messages(@Nullable Locale locale) {
            return messages(locale, (Message[]) null);
        }

        @Nonnull
        protected static Messages messages(@Nullable Locale locale, @Nullable String... idToContent) {
            final Map<String, String> idToPlainMessage = asMap(idToContent);
            final List<Message> messages = new ArrayList<>();
            for (final Entry<String, String> idAndPlainMessage : idToPlainMessage.entrySet()) {
                messages.add(message(idAndPlainMessage.getKey(), idAndPlainMessage.getValue()));
            }
            return messages(locale, messages.toArray(new Message[messages.size()]));
        }

        @Nonnull
        protected static Messages messages(@Nullable Locale locale, @Nullable Message... messages) {
            final Map<String, Message> idToMessage = new LinkedHashMap<>();
            if (messages != null) {
                for (final Message message : messages) {
                    idToMessage.put(message.getId(), message);
                }
            }
            return new MessagesImpl(locale, idToMessage);
        }

        @Nullable
        private final Locale _locale;
        @Nonnull
        private final Map<String, Message> _idToMessage;

        public MessagesImpl(@Nullable Locale locale, @Nonnull Map<String, Message> idToMessage) {
            _locale = locale;
            _idToMessage = idToMessage;
        }

        @Nullable
        @Override
        public Locale getLocale() {
            return _locale;
        }

        @Nonnull
        @Override
        public Message get(@Nonnull String id) {
            final Message result = _idToMessage.get(id);
            return result != null ? result : new DummyMessage(_locale, id);
        }

        @Override
        public boolean contains(@Nonnull String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Message> iterator() {
            return _idToMessage.values().iterator();
        }
    }

    protected static class MessageImpl extends MessageSupport {

        @Nonnull
        protected static Message message(@Nonnull String id, @Nonnull String content) {
            return new MessageImpl(id, content);
        }

        @Nonnull
        private final String _id;
        @Nonnull
        private final String _content;

        public MessageImpl(@Nonnull String id, @Nonnull String content) {
            _id = id;
            _content = content;
        }

        @Nonnull
        @Override
        public String get() {
            return _content;
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Locale getLocale() {
            return null;
        }

        @Nullable
        @Override
        public String getId() {
            return _id;
        }

    }
}