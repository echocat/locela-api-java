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
import static org.echocat.locela.api.java.annotations.AnnotationFormatter.annotationFormatter;

public class AnnotationFormatterTest {

    protected static final AnnotationFormatter FORMATTER = annotationFormatter();

    @Test
    public void aNull() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("foo", null)), is("foo(null)"));
    }

    @Test
    public void aString() throws Exception {
        assertThat(FORMATTER.format(new CommentAnnotation("foo bar")), is("comment(\"foo bar\")"));
    }

    @Test
    public void aBoolean() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("boolean", true)), is("boolean(true)"));
    }

    @Test
    public void aDouble() throws Exception {
        assertThat(FORMATTER.format(new GenericAnnotation<>("double", -12.34D)), is("double(-12.34)"));
    }

    @Test
    public void multipleArguments() throws Exception {
        assertThat(FORMATTER.format(new FooAnnotation(true,1,"a")), is("foo(true,1.0,\"a\")"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedArgumentType() throws Exception {
        FORMATTER.format(new GenericAnnotation<>("unsupported", AnnotationFormatterTest.class));
    }

    @Test
    public void withStringEscaping() throws Exception {
        assertThat(FORMATTER.format(new CommentAnnotation("foo \nbar")), is("comment(\"foo \\nbar\")"));
        assertThat(FORMATTER.format(new CommentAnnotation("foo \rbar")), is("comment(\"foo \\rbar\")"));
        assertThat(FORMATTER.format(new CommentAnnotation("foo \tbar")), is("comment(\"foo \\tbar\")"));
        assertThat(FORMATTER.format(new CommentAnnotation("foo \\bar")), is("comment(\"foo \\\\bar\")"));
        assertThat(FORMATTER.format(new CommentAnnotation("foo \"bar")), is("comment(\"foo \\\"bar\")"));
    }

}