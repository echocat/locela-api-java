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

import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableMap;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class LocaleAwareMessagesUnitTest {

    protected static final Message MESSAGE1A = message(GERMANY, "1", "a");
    protected static final Message MESSAGE1B = message(GERMAN, "1", "b");
    protected static final Message MESSAGE1C = message(US, "1", "c");

    protected static final Message MESSAGE2A = message(GERMANY, "2", "a");
    protected static final Message MESSAGE2B = message(GERMAN, "2", "b");
    protected static final Message MESSAGE2C = message(US, "2", "c");

    protected static final Message MESSAGE3A = message(GERMANY, "3", "a");
    protected static final Message MESSAGE3B = message(GERMAN, "3", "b");
    protected static final Message MESSAGE3C = message(US, "3", "c");

    protected static final Messages MESSAGES1 = messagesFor(MESSAGE1A, MESSAGE3A);
    protected static final Messages MESSAGES2 = messagesFor(MESSAGE1B, MESSAGE2B);
    protected static final Messages MESSAGES3 = messagesFor(MESSAGE2C, MESSAGE3C);

    protected static final Map<Locale, Messages> LOCALE_TO_MESSAGES = asImmutableMap(
        GERMANY, MESSAGES1,
        GERMAN, MESSAGES2,
        US, MESSAGES3
    );

    @Test
    public void testFind() throws Exception {
        final Messages messages1 = new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES, US);
        assertThat(messages1.find("1"), is(MESSAGE1A));
        assertThat(messages1.find("2"), is(MESSAGE2B));
        assertThat(messages1.find("3"), is(MESSAGE3A));
        assertThat(messages1.find("4"), is(null));

        final Messages messages2 = new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES, US);
        assertThat(messages2.find("1"), is(MESSAGE1B));
        assertThat(messages2.find("2"), is(MESSAGE2B));
        assertThat(messages2.find("3"), is(MESSAGE3C));
        assertThat(messages2.find("4"), is(null));

        final Messages messages3 = new LocaleAwareMessages(null, LOCALE_TO_MESSAGES, US);
        assertThat(messages3.find("1"), is(null));
        assertThat(messages3.find("2"), is(MESSAGE2C));
        assertThat(messages3.find("3"), is(MESSAGE3C));
        assertThat(messages3.find("4"), is(null));

        final Messages messages4 = new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES);
        assertThat(messages4.find("1"), is(MESSAGE1A));
        assertThat(messages4.find("2"), is(MESSAGE2B));
        assertThat(messages4.find("3"), is(MESSAGE3A));
        assertThat(messages4.find("4"), is(null));

        final Messages messages5 = new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES);
        assertThat(messages5.find("1"), is(MESSAGE1B));
        assertThat(messages5.find("2"), is(MESSAGE2B));
        assertThat(messages5.find("3"), is(null));
        assertThat(messages5.find("4"), is(null));

        final Messages messages6 = new LocaleAwareMessages(null, LOCALE_TO_MESSAGES);
        assertThat(messages6.find("1"), is(null));
        assertThat(messages6.find("2"), is(null));
        assertThat(messages6.find("3"), is(null));
        assertThat(messages6.find("4"), is(null));
    }

    @Test
    public void testContains() throws Exception {
        final Messages messages1 = new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES, US);
        assertThat(messages1.contains("1"), is(true));
        assertThat(messages1.contains("2"), is(true));
        assertThat(messages1.contains("3"), is(true));
        assertThat(messages1.contains("4"), is(false));

        final Messages messages2 = new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES, US);
        assertThat(messages2.contains("1"), is(true));
        assertThat(messages2.contains("2"), is(true));
        assertThat(messages2.contains("3"), is(true));
        assertThat(messages2.contains("4"), is(false));

        final Messages messages3 = new LocaleAwareMessages(null, LOCALE_TO_MESSAGES, US);
        assertThat(messages3.contains("1"), is(false));
        assertThat(messages3.contains("2"), is(true));
        assertThat(messages3.contains("3"), is(true));
        assertThat(messages3.contains("4"), is(false));

        final Messages messages4 = new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES);
        assertThat(messages4.contains("1"), is(true));
        assertThat(messages4.contains("2"), is(true));
        assertThat(messages4.contains("3"), is(true));
        assertThat(messages4.contains("4"), is(false));

        final Messages messages5 = new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES);
        assertThat(messages5.contains("1"), is(true));
        assertThat(messages5.contains("2"), is(true));
        assertThat(messages5.contains("3"), is(false));
        assertThat(messages5.contains("4"), is(false));

        final Messages messages6 = new LocaleAwareMessages(null, LOCALE_TO_MESSAGES);
        assertThat(messages6.contains("1"), is(false));
        assertThat(messages6.contains("2"), is(false));
        assertThat(messages6.contains("3"), is(false));
        assertThat(messages6.contains("4"), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        assertThat(new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES, US), isEqualTo(MESSAGE1A, MESSAGE3A, MESSAGE2B));
        assertThat(new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES, US), isEqualTo(MESSAGE1B, MESSAGE3C, MESSAGE2B));
        assertThat(new LocaleAwareMessages(null, LOCALE_TO_MESSAGES, US), isEqualTo(MESSAGE3C, MESSAGE2C));

        assertThat(new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES), isEqualTo(MESSAGE1A, MESSAGE3A, MESSAGE2B));
        assertThat(new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES), isEqualTo(MESSAGE1B, MESSAGE2B));
        assertThat(new LocaleAwareMessages(null, LOCALE_TO_MESSAGES), IterableMatchers.<Message>isEqualTo());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemoveNotSupported() throws Exception {
        new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES).iterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorNoSuchElement() throws Exception {
        new LocaleAwareMessages(null, LOCALE_TO_MESSAGES).iterator().next();
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES, US).toString(), is("de_DE: " + messagesFor(MESSAGE1A, MESSAGE3A, MESSAGE2B).toString()));
        assertThat(new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES, US).toString(), is("de: " + messagesFor(MESSAGE1B, MESSAGE3C, MESSAGE2B).toString()));
        assertThat(new LocaleAwareMessages(null, LOCALE_TO_MESSAGES, US).toString(), is("null: " + messagesFor(MESSAGE3C, MESSAGE2C).toString()));

        assertThat(new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES).toString(), is("de_DE: " + messagesFor(MESSAGE1A, MESSAGE3A, MESSAGE2B).toString()));
        assertThat(new LocaleAwareMessages(GERMAN, LOCALE_TO_MESSAGES).toString(), is("de: " + messagesFor(MESSAGE1B, MESSAGE2B).toString()));
        assertThat(new LocaleAwareMessages(null, LOCALE_TO_MESSAGES).toString(), is("null: " + messagesFor().toString()));
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat(new LocaleAwareMessages(GERMANY, LOCALE_TO_MESSAGES, US).getLocale(), is(GERMANY));
        assertThat(new LocaleAwareMessages(US, LOCALE_TO_MESSAGES, US).getLocale(), is(US));
    }


}