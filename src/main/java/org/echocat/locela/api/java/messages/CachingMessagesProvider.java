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

import org.echocat.jomon.cache.Cache;
import org.echocat.jomon.cache.LfuCache;
import org.echocat.jomon.cache.management.CacheProvider;
import org.echocat.jomon.runtime.util.ValueProducer;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Locale;

import static org.echocat.jomon.cache.management.DefaultCacheDefinition.lfuCache;

public class CachingMessagesProvider extends MessagesProviderSupport {

    @Nonnull
    private final MessagesProvider _delegate;
    @Nonnull
    private final Cache<CacheKey, Messages> _cache;

    @Nonnull
    private final ValueProducer<CacheKey, Messages> _valueProducer = new ValueProducer<CacheKey, Messages>() { @Nullable @Override public Messages produce(@Nullable CacheKey key) throws Exception {
        return delegate().provideBy(key.getLocale(), key.getAccessor(), key.getBaseFile());
    }};

    public CachingMessagesProvider(@Nonnull MessagesProvider delegate, @Nonnull long capacity) {
        _delegate = delegate;
        _cache = createCache(capacity);
    }

    public CachingMessagesProvider(@Nonnull MessagesProvider delegate, @Nonnull CacheProvider cacheProvider) {
        _delegate = delegate;
        _cache = createCache(cacheProvider);
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
        final CacheKey key = new CacheKey(locale, accessor, baseFile);
        return cache().get(key, valueProducer());
    }

    @Nonnull
    protected Cache<CacheKey, Messages> createCache(@Nonnegative long capacity) {
        final LfuCache<CacheKey, Messages> cache = new LfuCache<>(CacheKey.class, Messages.class);
        cache.setCapacity(capacity);
        return cache;
    }

    @Nonnull
    protected Cache<CacheKey, Messages> createCache(@Nonnull CacheProvider with) {
        return with.provide(CachingMessagesProvider.class, "messages", lfuCache(CacheKey.class, Messages.class)
            .withCapacity(100)
            .withMaximumLifetime("1h")
        );
    }

    @Nonnull
    protected Cache<CacheKey, Messages> cache() {
        return _cache;
    }

    @Nonnull
    protected MessagesProvider delegate() {
        return _delegate;
    }

    @Nonnull
    protected ValueProducer<CacheKey, Messages> valueProducer() {
        return _valueProducer;
    }

    protected static class CacheKey {

        @Nullable
        private final Locale _locale;
        @Nonnull
        private final FileAccessor _accessor;
        @Nonnull
        private final String _baseFile;

        public CacheKey(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) {
            _locale = locale;
            _accessor = accessor;
            _baseFile = baseFile;
        }

        @Nullable
        public Locale getLocale() {
            return _locale;
        }

        @Nonnull
        public FileAccessor getAccessor() {
            return _accessor;
        }

        @Nonnull
        public String getBaseFile() {
            return _baseFile;
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else if (o == null || !(o instanceof CacheKey)) {
                result = false ;
            } else {
                final CacheKey that = (CacheKey) o;
                if (getBaseFile().equals(that.getBaseFile())) {
                    final Locale locale = getLocale();
                    if (locale != null ? locale.equals(that.getLocale()) : that.getLocale() == null) {
                        result = getAccessor().equals(that.getAccessor());
                    } else {
                        result = false;
                    }
                } else {
                    result = false;
                }

            }
            return result;
        }

        @Override
        public int hashCode() {
            final Locale locale = getLocale();
            return 0
                + (locale != null ? locale.hashCode() : 0)
                + getAccessor().hashCode()
                + getBaseFile().hashCode()
                ;
        }

    }

}
