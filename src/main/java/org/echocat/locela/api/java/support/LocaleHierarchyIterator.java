/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.support;

import org.echocat.jomon.runtime.StringUtils;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

public class LocaleHierarchyIterator implements Iterator<Locale> {

    protected static final Locale END = new Locale("");

    @Nullable
    private final Locale _fallback;

    @Nullable
    private Locale _current;

    public LocaleHierarchyIterator(@Nullable Locale start) {
        this(start, END);
    }

    public LocaleHierarchyIterator(@Nullable Locale start, @Nullable Locale fallback) {
        _current = start;
        _fallback = fallback;
    }

    @Override
    public boolean hasNext() {
        // noinspection ObjectEquality
        return _current != END;
    }

    @Override
    public Locale next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final Locale current = _current;
        if (equals(_current, _fallback)) {
            _current = END;
        } else if (!isEmpty(_current)) {
            final String variant = _current.getVariant();
            if (!StringUtils.isEmpty(variant)) {
                _current = new Locale(_current.getLanguage(), _current.getCountry());
            } else {
                final String country = _current.getCountry();
                if (!StringUtils.isEmpty(country)) {
                    _current = new Locale(_current.getLanguage());
                } else {
                    _current = null;
                }
            }
        } else {
            _current = _fallback;
        }
        return current;
    }

    protected boolean isEmpty(@Nullable Locale locale) {
        return locale == null || StringUtils.isEmpty(locale.getLanguage());
    }

    protected boolean equals(@Nullable Locale a, @Nullable Locale b) {
        return a != null ? a.equals(b) : b == null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
