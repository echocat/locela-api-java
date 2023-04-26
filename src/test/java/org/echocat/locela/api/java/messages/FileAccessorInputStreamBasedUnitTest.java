package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.messages.FileAccessor.InputStreamBased;
import org.echocat.locela.api.java.utils.IOUtils;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static org.hamcrest.MatcherAssert.assertThat;
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
