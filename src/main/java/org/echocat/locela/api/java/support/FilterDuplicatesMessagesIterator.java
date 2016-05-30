/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;

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
        _messagesIterator = inputs != null ? inputs : CollectionUtils.<Messages>emptyIterator();
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

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}
