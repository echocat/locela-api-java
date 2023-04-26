package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Messages extends Iterable<Message> {

    @Nonnull
    Message get(@Nonnull String id);

    @Nullable
    Message find(@Nonnull String id);

    boolean contains(@Nonnull String id);

}
