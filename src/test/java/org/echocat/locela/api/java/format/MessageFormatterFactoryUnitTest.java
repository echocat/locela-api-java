/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.format;

import org.echocat.locela.api.java.testing.BaseMatchers;
import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MessageFormatterFactoryUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void testGetFactoryWithIllegalId() throws Exception {
        new MessageFormatterFactory().getFactoryBy("foo");
    }

    @Test
    public void testGetFactory() throws Exception {
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        doReturn("foo").when(other).getId();
        assertThat(new MessageFormatterFactory(other).getFactoryBy("foo"), BaseMatchers.<FormatterFactory<?>>is(other));
    }

    @Test
    public void testIterator() throws Exception {
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        doReturn("foo").when(other).getId();
        final MessageFormatterFactory messageFormatterFactory = new MessageFormatterFactory(other);
        //noinspection RedundantTypeArguments
        assertThat(messageFormatterFactory.getFactories(), IterableMatchers.<FormatterFactory<?>>isEqualTo(messageFormatterFactory, other));
    }

    @Test(expected = IllegalStateException.class)
    public void testNotSameRoot() throws Exception {
        final MessageFormatterFactory that = new MessageFormatterFactory();
        final FormatterFactory<?> other = mock(FormatterFactory.class);
        that.createBy(Locale.US, null, other);
    }

}