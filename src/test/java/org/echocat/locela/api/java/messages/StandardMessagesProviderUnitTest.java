package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import static java.util.Locale.GERMAN;
import static java.util.Locale.GERMANY;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.StandardMessage.message;
import static org.echocat.locela.api.java.messages.StandardMessagesProvider.messagesProvider;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardMessagesProviderUnitTest {

    protected static final StandardMessagesProvider PROVIDER = messagesProvider();

    @Test
    public void provideByNullLocale1() throws Exception {
        final Messages messages = PROVIDER.provideBy(null, StandardMessagesProviderUnitTest.class, "testfiles/foo1.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(null, "a", "a1"),
            message(null, "b", "b1"),
            message(null, "c", "c1")
        ));
    }

    @Test
    public void provideByNullLocale2() throws Exception {
        final Messages messages = PROVIDER.provideBy(null, StandardMessagesProviderUnitTest.class, "testfiles/foo2.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(null, "a", "a2"),
            message(null, "b", "b2"),
            message(null, "c", "c2")
        ));
    }

    @Test
    public void provideByNullLocale3() throws Exception {
        final Messages messages = PROVIDER.provideBy(null, StandardMessagesProviderUnitTest.class, "testfiles/foo3.properties");
        assertThat(messages, is(null));
    }

    @Test
    public void provideByGermanLocale1() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMAN, StandardMessagesProviderUnitTest.class, "testfiles/foo1.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(GERMAN, "a", "a1_de"),
            message(GERMAN, "b", "b1_de"),
            message(GERMAN, "c", "c1_de")
        ));
    }

    @Test
    public void provideByGermanLocale2() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMAN, StandardMessagesProviderUnitTest.class, "testfiles/foo2.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(GERMAN, "a", "a2_de"),
            message(GERMAN, "b", "b2_de"),
            message(GERMAN, "c", "c2_de")
        ));
    }

    @Test
    public void provideByGermanLocale3() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMAN, StandardMessagesProviderUnitTest.class, "testfiles/foo3.properties");
        assertThat(messages, is(null));
    }

    @Test
    public void provideByGermanyLocale1() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMANY, StandardMessagesProviderUnitTest.class, "testfiles/foo1.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(GERMANY, "a", "a1_de_DE"),
            message(GERMANY, "b", "b1_de_DE"),
            message(GERMANY, "c", "c1_de_DE")
        ));
    }

    @Test
    public void provideByGermanyLocale2() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMANY, StandardMessagesProviderUnitTest.class, "testfiles/foo2.properties");
        assertThat(messages, IterableMatchers.isEqualTo(
            message(GERMANY, "a", "a2_de_DE"),
            message(GERMANY, "b", "b2_de_DE"),
            message(GERMANY, "c", "c2_de_DE")
        ));
    }

    @Test
    public void provideByGermanyLocale3() throws Exception {
        final Messages messages = PROVIDER.provideBy(GERMANY, StandardMessagesProviderUnitTest.class, "testfiles/foo3.properties");
        assertThat(messages, is(null));
    }

}
