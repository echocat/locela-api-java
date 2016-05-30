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

import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class StandardMessagesUnitTest {

    protected static final Message MESSAGE1 = message("1", "a");
    protected static final Message MESSAGE2 = message("2", "b");
    protected static final Message MESSAGE3 = message("3", "c");

    @Test
    public void testFind() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages.find("1"), is(MESSAGE1));
        assertThat(messages.find("2"), is(MESSAGE2));
        assertThat(messages.find("3"), is(MESSAGE3));
        assertThat(messages.find("4"), is(null));
    }

    @Test
    public void testContains() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages.contains("1"), is(true));
        assertThat(messages.contains("2"), is(true));
        assertThat(messages.contains("3"), is(true));
        assertThat(messages.contains("4"), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages, isEqualTo(MESSAGE1, MESSAGE2, MESSAGE3));
    }

}