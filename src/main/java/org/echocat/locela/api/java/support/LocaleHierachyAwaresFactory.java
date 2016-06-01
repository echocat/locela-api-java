/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2016 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.LocaleHierarchyAwareMessagesProvider;
import org.echocat.locela.api.java.messages.MessagesProvider;
import org.echocat.locela.api.java.messages.RecursiveMessagesProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableList;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

public class LocaleHierachyAwaresFactory {

    @Nonnull
    private final List<Locale> _allowedLocales;
    @Nullable
    private final Locale _fallbackLocale;

    public LocaleHierachyAwaresFactory(@Nullable Locale fallbackLocale, @Nullable Locale... allowedLocales) {
        this(fallbackLocale, asList(allowedLocales));
    }

    public LocaleHierachyAwaresFactory(@Nullable Locale fallbackLocale, @Nullable Iterable<Locale> allowedLocales) {
        _fallbackLocale = fallbackLocale;
        _allowedLocales = asImmutableList(allowedLocales);
    }

    @Nonnull
    public MessagesProvider createMessagesProvider() {
        final LocaleHierarchyAwareMessagesProvider localeHierarchyAwareMessagesProvider = new LocaleHierarchyAwareMessagesProvider(_allowedLocales, asImmutableList(_fallbackLocale));
        return new RecursiveMessagesProvider(localeHierarchyAwareMessagesProvider);
    }

    @Nonnull
    public LocaleNormalizer createLocaleNormalizer() {
        return new StandardLocaleNormalizer(_fallbackLocale, _allowedLocales);
    }

    @Nonnull
    public List<Locale> getAllowedLocales() {
        return _allowedLocales;
    }

    @Nullable
    public Locale getFallbackLocale() {
        return _fallbackLocale;
    }
}
