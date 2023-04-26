package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

public class StandardMessages extends MessagesSupport {

    @Nonnull
    public static StandardMessages messagesFor(@Nullable Message... messages) {
        return messagesFor(asList(messages));
    }

    @Nonnull
    public static StandardMessages messagesFor(@Nonnull Iterable<Message> messages) {
        return new StandardMessages(messages);
    }

    @Nonnull
    private final Map<String, Message> _idToMessage;

    public StandardMessages(@Nullable Iterable<Message> messages) {
        _idToMessage = new LinkedHashMap<>();
        if (messages != null) {
            for (final Message message : messages) {
                if (message != null) {
                    _idToMessage.put(message.getId(), message);
                }
            }
        }
    }

    @Nonnull
    @Override
    public Message find(@Nonnull String id) {
        return idToMessages().get(id);
    }

    @Override
    public boolean contains(@Nonnull String id) {
        return idToMessages().containsKey(id);
    }

    @Override
    public Iterator<Message> iterator() {
        return idToMessages().values().iterator();
    }

    @Nonnull
    protected Map<String, Message> idToMessages() {
        return _idToMessage;
    }

}
