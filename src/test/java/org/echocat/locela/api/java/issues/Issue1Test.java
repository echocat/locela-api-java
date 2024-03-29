package org.echocat.locela.api.java.issues;

import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.echocat.locela.api.java.properties.Line;
import org.echocat.locela.api.java.properties.Line.PropertyLine;
import org.echocat.locela.api.java.properties.Properties;
import org.echocat.locela.api.java.properties.Property;
import org.echocat.locela.api.java.properties.StandardPropertiesLineReader;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.echocat.locela.api.java.utils.CollectionUtils.countElementsOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isInstanceOf;
import static org.echocat.locela.api.java.testing.BaseMatchers.isNotNull;
import static org.echocat.locela.api.java.messages.StandardMessagesProvider.messagesProvider;
import static org.echocat.locela.api.java.properties.StandardPropertiesReader.propertiesReader;

public class Issue1Test {

    @Test
    public void testWholeMessages() throws Exception {
        final Messages messages = messagesProvider().provideBy(null, Issue1Test.class, "issue1TestFile.properties");
        assertThat(countElementsOf(messages), is(1L));
        final Message message = messages.get("foo.bar");
        assertThat(message, isNotNull());
        assertThat(message.get(), is("Now is \n{time,datetime}."));
    }

    @Test
    public void testProperties() throws Exception {
        final Properties<String> properties = readProperties();
        assertThat(countElementsOf(properties), is(1L));
        final Property<String> property = properties.get("foo.bar");
        assertThat(property, isNotNull());
        assertThat(property.get(), is("Now is \n{time,datetime}."));
    }

    @Nonnull
    protected Properties<String> readProperties() throws IOException {
        try (final InputStream is = Issue1Test.class.getResourceAsStream("issue1TestFile.properties")) {
            assertThat("Expected test resource.", is, isNotNull());
            try (final Reader reader = new InputStreamReader(is)) {
                return propertiesReader().read(reader);
            }
        }
    }

    @Test
    public void testPropertiesLineReader() throws Exception {
        try (final InputStream is = Issue1Test.class.getResourceAsStream("issue1TestFile.properties")) {
            assertThat("Expected test resource.", is, isNotNull());
            try (final Reader reader = new InputStreamReader(is)) {
                final StandardPropertiesLineReader lineReader = new StandardPropertiesLineReader(reader);
                final Line line = lineReader.read();
                assertThat(line, isInstanceOf(PropertyLine.class));
                assertThat(line.toString(), is("foo.bar = Now is \\n{time,datetime}.\n"));
            }
        }
    }
}
