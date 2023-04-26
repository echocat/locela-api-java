package org.echocat.locela.api.java.format;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

public interface Formatter {

    void format(@Nullable Object value, @Nonnull Writer to) throws IOException;

    @Nullable
    Locale getLocale();

}
