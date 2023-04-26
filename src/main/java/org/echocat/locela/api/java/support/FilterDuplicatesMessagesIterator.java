package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.echocat.locela.api.java.utils.CollectionUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class FilterDuplicatesMessagesIterator implements Iterator<Message> {

    @Nonnull
    private final Set<String> _alreadyReturnedMessageIds = new HashSet<>();
    @Nonnull
    private final Iterator<Messages> _messagesIterator;

    @Nullable
    private Iterator<Message> _messageIterator;
    @Nullable
    private Message _next;

    public FilterDuplicatesMessagesIterator(@Nullable Iterable<Messages> inputs) {
        this(inputs != null ? inputs.iterator() : null);
    }

    public FilterDuplicatesMessagesIterator(@Nullable Iterator<Messages> inputs) {
        _messagesIterator = inputs != null ? inputs : CollectionUtils.emptyIterator();
    }

    @Override
    public boolean hasNext() {
        while (_next == null) {
            if (_messageIterator == null) {
                if (!_messagesIterator.hasNext()) {
                    return false;
                }
                final Messages messages = _messagesIterator.next();
                _messageIterator = messages.iterator();
            }
            if (_messageIterator.hasNext()) {
                final Message message = _messageIterator.next();
                final String id = message.getId();
                if (!_alreadyReturnedMessageIds.contains(id)) {
                    _next = message;
                    _alreadyReturnedMessageIds.add(id);
                }
            } else {
                _messageIterator = null;
            }
        }
        return true;
    }

    @Override
    public Message next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final Message next = _next;
        _next = null;
        return next;
    }

}
