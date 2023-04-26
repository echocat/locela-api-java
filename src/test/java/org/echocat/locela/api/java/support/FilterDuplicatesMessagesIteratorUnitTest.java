package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.junit.Test;

import java.util.Iterator;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessages.messagesFor;

public class FilterDuplicatesMessagesIteratorUnitTest {

    protected static final Message MESSAGE1 = message("1", "a");
    protected static final Message MESSAGE2 = message("2", "b");
    protected static final Message MESSAGE3 = message("3", "c");

    protected static final Messages MESSAGES1 = messagesFor(MESSAGE1, MESSAGE3, null);
    protected static final Messages MESSAGES2 = messagesFor(MESSAGE2, MESSAGE3);
    protected static final Messages MESSAGES3 = messagesFor(MESSAGE3, MESSAGE1);

    @Test
    public void regular() throws Exception {
        final Iterator<Message> i = new FilterDuplicatesMessagesIterator(asList(MESSAGES1, MESSAGES2, MESSAGES3));
        assertThat(asList(i), isEqualTo(MESSAGE1, MESSAGE3, MESSAGE2));
    }


}
