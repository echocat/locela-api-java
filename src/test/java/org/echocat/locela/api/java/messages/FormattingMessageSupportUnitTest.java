package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.format.Formatter;
import org.echocat.locela.api.java.format.MessageFormatter;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isInstanceOf;

public class FormattingMessageSupportUnitTest {

    @Test
    public void createFormatter() throws Exception {
        final Formatter formatter = new MessageImpl(US, "foo{0}").formatter();
        assertThat(formatter, isInstanceOf(MessageFormatter.class));
        assertThat(((MessageFormatter)formatter).format("bar"), is("foobar"));
    }

    @Test
    public void format() throws Exception {
        assertThat(new MessageImpl(US, "foo{0}").format("bar"), is("foobar"));
    }

    protected static class MessageImpl extends FormattingMessageSupport {

        @Nullable
        private final Locale _locale;
        @Nonnull
        private final String _content;

        public MessageImpl(Locale locale, @Nonnull String content) {
            super(null);
            _locale = locale;
            _content = content;
        }

        @Nonnull
        @Override
        public String get() {
            return _content;
        }

        @Nullable
        @Override
        public Locale getLocale() {
            return _locale;
        }

        @Nullable
        @Override
        public String getId() {
            throw new UnsupportedOperationException();
        }

    }
}
