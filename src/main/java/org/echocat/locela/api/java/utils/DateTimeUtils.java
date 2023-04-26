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
