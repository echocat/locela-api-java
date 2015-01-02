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

import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.jomon.testing.CollectionMatchers.isEqualTo;

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
    public void testToString() throws Exception {
        assertThat(new LocaleHierarchy(GERMANY).toString(), is("[de_DE, de]"));
    }

}