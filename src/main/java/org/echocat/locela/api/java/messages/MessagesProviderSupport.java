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

import org.echocat.locela.api.java.messages.FileAccessor.ClassLoaderBased;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static java.io.File.separator;
import static org.echocat.locela.api.java.messages.FileAccessor.FileSystemBased.fileSystemFileAccessor;

public abstract class MessagesProviderSupport implements MessagesProvider {

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> type) throws IOException {
        return provideBy(locale, type.getClassLoader(), type.getName().replace('.', '/') + ".properties");
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> baseType, @Nonnull String baseFile) throws IOException {
        return provideBy(locale, baseType.getClassLoader(), baseType.getPackage().getName().replace('.', '/') + "/" + baseFile);
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull ClassLoader loader, @Nonnull String baseFile) throws IOException {
        return provideBy(locale, new ClassLoaderBased(loader), baseFile);
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull File directory, @Nonnull String baseFilename) throws IOException {
        return provideBy(locale, fileSystemFileAccessor(), directory.getCanonicalPath() + separator + baseFilename);
    }

}
