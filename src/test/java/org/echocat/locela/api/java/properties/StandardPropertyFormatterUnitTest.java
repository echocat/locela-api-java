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

import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.properties.StandardProperty.property;
import static org.echocat.locela.api.java.properties.StandardPropertyFormatter.propertyFormatter;

public class StandardPropertyFormatterUnitTest {

    protected static final PropertyFormatter FORMATTER = propertyFormatter();

    @Test
    public void regular() throws Exception {
        assertThat(FORMATTER.format(property("foo").set("bar")), is("foo = bar"));
    }

    @Test
    public void whitespacesInKey() throws Exception {
        assertThat(FORMATTER.format(property(" foo bar ").set("xxx")), is("\\ foo\\ bar\\  = xxx"));
    }

    @Test
    public void whitespacesInValue() throws Exception {
        assertThat(FORMATTER.format(property("foo").set("follow the white rabbit")), is("foo = follow the white rabbit"));
    }

    @Test
    public void escapeSpecialCharacters() throws Exception {
        assertThat(FORMATTER.format(property("\n\r\t\f\\:=").set("\n\r\t\f\\:=")), is("\\n\\r\\t\\f\\\\\\:\\= = \\n\\r\\t\\f\\\\\\:\\="));
    }


}