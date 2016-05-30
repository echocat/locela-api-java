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

import org.echocat.locela.api.java.messages.FileAccessor.ClassLoaderBased;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isSameAs;
import static org.echocat.locela.api.java.messages.FileAccessor.FileSystemBased.fileSystemFileAccessor;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class MessagesProviderSupportUnitTest {

    protected static final Messages FOO = messagesFor();

    @Test
    public void provideByClass() throws Exception {
        final MessagesProviderImpl provider = new MessagesProviderImpl();

        assertThat(provider.provideBy(US, MessagesProviderSupportUnitTest.class), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(US));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderSupportUnitTest.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is(MessagesProviderSupportUnitTest.class.getName().replace('.', '/') + ".properties"));

        assertThat(provider.provideBy(null, MessagesProviderImpl.class), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(null));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderImpl.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is(MessagesProviderImpl.class.getName().replace('.', '/') + ".properties"));
    }

    @Test
    public void provideByClassAndFile() throws Exception {
        final MessagesProviderImpl provider = new MessagesProviderImpl();

        assertThat(provider.provideBy(US, MessagesProviderSupportUnitTest.class, "foo1.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(US));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderSupportUnitTest.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is(MessagesProviderSupportUnitTest.class.getPackage().getName().replace('.', '/') + "/foo1.properties"));

        assertThat(provider.provideBy(null, MessagesProviderImpl.class, "foo2.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(null));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderImpl.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is(MessagesProviderImpl.class.getPackage().getName().replace('.', '/') + "/foo2.properties"));
    }

    @Test
    public void provideByClasspath() throws Exception {
        final MessagesProviderImpl provider = new MessagesProviderImpl();

        assertThat(provider.provideBy(US, MessagesProviderSupportUnitTest.class.getClassLoader(), "foo/bar.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(US));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderSupportUnitTest.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is("foo/bar.properties"));

        assertThat(provider.provideBy(null, MessagesProviderSupportUnitTest.class.getClassLoader(), "foo/bar2.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(null));
        assertThat(provider.getLastAccessor(), is((FileAccessor) new ClassLoaderBased(MessagesProviderSupportUnitTest.class.getClassLoader())));
        assertThat(provider.getLastBaseFile(), is("foo/bar2.properties"));
    }

    @Test
    public void provideByFile() throws Exception {
        final MessagesProviderImpl provider = new MessagesProviderImpl();

        assertThat(provider.provideBy(US, new File("/foo"), "bar.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(US));
        assertThat(provider.getLastAccessor(), is((FileAccessor) fileSystemFileAccessor()));
        assertThat(provider.getLastBaseFile(), is(new File("/foo", "bar.properties").getCanonicalPath()));

        assertThat(provider.provideBy(null, new File("/foo"), "bar2.properties"), isSameAs(FOO));
        assertThat(provider.getLastLocale(), is(null));
        assertThat(provider.getLastAccessor(), is((FileAccessor) fileSystemFileAccessor()));
        assertThat(provider.getLastBaseFile(), is(new File("/foo", "bar2.properties").getCanonicalPath()));
    }

    protected static class MessagesProviderImpl extends MessagesProviderSupport {

        @Nullable
        private Locale _lastLocale;
        @Nullable
        private FileAccessor _lastAccessor;
        @Nullable
        private String _lastBaseFile;

        @Nullable
        @Override
        public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
            _lastLocale = locale;
            _lastAccessor = accessor;
            _lastBaseFile = baseFile;
            return FOO;
        }

        @Nullable
        public Locale getLastLocale() {
            return _lastLocale;
        }

        @Nullable
        public FileAccessor getLastAccessor() {
            return _lastAccessor;
        }

        @Nullable
        public String getLastBaseFile() {
            return _lastBaseFile;
        }

    }

}