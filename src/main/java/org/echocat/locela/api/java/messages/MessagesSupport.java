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
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public abstract class MessagesSupport implements Messages {

    @Override
    public boolean equals(Object o) {
        boolean result;
        if (this == o) {
            result = true;
        } else  if (o == null || !(o instanceof Messages)) {
            result = false;
        } else {
            final Messages that = (Messages) o;
            final Locale locale = getLocale();
            if (locale != null ? locale.equals(that.getLocale()): that.getLocale() == null) {
                final Set<String> thisIds = getIdsOf(this);
                final Set<String> thatIds = getIdsOf(that);
                if (thisIds.equals(thatIds)) {
                    result = true;
                    for (final Message thisMessage : this) {
                        final Message thatMessage = that.get(thisMessage.getId());
                        if (!thisMessage.equals(thatMessage)) {
                            result = false;
                            break;
                        }
                    }
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @Nonnull
    protected Set<String> getIdsOf(@Nonnull Iterable<Message> messages) {
        final Set<String> result = new HashSet<>();
        for (final Message message : messages) {
            result.add(message.getId());
        }
        return result;
    }

    @Override
    public int hashCode() {
        final Locale locale = getLocale();
        int result = locale != null ? locale.hashCode() : 0;
        for (final String id : getIdsOf(this)) {
            result = 31 * result + get(id).hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Locale locale = getLocale();
        if (locale != null) {
            sb.append('(').append(locale).append(')');
        }
        sb.append('{');
        boolean firstMessage = true;
        for (final Message message : this) {
            if (firstMessage) {
                firstMessage = false;
            } else {
                sb.append(", ");
            }
            sb.append(message);
        }
        sb.append('}');
        return sb.toString();
    }

}
