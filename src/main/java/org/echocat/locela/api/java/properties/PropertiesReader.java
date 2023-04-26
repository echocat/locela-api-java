package org.echocat.locela.api.java.properties;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

public interface PropertiesReader {

    @Nonnull
    Properties<String> read(@Nonnull Reader reader) throws IOException;

}
