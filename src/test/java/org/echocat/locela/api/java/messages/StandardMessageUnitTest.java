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

package org.echocat.locela.api.java.messages;

import org.junit.Test;

import static java.util.Locale.US;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.messages.StandardMessage.message;

public class StandardMessageUnitTest {

    @Test
    public void testGet() throws Exception {
        assertThat(message("a", "b").get(), is("b"));
    }

    @Test
    public void testGetLocale() throws Exception {
        assertThat(message(US, "a", "b").getLocale(), is(US));
    }

    @Test
    public void testGetId() throws Exception {
        assertThat(message("a", "b").getId(), is("a"));
    }

}