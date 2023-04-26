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

public class LocaleHierarchyAwareFactory {

    @Nonnull
    private final List<Locale> _allowedLocales;
    @Nullable
    private final Locale _fallbackLocale;

    public LocaleHierarchyAwareFactory(@Nullable Locale fallbackLocale, @Nullable Locale... allowedLocales) {
        this(fallbackLocale, asList(allowedLocales));
    }

    public LocaleHierarchyAwareFactory(@Nullable Locale fallbackLocale, @Nullable Iterable<Locale> allowedLocales) {
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
