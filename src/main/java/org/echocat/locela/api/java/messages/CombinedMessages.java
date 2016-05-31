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

import org.echocat.locela.api.java.support.FilterDuplicatesMessagesIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

public class CombinedMessages extends MessagesSupport {

    @Nonnull
    private final Iterable<Messages> _delegates;

    public CombinedMessages(@Nullable Messages... delegates) {
        this(asList(delegates));
    }

    public CombinedMessages(@Nullable Iterable<Messages> delegates) {
        _delegates = delegates != null ? delegates : Collections.<Messages>emptyList();
    }

    @Nullable
    @Override
    public Message find(@Nonnull String id) {
        Message result = null;
        for (final Messages delegate : delegates()) {
            result = delegate.find(id);
            if (result != null) {
                break;
            }
        }
        return result;
    }

    @Override
    public boolean contains(@Nonnull String id) {
        boolean result = false;
        for (final Messages delegate : delegates()) {
            result = delegate.contains(id);
            if (result) {
                break;
            }
        }
        return result;
    }

    @Override
    public Iterator<Message> iterator() {
        return new FilterDuplicatesMessagesIterator(delegates());
    }

    @Nonnull
    protected Iterable<Messages> delegates() {
        return _delegates;
    }

}
