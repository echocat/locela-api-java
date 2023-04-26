package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.messages.FileAccessor.ClassLoaderBased;
import org.echocat.locela.api.java.utils.IOUtils;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isNot;
import static org.echocat.locela.api.java.messages.FileAccessor.ClassLoaderBased.classPathFileAccessor;

public class FileAccessorClassLoaderBasedUnitTest {

    protected static final FileAccessor ACCESSOR = new ClassLoaderBased(FileAccessorClassLoaderBasedUnitTest.class.getClassLoader());

    @Test
    public void foo1() throws Exception {
        try (final Reader reader = ACCESSOR.open(base() + "testfiles/foo1.txt")) {
            assertThat(IOUtils.toString(reader), is("foo1 contänt"));
        }
    }

    @Test
    public void foo2() throws Exception {
        try (final Reader reader = ACCESSOR.open("/" + base() + "testfiles/foo2.txt")) {
            assertThat(IOUtils.toString(reader), is("foo2 contänt"));
        }
    }

    @Test
    public void foo3() throws Exception {
        try (final Reader reader = ACCESSOR.open(base() + "testfiles/foo3.txt")) {
            assertThat(reader, is(null));
        }
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(ACCESSOR.equals(new ClassLoaderBased(FileAccessorClassLoaderBasedUnitTest.class.getClassLoader())), is(true));
        assertThat(ACCESSOR.equals(new ClassLoaderBased(new URLClassLoader(new URL[0]))), is(false));
        assertThat(classPathFileAccessor().equals(new ClassLoaderBased(Thread.currentThread().getContextClassLoader())), is(true));
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
        assertThat(ACCESSOR.hashCode(), is(new ClassLoaderBased(FileAccessorClassLoaderBasedUnitTest.class.getClassLoader()).hashCode()));
        assertThat(ACCESSOR.hashCode(), isNot(new ClassLoaderBased(new URLClassLoader(new URL[0])).hashCode()));
    }

    @Nonnull
    protected static String base() {
        return FileAccessorClassLoaderBasedUnitTest.class.getPackage().getName().replace('.', '/') + "/";
    }

}
