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

import org.echocat.jomon.runtime.util.Value;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.echocat.jomon.runtime.CollectionUtils.asImmutableList;
import static org.echocat.locela.api.java.format.MessageFormatterFactory.messageFormatterFactory;

@ThreadSafe
public class MessageFormatter extends FormatterSupport implements Iterable<Formatter> {

    protected static final ThreadLocal<Value<Object>> CALLED_VALUE = new ThreadLocal<>();

    protected static void formatInternal(@Nonnull Locale locale, @Nonnull String pattern, @Nullable Object value, @Nonnull Writer to) throws IOException {
        messageFormatterFactory().createBy(locale, pattern).format(value, to);
    }

    @Nonnull
    protected static String formatInternal(@Nonnull Locale locale, @Nonnull String pattern, @Nullable Object value) {
        try (final StringWriter writer = new StringWriter()) {
            messageFormatterFactory().createBy(locale, pattern).format(value, writer);
            return writer.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void format(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull Writer to, @Nullable Object... values) throws IOException {
        formatInternal(locale, pattern, values, to);
    }

    @Nonnull
    public static String format(@Nonnull Locale locale, @Nonnull String pattern, @Nullable Object... values) {
        return formatInternal(locale, pattern, values);
    }

    public static void format(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull Writer to, @Nullable Iterable<?> values) throws IOException {
        formatInternal(locale, pattern, values, to);
    }

    @Nonnull
    public static String format(@Nonnull Locale locale, @Nonnull String pattern, @Nullable Iterable<?> values) {
        return formatInternal(locale, pattern, values);
    }

    public static void format(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull Writer to, @Nullable Map<?, ?> values) throws IOException {
        formatInternal(locale, pattern, values, to);
    }

    @Nonnull
    public static String format(@Nonnull Locale locale, @Nonnull String pattern, @Nullable Map<?, ?> values) {
        return formatInternal(locale, pattern, values);
    }

    @Nonnull
    private final List<Formatter> _subFormatter;

    public MessageFormatter(@Nonnull Locale locale, @Nullable String pattern) throws IllegalArgumentException {
        this(locale, pattern, messageFormatterFactory());
    }

    public MessageFormatter(@Nonnull Locale locale, @Nullable String pattern, @Nullable FormatterFactory<?>... factories) throws IllegalArgumentException {
        this(locale, pattern, new MessageFormatterFactory(factories));
    }

    protected MessageFormatter(@Nonnull Locale locale, @Nullable String pattern, @Nonnull MessageFormatterFactory root) throws IllegalArgumentException {
        super(locale);
        _subFormatter = parseSubFormatters(locale, pattern, root);
    }

    protected MessageFormatter(@Nonnull Locale locale, @Nullable List<Formatter> subFormatter) throws IllegalArgumentException {
        super(locale);
        _subFormatter = subFormatter != null ? asImmutableList(subFormatter) : Collections.<Formatter>emptyList();
    }

    @Override
    public Iterator<Formatter> iterator() {
        return getSubFormatter().iterator();
    }

    @Nonnull
    public List<Formatter> getSubFormatter() {
        return _subFormatter;
    }

    @Nonnull
    protected List<Formatter> parseSubFormatters(@Nonnull Locale locale, @Nullable String pattern, @Nonnull MessageFormatterFactory root) throws IllegalArgumentException {
        final List<Formatter> result = new ArrayList<>();
        if (pattern != null) {
            boolean inEscape = false;
            int depth = 0;
            final char[] chars = pattern.toCharArray();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                final char c = chars[i];
                if (c == '\'') {
                    if (i + 1 < chars.length && chars[i + 1] == '\'') {
                        sb.append('\'');
                        //noinspection AssignmentToForLoopParameter
                        i++;
                    } else {
                        inEscape = !inEscape;
                    }
                } else if (inEscape) {
                    sb.append(c);
                } else if (c == '{') {
                    if (depth == 0) {
                        result.add(new StaticFormatter(locale, sb.toString()));
                        sb.setLength(0);
                    } else {
                        sb.append('{');
                    }
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        result.add(parseSubFormatter(locale, sb.toString(), root));
                        sb.setLength(0);
                    } else {
                        sb.append('}');
                    }
                } else {
                    sb.append(c);
                }
            }
            if (depth > 0) {
                throw new IllegalArgumentException("Unmatched braces in the pattern.");
            }
            if (inEscape) {
                sb.append('\'');
            }
            if (sb.length() > 0) {
                result.add(new StaticFormatter(locale, sb.toString()));
            }
        }
        return asImmutableList(result);
    }

    @Nonnull
    protected Formatter parseSubFormatter(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull MessageFormatterFactory root) throws IllegalArgumentException {
        String parameter = null;
        String formatterId = null;
        String formatterPattern = null;
        final char[] chars = pattern.toCharArray();
        final StringBuilder sb = new StringBuilder();
        for (final char c : chars) {
            if (c == ',') {
                if (parameter == null) {
                    parameter = sb.toString();
                    sb.setLength(0);
                } else if (formatterId == null) {
                    formatterId = sb.toString();
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        if (parameter == null) {
            parameter = sb.toString();
        } else if (formatterId == null) {
            formatterId = sb.toString();
        } else {
            formatterPattern = sb.toString();
        }
        return getSubFormatterFor(locale, parameter, formatterId, formatterPattern, root);
    }

    @Nonnull
    protected Formatter getSubFormatterFor(@Nonnull Locale locale, @Nonnull String parameter, @Nullable String id, @Nullable String pattern, @Nonnull MessageFormatterFactory root) throws IllegalArgumentException {
        final Formatter formatter;
        if (id != null) {
            final FormatterFactory<?> factory = root.getFactoryBy(id);
            formatter = factory.createBy(locale, pattern, root);
        } else {
            formatter = new PassThruFormatter(locale);
        }
        return new ParameterAwareFormatter(locale, parameter, formatter);
    }

    @Override
    public void format(@Nullable final Object value, @Nonnull Writer to) throws IOException {
        final Value<Object> calledValue = CALLED_VALUE.get();
        final Object targetValue;
        if (calledValue == null) {
            // noinspection ConstantConditions
            CALLED_VALUE.set(new Value<Object>() {
                @Nonnull
                @Override
                public Object getValue() {
                    return value;
                }
            });
            targetValue = value;
        } else {
            targetValue = calledValue.getValue();
        }
        try {
            for (final Formatter formatter : this) {
                formatter.format(targetValue, to);
            }
        } finally {
            if (calledValue == null) {
                CALLED_VALUE.remove();
            }
        }
    }

    public void format(@Nonnull Writer to, @Nullable Object... values) throws IOException {
        format(values, to);
    }

    @Nonnull
    public String format(@Nullable Object... values) {
        return formatInternal(values);
    }

    public void format(@Nonnull Writer to, @Nullable Iterable<?> values) throws IOException {
        format(values, to);
    }

    @Nonnull
    public String format(@Nullable Iterable<?> values) {
        return formatInternal(values);
    }

    public void format(@Nonnull Writer to, @Nullable Map<?, ?> values) throws IOException {
        format(values, to);
    }

    @Nonnull
    public String format(@Nullable Map<?, ?> values) {
        return formatInternal(values);
    }

    @Nonnull
    protected String formatInternal(@Nullable Object value) {
        try (final StringWriter writer = new StringWriter()) {
            format(value, writer);
            return writer.toString();
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Formatter formatter : this) {
            sb.append(formatter);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || !(o instanceof MessageFormatter)) {
            result = false;
        } else {
            final MessageFormatter that = (MessageFormatter) o;
            result = getSubFormatter().equals(that.getSubFormatter());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getSubFormatter().hashCode();
    }

    @ThreadSafe
    protected static class PassThruFormatter extends FormatterSupport {

        public PassThruFormatter(@Nonnull Locale locale) {
            super(locale);
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            if (value != null) {
                to.write(value.toString());
            }
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else {
                result = !(o == null || getClass() != o.getClass());
            }
            return result;
        }

        @Override
        public int hashCode() {
            return 0;
        }

    }

    @ThreadSafe
    protected static class ParameterAwareFormatter extends FormatterSupport {

        @Nonnull
        private final String _parameter;
        @Nullable
        private final Integer _parameterAsInteger;
        @Nonnull
        private final Formatter _delegate;

        public ParameterAwareFormatter(@Nonnull Locale locale, @Nonnull String parameter, @Nonnull Formatter delegate) {
            super(locale);
            _parameter = parameter;
            _parameterAsInteger = tryGetAsInteger(parameter);
            _delegate = delegate;
        }

        @Nullable
        protected Integer tryGetAsInteger(@Nonnull String parameter) {
            Integer result;
            try {
                result = Integer.parseInt(parameter);
            } catch (final NumberFormatException ignored) {
                result = null;
            }
            return result;
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            _delegate.format(selectValueFrom(value), to);
        }

        @Nullable
        protected Object selectValueFrom(@Nullable Object input) {
            Object result;
            if (input == null) {
                result = null;
            } else if (input instanceof Map) {
                final Map<?, ?> map = (Map<?, ?>) input;
                result = map.get(_parameter);
                if (result == null && _parameterAsInteger != null) {
                    result = map.get(_parameterAsInteger);
                }
            } else if (input instanceof Object[] && _parameterAsInteger != null) {
                final Object[] array = (Object[]) input;
                result = array.length > _parameterAsInteger ? array[_parameterAsInteger] : null;
            } else if (input instanceof List && _parameterAsInteger != null) {
                final List<?> list = (List<?>) input;
                result = list.size() > _parameterAsInteger ? list.get(_parameterAsInteger) : null;
            } else {
                result = "0".equals(_parameter) ? input : null;
            }
            return result;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append('{').append(_parameter);
            final String delegateAsString = _delegate instanceof PassThruFormatter ? null : _delegate.toString();
            if (!isEmpty(delegateAsString)) {
                sb.append(',').append(delegateAsString);
            }
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else if (o == null || getClass() != o.getClass()) {
                result = false;
            } else {
                final ParameterAwareFormatter that = (ParameterAwareFormatter) o;
                result = _delegate.equals(that._delegate) && _parameter.equals(that._parameter);
            }
            return result;
        }

        @Override
        public int hashCode() {
            return 31 * _parameter.hashCode() + _delegate.hashCode();
        }
    }

    @ThreadSafe
    protected static class StaticFormatter extends FormatterSupport {

        @Nonnull
        private final String _content;

        public StaticFormatter(@Nonnull Locale locale, @Nonnull String content) {
            super(locale);
            _content = content;
        }

        @Override
        public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
            to.write(getContent());
        }

        @Nonnull
        public String getContent() {
            return _content;
        }

        @Override
        public String toString() {
            return getContent();
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else if (o == null || getClass() != o.getClass()) {
                result = false;
            } else {
                final StaticFormatter that = (StaticFormatter) o;
                result = _content.equals(that._content);
            }
            return result;
        }

        @Override
        public int hashCode() {
            return _content.hashCode();
        }

    }

}
