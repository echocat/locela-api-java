package org.echocat.locela.api.java.messages;

import org.junit.Test;

import java.io.StringWriter;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class DummyMessageUnitTest {

    @Test
    public void testGet() throws Exception {
        assertThat(new DummyMessage(US, "foo").get(), is(""));
        assertThat(new DummyMessage("foo").get(), is(""));
    }

    @Test
    public void testFormat() throws Exception {
        try (final StringWriter writer = new StringWriter()) {
            new DummyMessage(US, "foo").format(new Object(), writer);
            assertThat(writer.toString(), is(""));
        }
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat(new DummyMessage("foo").getLocale(), is(null));
        assertThat(new DummyMessage(null, "foo").getLocale(), is(null));
        assertThat(new DummyMessage(GERMANY, "foo").getLocale(), is(GERMANY));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(new DummyMessage(US, "foo").getId(), is("foo"));
        assertThat(new DummyMessage("foo").getId(), is("foo"));
    }
}
