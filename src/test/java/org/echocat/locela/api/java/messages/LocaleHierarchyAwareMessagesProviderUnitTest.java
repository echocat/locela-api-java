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

package org.echocat.locela.api.java.messages;

import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableList;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isEmpty;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.messages.StandardMessagesProvider.messagesProvider;
import static org.junit.Assert.assertThat;

public class LocaleHierarchyAwareMessagesProviderUnitTest {

    protected static final Class<LocaleHierarchyAwareMessagesProviderUnitTest> TYPE = LocaleHierarchyAwareMessagesProviderUnitTest.class;

    protected static final Iterable<Locale> LOCALES = asImmutableList(GERMANY, GERMAN, US, ENGLISH, FRANCE, FRENCH);
    protected static final Iterable<Locale> US_FALLBACK = asImmutableList(US);

    protected static final Messages MESSAGES_GERMANY = provideReferencesBy(GERMANY);
    protected static final Messages MESSAGES_GERMAN = provideReferencesBy(GERMAN);
    protected static final Messages MESSAGES_NULL = provideReferencesBy(null);
    protected static final Messages MESSAGES_US = provideReferencesBy(US);
    protected static final Messages MESSAGES_ENGLISH = provideReferencesBy(ENGLISH);

    @Test
    public void provideGermany() throws Exception {
        assertThat(provideBy(US_FALLBACK, GERMANY).getLocale(), is(GERMANY));
        assertThat(provideBy(US_FALLBACK, GERMANY).locales().getFallbacks(), is(US_FALLBACK));
        assertThat(provideAllBy(US_FALLBACK, GERMANY), isEqualTo(
            MESSAGES_GERMANY,
            MESSAGES_GERMAN,
            MESSAGES_US,
            MESSAGES_ENGLISH
        ));
    }

    @Test
    public void provideGerman() throws Exception {
        assertThat(provideBy(US_FALLBACK, GERMAN).getLocale(), is(GERMAN));
        assertThat(provideBy(US_FALLBACK, GERMAN).locales().getFallbacks(), is(US_FALLBACK));
        assertThat(provideAllBy(US_FALLBACK, GERMAN), isEqualTo(
            MESSAGES_GERMANY,
            MESSAGES_GERMAN,
            MESSAGES_US,
            MESSAGES_ENGLISH
        ));
    }

    @Test
    public void provideNull() throws Exception {
        assertThat(provideBy(US_FALLBACK, null).getLocale(), is(null));
        assertThat(provideBy(US_FALLBACK, null).locales().getFallbacks(), is(US_FALLBACK));
        assertThat(provideAllBy(US_FALLBACK, null), isEqualTo(
            MESSAGES_GERMANY,
            MESSAGES_GERMAN,
            MESSAGES_US,
            MESSAGES_ENGLISH
        ));
    }

    @Test
    public void provideUs() throws Exception {
        assertThat(provideBy(US_FALLBACK, US).getLocale(), is(US));
        assertThat(provideBy(US_FALLBACK, US).locales().getFallbacks(), is(US_FALLBACK));
        assertThat(provideAllBy(US_FALLBACK, US), isEqualTo(
            MESSAGES_GERMANY,
            MESSAGES_GERMAN,
            MESSAGES_US,
            MESSAGES_ENGLISH
        ));
    }

    @Test
    public void provideEnglish() throws Exception {
        assertThat(provideBy(US_FALLBACK, ENGLISH).getLocale(), is(ENGLISH));
        assertThat(provideBy(US_FALLBACK, ENGLISH).locales().getFallbacks(), is(US_FALLBACK));
        assertThat(provideAllBy(US_FALLBACK, ENGLISH), isEqualTo(
            MESSAGES_GERMANY,
            MESSAGES_GERMAN,
            MESSAGES_US,
            MESSAGES_ENGLISH
        ));
    }

    @Test
    public void alternativeConstructor() throws Exception {
        final LocaleHierarchyAwareMessagesProvider provider = new LocaleHierarchyAwareMessagesProvider(LOCALES);
        assertThat(provider.locales(), is(LOCALES));
        assertThat(provider.fallbackLocales(), isEmpty());
    }

    @Nullable
    protected static Iterable<Messages> provideAllBy(@Nullable Iterable<Locale> fallbackLocales, @Nullable Locale targetLoale) throws Exception {
        final LocaleHierarchyAwareMessagesProvider provider = new LocaleHierarchyAwareMessagesProvider(LOCALES, fallbackLocales);
        final LocaleAwareMessages messages = (LocaleAwareMessages) provider.provideBy(targetLoale, TYPE, "testfiles/foo1.properties");
        final Map<Locale, Messages> localeToMessages = messages != null ? messages.localeToMessages() : null;
        return localeToMessages != null ? localeToMessages.values() : null;
    }

    @Nullable
    protected static LocaleAwareMessages provideBy(@Nullable Iterable<Locale> fallbackLocales, @Nullable Locale targetLoale) throws Exception {
        final LocaleHierarchyAwareMessagesProvider provider = new LocaleHierarchyAwareMessagesProvider(LOCALES, fallbackLocales);
        final LocaleAwareMessages messages = (LocaleAwareMessages) provider.provideBy(targetLoale, TYPE, "testfiles/foo1.properties");
        return messages;
    }

    @Nonnull
    protected static Messages provideReferencesBy(@Nullable Locale locale) {
        try {
            return messagesProvider().provideBy(locale, TYPE, "testfiles/foo1.properties");
        } catch (final IOException e) {
            throw new RuntimeException("Could not load required properties.", e);
        }
    }
}