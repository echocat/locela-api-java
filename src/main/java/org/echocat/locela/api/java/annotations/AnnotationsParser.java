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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.ArrayList;
import java.util.List;

import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableList;

@ThreadSafe
public class AnnotationsParser {

    private static final AnnotationsParser INSTANCE = new AnnotationsParser();

    @Nonnull
    public static AnnotationsParser annotationsParser() {
        return INSTANCE;
    }

    @Nonnull
    private final AnnotationParser _annotaionParser;

    public AnnotationsParser() {
        this(null);
    }

    public AnnotationsParser(@Nullable AnnotationParser annotaionParser) {
        _annotaionParser = annotaionParser != null ? annotaionParser : AnnotationParser.annotationParser();
    }

    @SuppressWarnings("OverlyLongMethod")
    @Nonnull
    public Iterable<Annotation> parse(@Nullable String plain) {
        final List<Annotation> result = new ArrayList<>();
        final StringBuilder comment = new StringBuilder();
        final StringBuilder sb = new StringBuilder();
        final char[] chars = plain.toCharArray();

        boolean lineBeginning = true;
        boolean atFound = false;
        for (int i = 0; i < chars.length; i++) {
            final char c = chars[i];
            if (c == ' ' || c == '\t') {
                if (!lineBeginning) {
                    if (atFound) {
                        sb.append(c);
                    } else {
                        comment.append(c);
                    }
                }
            } else if (c == '\r' || c == '\n') {
                if (!lineBeginning) {
                    if (atFound) {
                        result.add(annotationParser().parse(sb.toString()));
                        sb.setLength(0);
                        atFound = false;
                    } else {
                        comment.append(c);
                    }
                    lineBeginning = true;
                } else if (!atFound) {
                    comment.append(c);
                }
            } else if (c == '@' && lineBeginning) {
                atFound = true;
                final String extractedComment = comment.toString().trim();
                comment.setLength(0);
                if (!extractedComment.isEmpty()) {
                    result.add(new CommentAnnotation(extractedComment));
                }
            } else {
                lineBeginning = false;
                if (atFound) {
                    sb.append(c);
                } else {
                    comment.append(c);
                }
            }
        }
        if (atFound) {
            result.add(annotationParser().parse(sb.toString()));
        }
        final String extractedComment = comment.toString().trim();
        if (!extractedComment.isEmpty()) {
            result.add(new CommentAnnotation(extractedComment));
        }
        return asImmutableList(result);
    }

    @Nonnull
    protected AnnotationParser annotationParser() {
        return _annotaionParser;
    }

}
