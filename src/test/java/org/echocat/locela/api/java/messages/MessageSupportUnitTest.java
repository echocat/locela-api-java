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

import org.echocat.jomon.runtime.CollectionUtils;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.jomon.runtime.CollectionUtils.asList;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.MessageSupportUnitTest.MessageImpl.message;

public class MessageSupportUnitTest {

    @Test
    public void vararg() throws Exception {
        assertThat(message("foo").format("a", "b"), is("foo:[a, b]"));
    }

    @Test
    public void iterable() throws Exception {
        assertThat(message("foo").format(asList("a", "b")), is("foo:[a, b]"));
    }

    @Test
    public void map() throws Exception {
        assertThat(message("foo").format(CollectionUtils.<String, Object>asMap("a", "b")), is("foo:{a=b}"));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(message("foo").toString(), is("anId = foo"));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(message("a", "1", US).equals(message("a", "1", US)), is(true));
        assertThat(message("a", "1", null).equals(message("a", "1", null)), is(true));
    }

    @Test
    public void testEqualsDoesNotMatchOnContent() throws Exception {
        assertThat(message("a", "1", US).equals(message("a", "2", US)), is(false));
        assertThat(message("a", "1", null).equals(message("a", "2", null)), is(false));
    }

    @Test
    public void testEqualsDoesNotMatchOnKeys() throws Exception {
        assertThat(message("a", "1", US).equals(message("b", "1", US)), is(false));
        assertThat(message("a", "1", null).equals(message("b", "1", null)), is(false));
    }

    @Test
    public void testEqualsDoesNotMatchOnLocale() throws Exception {
        assertThat(message("a", "1", US).equals(message("a", "1", GERMANY)), is(false));
    }

    @Test
    public void testEqualsOnSame() throws Exception {
        final Message message = message("a", "1", US);
        // noinspection EqualsWithItself
        assertThat(message.equals(message), is(true));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        // noinspection ObjectEqualsNull
        assertThat(message("a", "1", US).equals(null), is(false));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(message("a", "1", US).hashCode(), is(message("a", "1", US).hashCode()));
        assertThat(message("a", "1", null).hashCode(), is(message("a", "1", null).hashCode()));
    }

    protected static class MessageImpl extends MessageSupport {

        @Nonnull
        protected static Message message(@Nonnull String content) {
            return message("anId", content, null);
        }

        @Nonnull
        protected static Message message(@Nonnull String id, @Nonnull String content, @Nullable Locale locale) {
            return new MessageImpl(id, content, locale);
        }

        @Nonnull
        private final String _id;
        @Nonnull
        private final String _content;
        @Nullable
        private final Locale _locale;

        public MessageImpl(@Nonnull String id, @Nonnull String content, @Nullable Locale locale) {
            _id = id;
            _content = content;
            _locale = locale;
        }

        @Nonnull
        @Override
        public String get() {
            return _content;
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            to.write(_content);
            to.write(":");
            if (value instanceof Object[]) {
                to.write(Arrays.toString((Object[])value));
            } else {
                to.write(value != null ? value.toString() : null);
            }
        }

        @Nullable
        @Override
        public Locale getLocale() {
            return _locale;
        }

        @Nullable
        @Override
        public String getId() {
            return _id;
        }

    }
}