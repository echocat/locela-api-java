package org.echocat.locela.api.java.properties;

import javax.annotation.Nonnull;

public interface PropertyFormatter {

    @Nonnull
    String format(@Nonnull Property<String> property);

}
