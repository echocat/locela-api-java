package org.echocat.locela.api.java.messages;

import org.junit.Test;

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.locela.api.java.messages.PropertyMessage.messageFor;
import static org.echocat.locela.api.java.properties.StandardProperty.property;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyMessageUnitTest {

    @Test
    public void testGetLocale() throws Exception {
        assertThat(messageFor(US, property("foo")).getLocale(), is(US));
        assertThat(messageFor(GERMANY, property("foo")).getLocale(), is(GERMANY));
    }

    @Test
    public void testGet() throws Exception {
        assertThat(messageFor(US, property("foo").set("bar")).get(), is("bar"));
        assertThat(messageFor(US, property("foo").set("bar")).get(), is("bar"));

        assertThat(messageFor(US, property("foo")).get(), is(""));
        assertThat(messageFor(US, property("foo")).get(), is(""));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(messageFor(US, property("foo")).getId(), is("foo"));
        assertThat(messageFor(US, property("foo")).getId(), is("foo"));
    }
}
