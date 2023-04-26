package org.echocat.locela.api.java.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public interface FormatterFactory<F extends Formatter> {

    @Nonnull
    String getId();

    @Nonnull
    F createBy(@Nullable Locale locale, @Nullable String pattern, @Nonnull FormatterFactory<?> root);

}
