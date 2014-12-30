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

import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.annotations.AnnotationsFormatter.annotationsFormatter;

public class AnnotaionsFormatterUnitTest {

    protected static final AnnotationsFormatter FORMATTER = annotationsFormatter();

    @Test
    public void mixed() throws Exception {
        assertThat(FORMATTER.format(
            new CommentAnnotation("foo\nbar"),
            new GenericAnnotation<>("boolean", true),
            new GenericAnnotation<>("string", "foo bar"),
            new GenericAnnotation<>("double",-12.34D),
            new FooAnnotation(true, 1, "a")
        ), is("foo\nbar\n" +
            "@boolean(true)\n" +
            "@string(\"foo bar\")\n" +
            "@double(-12.34)\n" +
            "@foo(true,1.0,\"a\")\n"));
    }

    @Test
    public void comment() throws Exception {
        assertThat(FORMATTER.format(new CommentAnnotation("foo bar")), is("foo bar\n"));
    }

    @Test
    public void aString() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("string", "foo bar")), is("@string(\"foo bar\")\n"));
    }

    @Test
    public void aBoolean() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("boolean", true)), is("@boolean(true)\n"));
    }

    @Test
    public void aDouble() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("double", -12.34D)), is("@double(-12.34)\n"));
    }

    @Test
    public void multipleArguments() throws Exception {
        assertThat(FORMATTER.format(new FooAnnotation(true, 1, "a")), is("@foo(true,1.0,\"a\")\n"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedArgumentType() throws Exception {
        FORMATTER.format(new GenericAnnotation<>("unsupported", AnnotationFormatterTest.class));
    }

}
