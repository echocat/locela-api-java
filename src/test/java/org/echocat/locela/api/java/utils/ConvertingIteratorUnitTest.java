package org.echocat.locela.api.java.utils;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;

public class ConvertingIteratorUnitTest {

    private static final int MAX_VALUES = 100;

    @Test
    public void testAll() throws Exception {
        final AtomicInteger integer = new AtomicInteger();
        integer.set(0);

        final Iterator<String> originalInput = new Iterator<String>() {

            @Override
            public boolean hasNext() {
                return integer.get() < MAX_VALUES;
            }

            @Override
            public String next() {
                return "" + integer.getAndIncrement();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        try (final ConvertingIterator<String, String> ci = new ConvertingIterator<String, String>(originalInput) {
            @Override
            protected String convert(String input) {
                return input + "x";
            }
        }) {

            int i = 0;
            while(ci.hasNext()) {
                assertThat(ci.next(), CoreMatchers.equalTo(i + "x"));
                i++;
            }
            assertThat(i, CoreMatchers.equalTo(MAX_VALUES));
        }

    }

}
