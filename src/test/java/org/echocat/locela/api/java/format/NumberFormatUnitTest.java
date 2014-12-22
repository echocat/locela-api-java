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

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.CURRENCY;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.DEFAULT;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.INTEGER;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.PERCENT;
import static java.util.Locale.US;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;

public class NumberFormatUnitTest {

    protected static final NumberFormat NULL_FORMAT = null;
    protected static final NumberFormat DEFAULT_FORMAT = DEFAULT.toFormat(US);
    protected static final NumberFormat INTEGER_FORMAT = INTEGER.toFormat(US);
    protected static final NumberFormat PERCENT_FORMAT = PERCENT.toFormat(US);
    protected static final NumberFormat CURRENCY_FORMAT = CURRENCY.toFormat(US);

    @Test
    public void testAlternativeConstructors() throws Exception {
        assertThat(new NumberFormatter(US, NULL_FORMAT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new NumberFormatter(US, NULL_FORMAT).getLocale(), is(US));

        assertThat(new NumberFormatter(US, DEFAULT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new NumberFormatter(US, INTEGER).getFormat(), is(INTEGER_FORMAT));
        assertThat(new NumberFormatter(US, PERCENT).getFormat(), is(PERCENT_FORMAT));
        assertThat(new NumberFormatter(US, CURRENCY).getFormat(), is(CURRENCY_FORMAT));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new NumberFormatter(US, DEFAULT).toString(), is("number,default"));
        assertThat(new NumberFormatter(US, "default").toString(), is("number,default"));
        assertThat(new NumberFormatter(US, "#").toString(), is("number,#"));
        assertThat(new NumberFormatter(US, new DecimalFormat("#")).toString(), is("number,<raw>"));
    }

}