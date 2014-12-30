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

import org.echocat.jomon.runtime.util.Value.Lazy;
import org.echocat.jomon.runtime.util.ValueProducer;
import org.echocat.locela.api.java.format.Formatter;
import org.echocat.locela.api.java.format.FormatterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import static java.lang.Boolean.TRUE;
import static java.util.Locale.US;
import static org.echocat.jomon.runtime.util.ProducingType.nonBlocking;
import static org.echocat.locela.api.java.format.MessageFormatter.messageFormatterFactory;

public abstract class FormattingMessageSupport extends MessageSupport {

    protected static final Locale DEFAULT_LOCALE = US;

    @Nonnull
    private final FormatterFactory<?> _formatterFactory;
    @Nonnull
    private final ValueProducer<Boolean, Formatter> _formatterProducer;
    @Nonnull
    private final Lazy<Boolean, Formatter> _formatter;

    public FormattingMessageSupport(@Nullable FormatterFactory<?> formatterFactory) {
        _formatterFactory = formatterFactory != null ? formatterFactory : messageFormatterFactory;
        _formatterProducer = new FormatterProducer();
        _formatter = new Lazy<>(TRUE, _formatterProducer, nonBlocking);
    }

    @Override
    public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
        formatter().format(value, to);
    }

    @Nonnull
    protected Formatter formatter() {
        return _formatter.getValue();
    }

    protected class FormatterProducer implements ValueProducer<Boolean, Formatter> {

        @Nullable
        @Override
        public Formatter produce(@Nullable Boolean key) throws Exception {
            final Locale locale = getLocale();
            return _formatterFactory.createBy(locale != null ? locale : DEFAULT_LOCALE, get(), _formatterFactory);
        }

    }

}
