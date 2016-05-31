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

import javax.annotation.Nonnull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

    public static final String ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    @Nonnull
    public static Date parseIsoDate(@Nonnull String asString) throws IllegalArgumentException {
        try {
            return new SimpleDateFormat(ISO_PATTERN).parse(asString);
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Could not parse: " + asString + ", it does not match pattern: " + ISO_PATTERN, e);
        }
    }

}
