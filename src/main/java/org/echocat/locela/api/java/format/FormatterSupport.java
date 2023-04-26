package org.echocat.locela.api.java.format;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Locale;

@ThreadSafe
public abstract class FormatterSupport implements Formatter {

    @Nullable
    private final Locale _locale;

    public FormatterSupport(@Nullable Locale locale) {
        _locale = locale;
    }

    @Override
    @Nullable
    public Locale getLocale() {
        return _locale;
    }

}
