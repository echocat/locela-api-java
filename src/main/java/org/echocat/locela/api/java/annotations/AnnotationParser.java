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

package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.annotations.Annotation.Factory;
import org.echocat.locela.api.java.annotations.Annotation.Factory.Provider;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import java.util.ArrayList;
import java.util.List;

import static org.echocat.locela.api.java.annotations.StandardAnnotationFactoryProvider.annotaionFactoryProvider;

@ThreadSafe
public class AnnotationParser {

    private static final AnnotationParser INSTANCE = new AnnotationParser();

    @Nonnull
    public static AnnotationParser annotationParser() {
        return INSTANCE;
    }

    @Nonnull
    private final Annotation.Factory.Provider _annotationFactoryProvider;

    public AnnotationParser() {
        this(null);
    }

    public AnnotationParser(@Nullable Annotation.Factory.Provider provider) {
        _annotationFactoryProvider = provider != null ? provider : annotaionFactoryProvider();
    }

    @Nonnull
    public Annotation parse(@Nonnull String plain) throws IllegalArgumentException {
        final char[] chars = plain.toCharArray();
        final Extraction<String> id = extractId(chars, 0);
        final Extraction<List<Object>> plainObjects = extractObjects(chars, id.getEnd());
        final Factory<? extends Annotation> factory = annotationFactoryProvider().provideBy(id.getContent());
        return factory.createBy(plainObjects.getContent().toArray());
    }

    @Nonnull
    protected Extraction<String> extractId(@Nonnull char[] chars, @Nonnegative int begin) {
        final StringBuilder sb = new StringBuilder();
        boolean idFinished = false;
        boolean foundBracket = false;
        int i = begin;
        for (; i < chars.length; i++) {
            final char c = chars[i];
            if (c == ' ' || c == '\t') {
                if (sb.length() > 0) {
                    idFinished = true;
                }
            } else if (c == '(') {
                // noinspection UnusedAssignment
                idFinished = true;
                foundBracket = true;
                break;
            } else if (isValidIdCharacter(c)) {
                if (idFinished) {
                    throw new IllegalArgumentException("Unexpected character '" + c + "'.");
                }
                sb.append(c);
            } else {
                throw new IllegalArgumentException("Unexpected character '" + c + "'.");
            }
        }
        if (sb.length() <= 0) {
            throw new IllegalArgumentException("Unexpected end of annotation id.");
        }
        if (!foundBracket) {
            throw new IllegalArgumentException("Unexpected end of annotation id. Bracket missing");
        }
        return new Extraction<>(sb.toString(), i + 1);
    }

    @SuppressWarnings("OverlyLongMethod")
    @Nonnull
    protected Extraction<List<Object>> extractObjects(@Nonnull char[] chars, @Nonnegative int begin) {
        final List<Object> objects = new ArrayList<>();
        final StringBuilder sb = new StringBuilder();
        boolean inString = false;
        boolean expectString = false;
        boolean objectEnded = false;
        boolean foundClosingBracket = false;
        int i = begin;
        for (; i < chars.length; i++) {
            final char c = chars[i];
            if (foundClosingBracket && (c != ' ' && c != '\t')) {
                throw new IllegalArgumentException("Unexpected character after closing bracket: " + c);
            } else if (c == '"') {
                if (inString) {
                    inString = false;
                    objectEnded = true;
                } else {
                    if (sb.length() > 0) {
                        throw new IllegalArgumentException("Unexpected begin of string at position " + i + " after recorded '" + sb + "'.");
                    }
                    inString = true;
                    expectString = true;
                }
            } else if (c == '\\') {
                if (i + 1 < chars.length) {
                    i++;
                    final char nc = chars[i];
                    if (nc == 'n') {
                        sb.append('\n');
                    } else if (nc == 'r') {
                        sb.append('\r');
                    } else if (nc == 't') {
                        sb.append('\t');
                    } else {
                        sb.append(nc);
                    }
                } else {
                    throw new IllegalArgumentException("Unexpected end of annotation pattern after escape: \\");
                }
            } else if (inString) {
                sb.append(c);
            } else if (c == ' ' || c == '\t') {
                if (sb.length() > 0) {
                    objectEnded = true;
                }
            } else if (c == ',') {
                objects.add(evalObject(sb.toString(), expectString));
                sb.setLength(0);
                objectEnded = false;
                expectString = false;
            } else if (c == ')') {
                if (!objects.isEmpty() || sb.length() > 0 || expectString) {
                    objects.add(evalObject(sb.toString(), expectString));
                }
                foundClosingBracket = true;
            } else {
                if (objectEnded) {
                    throw new IllegalArgumentException("Unexpected character after end of literal (" + sb + "): " + c);
                }
                sb.append(c);
            }
        }
        if (!foundClosingBracket) {
            throw new IllegalArgumentException("Could not find closing bracket.");
        }
        return new Extraction<>(objects, i);
    }

    @Nullable
    private Object evalObject(@Nonnull String plain, boolean expectString) {
        final Object result;
        if (expectString) {
            result = plain;
        } else if (plain.isEmpty()) {
            throw new IllegalArgumentException("Missing literal.");
        } else if ("null".equalsIgnoreCase(plain)) {
            result = null;
        } else if ("true".equalsIgnoreCase(plain) || "on".equalsIgnoreCase(plain) || "yes".equalsIgnoreCase(plain)) {
            result = true;
        } else if ("false".equalsIgnoreCase(plain) || "off".equalsIgnoreCase(plain) || "no".equalsIgnoreCase(plain)) {
            result = false;
        } else {
            try {
                result = Double.valueOf(plain);
            } catch (final NumberFormatException ignored) {
                throw new IllegalArgumentException("No valid literal: " + plain);
            }
        }
        return result;
    }

    @Nonnull
    protected Provider annotationFactoryProvider() {
        return _annotationFactoryProvider;
    }

    protected boolean isValidIdCharacter(char c) {
        return (c >= 'a' && c <= 'z')
            || (c >= 'A' && c <= 'Z')
            || (c >= '0' && c <= '9')
            || c == '-'
            || c == '_'
            || c == '.'
            ;
    }

    protected static class Extraction<T> {

        @Nonnull
        private final T _content;
        @Nonnegative
        private final int _end;

        public Extraction(@Nonnull T content, int end) {
            _content = content;
            _end = end;
        }

        @Nonnull
        public T getContent() {
            return _content;
        }

        @Nonnegative
        public int getEnd() {
            return _end;
        }

    }

}
