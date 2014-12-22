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

package org.echocat.locela.api.java.format;

import javax.annotation.Nonnull;
import java.util.Locale;

public abstract class FormatterSupport implements Formatter {

    @Nonnull
    private final Locale _locale;

    public FormatterSupport(@Nonnull Locale locale) {
        _locale = locale;
    }

    @Override
    @Nonnull
    public Locale getLocale() {
        return _locale;
    }

}
