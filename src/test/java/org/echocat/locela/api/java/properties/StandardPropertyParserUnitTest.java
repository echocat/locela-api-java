package org.echocat.locela.api.java.properties;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.properties.StandardPropertyParser.propertyParser;

public class StandardPropertyParserUnitTest {

    protected static final StandardPropertyParser PARSER = propertyParser();

    @Test
    public void simple() throws Exception {
        assertThat("a=1", isProperty("a", "1"));
        assertThat("b:2", isProperty("b", "2"));
    }

    @Test
    public void whiteSpacesInContent() throws Exception {
        assertThat("a=  foo\t bar", isProperty("a", "foo\t bar"));
    }

    @Test
    public void emptyKey() throws Exception {
        assertThat("=1", isProperty("", "1"));
        assertThat(":2", isProperty("", "2"));
        assertThat("   :3", isProperty("", "3"));
    }

    @Test
    public void emptyValue() throws Exception {
        assertThat("a=", isProperty("a", ""));
        assertThat("b:", isProperty("b", ""));
        assertThat(":", isProperty("", ""));
    }

    @Test
    public void whitespacesInKey() throws Exception {
        assertThat("a =1", isProperty("a", "1"));
        assertThat("b\t:2", isProperty("b", "2"));
        assertThat(" c 3", isProperty("c", "3"));
        assertThat("\r4", isProperty("\r4", ""));
        assertThat("\n5", isProperty("\n5", ""));
        assertThat("\t6", isProperty("6", ""));
        assertThat("\f7", isProperty("\f7", ""));
    }

    @Test
    public void escapingInKeys() throws Exception {
        assertThat("\\r 1", isProperty("\r", "1"));
        assertThat("\\n 2", isProperty("\n", "2"));
        assertThat("\\t 3", isProperty("\t", "3"));
        assertThat("\\f 4", isProperty("\f", "4"));

        assertThat("\\r=1", isProperty("\r", "1"));
        assertThat("\\n=2", isProperty("\n", "2"));
        assertThat("\\t=3", isProperty("\t", "3"));
        assertThat("\\f=4", isProperty("\f", "4"));
    }

    @Test
    public void unicode() throws Exception {
        assertThat("\\u00a7 1", isProperty("§", "1"));
        assertThat("\\u00A7 2", isProperty("§", "2"));
        assertThat("a \\u00a7", isProperty("a", "§"));
        assertThat("b \\u00A7", isProperty("b", "§"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedUnicode() throws Exception {
        PARSER.parse("\\u00x7 1", null);
    }

    @Test
    public void tooShortUnicode() throws Exception {
        assertThat("a \\u00", isProperty("a", "u00"));
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    @Nonnull
    protected static Matcher<String> isProperty(@Nonnull String id, @Nullable String content) {
        return isProperty(null, id, content);
    }

    @SuppressWarnings("NonBooleanMethodNameMayNotStartWithQuestion")
    @Nonnull
    protected static Matcher<String> isProperty(@Nullable final Locale locale, @Nonnull final String id, @Nullable final String content) {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(@Nonnull String plain) {
                final Property<String> property = PARSER.parse(plain, null);
                final boolean result;
                if (id.equals(property.getId())) {
                    result = content != null ? content.equals(property.get()) : property.get() == null;
                } else {
                    result = false;
                }
                return result;
            }

            @Override
            public void describeTo(@Nonnull Description description) {
                description.appendText("is property ").appendValue(id).appendText("=").appendValue(content);
                if (locale != null) {
                    description.appendText("(").appendValue(locale).appendText(")");
                }
            }

            @Override
            protected void describeMismatchSafely(@Nonnull String plain, @Nonnull Description description) {
                final Property<String> property = PARSER.parse(plain, null);
                description.appendText("was ").appendValue(property.getId()).appendText("=").appendValue(property.get());
                if (locale != null) {
                    description.appendText("(").appendValue(locale).appendText(")");
                }


            }
        };
    }


}
