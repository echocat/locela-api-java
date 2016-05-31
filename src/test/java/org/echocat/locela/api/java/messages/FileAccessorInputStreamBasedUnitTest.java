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

import org.echocat.locela.api.java.messages.FileAccessor.InputStreamBased;
import org.echocat.locela.api.java.utils.IOUtils;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;


public class FileAccessorInputStreamBasedUnitTest {

    protected static final FileAccessor ACCESSOR = new AccessorImpl();

    @Test
    public void foo1() throws Exception {
        try (final Reader reader = ACCESSOR.open("testfiles/foo1.txt")) {
            assertThat(IOUtils.toString(reader), is("foo1 contänt"));
        }
    }

    @Test
    public void foo2() throws Exception {
        try (final Reader reader = ACCESSOR.open("testfiles/foo2.txt")) {
            assertThat(IOUtils.toString(reader), is("foo2 contänt"));
        }
    }

    protected static final class AccessorImpl extends InputStreamBased {

        @Nullable
        @Override
        protected InputStream openStream(@Nonnull String file) throws IOException {
            return getClass().getResourceAsStream(file);
        }

    }
}