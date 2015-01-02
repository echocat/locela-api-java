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

import org.echocat.jomon.cache.management.CombinedCacheCreator;
import org.echocat.jomon.cache.management.DefaultCacheRepository;
import org.echocat.jomon.testing.CollectionMatchers;
import org.echocat.locela.api.java.messages.CachingMessagesProvider.CacheKey;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.annotation.Nonnull;
import java.util.Locale;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.*;
import static org.echocat.locela.api.java.messages.FileAccessor.ClassLoaderBased.classPathFileAccessor;
import static org.echocat.locela.api.java.messages.FileAccessor.FileSystemBased.fileSystemFileAccessor;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CachingMessagesProviderUnitTest {

    @Nonnull
    private final DefaultCacheRepository _cacheProvider = new DefaultCacheRepository(new CombinedCacheCreator());

    @Test
    public void regular() throws Exception {
        final MessagesProvider original = createOriginalMessagesProvider();
        final MessagesProvider cached = new CachingMessagesProvider(original, _cacheProvider);

        final Messages messages1 = cached.provideBy(null, fileSystemFileAccessor(), "foo1.txt");
        verify(original, times(1)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo1.txt"));
        assertThat(messages1, CollectionMatchers.<Message>isEqualTo(message("file", "foo1.txt")));

        final Messages messages2 = cached.provideBy(null, fileSystemFileAccessor(), "foo1.txt");
        verify(original, times(1)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo1.txt"));
        assertThat(messages2, isSameAs(messages1));
    }

    @Test
    public void withCapacityReached() throws Exception {
        final MessagesProvider original = createOriginalMessagesProvider();
        final MessagesProvider cached = new CachingMessagesProvider(original, 1);

        final Messages messages11 = cached.provideBy(null, fileSystemFileAccessor(), "foo1.txt");
        verify(original, times(1)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo1.txt"));
        assertThat(messages11, CollectionMatchers.<Message>isEqualTo(message("file", "foo1.txt")));

        final Messages messages21 = cached.provideBy(null, fileSystemFileAccessor(), "foo2.txt");
        verify(original, times(1)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo2.txt"));
        assertThat(messages21, CollectionMatchers.<Message>isEqualTo(message("file", "foo2.txt")));

        final Messages messages22 = cached.provideBy(null, fileSystemFileAccessor(), "foo2.txt");
        verify(original, times(1)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo2.txt"));
        assertThat(messages22, isSameAs(messages21));

        final Messages messages12 = cached.provideBy(null, fileSystemFileAccessor(), "foo1.txt");
        verify(original, times(2)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), eq("foo1.txt"));
        assertThat(messages12, not(isSameAs(messages21)));
        assertThat(messages12, CollectionMatchers.<Message>isEqualTo(message("file", "foo1.txt")));
    }

    @Test
    public void cacheKeyEquals() throws Exception {
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(new CacheKey(US, fileSystemFileAccessor(), "1")), is(true));

        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(new CacheKey(US, fileSystemFileAccessor(), "2")), is(false));
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(new CacheKey(GERMANY, fileSystemFileAccessor(), "1")), is(false));
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(new CacheKey(GERMANY, classPathFileAccessor(), "1")), is(false));
    }

    @Test
    public void cacheKeyEqualsSame() throws Exception {
        final CacheKey key = new CacheKey(US, fileSystemFileAccessor(), "1");
        // noinspection EqualsWithItself
        assertThat(key.equals(key), is(true));
    }

    @Test
    public void cacheKeyEqualsNull() throws Exception {
        // noinspection ObjectEqualsNull
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(null), is(false));
    }

    @Test
    public void cacheKeyEqualsOtherObject() throws Exception {
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").equals(new Object()), is(false));
    }

    @Test
    public void cacheKeyHashCode() throws Exception {
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").hashCode(), is(new CacheKey(US, fileSystemFileAccessor(), "1").hashCode()));

        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").hashCode(), isNot(new CacheKey(US, fileSystemFileAccessor(), "2").hashCode()));
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").hashCode(), isNot(new CacheKey(GERMANY, fileSystemFileAccessor(), "1").hashCode()));
        assertThat(new CacheKey(US, fileSystemFileAccessor(), "1").hashCode(), isNot(new CacheKey(GERMANY, classPathFileAccessor(), "1").hashCode()));
    }


    @Nonnull
    protected MessagesProvider createOriginalMessagesProvider() throws Exception {
        final MessagesProvider original = mock(MessagesProvider.class);
        configureAnswerOfProvideByFor(original);
        verify(original, times(0)).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), anyString());
        return original;
    }

    protected void configureAnswerOfProvideByFor(@Nonnull MessagesProvider original) throws Exception {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return messagesFor(message((Locale) invocation.getArguments()[0], "file", (String) invocation.getArguments()[2]));
            }
        }).when(original).provideBy(eq((Locale) null), eq(fileSystemFileAccessor()), anyString());
    }

}