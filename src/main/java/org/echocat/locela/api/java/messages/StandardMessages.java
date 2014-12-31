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

package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.echocat.jomon.runtime.CollectionUtils.asList;

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
