package org.echocat.locela.api.java.properties;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;

public interface PropertiesLineReader extends Closeable {

    @Nullable
    Line read() throws IOException;

}
