package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.properties.Line.CommentLine;
import org.echocat.locela.api.java.properties.Line.EmptyLine;
import org.echocat.locela.api.java.properties.Line.LineWithContent;
import org.echocat.locela.api.java.properties.Line.PropertyLine;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.StringReader;

import static org.echocat.locela.api.java.testing.BaseMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardPropertiesLineReaderUnitTest {

    @Test
    public void testRead() throws Exception {
        try (final PropertiesLineReader reader = new StandardPropertiesLineReader(new StringReader("text1\n" +
            " text2\n" +
            "#comment1\n" +
            "   #comment2  \n" +
            "!comment3\n" +
            "   !comment4\n" +
            "\n" +
            "\n" +
            ""), 5)) {
            assertThat(reader.read(), isProperty("text1"));
            assertThat(reader.read(), isProperty("text2"));
            assertThat(reader.read(), isComment("comment1"));
            assertThat(reader.read(), isComment("comment2"));
            assertThat(reader.read(), isComment("comment3"));
            assertThat(reader.read(), isComment("comment4"));
            assertThat(reader.read(), isEmpty());
            assertThat(reader.read(), isEmpty());
            assertThat(reader.read(), isNull());
        }
    }

    @Test
    public void testEscapeAtEndOfLine() throws Exception {
        try (final PropertiesLineReader reader = new StandardPropertiesLineReader(new StringReader(
            "text1\\\r\n" +
            "     text2\n"+
            "text3\n" +
            "text4\\ \n" +
            "text5\n"), 5)) {
            assertThat(reader.read(), isProperty("text1text2"));
            assertThat(reader.read(), isProperty("text3"));
            assertThat(reader.read(), isProperty("text4\\"));
            assertThat(reader.read(), isProperty("text5"));
            assertThat(reader.read(), isNull());
        }
    }

    @Test
    public void testEscapeAtEndOfLineWithPotentialComment() throws Exception {
        try (final PropertiesLineReader reader = new StandardPropertiesLineReader(new StringReader(
            "text1\\\r\n" +
            "     #text2\n"), 5)) {
            assertThat(reader.read(), isProperty("text1#text2"));
            assertThat(reader.read(), isNull());
        }
    }

    @Test
    public void testBackslashAtOtherPlaces() throws Exception {
        try (final PropertiesLineReader reader = new StandardPropertiesLineReader(new StringReader(
            "t\\e\\ \n" +
            " \\n\\text2\n"), 5)) {
            assertThat(reader.read(), isProperty("t\\e\\"));
            assertThat(reader.read(), isProperty("\\n\\text2"));
            assertThat(reader.read(), isNull());
        }
    }

    @Test
    public void testReadLineWithNoLineFeed() throws Exception {
        try (final PropertiesLineReader reader = new StandardPropertiesLineReader(new StringReader("text1"), 5)) {
            assertThat(reader.read(), isProperty("text1"));
            assertThat(reader.read(), isNull());
        }
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    @Nonnull
    protected static Matcher<? super Line> isProperty(@Nonnull final String content) {
        return new TypeSafeMatcher<Line>() {
            @Override
            protected boolean matchesSafely(@Nonnull Line line) {
                return line instanceof PropertyLine && content.equals(((LineWithContent) line).getContent());
            }

            @Override
            public void describeTo(@Nonnull Description description) {
                description.appendText("is property ").appendValue(content);
            }

        };
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    @Nonnull
    protected static Matcher<? super Line> isComment(@Nonnull final String content) {
        return new TypeSafeMatcher<Line>() {
            @Override
            protected boolean matchesSafely(@Nonnull Line line) {
                return line instanceof CommentLine && content.equals(((LineWithContent) line).getContent());
            }

            @Override
            public void describeTo(@Nonnull Description description) {
                description.appendText("is comment ").appendValue(content);
            }
        };
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    @Nonnull
    protected static Matcher<? super Line> isEmpty() {
        return new TypeSafeMatcher<Line>() {
            @Override
            protected boolean matchesSafely(@Nonnull Line line) {
                return line instanceof EmptyLine;
            }

            @Override
            public void describeTo(@Nonnull Description description) {
                description.appendText("is empty");
            }
        };
    }
}
