package org.echocat.locela.api.java.messages;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class StandardMessagesUnitTest {

    protected static final Message MESSAGE1 = message("1", "a");
    protected static final Message MESSAGE2 = message("2", "b");
    protected static final Message MESSAGE3 = message("3", "c");

    @Test
    public void testFind() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages.find("1"), is(MESSAGE1));
        assertThat(messages.find("2"), is(MESSAGE2));
        assertThat(messages.find("3"), is(MESSAGE3));
        assertThat(messages.find("4"), is(null));
    }

    @Test
    public void testContains() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages.contains("1"), is(true));
        assertThat(messages.contains("2"), is(true));
        assertThat(messages.contains("3"), is(true));
        assertThat(messages.contains("4"), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        final Messages messages = messagesFor(MESSAGE1, MESSAGE2, MESSAGE3);
        assertThat(messages, isEqualTo(MESSAGE1, MESSAGE2, MESSAGE3));
    }

}
