package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.messages.FileAccessor;
import org.echocat.locela.api.java.messages.Message;
import org.echocat.locela.api.java.messages.Messages;
import org.echocat.locela.api.java.messages.MessagesProvider;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MessageProviderBasedMessageSource implements MessageSource {

    @Nonnull
    private final Map<Locale, Messages> _cache = new LinkedHashMap<>();
    @Nonnull
    private final ReadWriteLock _lock = new ReentrantReadWriteLock();

    @Nonnull
    private final MessagesProvider _messagesProvider;
    @Nullable
    private final LocaleNormalizer _localeNormalizer;

    @Nullable
    private final Class<?> _type;
    @Nullable
    private final ClassLoader _classLoader;
    @Nullable
    private final File _directory;
    @Nullable
    private final FileAccessor _fileAccessor;
    @Nullable
    private final String _baseFile;

    private Integer _maxCacheEntries = 100;

    public MessageProviderBasedMessageSource(@Nonnull MessagesProvider messagesProvider, @Nullable LocaleNormalizer localeNormalizer, @Nonnull Class<?> type) {
        _messagesProvider = messagesProvider;
        _localeNormalizer = localeNormalizer;
        _type = type;
        _baseFile = null;
        _classLoader = null;
        _directory = null;
        _fileAccessor = null;
    }

    public MessageProviderBasedMessageSource(@Nonnull MessagesProvider messagesProvider, @Nullable LocaleNormalizer localeNormalizer, @Nonnull Class<?> baseType, @Nonnull String baseFile) {
        _messagesProvider = messagesProvider;
        _localeNormalizer = localeNormalizer;
        _type = baseType;
        _baseFile = baseFile;
        _classLoader = null;
        _directory = null;
        _fileAccessor = null;
    }

    public MessageProviderBasedMessageSource(@Nonnull MessagesProvider messagesProvider, @Nullable LocaleNormalizer localeNormalizer, @Nonnull ClassLoader loader, @Nonnull String baseFile) {
        _messagesProvider = messagesProvider;
        _localeNormalizer = localeNormalizer;
        _type = null;
        _baseFile = baseFile;
        _classLoader = loader;
        _directory = null;
        _fileAccessor = null;
    }

    public MessageProviderBasedMessageSource(@Nonnull MessagesProvider messagesProvider, @Nullable LocaleNormalizer localeNormalizer, @Nonnull File directory, @Nonnull String baseFilename) {
        _messagesProvider = messagesProvider;
        _localeNormalizer = localeNormalizer;
        _type = null;
        _baseFile = baseFilename;
        _classLoader = null;
        _directory = directory;
        _fileAccessor = null;
    }

    public MessageProviderBasedMessageSource(@Nonnull MessagesProvider messagesProvider, @Nullable LocaleNormalizer localeNormalizer, @Nonnull FileAccessor accessor, @Nonnull String baseFile) {
        _messagesProvider = messagesProvider;
        _localeNormalizer = localeNormalizer;
        _type = null;
        _baseFile = baseFile;
        _classLoader = null;
        _directory = null;
        _fileAccessor = accessor;
    }


    @Override
    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        final Messages messages = messagesFor(locale);
        if (messages == null) {
            return defaultMessage;
        }
        final Message message = messages.find(code);
        if (message == null) {
            return defaultMessage;
        }
        return message.format(args);
    }

    @Override
    public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
        final Messages messages = messagesFor(locale);
        if (messages == null) {
            throw new NoSuchMessageException(code, locale);
        }
        final Message message = messages.find(code);
        if (message == null) {
            throw new NoSuchMessageException(code, locale);
        }
        return message.format(args);
    }

    @Override
    public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
        final String[] codes = resolvable.getCodes();
        for (final String code : codes) {
            final Messages messages = messagesFor(locale);
            if (messages != null) {
                final Message message = messages.find(code);
                if (message != null) {
                    return message.format(resolvable.getArguments());
                }
            }
        }
        final String defaultMessage = resolvable.getDefaultMessage();
        if (defaultMessage != null) {
            return defaultMessage;
        }
        if (codes != null && codes.length > 1) {
            throw new NoSuchMessageException(codes[0], locale);
        }
        throw new NoSuchMessageException("unknown", locale);
    }

    @Nullable
    protected Messages messagesFor(Locale locale) {
        final Locale targetLocale = _localeNormalizer != null ? _localeNormalizer.normalize(locale) : locale;
        if (_maxCacheEntries == null) {
            return createMessagesFor(targetLocale);
        }
        Messages result;
        _lock.readLock().lock();
        try {
            result = _cache.get(targetLocale);
        } finally {
            _lock.readLock().unlock();
        }
        if (result == null) {
            _lock.writeLock().lock();
            try {
                result = createMessagesFor(targetLocale);
                _cache.put(targetLocale, result);
                while (_cache.size() > _maxCacheEntries) {
                    final Iterator<Locale> i = _cache.keySet().iterator();
                    if (i.hasNext()) {
                        i.next();
                        i.remove();
                    }
                }
            } finally {
                _lock.writeLock().unlock();
            }
        }
        return result;
    }

    @Nullable
    protected Messages createMessagesFor(Locale locale) {
        try {
            if (_type != null && _baseFile == null) {
                return _messagesProvider.provideBy(locale, _type);
            }
            if (_type != null && _baseFile != null) {
                return _messagesProvider.provideBy(locale, _type, _baseFile);
            }
            if (_classLoader != null && _baseFile != null) {
                return _messagesProvider.provideBy(locale, _classLoader, _baseFile);
            }
            if (_directory != null && _baseFile != null) {
                return _messagesProvider.provideBy(locale, _directory, _baseFile);
            }
            if (_fileAccessor != null && _baseFile != null) {
                return _messagesProvider.provideBy(locale, _fileAccessor, _baseFile);
            }
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        throw new IllegalArgumentException("There was no valid accessor combination provided.");
    }

    public Integer getMaxCacheEntries() {
        return _maxCacheEntries;
    }

    @Nonnull
    public MessageProviderBasedMessageSource setMaxCacheEntries(Integer maxCacheEntries) {
        _maxCacheEntries = maxCacheEntries;
        return this;
    }

    @Nonnull
    protected MessagesProvider messagesProvider() {
        return _messagesProvider;
    }

    @Nonnull
    protected Map<Locale, Messages> cache() {
        return _cache;
    }
}
