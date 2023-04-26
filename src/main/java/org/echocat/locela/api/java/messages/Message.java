package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.format.Formatter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public interface Message extends Formatter {

    @Nullable
    String getId();

    @Nonnull
    String format(@Nullable Map<String, ?> values);

    @Nonnull
    String format(@Nullable Iterable<?> values);

    @Nonnull
    String format(@Nullable Object... values);

    @Nonnull
    String get();

}
