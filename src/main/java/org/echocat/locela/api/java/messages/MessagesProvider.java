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

package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public interface MessagesProvider {

    @Nullable
    public Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> type) throws IOException;

    @Nullable
    public Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> baseType, @Nonnull String baseFile) throws IOException;

    @Nullable
    public Messages provideBy(@Nullable Locale locale, @Nonnull ClassLoader loader, @Nonnull String baseFile) throws IOException;

    @Nullable
    public Messages provideBy(@Nullable Locale locale, @Nonnull File directory, @Nonnull String baseFilename) throws IOException;

    @Nullable
    public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException;

}
