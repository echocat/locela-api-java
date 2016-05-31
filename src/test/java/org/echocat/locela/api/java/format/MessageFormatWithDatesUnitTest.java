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

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import static org.echocat.locela.api.java.format.MessageFormatter.format;
import static java.util.Locale.GERMANY;
import static java.util.Locale.US;
import static org.echocat.locela.api.java.utils.DateTimeUtils.parseIsoDate;

@RunWith(Parameterized.class)
public class MessageFormatWithDatesUnitTest extends BundledMessageFormatTestSupport {

    protected static final Date DATE = parseIsoDate("2010-01-02T03:04:05+0100");

    static {
        DATE.setTime(DATE.getTime() + 6);
    }

    @Parameters(name = "{0}:{1}")
    public static Collection<Object[]> getTests() throws Exception {
        return createParameters("dates", US, GERMANY);
    }

    public MessageFormatWithDatesUnitTest(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull String expectedResult) {
        super(locale, pattern, expectedResult);
    }

    @Override
    protected String executeFormat(@Nonnull Locale locale, @Nonnull String pattern) throws Exception {
        return format(locale, pattern, DATE);
    }

}
