package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.echocat.locela.api.java.messages.StandardMessagesProvider.messagesProvider;

public class LocaleHierarchyAwareMessagesProvider extends MessagesProviderSupport {

    @Nonnull
    private final MessagesProvider _delegate;
    @Nonnull
    private final Iterable<Locale> _locales;
    @Nonnull
    private final Iterable<Locale> _fallbackLocales;

    public LocaleHierarchyAwareMessagesProvider(@Nullable Iterable<Locale> locales) {
        this(locales, null);
    }

    public LocaleHierarchyAwareMessagesProvider(@Nullable Iterable<Locale> locales, @Nullable Iterable<Locale> fallbackLocales) {
        this(null, locales, fallbackLocales);
    }

    public LocaleHierarchyAwareMessagesProvider(@Nullable MessagesProvider delegate, @Nullable Iterable<Locale> locales, @Nullable Iterable<Locale> fallbackLocales) {
        _delegate = delegate != null ? delegate : messagesProvider();
        _locales = locales != null ? locales : Collections.emptyList();
        _fallbackLocales = fallbackLocales != null ? fallbackLocales : Collections.emptyList();
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
        final Map<Locale, Messages> localeToMessages = provideLocaleToMessagesBy(accessor, baseFile);
        return !localeToMessages.isEmpty() ? new LocaleAwareMessages(locale, localeToMessages, fallbackLocales()) : null;
    }

    @Nonnull
    protected Map<Locale, Messages> provideLocaleToMessagesBy(@Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
        final Map<Locale, Messages> result = new LinkedHashMap<>();
        for (final Locale locale : locales()) {
            final Messages messages = delegate().provideBy(locale, accessor, baseFile);
            if (messages != null) {
                result.put(locale, messages);
            }
        }
        return result;
    }

    @Nonnull
    protected MessagesProvider delegate() {
        return _delegate;
    }

    @Nonnull
    protected Iterable<Locale> locales() {
        return _locales;
    }

    @Nonnull
    protected Iterable<Locale> fallbackLocales() {
        return _fallbackLocales;
    }

}
