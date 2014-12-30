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
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class AnnotationFormatter {

    private static final AnnotationFormatter INSTANCE = new AnnotationFormatter();

    @Nonnull
    public static AnnotationFormatter annotationFormatter() {
        return INSTANCE;
    }

    @Nonnull
    public String format(@Nonnull Annotation annotation) {
        final StringBuilder sb = new StringBuilder();
        sb.append(annotation.getId()).append('(');
        boolean first = true;
        for (final Object argument : annotation.getArguments()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            formatArgument(annotation, argument, sb);
        }
        sb.append(')');
        return sb.toString();
    }

    protected void formatArgument(@Nonnull Annotation annotation, @Nonnull Object argument, @Nonnull StringBuilder to) {
        if (argument == null) {
            to.append("null");
        } else if (argument instanceof String) {
            formatString(((String) argument), to);
        } else if (argument instanceof Boolean) {
            to.append(argument);
        } else if (argument instanceof Number) {
            to.append(((Number) argument).doubleValue());
        } else {
            throw new IllegalArgumentException("Could not format arguments of type " + argument.getClass().getName() + " for annoation " + annotation.getId() + ".");
        }
    }

    protected void formatString(@Nonnull String argument, @Nonnull StringBuilder to) {
        final char[] chars = argument.toCharArray();
        to.append('"');
        for (final char c : chars) {
            if (c == '\n') {
                to.append("\\n");
            } else if (c == '\r') {
                to.append("\\r");
            } else if (c == '\t') {
                to.append("\\t");
            } else if (c == '\\') {
                to.append("\\\\");
            } else if (c == '"') {
                to.append("\\\"");
            } else {
                to.append(c);
            }
        }
        to.append('"');
    }

}
