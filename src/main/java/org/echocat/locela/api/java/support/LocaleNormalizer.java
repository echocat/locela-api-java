package org.echocat.locela.api.java.support;

import javax.annotation.Nullable;
import java.util.Locale;

public interface LocaleNormalizer {

    @Nullable
    Locale normalize(@Nullable Locale input);

}
