package org.echocat.locela.api.java.support;

import org.echocat.locela.api.java.utils.CollectionUtils;
import org.echocat.locela.api.java.utils.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class LocaleHierarchyIterator implements Iterator<Locale> {

    @Nonnull
    private final Set<Locale> _alreadyReturned = new HashSet<>();
    @Nonnull
    private final Iterator<Locale> _fallbacks;

    @Nullable
    private Boolean _hasNext = true;
    @Nullable
    private Locale _next;

    private boolean _disassembleLocale = true;

    public LocaleHierarchyIterator(@Nullable Locale start) {
        this(start, (Iterator<Locale>) null);
    }

    public LocaleHierarchyIterator(@Nullable Locale start, @Nullable Iterable<Locale> fallbacks) {
        this(start, fallbacks != null ? fallbacks.iterator() : null);
    }

    public LocaleHierarchyIterator(@Nullable Locale start, @Nullable Iterator<Locale> fallbacks) {
        _next = start;
        _fallbacks = fallbacks != null ? fallbacks : CollectionUtils.emptyIterator();
    }

    @Override
    public boolean hasNext() {
        while (_hasNext == null) {
            if (_hasNext == null && isDisassembleLocale() && _next != null) {
                final String variant = _next.getVariant();
                if (!StringUtils.isEmpty(variant)) {
                    _next = new Locale(_next.getLanguage(), _next.getCountry());
                    _hasNext = true;
                } else {
                    final String country = _next.getCountry();
                    if (!StringUtils.isEmpty(country)) {
                        _next = new Locale(_next.getLanguage());
                        _disassembleLocale = false;
                        _hasNext = true;
                    }
                }
            }
            if (_hasNext == null) {
                if (fallbacks().hasNext()) {
                    _disassembleLocale = false;
                    _hasNext = true;
                    _next = fallbacks().next();
                } else {
                    _hasNext = false;
                }
            }
            if (_hasNext) {
                if (_alreadyReturned.contains(_next)) {
                    _hasNext = null;
                } else {
                    _alreadyReturned.add(_next);
                }
            }
        }
        return _hasNext != null && _hasNext;
    }

    @Override
    public Locale next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        _hasNext = null;
        return _next;
    }

    @Nonnull
    protected Iterator<Locale> fallbacks() {
        return _fallbacks;
    }

    protected boolean isDisassembleLocale() {
        return _disassembleLocale;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
