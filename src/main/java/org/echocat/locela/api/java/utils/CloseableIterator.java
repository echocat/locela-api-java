package org.echocat.locela.api.java.utils;

import java.util.Iterator;

public interface CloseableIterator<T> extends AutoCloseable, Iterator<T> {

    @Override
    void close();
}
