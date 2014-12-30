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

import org.echocat.jomon.runtime.util.IdEnabled;
import org.echocat.locela.api.java.format.Formatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface Message extends Formatter, IdEnabled<String> {

    @Nonnull
    public String format(@Nullable Map<String, ?> values);

    @Nonnull
    public String format(@Nullable Iterable<?> values);

    @Nonnull
    public String format(@Nullable Object... values);

    @Nonnull
    public String get();

}
