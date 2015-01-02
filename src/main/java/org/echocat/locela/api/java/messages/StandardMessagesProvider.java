/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.messages;

import org.echocat.jomon.runtime.StringUtils;
import org.echocat.locela.api.java.format.FormatterFactory;
import org.echocat.locela.api.java.properties.Properties;
import org.echocat.locela.api.java.properties.PropertiesReader;
import org.echocat.locela.api.java.properties.StandardPropertiesReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.util.Locale;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.io.FilenameUtils.removeExtension;
import static org.echocat.locela.api.java.format.MessageFormatterFactory.messageFormatterFactory;

public class StandardMessagesProvider extends MessagesProviderSupport {

    private static final StandardMessagesProvider INSTANCE = new StandardMessagesProvider();

    @Nonnull
    public static StandardMessagesProvider messagesProvider() {
        return INSTANCE;
    }

    @Nonnull
    private final PropertiesReader _propertiesReader;
    @Nonnull
    private final FormatterFactory<?> _formatterFactory;

    public StandardMessagesProvider() {
        this(null, null);
    }

    public StandardMessagesProvider(@Nullable PropertiesReader propertiesReader, @Nonnull FormatterFactory<?> formatterFactory) {
        _propertiesReader = propertiesReader != null ? propertiesReader : StandardPropertiesReader.propertiesReader();
        _formatterFactory = formatterFactory != null ? formatterFactory : messageFormatterFactory();
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
        final String file = buildFileFrom(locale, baseFile);
        final Messages result;
        try (final Reader reader = accessor.open(file)) {
            if (reader != null) {
                final Properties<String> properties = propertiesReader().read(reader);
                result = new PropertiesMessages(locale, properties, formatterFactory());
            } else {
                result = null;
            }
        }
        return result;
    }

    @Nonnull
    protected String buildFileFrom(@Nullable Locale locale, @Nonnull String baseFile) {
        final String prefix = removeExtension(baseFile);
        final String suffix = getExtension(baseFile);
        final StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (!isEmpty(locale)) {
            sb.append('_').append(locale);
        }
        if (!StringUtils.isEmpty(suffix)) {
            sb.append('.').append(suffix);
        }
        return sb.toString();
    }

    protected boolean isEmpty(@Nullable Locale locale) {
        final boolean result;
        if (locale == null) {
            result = true;
        } else {
            result = StringUtils.isEmpty(locale.getLanguage()) && StringUtils.isEmpty(locale.getCountry()) && StringUtils.isEmpty(locale.getVariant());
        }
        return result;
    }

    @Nonnull
    protected PropertiesReader propertiesReader() {
        return _propertiesReader;
    }

    @Nonnull
    protected FormatterFactory<?> formatterFactory() {
        return _formatterFactory;
    }

}
