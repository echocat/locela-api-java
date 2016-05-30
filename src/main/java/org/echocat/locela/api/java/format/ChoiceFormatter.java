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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.format.ChoiceFormatter.Operator.*;

@ThreadSafe
public class ChoiceFormatter extends FormatterSupport {

    private static final FormatterFactory<ChoiceFormatter> FACTORY_INSTANCE = new Factory();

    @Nonnull
    public static FormatterFactory<ChoiceFormatter> choiceFormatterFactory() {
        return FACTORY_INSTANCE;
    }

    @Nonnull
    private final List<Condition> _conditions;

    public ChoiceFormatter(@Nonnull Locale locale, @Nullable List<Condition> conditions) {
        super(locale);
        _conditions = conditions != null ? conditions : Collections.<Condition>emptyList();
    }

    public ChoiceFormatter(@Nonnull Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root) {
        super(locale);
        _conditions = parse(pattern, root);
    }

    @Nonnull
    protected List<Condition> parse(@Nullable String pattern, @Nonnull FormatterFactory<?> root) {
        final List<Condition> conditions = new ArrayList<>();
        final char[] chars = pattern != null ? pattern.toCharArray() : new char[0];

        String test = null;
        Operator operator = null;
        for (int i = 0; i < chars.length; i++) {
            if (test == null) {
                final ExtractionWithOperator testAndOperator = extractTestAndOperator(chars, i);
                test = testAndOperator.getContent();
                operator = testAndOperator.getOperator();

                // noinspection AssignmentToForLoopParameter
                i = testAndOperator.getEnd();
            } else {
                final Extraction extraction = extractPattern(chars, i);
                final String subPattern = extraction.getContent();

                final Formatter subFormat = root.createBy(getLocale(), subPattern, root);
                final Condition condition = new Condition(test, operator, subFormat);
                conditions.add(condition);

                // noinspection AssignmentToForLoopParameter
                i = extraction.getEnd();
                test = null;
                operator = null;
            }
        }
        if (test != null) {
            throw new IllegalArgumentException("Unexpected end of pattern: " + pattern);
        }

        return conditions;
    }

    @Nonnull
    protected ExtractionWithOperator extractTestAndOperator(@Nonnull char[] chars, @Nonnegative int begin) throws IllegalArgumentException {
        int end = begin;
        boolean inEscape = false;
        final StringBuilder sb = new StringBuilder();
        Operator operator = null;
        for (int i = begin; operator == null && i < chars.length; i++) {
            final char c = chars[i];
            if (c == '\'') {
                if (i + 1 <= chars.length && chars[i + 1] == '\'') {
                    sb.append('\'');
                    //noinspection AssignmentToForLoopParameter
                    i++;
                } else {
                    inEscape = !inEscape;
                }
            } else if (inEscape) {
                sb.append(c);
            } else {
                if (c == '|') {
                    throw new IllegalArgumentException("Unexpected separation character ' of pattern: " + new String(chars, begin, chars.length - begin));
                }
                operator = findOperatorFor(c);
                if (operator != null) {
                    end = i;
                } else {
                    sb.append(c);
                }
            }
        }
        if (operator == null) {
            throw new IllegalArgumentException("Unexpected end of pattern: " + new String(chars, begin, chars.length - begin));
        }
        return new ExtractionWithOperator(sb.toString(), begin, end, operator);
    }

    @Nonnull
    protected Extraction extractPattern(@Nonnull char[] chars, @Nonnegative int begin) throws IllegalArgumentException {
        int i;
        boolean inEscape = false;
        final StringBuilder sb = new StringBuilder();
        for (i = begin; i < chars.length; i++) {
            final char c = chars[i];
            if (c == '\'') {
                if (i + 1 <= chars.length && chars[i + 1] == '\'') {
                    sb.append('\'');
                    //noinspection AssignmentToForLoopParameter
                    i++;
                } else {
                    inEscape = !inEscape;
                }
            } else if (inEscape) {
                sb.append(c);
            } else if (c == '|') {
                break;
            } else {
                sb.append(c);
            }
        }
        return new Extraction(sb.toString(), begin, i);
    }

    @Override
    public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
        for (final Condition condition : _conditions) {
            if (condition.apply(value)) {
                condition.getSubFormat().format(value, to);
                break;
            }
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("choice,");
        boolean first = true;
        for (final Condition condition : _conditions) {
            if (first) {
                first = false;
            } else {
                sb.append('|');
            }
            sb.append(condition);
        }
        return sb.toString();
    }


    @ThreadSafe
    public static class Condition {

        @Nonnull
        private final String _test;
        @Nullable
        private final Double _testAsDouble;
        private final boolean _testAsBoolean;
        @Nonnull
        private final Operator _operator;
        @Nonnull
        private final Formatter _subFormat;

        public Condition(@Nonnull String test, @Nonnull Operator operator, @Nonnull Formatter subFormat) {
            _test = test;
            _operator = operator;
            _subFormat = subFormat;
            _testAsDouble = toDouble(test);
            _testAsBoolean = toBoolean(test);
        }

        @Nullable
        private Double toDouble(@Nonnull String test) {
            Double result;
            try {
                result = Double.valueOf(test);
            } catch (final NumberFormatException ignored) {
                result = "".equals(test) || "null".equals(test) ? 0D : null;
            }
            return result;
        }

        private boolean toBoolean(@Nonnull String test) {
            return "true".equalsIgnoreCase(test) || "on".equalsIgnoreCase(test) || "1".equals(test);
        }

        @Nonnull
        public Object getTest() {
            return _test;
        }

        @Nonnull
        public Operator getOperator() {
            return _operator;
        }

        @Nonnull
        public Formatter getSubFormat() {
            return _subFormat;
        }

        public boolean apply(@Nullable Object value) {
            final boolean result;
            if (value == null) {
                result = applyNullValue();
            } else if (value instanceof Number) {
                result = applyNumberValue((Number) value);
            } else if (value instanceof Boolean) {
                result = applyBooleanValue((Boolean) value);
            } else if (value instanceof String) {
                result = applyStringValue(value.toString());
            } else {
                result = false;
            }
            return result;
        }

        protected boolean applyBooleanValue(@Nonnull Boolean value) {
            final boolean result;
            if (_operator == equals) {
                result = _testAsBoolean == value;
            } else {
                throw new UnsupportedOperationException("Operator " + _operator + " is not supported.");
            }
            return result;
        }

        protected boolean applyNumberValue(@Nonnull Number value) {
            final boolean result;
            if (_testAsDouble == null) {
                result = false;
            } else if (_operator == equals) {
                result = _testAsDouble == value.doubleValue();
            } else if (_operator == greaterThan) {
                result = _testAsDouble < value.doubleValue();
            } else {
                throw new UnsupportedOperationException("Operator " + _operator + " is not supported.");
            }
            return result;
        }

        protected boolean applyStringValue(@Nonnull String value) {
            final boolean result;
            if (_operator == equals) {
                result = _test.equals(value);
            } else if (_operator == greaterThan) {
                result = _test.compareTo(value) < 0;
            } else {
                throw new UnsupportedOperationException("Operator " + _operator + " is not supported.");
            }
            return result;
        }

        protected boolean applyNullValue() {
            return _test.equals("0") || _test.equals("") || _test.equals("null");
        }

        @Override
        public String toString() {
            return _test + _operator + _subFormat;
        }
    }

    public enum Operator {
        equals('#'),
        greaterThan('<');

        private final char _character;

        Operator(char character) {
            _character = character;
        }

        public char getCharacter() {
            return _character;
        }

        @Override
        public String toString() {
            return Character.toString(getCharacter());
        }

        @Nullable
        public static Operator findOperatorFor(char c) {
            Operator result = null;
            for (final Operator candidate :values()) {
                if (c == candidate.getCharacter()) {
                    result = candidate;
                    break;
                }
            }
            return result;
        }

    }

    @ThreadSafe
    protected static class Factory implements FormatterFactory<ChoiceFormatter> {

        @Nonnull
        @Override
        public String getId() {
            return "choice";
        }

        @Nonnull
        @Override
        public ChoiceFormatter createBy(@Nullable Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root) {
            return new ChoiceFormatter(locale != null ? locale : US, pattern, root);
        }

    }

    @ThreadSafe
    protected static class Extraction {

        @Nonnull
        private final String _content;
        @Nonnegative
        private final int _begin;
        @Nonnegative
        private final int _end;

        public Extraction(@Nonnull String content, int begin, int end) {
            _content = content;
            _begin = begin;
            _end = end;
        }

        @Nonnull
        public String getContent() {
            return _content;
        }

        @Nonnegative
        public int getBegin() {
            return _begin;
        }

        @Nonnegative
        public int getEnd() {
            return _end;
        }

    }

    protected static class ExtractionWithOperator extends Extraction {

        @Nonnull
        private final Operator _operator;

        public ExtractionWithOperator(@Nonnull String content, @Nonnegative int begin, @Nonnegative int end, @Nonnull Operator operator) {
            super(content, begin, end);
            _operator = operator;
        }

        @Nonnull
        public Operator getOperator() {
            return _operator;
        }
    }



}
