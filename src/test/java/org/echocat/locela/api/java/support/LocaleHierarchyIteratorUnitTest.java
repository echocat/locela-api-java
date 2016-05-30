/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.support;

import org.junit.Test;

import java.util.Locale;
import java.util.NoSuchElementException;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.support.CollectionUtils.asList;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
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