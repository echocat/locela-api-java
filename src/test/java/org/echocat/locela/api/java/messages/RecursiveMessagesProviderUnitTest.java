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

import static java.util.Locale.GERMAN;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.RecursiveMessagesProvider.recursiveMessagesProvider;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class RecursiveMessagesProviderUnitTest {

    public static final RecursiveMessagesProvider PROVIDER = recursiveMessagesProvider();

    @Test
    public void fooBarTestNull() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(null, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/bar/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message("a", "foo/bar/test")),
            messagesFor(message("a", "foo/bar")),
            messagesFor(message("a", "foo")),
            messagesFor(message("a", "root"))
        ));
    }

    @Test
    public void fooBarNull() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(null, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/bar/testX.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message("a", "foo/bar")),
            messagesFor(message("a", "foo")),
            messagesFor(message("a", "root"))
        ));
    }

    @Test
    public void fooNull() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(null, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/barX/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message("a", "foo")),
            messagesFor(message("a", "root"))
        ));
    }

    @Test
    public void rootNull() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(null, RecursiveMessagesProviderUnitTest.class, "testfiles/fooX/bar/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message("a", "root"))
        ));
    }

    @Test
    public void fooBarTestGerman() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(GERMAN, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/bar/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message(GERMAN, "a", "foo/bar/test_de")),
            messagesFor(message(GERMAN, "a", "foo/bar_de")),
            messagesFor(message(GERMAN, "a", "foo_de")),
            messagesFor(message(GERMAN, "a", "root_de"))
        ));
    }

    @Test
    public void fooBarGerman() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(GERMAN, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/bar/testX.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message(GERMAN, "a", "foo/bar_de")),
            messagesFor(message(GERMAN, "a", "foo_de")),
            messagesFor(message(GERMAN, "a", "root_de"))
        ));
    }

    @Test
    public void fooGerman() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(GERMAN, RecursiveMessagesProviderUnitTest.class, "testfiles/foo/barX/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message(GERMAN, "a", "foo_de")),
            messagesFor(message(GERMAN, "a", "root_de"))
        ));
    }

    @Test
    public void rootGerman() throws Exception {
        final CombinedMessages messages = (CombinedMessages) PROVIDER.provideBy(GERMAN, RecursiveMessagesProviderUnitTest.class, "testfiles/fooX/bar/test.properties");
        assertThat(messages.delegates(), IterableMatchers.<Messages>isEqualTo(
            messagesFor(message(GERMAN, "a", "root_de"))
        ));
    }

    @Test
    public void getParent() throws Exception {
        final RecursiveMessagesProvider provider = new RecursiveMessagesProviderReImpl();
        assertThat(provider.getParent("a/b/c"), is("a/b"));
        assertThat(provider.getParent("a\\b\\c"), is("a\\b"));

        assertThat(provider.getParent("/a/b/c"), is("/a/b"));
        assertThat(provider.getParent("\\a\\b\\c"), is("\\a\\b"));

        assertThat(provider.getParent("a/b"), is("a"));
        assertThat(provider.getParent("a\\b"), is("a"));

        assertThat(provider.getParent("/a/b"), is("/a"));
        assertThat(provider.getParent("\\a\\b"), is("\\a"));

        assertThat(provider.getParent("a"), is(null));
        assertThat(provider.getParent("a"), is(null));

        assertThat(provider.getParent("/a"), is(""));
        assertThat(provider.getParent("\\a"), is(""));

        assertThat(provider.getParent(""), is(null));
        assertThat(provider.getParent(null), is(null));

    }

    protected static class RecursiveMessagesProviderReImpl extends RecursiveMessagesProvider {

        @Override
        protected char systemFileSeparator() {
            return '\\';
        }
    }
}