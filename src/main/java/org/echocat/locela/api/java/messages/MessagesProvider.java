package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

public interface MessagesProvider {

    @Nullable
    Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> type) throws IOException;

    @Nullable
    Messages provideBy(@Nullable Locale locale, @Nonnull Class<?> baseType, @Nonnull String baseFile) throws IOException;

    @Nullable
    Messages provideBy(@Nullable Locale locale, @Nonnull ClassLoader loader, @Nonnull String baseFile) throws IOException;

    @Nullable
    Messages provideBy(@Nullable Locale locale, @Nonnull File directory, @Nonnull String baseFilename) throws IOException;

    @Nullable
    Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException;

}
