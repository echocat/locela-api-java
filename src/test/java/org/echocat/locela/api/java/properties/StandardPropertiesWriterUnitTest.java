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

import org.echocat.locela.api.java.annotations.BarAnnotation;
import org.echocat.locela.api.java.annotations.CommentAnnotation;
import org.echocat.locela.api.java.annotations.FooAnnotation;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.StringWriter;

import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.properties.StandardProperties.properties;
import static org.echocat.locela.api.java.properties.StandardPropertiesWriter.propertyWriter;
import static org.echocat.locela.api.java.properties.StandardProperty.property;

public class StandardPropertiesWriterUnitTest {

    protected static final StandardPropertiesWriter WRITER = propertyWriter();

    @Test
    public void regular() throws Exception {
        assertThat(write(properties(
            property("a", String.class).set("1"),
            property("b", String.class).set("2"),
            property("c", String.class).set("3")
        )), is(
            "a = 1\n" +
            "b = 2\n" +
            "c = 3\n"
        ));
    }

    @Test
    public void withAnnotationsAtProperties() throws Exception {
        final CommentAnnotation comment = new CommentAnnotation("This is a comment");
        final FooAnnotation foo = new FooAnnotation(true, 123.456D, "Hello world!");
        final BarAnnotation bar = new BarAnnotation();

        assertThat(write(properties(
            property("a", String.class).set("1").withAnnotations(comment, bar, foo),
            property("b", String.class).set("2").withAnnotations(foo),
            property("c", String.class).set("3").withAnnotations(bar, comment)
        )), is("" +
                "# This is a comment\n" +
                "# @bar()\n" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "a = 1\n" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "b = 2\n" +
                "# @bar()\n" +
                "# This is a comment\n" +
                "c = 3\n"
        ));
    }

    @Test
    public void withAnnotationsAtHead() throws Exception {
        final CommentAnnotation comment = new CommentAnnotation("This is a comment");
        final FooAnnotation foo = new FooAnnotation(true, 123.456D, "Hello world!");
        final BarAnnotation bar = new BarAnnotation();

        assertThat(write(properties(
            property("a", String.class).set("1"),
            property("b", String.class).set("2"),
            property("c", String.class).set("3")
        ).withAnnotations(foo, bar, comment)), is("" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "# @bar()\n" +
                "# This is a comment\n" +
                "\n" +
                "a = 1\n" +
                "b = 2\n" +
                "c = 3\n"
        ));
    }

    @Test
    public void withAnnotationsAtHeadOnly() throws Exception {
        final CommentAnnotation comment = new CommentAnnotation("This is a comment");
        final FooAnnotation foo = new FooAnnotation(true, 123.456D, "Hello world!");
        final BarAnnotation bar = new BarAnnotation();

        assertThat(write(StandardProperties.<String>properties().withAnnotations(foo, bar, comment)), is("" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "# @bar()\n" +
                "# This is a comment\n" +
                "\n"
        ));
    }

    @Test
    public void withAnnotationsAtHeadAndProperties() throws Exception {
        final CommentAnnotation comment = new CommentAnnotation("This is a comment");
        final FooAnnotation foo = new FooAnnotation(true, 123.456D, "Hello world!");
        final BarAnnotation bar = new BarAnnotation();

        assertThat(write(properties(
            property("a", String.class).set("1").withAnnotations(comment, bar, foo),
            property("b", String.class).set("2").withAnnotations(foo),
            property("c", String.class).set("3").withAnnotations(bar, comment)
        ).withAnnotations(foo, comment)), is("" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "# This is a comment\n" +
                "\n" +
                "# This is a comment\n" +
                "# @bar()\n" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "a = 1\n" +
                "# @foo(true,123.456,\"Hello world!\")\n" +
                "b = 2\n" +
                "# @bar()\n" +
                "# This is a comment\n" +
                "c = 3\n"
        ));
    }

    @Nonnull
    protected static String write(@Nullable Properties<String> properties) throws Exception {
        try (final StringWriter writer = new StringWriter()) {
            WRITER.write(properties, writer);
            return writer.toString();
        }
    }

}