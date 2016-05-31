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
import org.echocat.locela.api.java.properties.Property;
import org.junit.Test;

import java.util.MissingResourceException;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.messages.PropertyMessage.messageFor;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;
import static org.echocat.locela.api.java.properties.StandardProperty.property;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isEmpty;
import static org.echocat.locela.api.java.testing.IterableMatchers.containsAllItemsOf;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.echocat.locela.api.java.utils.MessagesUtils.emptyMessages;
import static org.echocat.locela.api.java.utils.MessagesUtils.toResourceBundle;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class MessagesUtilsUnitTest {

    protected static final Property<String> PROPERTY1 = property("a").set("1");
    protected static final Property<String> PROPERTY2 = property("b").set("2");
    protected static final Property<String> PROPERTY3 = property("c").set("3");
    protected static final Property<String> PROPERTY4 = property("d").set("4");

    protected static final Message MESSAGE1 = messageFor(US, PROPERTY1);
    protected static final Message MESSAGE2 = messageFor(US, PROPERTY2);
    protected static final Message MESSAGE3 = messageFor(US, PROPERTY3);
    protected static final Message MESSAGE4 = messageFor(US, PROPERTY4);

    protected static final Messages MESSAGES = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3, MESSAGE4);

    @Test
    public void containsOfEmptyMessagesShouldAlwaysReturnFalse() throws Exception {
        assertThat(emptyMessages().contains("foo"), is(false));
        assertThat(emptyMessages().contains("bar"), is(false));
        assertThat(emptyMessages().contains("abc"), is(false));
        assertThat(emptyMessages().contains("def"), is(false));
    }

    @Test
    public void findOfEmptyMessagesShouldAlwaysReturnNull() throws Exception {
        assertThat(emptyMessages().find("foo"), is(null));
        assertThat(emptyMessages().find("bar"), is(null));
        assertThat(emptyMessages().find("abc"), is(null));
        assertThat(emptyMessages().find("def"), is(null));
    }

    @Test
    public void iteratorOfEmptyMessagesShouldAlwaysBeEmpty() throws Exception {
        assertThat(asList(emptyMessages().iterator()), isEmpty());
    }

    @Test
    public void handleGetObjectOfToResourceBundleShouldReturnValuesOfMessages() throws Exception {
        assertThat(toResourceBundle(MESSAGES).getString("a"), is("1"));
        assertThat(toResourceBundle(MESSAGES).getString("b"), is("2"));
        assertThat(toResourceBundle(MESSAGES).getString("c"), is("3"));
        assertThat(toResourceBundle(MESSAGES).getString("d"), is("4"));
    }

    @Test
    public void handleGetObjectOfToResourceBundleShouldReturnErrorOfNonExistingValues() throws Exception {
        try {
            toResourceBundle(MESSAGES).getString("e");
            fail("Expected exception missing.");
        } catch (final MissingResourceException ignored) {}
    }

    @Test
    public void getKeyOfToResourceBundleShouldReturnIdsOfMessages() throws Exception {
        assertThat(asList(toResourceBundle(MESSAGES).getKeys()), containsAllItemsOf("a", "b", "c", "d"));
    }

}