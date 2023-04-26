package org.echocat.locela.api.java.support;

import org.junit.Test;

import java.util.Locale;
import java.util.NoSuchElementException;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class LocaleHierarchyIteratorUnitTest {

    @Test
    public void languageCountryVariant() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a", "b", "c"));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b", "c")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void languageCountryVariantAndFallback() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a", "b", "c"), asList(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b", "c")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void languageCountryVariantAndFallbacks() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a", "b", "c"), asList(null, US, FRANCE));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b", "c")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(null));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FRANCE));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void languageCountry() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a", "b"));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void languageCountryAndFallback() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a", "b"), asList(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a", "b")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void language() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a"));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void languageAndFallback() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(new Locale("a"), asList(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(new Locale("a")));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void empty() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(null);

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(null));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void emptyAndFallback() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(null, asList(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(null));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void emptyAndFallbacks() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(null, asList(US, FRANCE));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(null));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FRANCE));

        assertThat(i.hasNext(), is(false));
    }

    @Test
    public void oneLocaleAndFallbackIsEqual() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(US, asList(FRENCH, ENGLISH, FRANCE));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(US));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(ENGLISH));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FRENCH));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FRANCE));

        assertThat(i.hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void noMoreElement() throws Exception {
        final LocaleHierarchyIterator i = new LocaleHierarchyIterator(null);
        i.next();
        i.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeNotSupported() throws Exception {
        new LocaleHierarchyIterator(null).remove();
    }

}
