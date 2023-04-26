package org.echocat.locela.api.java.support;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardLocaleNormalizerUnitTest {

    @Test
    public void normalize() throws Exception {
        final StandardLocaleNormalizer normalizer = new StandardLocaleNormalizer(US, US, GERMANY, FRANCE);
        assertThat(normalizer.normalize(GERMANY), is(GERMANY));
        assertThat(normalizer.normalize(GERMAN), is(GERMANY));
        assertThat(normalizer.normalize(new Locale("de", "AT")), is(GERMANY));
        assertThat(normalizer.normalize(new Locale("nl", "NL")), is(US));
    }

}
