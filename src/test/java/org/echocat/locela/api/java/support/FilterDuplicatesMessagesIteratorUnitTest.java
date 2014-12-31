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

package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.junit.Test;

import java.util.Iterator;

import static org.echocat.jomon.runtime.CollectionUtils.asList;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.CollectionMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class FilterDuplicatesMessagesIteratorUnitTest {

    protected static final Message MESSAGE1 = message("1", "a");
    protected static final Message MESSAGE2 = message("2", "b");
    protected static final Message MESSAGE3 = message("3", "c");

    protected static final Messages MESSAGES1 = messagesFor(MESSAGE1, MESSAGE3, null);
    protected static final Messages MESSAGES2 = messagesFor(MESSAGE2, MESSAGE3);
    protected static final Messages MESSAGES3 = messagesFor(MESSAGE3, MESSAGE1);

    @Test
    public void regular() throws Exception {
        final Iterator<Message> i = new FilterDuplicatesMessagesIterator(asList(MESSAGES1, MESSAGES2, MESSAGES3));
        assertThat(asList(i), isEqualTo(MESSAGE1, MESSAGE3, MESSAGE2));
    }


}