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

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public interface Line {

    public void write(@Nonnull Writer to) throws IOException;

    @ThreadSafe
    public abstract static class LineSupport implements Line {

        @Override
        public String toString() {
            try(final StringWriter writer = new StringWriter()) {
                write(writer);
                return writer.toString();
            } catch (final IOException e) {
                throw new RuntimeException("Problems while write to StringWriter?", e);
            }
        }

    }

    @ThreadSafe
    public abstract static class LineWithContent extends LineSupport {

        @Nonnull
        private final String _content;

        public LineWithContent(@Nonnull String content) {
            _content = content;
        }

        @Nonnull
        public String getContent() {
            return _content;
        }

    }


    @ThreadSafe
    public static class PropertyLine extends LineWithContent {

        public PropertyLine(@Nonnull String content) {
            super(content);
        }

        @Override
        public void write(@Nonnull Writer to) throws IOException {
            to.write(getContent());
            to.write('\n');
        }

    }

    @ThreadSafe
    public static class CommentLine extends LineWithContent {

        private static final char[] PREFIX = {'#', ' '};

        public CommentLine(@Nonnull String content) {
            super(content);
        }

        @Override
        public void write(@Nonnull Writer to) throws IOException {
            final char[] chars = getContent().trim().toCharArray();
            if (chars.length > 0) {
                to.write(PREFIX);
                char lastNewLineCharacter = 0;
                for (final char c : chars) {
                    if (lastNewLineCharacter != 0 && (lastNewLineCharacter == c || (c != '\r' && c != '\n'))) {
                        to.write(PREFIX);
                        lastNewLineCharacter = 0;
                    }
                    to.write(c);
                    if (lastNewLineCharacter == 0 && (c == '\r' || c == '\n')) {
                        lastNewLineCharacter = c;
                    }
                }
                to.write('\n');
            }
        }

    }

    @ThreadSafe
    public static class EmptyLine extends LineSupport {

        @Override
        public void write(@Nonnull Writer to) throws IOException {}

    }

}
