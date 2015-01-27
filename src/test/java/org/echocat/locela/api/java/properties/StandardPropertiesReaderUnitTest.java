/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.properties;

import org.echocat.jomon.testing.CollectionMatchers;
import org.echocat.locela.api.java.annotations.BarAnnotation;
import org.echocat.locela.api.java.annotations.CommentAnnotation;
import org.echocat.locela.api.java.annotations.GenericAnnotation;
import org.echocat.locela.api.java.annotations.Annotation;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.echocat.jomon.runtime.CollectionUtils.asList;
import static org.echocat.jomon.runtime.CollectionUtils.asMap;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.CollectionMatchers.isEqualTo;
import static org.echocat.locela.api.java.properties.StandardPropertiesReader.propertiesReader;

public class StandardPropertiesReaderUnitTest {

    protected static final StandardPropertiesReader PARSER = propertiesReader();

    @Test
    public void simple() throws Exception {
        assertThat("a=1\n" +
            "b:2"
            , areProperties(
            "a", "1",
            "b", "2"
        ));
    }

    @Test
    public void annotationsWithCommentsOnly() throws Exception {
        final Properties<String> properties = toProperties("" +
            "# this is a comment\n" +
            "a=1\n" +
            "\n" +
            "# this is another comment\n" +
            "#\n" +
            "\n" +
            "b=2");
        assertThat(properties.get("a").annotations(), CollectionMatchers.<Annotation>isEqualTo(new CommentAnnotation("this is a comment")));
        assertThat(properties.get("b").annotations(), CollectionMatchers.<Annotation>isEqualTo(new CommentAnnotation("this is another comment")));
    }

    @Test
    public void annotationsAtProperties() throws Exception {
        final Properties<String> properties = toProperties("" +
            "# this is a comment\n" +
            "# @string(\"this is a string\")\n" +
            "# @bar()\n" +
            "# and some more comment\n" +
            "a=1\n" +
            "\n" +
            "# this is another comment\n" +
            "# and more line\n" +
            "\n" +
            "b=2");
        final Property<?> a = properties.get("a");
        assertThat(asList(a.annotations()), isEqualTo(
            new CommentAnnotation("this is a comment"),
            new GenericAnnotation<>("string", "this is a string"),
            new BarAnnotation(),
            new CommentAnnotation("and some more comment")
        ));
        final Property<?> b = properties.get("b");
        assertThat(asList(b.annotations()), CollectionMatchers.<Annotation>isEqualTo(
            new CommentAnnotation("this is another comment\nand more line")
        ));
    }

    @Test
    public void annotationsAtHead() throws Exception {
        final Properties<String> properties = toProperties("" +
            "# this is a comment\n" +
            "# @string(\"this is a string\")\n" +
            "# @bar()\n" +
            "# and some more comment\n" +
            "\n" +
            "a=1\n" +
            "\n" +
            "b=2");

        assertThat(asList(properties.annotations()), isEqualTo(
            new CommentAnnotation("this is a comment"),
            new GenericAnnotation<>("string", "this is a string"),
            new BarAnnotation(),
            new CommentAnnotation("and some more comment")
        ));
        final Property<?> a = properties.get("a");
        assertThat(asList(a.annotations()), CollectionMatchers.<Annotation>isEqualTo());
        final Property<?> b = properties.get("b");
        assertThat(asList(b.annotations()), CollectionMatchers.<Annotation>isEqualTo());
    }

    @Test
    public void annotationsAtHeadAndProperties() throws Exception {
        final Properties<String> properties = toProperties("" +
            "# this is a comment\n" +
            "# @string(\"this is a string\")\n" +
            "# @bar()\n" +
            "# and some more comment\n" +
            "\n" +
            "\n" +
            "# Comment for this property\n" +
            "# @bar()\n" +
            "# @string(\"Hello world!\")\n" +
            "# and another comment for this property\n" +
            "a=1\n" +
            "\n" +
            "# this is another comment\n" +
            "# and more line\n" +
            "\n" +
            "b=2");
        assertThat(asList(properties.annotations()), isEqualTo(
            new CommentAnnotation("this is a comment"),
            new GenericAnnotation<>("string", "this is a string"),
            new BarAnnotation(),
            new CommentAnnotation("and some more comment")
        ));
        final Property<?> a = properties.get("a");
        assertThat(asList(a.annotations()), isEqualTo(
            new CommentAnnotation("Comment for this property"),
            new BarAnnotation(),
            new GenericAnnotation<>("string", "Hello world!"),
            new CommentAnnotation("and another comment for this property")
        ));
        final Property<?> b = properties.get("b");
        assertThat(asList(b.annotations()), CollectionMatchers.<Annotation>isEqualTo(
            new CommentAnnotation("this is another comment\nand more line")
        ));
    }

    @Test
    public void annotationsAtHeadOnly() throws Exception {
        final Properties<String> properties = toProperties("" +
            "# this is a comment\n" +
            "# @string(\"this is a string\")\n" +
            "# @bar()\n" +
            "# and some more comment\n"
        );
        assertThat(asList(properties.annotations()), isEqualTo(
            new CommentAnnotation("this is a comment"),
            new GenericAnnotation<>("string", "this is a string"),
            new BarAnnotation(),
            new CommentAnnotation("and some more comment")
        ));
    }

    @Test
    public void emptyKey() throws Exception {
        assertThat("=1\n" +
            ":2"
            , areProperties(
            "", "1",
            "", "2"
        ));
    }

    @Test
    public void duplicates() throws Exception {
        assertThat("a=3\n" +
            "a:2\n" +
            "a=1"
            , areProperties(
            "a", "1"
        ));
    }

    @Test
    public void whitespacesInKey() throws Exception {
        assertThat("\ta =   1\n" +
            "b\t:\t2"
            , areProperties(
            "a", "1",
            "b", "2"
        ));
        assertThat("a A=1\r" +
            "b\\ B:2"
            , areProperties(
            "a", "A=1",
            "b B", "2"
        ));
    }

    @Test
    public void emptyContent() throws Exception {
        assertThat("a", areProperties(
            "a", ""
        ));
        assertThat("abc", areProperties(
            "abc", ""
        ));
    }

    @Test
    public void whitespacesAsKeys() throws Exception {
        assertThat("a=1\r" +
            "b:2\n" +
            "c\n" +
            "\n" +
            "\n" +
            "\\n=3\n" +
            "\\r=4\n" +
            "\\t=5\n" +
            "\\f=6\n"
            , areProperties(
            "a", "1",
            "b", "2",
            "c", "",
            "\n", "3",
            "\r", "4",
            "\t", "5",
            "\f", "6"
        ));
    }

    @Test
    public void escapingInKeys() throws Exception {
        assertThat("a\\ A=1\n" +
            "b\\rB=2\n" +
            "\\#=3"
            , areProperties(
            "a A", "1",
            "b\rB", "2",
            "#", "3"
        ));
    }

    @Test
    public void escapingAtEndOfLine() throws Exception {
        assertThat("a=1 \\\n" +
            "    b=2\n" +
            "c=3\\\r\n" +
            "d=4\n" +
            "e=5\\\n" +
            "  #f=6"
            , areProperties(
            "a", "1 b=2",
            "c", "3d=4",
            "e", "5#f=6"
        ));
    }

    @Test
    public void unicode() throws Exception {
        assertThat("a=\\u00a7\n" +
            "b=\\u00A7"
            , areProperties(
            "a", "§",
            "b", "§"
        ));
    }

    @Test
    public void comments() throws Exception {
        assertThat("#a=1\n" +
            "  #b=2\n" +
            "!c=3\n" +
            "   !d=4\n" +
            "e=5#foo\n" +
            "f=#6"
            , areProperties(
            "e", "5#foo",
            "f", "#6"
        ));
    }

    @Nonnull
    protected static Properties<String> toProperties(@Nonnull String source) {
        try (final StringReader reader = new StringReader(source)) {
            return PARSER.read(reader);
        } catch (final IOException e) {
            throw new RuntimeException("IOException in StringReader?", e);
        }
    }

    @Nonnull
    protected static Map<String, String> toPropertiesMap(@Nonnull String source) {
        final Map<String, String> result = new LinkedHashMap<>();
        for (final Property<String> property : toProperties(source)) {
            final Object o = property.get();
            result.put(property.getId(), o != null ? o.toString() : null);
        }
        return result;
    }


    @Nonnull
    protected static Matcher<String> areProperties(@Nullable final String... keyToValue) {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(@Nonnull String source) {
                final Map<String, String> expectedContent = asMap(keyToValue);
                //noinspection CollectionDeclaredAsConcreteClass
                final Map<String, String> properties = toPropertiesMap(source);
                final boolean result = expectedContent.equals(properties);
                return result;
            }

            @Override
            public void describeTo(@Nonnull Description description) {
                description.appendText("are properties ").appendValue(asMap(keyToValue));
            }

            @Override
            protected void describeMismatchSafely(@Nullable String source, @Nonnull Description description) {
                description.appendText("was ")
                    .appendValue(source != null ? toPropertiesMap(source).toString() : null);
            }
        };
    }

}