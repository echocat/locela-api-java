/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2016 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.utils;

import org.echocat.locela.api.java.testing.Assert;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

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

        final Iterator<String> convertingIterator = new ConvertingIterator<String, String>(originalInput) {
            @Override
            protected String convert(String input) {
                return input + "x";
            }
        };

        int i = 0;
        while(convertingIterator.hasNext()) {
            Assert.assertThat(convertingIterator.next(), CoreMatchers.equalTo(i + "x"));
            i++;
        }
        Assert.assertThat(i, CoreMatchers.equalTo(MAX_VALUES));
    }

}
