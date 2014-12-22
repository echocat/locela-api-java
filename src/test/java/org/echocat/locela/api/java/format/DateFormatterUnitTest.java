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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.echocat.locela.api.java.format.DateFormatter.Pattern.DEFAULT;
import static org.echocat.locela.api.java.format.DateFormatter.Pattern.FULL;
import static org.echocat.locela.api.java.format.DateFormatter.Pattern.LONG;
import static org.echocat.locela.api.java.format.DateFormatter.Pattern.MEDIUM;
import static org.echocat.locela.api.java.format.DateFormatter.Pattern.SHORT;
import static java.util.Locale.US;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;

public class DateFormatterUnitTest {

    protected static final DateFormat NULL_FORMAT = null;
    protected static final DateFormat DEFAULT_FORMAT = DEFAULT.toFormat(US);
    protected static final DateFormat SHORT_FORMAT = SHORT.toFormat(US);
    protected static final DateFormat MEDIUM_FORMAT = MEDIUM.toFormat(US);
    protected static final DateFormat LONG_FORMAT = LONG.toFormat(US);
    protected static final DateFormat FULL_FORMAT = FULL.toFormat(US);

    @Test
    public void testAlternativeConstructors() throws Exception {
        assertThat(new DateFormatter(US, NULL_FORMAT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new DateFormatter(US, NULL_FORMAT).getLocale(), is(US));

        assertThat(new DateFormatter(US, DEFAULT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new DateFormatter(US, SHORT).getFormat(), is(SHORT_FORMAT));
        assertThat(new DateFormatter(US, MEDIUM).getFormat(), is(MEDIUM_FORMAT));
        assertThat(new DateFormatter(US, LONG).getFormat(), is(LONG_FORMAT));
        assertThat(new DateFormatter(US, FULL).getFormat(), is(FULL_FORMAT));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new DateFormatter(US, DEFAULT).toString(), is("date,default"));
        assertThat(new DateFormatter(US, "default").toString(), is("date,default"));
        assertThat(new DateFormatter(US, "yyyy-MM-dd").toString(), is("date,yyyy-MM-dd"));
        assertThat(new DateFormatter(US, new SimpleDateFormat("yyyy-MM-dd")).toString(), is("date,<raw>"));
    }

    @Test
    public void testToStringOfPattern() throws Exception {
        assertThat(DEFAULT.toString(), is("default"));
        assertThat(SHORT.toString(), is("short"));
        assertThat(MEDIUM.toString(), is("medium"));
        assertThat(LONG.toString(), is("long"));
        assertThat(FULL.toString(), is("full"));
    }

}