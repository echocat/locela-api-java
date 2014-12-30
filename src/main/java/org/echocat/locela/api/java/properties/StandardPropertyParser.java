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

package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import static org.echocat.locela.api.java.properties.StandardProperty.property;

@ThreadSafe
public class StandardPropertyParser implements PropertyParser {

    private static final StandardPropertyParser INSTANCE = new StandardPropertyParser();

    @Nonnull
    public static StandardPropertyParser propertyParser() {
        return INSTANCE;
    }

    @Nonnull
    @Override
    public Property<String> parse(@Nonnull String plain, @Nullable Iterable<Annotation> annotations) {
        final char[] characters = plain.toCharArray();
        final Extraction id = extractId(characters);
        final Extraction content = extractContent(characters, id.getEnd());
        final Property<String> property = createPropertyBy(annotations, id.getContent(), content.getContent());
        return property;
    }

    @Nonnull
    protected Property<String> createPropertyBy(@Nullable Iterable<Annotation> annotations, @Nonnull String id, @Nullable String content) {
        final Property<String> property = property(id, String.class).withAnnotations(annotations);
        property.set(content);
        return property;
    }

    @Nonnull
    protected Extraction extractId(@Nonnull char[] characters) {
        boolean started = false;
        boolean inEscape = false;
        boolean finished = false;
        int lastCharacterAtPosition = 0;
        int i;
        final StringBuilder sb = new StringBuilder();
        for (i = 0; !finished && i < characters.length; i++) {
            final char c = characters[i];
            if (inEscape) {
                i += handleEscapeCharacter(characters, i, sb);
                inEscape = false;
                started = true;
                lastCharacterAtPosition = sb.length();
            } else if (c == ' ' || c == '\t') {
                if (started) {
                    finished = true;
                }
            } else if (c == '\\') {
                inEscape = true;
            } else if (c == '=' || c == ':') {
                finished = true;
            } else {
                started = true;
                sb.append(c);
                lastCharacterAtPosition = sb.length();
            }
        }
        sb.setLength(lastCharacterAtPosition);
        for (int j = i; j < characters.length; j++) {
            final char c = characters[j];
            if (c == ' ' || c == '\t' || c == '=' || c == ':') {
                i++;
            } else {
                break;
            }
        }
        return new Extraction(sb.toString(), i);
    }

    @Nonnull
    protected Extraction extractContent(@Nonnull char[] characters, @Nonnegative int begin) {
        boolean started = false;
        boolean inEscape = false;
        int lastCharacterAtPosition = 0;
        int i;
        final StringBuilder sb = new StringBuilder();
        for (i = begin; i < characters.length; i++) {
            final char c = characters[i];
            if (inEscape) {
                i += handleEscapeCharacter(characters, i, sb);
                started = true;
                lastCharacterAtPosition = sb.length();
            } else if (c == ' ' || c == '\t') {
                if (started) {
                    sb.append(c);
                }
            } else if (c == '\\') {
                inEscape = true;
            } else {
                started = true;
                sb.append(c);
                lastCharacterAtPosition = sb.length();
            }
        }
        sb.setLength(lastCharacterAtPosition);
        return new Extraction(sb.toString(), i);
    }

    @Nonnegative
    private int handleEscapeCharacter(@Nonnull char[] characters, @Nonnegative int at, @Nonnull StringBuilder addTo) {
        final char original = characters[at];
        final int skippedCharacters;
        if (original == 'n') {
            addTo.append('\n');
            skippedCharacters = 0;
        } else if (original == 'r') {
            addTo.append('\r');
            skippedCharacters = 0;
        } else if (original == 't') {
            addTo.append('\t');
            skippedCharacters = 0;
        } else if (original == 'f') {
            addTo.append('\f');
            skippedCharacters = 0;
        } else if (original == 'u') {
            if (characters.length > at + 4) {
                int value = 0;
                for (int i = 0; i < 4; i++) {
                    final char c = characters[at + i + 1];
                    if (c >= '0' && c <= '9') {
                        value = (value << 4) + (c - '0');
                    } else if (c >= 'a' && c <= 'f') {
                        value = (value << 4) + 10 + (c - 'a');
                    } else if (c >= 'A' && c <= 'F') {
                        value = (value << 4) + 10 + (c - 'A');
                    } else {
                        throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                    }
                }
                addTo.append((char) value);
                skippedCharacters = 4;
            } else {
                addTo.append(original);
                skippedCharacters = 0;
            }
        } else {
            addTo.append(original);
            skippedCharacters = 0;
        }
        return skippedCharacters;
    }

    protected static class Extraction {

        @Nonnull
        private final String _content;
        @Nonnegative
        private final int _end;

        public Extraction(@Nonnull String content, @Nonnegative int end) {
            _content = content;
            _end = end;
        }

        @Nonnull
        public String getContent() {
            return _content;
        }

        @Nonnegative
        public int getEnd() {
            return _end;
        }

    }



}
