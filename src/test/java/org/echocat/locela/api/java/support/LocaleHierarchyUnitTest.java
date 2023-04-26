package org.echocat.locela.api.java.support;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.hamcrest.MatcherAssert.assertThat;

public class LocaleHierarchyUnitTest {

    @Test
    public void languageCountryVariant() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a", "b", "c")), isEqualTo(
            new Locale("a", "b", "c"),
            new Locale("a", "b"),
            new Locale("a")
        ));
    }

    @Test
    public void languageCountryVariantAndFallback() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a", "b", "c"), US), isEqualTo(
            new Locale("a", "b", "c"),
            new Locale("a", "b"),
            new Locale("a"),
            US
        ));
    }

    @Test
    public void languageCountry() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a", "b")), isEqualTo(
            new Locale("a", "b"),
            new Locale("a")
        ));
    }

    @Test
    public void languageCountryAndFallback() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a", "b"), null, US), isEqualTo(
            new Locale("a", "b"),
            new Locale("a"),
            null,
            US
        ));
    }

    @Test
    public void language() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a")), isEqualTo(
            new Locale("a")
        ));
    }

    @Test
    public void languageAndFallback() throws Exception {
        assertThat(new LocaleHierarchy(new Locale("a"), US), isEqualTo(
            new Locale("a"),
            US
        ));
    }

    @Test
    public void empty() throws Exception {
        assertThat(new LocaleHierarchy(null), isEqualTo(
            (Locale) null
        ));
    }

    @Test
    public void emptyAndFallback() throws Exception {
        assertThat(new LocaleHierarchy(null, US), isEqualTo(
            null,
            US
        ));
    }

    @Test
    public void oneLocaleAndFallbackIsEqual() throws Exception {
        assertThat(new LocaleHierarchy(US, asList(FRENCH, ENGLISH, FRANCE)), isEqualTo(
            US,
            ENGLISH,
            FRENCH,
            FRANCE
        ));
    }



    @Test
    public void testToString() throws Exception {
        assertThat(new LocaleHierarchy(GERMANY).toString(), is("[de_DE, de]"));
    }

}
