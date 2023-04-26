package org.echocat.locela.api.java.messages;

import org.junit.Test;

import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.StandardMessage.message;

public class StandardMessageUnitTest {

    @Test
    public void testGet() throws Exception {
        assertThat(message("a", "b").get(), is("b"));
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat(message(US, "a", "b").getLocale(), is(US));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(message("a", "b").getId(), is("a"));
    }

}
