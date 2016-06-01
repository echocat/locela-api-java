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

package org.echocat.locela.api.java.support;

import org.junit.Test;

import java.util.Locale;

import static java.util.Locale.*;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.junit.Assert.assertThat;

public class StandardLocaleNormalizerUnitTest {

    @Test
    public void normalize() throws Exception {
        final StandardLocaleNormalizer normelizer = new StandardLocaleNormalizer(US, US, GERMANY, FRANCE);
        assertThat(normelizer.normelize(GERMANY), is(GERMANY));
        assertThat(normelizer.normelize(GERMAN), is(GERMANY));
        assertThat(normelizer.normelize(new Locale("de", "AT")), is(GERMANY));
        assertThat(normelizer.normelize(new Locale("nl", "NL")), is(US));
    }

}