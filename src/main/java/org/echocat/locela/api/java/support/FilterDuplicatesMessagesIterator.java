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

import com.google.common.base.Predicate;
import org.echocat.jomon.runtime.iterators.ChainedIterator;
import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.echocat.jomon.runtime.iterators.IteratorUtils.filter;

public class FilterDuplicatesMessagesIterator extends ChainedIterator<Messages, Message> {

    @Nonnull
    private final Predicate<Message> _predicate = new FilterDuplicateIds();

    public FilterDuplicatesMessagesIterator(@Nonnull Iterable<Messages> inputs) {
        super(inputs);
    }

    @Nullable
    @Override
    protected Iterator<Message> nextIterator(@Nullable Messages input) {
        return filter(input.iterator(), _predicate);
    }

    protected static class FilterDuplicateIds implements Predicate<Message> {

        @Nonnull
        private final Set<String> _alreadyAllowedIds = new HashSet<>();

        @Override
        public boolean apply(@Nonnull Message input) {
            final String id = input.getId();
            return _alreadyAllowedIds.add(id);
        }

    }

}
