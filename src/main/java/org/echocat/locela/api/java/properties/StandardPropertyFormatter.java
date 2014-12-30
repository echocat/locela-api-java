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

public class StandardPropertyFormatter implements PropertyFormatter {

    private static final StandardPropertyFormatter INSTANCE = new StandardPropertyFormatter();

    @Nonnull
    public static StandardPropertyFormatter propertyFormatter() {
        return INSTANCE;
    }

    @Nonnull
    @Override
    public String format(@Nonnull Property<String> property) {
        final StringBuilder sb = new StringBuilder();
        append(property.getId(), sb, true);
        sb.append(" = ");
        append(property.get(), sb, false);
        return sb.toString();
    }

    protected void append(@Nonnull String what, @Nonnull StringBuilder to, boolean escapeWhiteSpaces) {
        final char[] chars = what != null ? what.toCharArray() : new char[0];
        for (final char c : chars) {
            if (isEscapeNeeded(c)) {
                to.append('\\').append(c);
            } else if (c == '\n') {
                to.append("\\n");
            } else if (c == '\r') {
                to.append("\\r");
            } else if (c == '\t') {
                to.append("\\t");
            } else if (c == '\f') {
                to.append("\\f");
            } else if (escapeWhiteSpaces && c == ' ') {
                to.append("\\ ");
            } else {
                to.append(c);
            }
        }
    }

    protected boolean isEscapeNeeded(char c) {
        return c == ':' || c == '=' || c == '\\';
    }

}
