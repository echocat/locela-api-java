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

package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.FileAccessor;
import org.echocat.locela.api.java.messages.Messages;
import org.echocat.locela.api.java.messages.MessagesProvider;
import org.junit.Test;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isEmpty;
import static org.echocat.locela.api.java.testing.IterableMatchers.containsAllItemsOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class MessageProviderBasedMessageSourceUnitTest {

    private static final Object[] ARGS = {"arg1"};

    @Test
    public void constructorWithClassOnly() throws Exception {
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        new MessageProviderBasedMessageSource(messagesProvider, null, MessageProviderBasedMessageSourceUnitTest.class).getMessage("foo", ARGS, "def", US);
        verify(messagesProvider, times(1)).provideBy(US, MessageProviderBasedMessageSourceUnitTest.class);
    }

    @Test
    public void constructorWithClassAndFile() throws Exception {
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        new MessageProviderBasedMessageSource(messagesProvider, null, MessageProviderBasedMessageSourceUnitTest.class, "test").getMessage("foo", ARGS, "def", US);
        verify(messagesProvider, times(1)).provideBy(US, MessageProviderBasedMessageSourceUnitTest.class, "test");
    }

    @Test
    public void constructorWithLoaderAndFile() throws Exception {
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        new MessageProviderBasedMessageSource(messagesProvider, null, MessageProviderBasedMessageSourceUnitTest.class.getClassLoader(), "test").getMessage("foo", ARGS, "def", US);
        verify(messagesProvider, times(1)).provideBy(US, MessageProviderBasedMessageSourceUnitTest.class.getClassLoader(), "test");
    }

    @Test
    public void constructorWithDirectoryAndFile() throws Exception {
        final File dir = new File("dir");
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        new MessageProviderBasedMessageSource(messagesProvider, null, dir, "test").getMessage("foo", ARGS, "def", US);
        verify(messagesProvider, times(1)).provideBy(US, dir, "test");
    }

    @Test
    public void constructorWithAccessorAndFile() throws Exception {
        final FileAccessor accessor = mock(FileAccessor.class);
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        new MessageProviderBasedMessageSource(messagesProvider, null, accessor, "test").getMessage("foo", ARGS, "def", US);
        verify(messagesProvider, times(1)).provideBy(US, accessor, "test");
    }

    @Test
    public void getMessage() throws Exception {
        final MessageProviderBasedMessageSource messageSource = messageSource();
        configureProperty(messageSource.messagesProvider(), "foo1", "test1", US);
        configureProperty(messageSource.messagesProvider(), "foo2", "test2", GERMANY);

        assertThat(messageSource.getMessage("foo1", ARGS, US), is("test1"));
        assertThat(messageSource.getMessage("foo2", ARGS, GERMANY), is("test2"));

        assertThat(messageSource.getMessage("fooX", ARGS, "def", US), is("def"));


        assertThat(messageSource.getMessage(new DefaultMessageSourceResolvable(new String[]{"foo1"}, ARGS), US), is("test1"));
        assertThat(messageSource.getMessage(new DefaultMessageSourceResolvable(new String[]{"fooX", "foo2"}, ARGS), GERMANY), is("test2"));
        assertThat(messageSource.getMessage(new DefaultMessageSourceResolvable(new String[]{"fooX", "fooY"}, ARGS, "def"), US), is("def"));

        try {
            messageSource.getMessage("fooX", ARGS, US);
            fail("Exception missing.");
        } catch (final NoSuchMessageException ignored) {}

        try {
            messageSource.getMessage(new DefaultMessageSourceResolvable(new String[]{"fooX"}), US);
            fail("Exception missing.");
        } catch (final NoSuchMessageException ignored) {}
    }

    @Test
    public void testCaching() throws Exception {
        final MessageProviderBasedMessageSource messageSource = messageSource();
        configureProperty(messageSource.messagesProvider(), "foo1", "test1", US);
        configureProperty(messageSource.messagesProvider(), "foo2", "test2", GERMANY);
        configureProperty(messageSource.messagesProvider(), "foo3", "test3", FRANCE);

        messageSource.setMaxCacheEntries(2);

        assertThat(messageSource.getMessage("foo1", ARGS, US), is("test1"));
        assertThat(messageSource.cache().keySet(), containsAllItemsOf(US));
        assertThat(messageSource.getMessage("foo2", ARGS, GERMANY), is("test2"));
        assertThat(messageSource.cache().keySet(), containsAllItemsOf(US, GERMANY));

        assertThat(messageSource.getMessage("foo3", ARGS, FRANCE), is("test3"));
        assertThat(messageSource.cache().keySet(), containsAllItemsOf(GERMANY, FRANCE));

    }

    @Test
    public void testNonCaching() throws Exception {
        final MessageProviderBasedMessageSource messageSource = messageSource();
        configureProperty(messageSource.messagesProvider(), "foo1", "test1", US);
        configureProperty(messageSource.messagesProvider(), "foo2", "test2", GERMANY);
        configureProperty(messageSource.messagesProvider(), "foo3", "test3", FRANCE);

        messageSource.setMaxCacheEntries(null);

        assertThat(messageSource.getMessage("foo1", ARGS, US), is("test1"));
        assertThat(messageSource.cache().keySet(), isEmpty());
        assertThat(messageSource.getMessage("foo2", ARGS, GERMANY), is("test2"));
        assertThat(messageSource.cache().keySet(), isEmpty());
        assertThat(messageSource.getMessage("foo3", ARGS, FRANCE), is("test3"));
        assertThat(messageSource.cache().keySet(), isEmpty());

    }

    protected MessageProviderBasedMessageSource messageSource() {
        final MessagesProvider messagesProvider = mock(MessagesProvider.class);
        return new MessageProviderBasedMessageSource(messagesProvider, null, MessageProviderBasedMessageSourceUnitTest.class, "file");
    }

    protected void configureProperty(MessagesProvider messagesProvider, String id, String content, Locale locale) throws IOException {
        final Messages messages = messagesFor(id, content, locale);
        when(messagesProvider.provideBy(locale, MessageProviderBasedMessageSourceUnitTest.class, "file")).thenReturn(messages);
    }

    protected Messages messagesFor(String id, String content, Locale locale) {
        final Messages messages = mock(Messages.class);
        when(messages.get(id)).thenReturn(message(locale, id, content));
        when(messages.find(id)).thenReturn(message(locale, id, content));
        return messages;
    }

}