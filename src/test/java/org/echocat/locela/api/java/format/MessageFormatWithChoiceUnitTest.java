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
import java.util.Locale;
import java.util.Map;

import static org.echocat.locela.api.java.format.MessageFormatter.format;
import static org.echocat.jomon.runtime.CollectionUtils.asImmutableMap;

@RunWith(Parameterized.class)
public class MessageFormatWithChoiceUnitTest extends BundledMessageFormatTestSupport {

    protected static final Map<String, Object> VALUES = asImmutableMap(
        "string", "This is a string",
        "long", 666,
        "double", 12345.6789D,
        "negativeDouble", -9876.54321D,
        "zero", 0,
        "one", 1,
        "two", 2,
        "contidionString", "1#is one file|1",
        "oneWithEscape", "1'",
        "aTrue", true,
        "aFalse", false,
        "object", new Object()
    );

    @Parameters(name = "{1}")
    public static Collection<Object[]> getTests() throws Exception {
        return createParameters("choice");
    }

    public MessageFormatWithChoiceUnitTest(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull String expectedResult) {
        super(locale, pattern, expectedResult);
    }

    @Override
    protected String executeFormat(@Nonnull Locale locale, @Nonnull String pattern) throws Exception {
        return format(locale, pattern, VALUES);
    }

}
