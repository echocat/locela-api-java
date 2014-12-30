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

package org.echocat.locela.api.java.properties;

import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;


public class StandardPropertyUnitTest {

    @Test
    public void testGetId() throws Exception {
        assertThat(new StandardProperty<>("foo", String.class).getId(), is("foo"));
    }

    @Test
    public void testGetAndSet() throws Exception {
        final StandardProperty<String> property = new StandardProperty<>("foo", String.class);
        assertThat(property.get(), is(null));

        property.set(null);
        assertThat(property.get(), is(null));

        property.set("bar");
        assertThat(property.get(), is("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOfIllegalValue() throws Exception {
        // noinspection unchecked
        new StandardProperty("foo", String.class).set(123.345D);
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(new StandardProperty<>("foo", String.class).getType(), is(String.class));
    }

}