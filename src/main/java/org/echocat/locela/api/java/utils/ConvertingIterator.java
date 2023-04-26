package org.echocat.locela.api.java.utils;

import java.util.Iterator;

public abstract class ConvertingIterator<I, R> implements CloseableIterator<R> {

    private final Iterator<I> _inputIterator;

    public ConvertingIterator(Iterator<I> inputIterator) {
        _inputIterator = inputIterator;
    }

    protected abstract R convert(I input);

    @Override
    public boolean hasNext() {
        return _inputIterator.hasNext();
    }

    @Override
    public R next() {
        final I input = _inputIterator.next();
        final R output = convert(input);
        return output;
    }


    @Override
    public void remove() {
        _inputIterator.remove();
    }

    @Override
    public void close() {
        if (_inputIterator instanceof CloseableIterator) {
            ((CloseableIterator) _inputIterator).close();
        }
    }
}
