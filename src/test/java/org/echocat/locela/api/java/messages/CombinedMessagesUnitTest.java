package org.echocat.locela.api.java.messages;

import org.junit.Test;

import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;
import static org.hamcrest.MatcherAssert.assertThat;

public class CombinedMessagesUnitTest {

    protected static final Message MESSAGE1A = message("1", "a");
    protected static final Message MESSAGE1B = message("1", "b");

    protected static final Message MESSAGE2B = message("2", "b");
    protected static final Message MESSAGE2C = message("2", "c");

    protected static final Message MESSAGE3A = message("3", "a");
    protected static final Message MESSAGE3C = message("3", "c");

    protected static final Messages MESSAGES1 = messagesFor(MESSAGE1A, MESSAGE3A);
    protected static final Messages MESSAGES2 = messagesFor(MESSAGE1B, MESSAGE2B);
    protected static final Messages MESSAGES3 = messagesFor(MESSAGE2C, MESSAGE3C);


    @Test
    public void testFind() throws Exception {
        final Messages messages1 = new CombinedMessages(MESSAGES1, MESSAGES2, MESSAGES3);
        assertThat(messages1.find("1"), is(MESSAGE1A));
        assertThat(messages1.find("2"), is(MESSAGE2B));
        assertThat(messages1.find("3"), is(MESSAGE3A));
        assertThat(messages1.find("4"), is(null));

        final Messages messages2 = new CombinedMessages(MESSAGES2, MESSAGES3);
        assertThat(messages2.find("1"), is(MESSAGE1B));
        assertThat(messages2.find("2"), is(MESSAGE2B));
        assertThat(messages2.find("3"), is(MESSAGE3C));
        assertThat(messages2.find("4"), is(null));
    }

    @Test
    public void testContains() throws Exception {
        final Messages messages1 = new CombinedMessages(MESSAGES1, MESSAGES2, MESSAGES3);
        assertThat(messages1.contains("1"), is(true));
        assertThat(messages1.contains("2"), is(true));
        assertThat(messages1.contains("3"), is(true));
        assertThat(messages1.contains("4"), is(false));

        final Messages messages2 = new CombinedMessages(MESSAGES2, MESSAGES3);
        assertThat(messages2.contains("1"), is(true));
        assertThat(messages2.contains("2"), is(true));
        assertThat(messages2.contains("3"), is(true));
        assertThat(messages2.contains("4"), is(false));
    }

    @Test
    public void testIterator() throws Exception {
        assertThat(new CombinedMessages(MESSAGES1, MESSAGES2, MESSAGES3), isEqualTo(MESSAGE1A, MESSAGE3A, MESSAGE2B));
        assertThat(new CombinedMessages(MESSAGES2, MESSAGES3), isEqualTo(MESSAGE1B, MESSAGE2B, MESSAGE3C));
    }

}
