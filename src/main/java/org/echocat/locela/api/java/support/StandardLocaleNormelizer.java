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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableList;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.echocat.locela.api.java.utils.StringUtils.isEmpty;

public class StandardLocaleNormelizer implements LocaleNormelizer {

    private static final Locale NONE = new Locale("");
    private static final Locale[] CONTAINS_NULL = {null};

    @Nonnull
    private final List<Locale> _allowedLocales;
    @Nullable
    private final Locale _defaultLocale;

    public StandardLocaleNormelizer(@Nullable Locale defaultLocale, @Nullable Locale... allowedLocales) {
        this(defaultLocale, asList(allowedLocales));
    }

    public StandardLocaleNormelizer(@Nullable Locale defaultLocale, @Nullable Iterable<Locale> allowedLocales) {
        _defaultLocale = defaultLocale;
        _allowedLocales = asImmutableList(allowedLocales);
    }

    @Nullable
    @Override
    public Locale normelize(@Nullable Locale input) {
        Locale result = NONE;
        if (allowedLocales().contains(input)) {
            result = input;
        } else {
            final Iterator<Locale> i = new LocaleHierarchyIterator(input);
            if (i.hasNext()) {
                i.next();
            }
            // noinspection ObjectEquality
            while (i.hasNext() && result == NONE) {
                final Locale subLocale = i.next();
                if (!isEmpty(subLocale.getLanguage())) {
                    for (final Locale candidate : allowedLocales()) {
                        if (startsWith(candidate, subLocale)) {
                            result = candidate;
                            break;
                        }
                    }
                }
            }
            // noinspection ObjectEquality
            if (result == NONE) {
                result = defaultLocale();
            }

        }
        return result;
    }

    protected boolean startsWith(@Nullable Locale toTest, @Nullable Locale with) {
        final boolean result;
        if (toTest != null && with != null) {
            final String language = with.getLanguage();
            if (!isEmpty(language)) {
                if (language.equals(toTest.getLanguage())) {
                    final String country = with.getCountry();
                    if (!isEmpty(country)) {
                        if (country.equals(toTest.getCountry())) {
                            final String variant = with.getCountry();
                            if (!isEmpty(variant)) {
                                // noinspection RedundantIfStatement
                                if (variant.equals(toTest.getCountry())) {
                                    result = true;
                                } else {
                                    result = false;
                                }
                            } else {
                                result = true;
                            }
                        } else {
                            result = false;
                        }
                    } else {
                        result = true;
                    }
                } else {
                    result = false;
                }
            } else {
                result = true;
            }
        } else {
            result = toTest == null && with == null;
        }
        return result;
    }

    @Nonnull
    protected List<Locale> allowedLocales() {
        return _allowedLocales;
    }

    @Nullable
    protected Locale defaultLocale() {
        return _defaultLocale;
    }

}
