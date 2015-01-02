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

import org.echocat.jomon.runtime.CollectionUtils;
import org.echocat.jomon.runtime.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

public class LocaleHierarchyIterator implements Iterator<Locale> {

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
        _fallbacks = fallbacks != null ? fallbacks : CollectionUtils.<Locale>emptyIterator();
    }

    @Override
    public boolean hasNext() {
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
        if (_hasNext == null && fallbacks().hasNext()) {
            _disassembleLocale = false;
            _hasNext = true;
            _next = fallbacks().next();
        }
        // noinspection ObjectEquality
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
