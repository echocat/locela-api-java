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

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Locale;

import static org.echocat.locela.api.java.support.LocaleHierarchyIterator.END;

public class LocaleHierarchy implements Iterable<Locale> {

    @Nullable
    private final Locale _start;
    @Nullable
    private final Locale _fallback;

    public LocaleHierarchy(@Nullable Locale start) {
        this(start, END);
    }

    public LocaleHierarchy(@Nullable Locale start, @Nullable Locale fallback) {
        _start = start;
        _fallback = fallback;
    }

    @Override
    public Iterator<Locale> iterator() {
        return new LocaleHierarchyIterator(getStart(), getFallback());
    }

    @Nullable
    public Locale getStart() {
        return _start;
    }

    @Nullable
    public Locale getFallback() {
        return _fallback;
    }

}
