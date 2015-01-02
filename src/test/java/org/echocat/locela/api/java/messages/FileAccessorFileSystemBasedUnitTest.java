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

import org.apache.commons.io.IOUtils;
import org.echocat.locela.api.java.messages.FileAccessor.FileSystemBased;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.annotation.Nonnull;
import java.io.*;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.FileAccessor.FileSystemBased.fileSystemFileAccessor;

public class FileAccessorFileSystemBasedUnitTest {

    protected static final FileAccessor ACCESSOR = fileSystemFileAccessor();

    @Rule
    @Nonnull
    public final TemporaryFolder _folder = new TemporaryFolder();

    @Test
    public void foo1() throws Exception {
        prepareFolder();
        try (final Reader reader = ACCESSOR.open(_folder.getRoot() + "/foo1.txt")) {
            assertThat(IOUtils.toString(reader), is("foo1 contänt"));
        }
    }

    @Test
    public void foo2() throws Exception {
        prepareFolder();
        try (final Reader reader = ACCESSOR.open(_folder.getRoot() + "/foo2.txt")) {
            assertThat(IOUtils.toString(reader), is("foo2 contänt"));
        }
    }

    @Test
    public void foo3() throws Exception {
        try (final Reader reader = ACCESSOR.open(_folder.getRoot() + "/foo3.txt")) {
            assertThat(reader, is(null));
        }
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(ACCESSOR.equals(new FileSystemBased()), is(true));
    }

    @Test
    public void testEqualsOtherType() throws Exception {
        assertThat(ACCESSOR.equals(new Object()), is(false));
    }

    @Test
    public void testEqualsSame() throws Exception {
        // noinspection EqualsWithItself
        assertThat(ACCESSOR.equals(ACCESSOR), is(true));
    }
    @Test
    public void testEqualsNull() throws Exception {
        // noinspection ObjectEqualsNull
        assertThat(ACCESSOR.equals(null), is(false));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(ACCESSOR.hashCode(), is(0));
    }

    protected void prepareFolder() throws Exception {
        copy("testfiles/foo1.txt", _folder.newFile("foo1.txt"));
        copy("testfiles/foo2.txt", _folder.newFile("foo2.txt"));
    }

    protected void copy(@Nonnull String file, @Nonnull File to) throws Exception {
        try (final InputStream is = FileAccessorFileSystemBasedUnitTest.class.getResourceAsStream(file)) {
            if (is == null) {
                throw new IllegalStateException("File missing: " + file);
            }
            try (final OutputStream os = new FileOutputStream(to)) {
                IOUtils.copy(is, os);
            }
        }
    }

    @Nonnull
    protected static String base() {
        return FileAccessorClassLoaderBasedUnitTest.class.getPackage().getName().replace('.', '/') + "/";
    }

}