package org.echocat.locela.api.java.messages;

import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.Map.Entry;

import static org.echocat.locela.api.java.utils.CollectionUtils.asMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.MessagesSupportUnitTest.MessageImpl.message;
import static org.echocat.locela.api.java.messages.MessagesSupportUnitTest.MessagesImpl.messages;

public class MessagesSupportUnitTest {

    @Test
    public void testEquals() throws Exception {
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).equals(messages(
            "b", "2",
            "a", "1"
        )), is(true));

        assertThat(messages().equals(messages()), is(true));
    }

    @Test
    public void testEqualsDoesNotMatchOnContent() throws Exception {
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).equals(messages(
            "b", "3",
            "a", "1"
        )), is(false));
    }

    @Test
    public void testEqualsDoesNotMatchOnKeys() throws Exception {
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).equals(messages(
            "a", "1"
        )), is(false));
    }

    @Test
    public void testEqualsOnSame() throws Exception {
        final Messages messages = messages(
            "a", "1",
            "b", "2"
        );
        // noinspection EqualsWithItself
        assertThat(messages.equals(messages), is(true));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        // noinspection ObjectEqualsNull
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).equals(null), is(false));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).hashCode(), is(messages(
            "b", "2",
            "a", "1"
        ).hashCode()));

        assertThat(messages().hashCode(), is(messages().hashCode()));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(messages(
            "a", "1",
            "b", "2"
        ).toString(), is("" +
            "{\n" +
            "    a: 1\n" +
            "    b: 2\n" +
            "}"));

        assertThat(messages().toString(), is("{}"));
    }

    protected static class MessagesImpl extends MessagesSupport {

        @Nonnull
        protected static Messages messages() {
            return messages((Message[]) null);
        }

        @Nonnull
        protected static Messages messages(@Nullable String... idToContent) {
            //noinspection ConfusingArgumentToVarargsMethod
            final Map<String, String> idToPlainMessage = asMap(idToContent);
            final List<Message> messages = new ArrayList<>();
            for (final Entry<String, String> idAndPlainMessage : idToPlainMessage.entrySet()) {
                messages.add(message(idAndPlainMessage.getKey(), idAndPlainMessage.getValue()));
            }
            return messages(messages.toArray(new Message[0]));
        }

        @Nonnull
        protected static Messages messages(@Nullable Message... messages) {
            final Map<String, Message> idToMessage = new LinkedHashMap<>();
            if (messages != null) {
                for (final Message message : messages) {
                    idToMessage.put(message.getId(), message);
                }
            }
            return new MessagesImpl(idToMessage);
        }

        @Nonnull
        private final Map<String, Message> _idToMessage;

        public MessagesImpl(@Nonnull Map<String, Message> idToMessage) {
            _idToMessage = idToMessage;
        }

        @Nonnull
        @Override
        public Message find(@Nonnull String id) {
            return _idToMessage.get(id);
        }

        @Override
        public boolean contains(@Nonnull String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Message> iterator() {
            return _idToMessage.values().iterator();
        }
    }

    protected static class MessageImpl extends MessageSupport {

        @Nonnull
        protected static Message message(@Nonnull String id, @Nonnull String content) {
            return new MessageImpl(id, content);
        }

        @Nonnull
        private final String _id;
        @Nonnull
        private final String _content;

        public MessageImpl(@Nonnull String id, @Nonnull String content) {
            _id = id;
            _content = content;
        }

        @Nonnull
        @Override
        public String get() {
            return _content;
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Nullable
        @Override
        public Locale getLocale() {
            return null;
        }

        @Nullable
        @Override
        public String getId() {
            return _id;
        }

    }
}
