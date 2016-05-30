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

import org.echocat.locela.api.java.properties.Properties;
import org.echocat.locela.api.java.properties.Property;
import org.junit.Test;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isInstanceOf;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.PropertiesMessages.messagesFor;
import static org.echocat.locela.api.java.messages.PropertyMessage.messageFor;
import static org.echocat.locela.api.java.properties.StandardProperties.properties;
import static org.echocat.locela.api.java.properties.StandardProperty.property;
import static org.junit.Assert.assertThat;

public class PropertiesMessagesUnitTest {

    protected static final Property<String> PROPERTY1 = property("a").set("1");
    protected static final Property<String> PROPERTY2 = property("b").set("2");
    protected static final Property<String> PROPERTY3 = property("c").set("3");
    protected static final Property<String> PROPERTY4 = property("d").set("4");
    protected static final Property<String> TEXT_PROPERTY = property("text").set("foo{0}");

    protected static final Message MESSAGE1 = messageFor(US, PROPERTY1);
    protected static final Message MESSAGE2 = messageFor(US, PROPERTY2);
    protected static final Message MESSAGE3 = messageFor(US, PROPERTY3);
    protected static final Message MESSAGE4 = messageFor(US, PROPERTY4);
    protected static final Message TEXT_MESSAGE = messageFor(US, TEXT_PROPERTY);

    protected static final Properties<String> PROPERTIES = properties(
        PROPERTY1,
        PROPERTY2,
        PROPERTY3,
        PROPERTY4,
        TEXT_PROPERTY
    );

    @Test
    public void testGet() throws Exception {
        final PropertiesMessages messages = messagesFor(US, PROPERTIES);
        assertThat(messages.get("a"), is(MESSAGE1));
        assertThat(messages.get("b"), is(MESSAGE2));
        assertThat(messages.get("c"), is(MESSAGE3));
        assertThat(messages.get("d"), is(MESSAGE4));
        assertThat(messages.get("text"), is(TEXT_MESSAGE));
    }

    @Test
    public void testGetUnknown() throws Exception {
        final PropertiesMessages messages = messagesFor(US, PROPERTIES);
        final Message message = messages.get("unknown");
        assertThat(message, isInstanceOf(DummyMessage.class));
        assertThat(message.getId(), is("unknown"));
        assertThat(message.get(), is(""));
    }

    @Test
    public void testContains() throws Exception {
        final PropertiesMessages messages = messagesFor(US, PROPERTIES);
        assertThat(messages.contains("a"), is(true));
        assertThat(messages.contains("b"), is(true));
        assertThat(messages.contains("c"), is(true));
        assertThat(messages.contains("d"), is(true));
        assertThat(messages.contains("text"), is(true));

        assertThat(messages.contains("e"), is(false));
        assertThat(messages.contains("text2"), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        final PropertiesMessages messages = messagesFor(US, PROPERTIES);
        assertThat(messages, isEqualTo(
            MESSAGE1,
            MESSAGE2,
            MESSAGE3,
            MESSAGE4,
            TEXT_MESSAGE
        ));
    }

}