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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

public class LocaleHierarchy implements Iterable<Locale> {

    @Nullable
    private final Locale _start;
    @Nullable
    private final Iterable<Locale> _fallbacks;

    public LocaleHierarchy(@Nullable Locale start) {
        this(start, Collections.<Locale>emptyList());
    }

    public LocaleHierarchy(@Nullable Locale start, @Nullable Locale... fallbacks) {
        this(start, asList(fallbacks));
    }

    public LocaleHierarchy(@Nullable Locale start, @Nullable Iterable<Locale> fallbacks) {
        _start = start;
        _fallbacks = fallbacks;
    }

    @Override
    public Iterator<Locale> iterator() {
        return new LocaleHierarchyIterator(getStart(), getFallbacks());
    }

    @Nullable
    public Locale getStart() {
        return _start;
    }

    @Nullable
    public Iterable<Locale> getFallbacks() {
        return _fallbacks;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        sb.append('[');
        for (final Locale locale : this) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(locale);
        }
        sb.append(']');
        return sb.toString();
    }

}
