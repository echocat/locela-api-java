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

import org.echocat.locela.api.java.format.FormatterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public class StandardMessage extends FormattingMessageSupport {

    @Nonnull
    public static StandardMessage message(@Nullable Locale locale, @Nonnull String id, @Nonnull String content, @Nullable FormatterFactory<?> formatterFactory) {
        return new StandardMessage(locale, id, content, formatterFactory);
    }

    @Nonnull
    public static StandardMessage message(@Nullable Locale locale, @Nonnull String id, @Nonnull String content) {
        return message(locale, id, content, null);
    }

    @Nonnull
    public static StandardMessage message(@Nonnull String id, @Nonnull String content) {
        return message(null, id, content);
    }

    @Nullable
    private final Locale _locale;
    @Nonnull
    private final String _id;
    @Nonnull
    private final String _content;

    public StandardMessage(@Nullable Locale locale, @Nonnull String id, @Nonnull String content, @Nullable FormatterFactory<?> formatterFactory) {
        super(formatterFactory);
        _locale = locale;
        _id = id;
        _content = content;
    }

    @Nonnull
    @Override
    public String get() {
        return _content;
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return _locale;
    }

    @Nullable
    @Override
    public String getId() {
        return _id;
    }

}
