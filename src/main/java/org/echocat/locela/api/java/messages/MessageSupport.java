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
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import static org.echocat.locela.api.java.support.StringUtils.isEmpty;

public abstract class MessageSupport implements Message {

    @Nonnull
    protected String formatInternal(@Nullable Object value) {
        try (final StringWriter writer = new StringWriter()) {
            format(value, writer);
            return writer.toString();
        } catch (final IOException e) {
            throw new RuntimeException("IOException in StringWriter?", e);
        }
    }

    @Nonnull
    @Override
    public String format(@Nullable Map<String, ?> values) {
        return formatInternal(values);
    }

    @Nonnull
    @Override
    public String format(@Nullable Iterable<?> values) {
        return formatInternal(values);
    }

    @Nonnull
    @Override
    public String format(@Nullable Object... values) {
        return formatInternal(values);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        final Locale locale = getLocale();
        if (locale != null && !isEmpty(locale.getLanguage())) {
            sb.append('(').append(locale).append(") ");
        }
        sb.append(getId()).append(": ").append(get());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || !(o instanceof Message)) {
            result = false;
        } else {
            final Message that = (Message) o;
            if (getId().equals(that.getId())) {
                final Locale locale = getLocale();
                if (locale != null ? locale.equals(that.getLocale()) : that.getLocale() == null) {
                    result = get().equals(that.get());
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final Locale locale = getLocale();
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + getId().hashCode();
        result = 31 * result + get().hashCode();
        return result;
    }

}
