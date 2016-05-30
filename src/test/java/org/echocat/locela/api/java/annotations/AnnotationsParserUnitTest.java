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

import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.annotations.AnnotationsParser.annotationsParser;

public class AnnotationsParserUnitTest {

    protected static final AnnotationsParser PARSER = annotationsParser();

    @Test
    public void comment() throws Exception {
        assertThat(PARSER.parse("foo bar"), IterableMatchers.<Annotation>isEqualTo(
            new CommentAnnotation("foo bar")
        ));
        assertThat(PARSER.parse("foo\nbar"), IterableMatchers.<Annotation>isEqualTo(
            new CommentAnnotation("foo\nbar")
        ));
    }

    @Test
    public void foo() throws Exception {
        assertThat(PARSER.parse("  @foo(true,-123.456,\"bar\")"), IterableMatchers.<Annotation>isEqualTo(
            new FooAnnotation(true, -123.456D, "bar")
        ));
    }

    @Test
    public void bar() throws Exception {
        assertThat(PARSER.parse("  @bar()"), IterableMatchers.<Annotation>isEqualTo(
            new BarAnnotation()
        ));
    }

    @Test
    public void mixed() throws Exception {
        assertThat(PARSER.parse(
            "foo bar\n" +
                "@foo(false,1,\"bar\")\n" +
                "@bar()\n" +
                "@string(\"a string\")\n" +
                "@boolean(true)\n" +
                "@double(1.2)\n" +
                "another comment"
        ), IterableMatchers.isEqualTo(
            new CommentAnnotation("foo bar"),
            new FooAnnotation(false, 1D, "bar"),
            new BarAnnotation(),
            new GenericAnnotation<>("string", "a string"),
            new GenericAnnotation<>("boolean", true),
            new GenericAnnotation<>("double", 1.2D),
            new CommentAnnotation("another comment")
        ));
    }

}