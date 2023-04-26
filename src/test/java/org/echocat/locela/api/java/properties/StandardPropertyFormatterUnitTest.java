package org.echocat.locela.api.java.properties;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
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
