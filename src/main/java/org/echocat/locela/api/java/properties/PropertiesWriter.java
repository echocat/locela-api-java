package org.echocat.locela.api.java.properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;

public interface PropertiesWriter {

    void write(@Nullable Properties<String> properties, @Nonnull Writer to) throws IOException;

}
