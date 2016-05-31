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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.DEFAULT;
import static java.text.NumberFormat.getCurrencyInstance;
import static java.text.NumberFormat.getIntegerInstance;
import static java.text.NumberFormat.getPercentInstance;
import static org.echocat.locela.api.java.utils.StringUtils.isEmpty;

@ThreadSafe
public class NumberFormatter extends FormatterSupport {

    private static final FormatterFactory<NumberFormatter> FACTORY_INSTANCE = new Factory();

    @Nonnull
    public static FormatterFactory<NumberFormatter> numberFormatterFactory() {
        return FACTORY_INSTANCE;
    }

    @Nonnull
    private final NumberFormat _format;
    @Nullable
    private final String _plainFormat;

    public NumberFormatter(@Nonnull Locale locale, @Nullable NumberFormat format) {
        super(locale);
        if (format != null) {
            _format = format;
            _plainFormat = "<raw>";
        } else {
            _format = DEFAULT.toFormat(locale);
            _plainFormat = DEFAULT.toString();
        }
    }

    public NumberFormatter(@Nonnull Locale locale, @Nullable Pattern pattern) {
        super(locale);
        _format = pattern != null ? pattern.toFormat(locale) : DEFAULT.toFormat(locale);
        _plainFormat = pattern != null ? pattern.getId() : DEFAULT.getId();
    }

    public NumberFormatter(@Nonnull Locale locale, @Nullable String pattern) {
        super(locale);
        final FormatAndPattern formatAndPattern = evaluate(locale, pattern);
        _plainFormat = formatAndPattern.getPattern();
        _format = formatAndPattern.getFormat();
    }

    @Override
    public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
        to.write(_format.format(value != null ? value : 0));
    }

    @Nonnull
    protected FormatAndPattern evaluate(@Nonnull Locale locale, @Nullable String pattern) {
        final FormatAndPattern result;
        if (isEmpty(pattern)) {
            result = new FormatAndPattern(DEFAULT.toFormat(locale), DEFAULT.getId());
        } else {
            final Pattern predefined = Pattern.NAME_TO_PATTERN.get(pattern);
            if (predefined != null) {
                result = new FormatAndPattern(predefined.toFormat(locale), predefined.getId());
            } else {
                result = new FormatAndPattern(new DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale)), pattern);
            }
        }
        return result;
    }

    @Nonnull
    protected NumberFormat getFormat() {
        return _format;
    }

    @ThreadSafe
    protected static class FormatAndPattern {
        @Nonnull
        private final NumberFormat _format;
        @Nonnull
        private final String _pattern;

        public FormatAndPattern(@Nonnull NumberFormat format, @Nonnull String pattern) {
            _format = format;
            _pattern = pattern;
        }

        @Nonnull
        public NumberFormat getFormat() {
            return _format;
        }

        @Nonnull
        public String getPattern() {
            return _pattern;
        }
    }

    @Override
    public String toString() {
        return "number," + _plainFormat;
    }

    public enum Pattern {
        DEFAULT("default") {
            @Override
            public NumberFormat toFormat(@Nonnull Locale locale) {
                return NumberFormat.getInstance(locale);
            }
        },
        INTEGER("integer") {
            @Override
            public NumberFormat toFormat(@Nonnull Locale locale) {
                return getIntegerInstance(locale);
            }
        },
        CURRENCY("currency") {
            @Override
            public NumberFormat toFormat(@Nonnull Locale locale) {
                return getCurrencyInstance(locale);
            }
        },
        PERCENT("percent") {
            @Override
            public NumberFormat toFormat(@Nonnull Locale locale) {
                return getPercentInstance(locale);
            }
        };

        private static final Map<String, Pattern> NAME_TO_PATTERN = createNameToPattern();

        @Nonnull
        private final String _id;

        Pattern(@Nonnull String id) {
            _id = id;
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
        public String getId() {
            return _id;
        }

        public abstract NumberFormat toFormat(@Nonnull Locale locale);
    }

    @ThreadSafe
    protected static class Factory implements FormatterFactory<NumberFormatter> {

        @Nonnull
        @Override
        public String getId() {
            return "number";
        }

        @Nonnull
        @Override
        public NumberFormatter createBy(@Nullable Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root) {
            return new NumberFormatter(locale != null ? locale : US, pattern);
        }

    }

}
