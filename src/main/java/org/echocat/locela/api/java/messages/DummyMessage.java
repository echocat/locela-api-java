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

package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public class DummyMessage extends MessageSupport {

    @Nullable
    private final Locale _locale;
    @Nonnull
    private final String _id;

    public DummyMessage(@Nullable Locale locale, @Nonnull String id) {
        _locale = locale;
        _id = id;
    }

    @Nonnull
    @Override
    public String get() {
        return "";
    }

    @Override
    public void format(@Nullable Object value, @Nonnull Writer to) throws IOException {
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return _locale;
    }

    @Nullable
    @Override
    public String getId() {
        return _id;
    }

}
