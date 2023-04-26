package org.echocat.locela.api.java.format;

import org.echocat.locela.api.java.testing.BaseMatchers;
import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MessageFormatterFactoryUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetFactoryWithIllegalId() throws Exception {
        new MessageFormatterFactory().getFactoryBy("foo");
    }

    @Test
    public void testGetFactory() throws Exception {
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        doReturn("foo").when(other).getId();
        assertThat(new MessageFormatterFactory(other).getFactoryBy("foo"), BaseMatchers.is(other));
    }

    @Test
    public void testIterator() throws Exception {
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        doReturn("foo").when(other).getId();
        final MessageFormatterFactory messageFormatterFactory = new MessageFormatterFactory(other);
        //noinspection RedundantTypeArguments
        assertThat(messageFormatterFactory.getFactories(), IterableMatchers.<FormatterFactory<?>>isEqualTo(messageFormatterFactory, other));
    }

    @Test(expected = IllegalStateException.class)
    public void testNotSameRoot() throws Exception {
        final MessageFormatterFactory that = new MessageFormatterFactory();
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        that.createBy(Locale.US, null, other);
    }

}
