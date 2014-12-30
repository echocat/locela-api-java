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

import org.echocat.locela.api.java.properties.Line.CommentLine;
import org.echocat.locela.api.java.properties.Line.EmptyLine;
import org.echocat.locela.api.java.properties.Line.PropertyLine;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.Reader;

@ThreadSafe
public class StandardPropertiesLineReader implements PropertiesLineReader {

    @Nonnull
    private final Reader _delegate;
    @Nonnull
    private final char[] _buffer;
    @Nonnegative
    private int _bufferPosition;
    @Nonnegative
    private int _bufferSize;

    public StandardPropertiesLineReader(@Nonnull Reader delegate) {
        this(delegate, 4096);
    }

    public StandardPropertiesLineReader(@Nonnull Reader delegate, @Nonnegative int bufferSize) {
        _delegate = delegate;
        _buffer = new char[bufferSize];
    }

    @SuppressWarnings("OverlyLongMethod")
    @Override
    @Nullable
    public Line read() throws IOException {
        boolean atBeginOfLine = true;
        Line line = null;
        Character c = next();
        int lasLengthWithCharactersAtTheEnd = 0;
        final StringBuilder sb = new StringBuilder();
        boolean commentLine = false;
        boolean lastWasEscape = false;
        boolean continuedLine = false;
        while (c != null && line == null) {
            if (c == ' ' || c == '\t') {
                if (lastWasEscape) {
                    sb.append('\\');
                    lasLengthWithCharactersAtTheEnd = sb.length();
                    lastWasEscape = false;
                }
                if (!atBeginOfLine) {
                    sb.append(c);
                }
            } else if (c == '\r' || c == '\n') {
                if (lastWasEscape) {
                    atBeginOfLine = true;
                    continuedLine = true;
                } else {
                    sb.setLength(lasLengthWithCharactersAtTheEnd);
                    line = toLine(sb, commentLine);
                }
                lastWasEscape = false;
            } else if (atBeginOfLine) {
                if ((c == '!' || c == '#') && !continuedLine) {
                    commentLine = true;
                } else {
                    sb.append(c);
                    lasLengthWithCharactersAtTheEnd = sb.length();
                }
                atBeginOfLine = false;
                continuedLine = false;
            } else if (c == '\\') {
                if (lastWasEscape) {
                    sb.append(c);
                    lasLengthWithCharactersAtTheEnd = sb.length();
                    lastWasEscape = false;
                } else {
                    lastWasEscape = true;
                }
            } else {
                if (lastWasEscape) {
                    sb.append('\\');
                    lastWasEscape = false;
                }
                sb.append(c);
                lasLengthWithCharactersAtTheEnd = sb.length();
            }
            if (line == null) {
                c = next();
            }
        }
        if (line == null) {
            if (lastWasEscape) {
                sb.append('\\');
            }
            sb.setLength(lasLengthWithCharactersAtTheEnd);
            if (sb.length() > 0) {
                line = toLine(sb, commentLine);
            }
        }
        return line;
    }

    @Nonnull
    protected Line toLine(@Nonnull StringBuilder sb, boolean commentLine) {
        final Line result;
        if (commentLine) {
            result = new CommentLine(sb.toString());
        } else if (sb.length() > 0) {
            result = new PropertyLine(sb.toString());
        } else {
            result = new EmptyLine();
        }
        return result;
    }

    @Nullable
    protected Character next() throws IOException {
        while (_bufferPosition >= _bufferSize && _bufferSize > -1) {
            _bufferSize = _delegate.read(_buffer);
            _bufferPosition = 0;
        }
        return _bufferSize > -1 ? _buffer[_bufferPosition++] : null;
    }

    @Override
    public void close() throws IOException {
        _delegate.close();
    }

}
