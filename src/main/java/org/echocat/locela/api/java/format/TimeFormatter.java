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

package org.echocat.locela.api.java.format;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.format.TimeFormatter.Pattern.DEFAULT;
import static java.text.DateFormat.getTimeInstance;
import static org.echocat.locela.api.java.utils.StringUtils.isEmpty;

@ThreadSafe
public class TimeFormatter extends FormatterSupport {

    private static final FormatterFactory<TimeFormatter> FACTORY_INSTANCE = new Factory();

    @Nonnull
    public static FormatterFactory<TimeFormatter> timeFormatterFactory() {
        return FACTORY_INSTANCE;
    }

    @Nonnull
    private final DateFormat _format;
    @Nonnull
    private final String _plainFormat;

    public TimeFormatter(@Nonnull Locale locale, @Nullable DateFormat format) {
        super(locale);
        if (format != null) {
            _format = format;
            _plainFormat = "<raw>";
        } else {
            _format = DEFAULT.toFormat(locale);
            _plainFormat = DEFAULT.toString();
        }
    }

    public TimeFormatter(@Nonnull Locale locale, @Nullable Pattern pattern) {
        super(locale);
        final Pattern nonNullPattern = pattern != null ? pattern : DEFAULT;
        _format = nonNullPattern.toFormat(locale);
        _plainFormat = nonNullPattern.toString();
    }

    public TimeFormatter(@Nonnull Locale locale, @Nullable String pattern) {
        super(locale);
        _format = evaluate(locale, pattern);
        _plainFormat = isEmpty(pattern) ? DEFAULT.toString() : pattern;
    }

    @Override
    public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
        if (value != null) {
            to.write(_format.format(value));
        }
    }

    @Nonnull
    protected DateFormat evaluate(@Nonnull Locale locale, @Nullable String pattern) {
        final DateFormat result;
        if (isEmpty(pattern)) {
            result = DEFAULT.toFormat(locale);
        } else {
            final Pattern predefined = Pattern.NAME_TO_PATTERN.get(pattern);
            if (predefined != null) {
                result = predefined.toFormat(locale);
            } else {
                result = new SimpleDateFormat(pattern, locale);
            }
        }
        return result;
    }

    @Nonnull
    protected DateFormat getFormat() {
        return _format;
    }

    @Override
    public String toString() {
        return "time," + _plainFormat;
    }

    public enum Pattern {
        DEFAULT("default", DateFormat.DEFAULT),
        SHORT("short", DateFormat.SHORT),
        MEDIUM("medium", DateFormat.MEDIUM),
        LONG("long", DateFormat.LONG),
        FULL("full", DateFormat.FULL);

        private static final Map<String, Pattern> NAME_TO_PATTERN = createNameToPattern();

        @Nonnegative
        private final int _dateFormatValue;
        @Nonnull
        private final String _id;

        Pattern(@Nonnull String id, @Nonnegative int dateFormatValue) {
            _id = id;
            _dateFormatValue = dateFormatValue;
        }

        @Override
        public String toString() {
            return getId();
        }

        @Nonnull
        private static Map<String, Pattern> createNameToPattern() {
            final Pattern[] values = values();
            final Map<String, Pattern> result = new HashMap<>(values.length, 1.0f);
            for (final Pattern pattern : values) {
                result.put(pattern.getId(), pattern);
            }
            return result;
        }

        @Nonnull
        public DateFormat toFormat(@Nonnull Locale locale) {
            return getTimeInstance(_dateFormatValue, locale);
        }

        @Nonnull
        public String getId() {
            return _id;
        }

    }

    @ThreadSafe
    protected static class Factory implements FormatterFactory<TimeFormatter> {

        @Nonnull
        @Override
        public String getId() {
            return "time";
        }

        @Nonnull
        @Override
        public TimeFormatter createBy(@Nullable Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root) {
            return new TimeFormatter(locale != null ? locale : US, pattern);
        }

    }

}
